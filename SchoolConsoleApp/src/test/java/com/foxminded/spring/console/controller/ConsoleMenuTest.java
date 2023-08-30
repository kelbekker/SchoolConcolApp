package com.foxminded.spring.console.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import org.slf4j.event.LoggingEvent;
import com.foxminded.spring.console.service.GroupsServiceImpl;
import com.foxminded.spring.console.service.StudentServiceImpl;
import com.foxminded.spring.console.tables.Courses;
import com.foxminded.spring.console.tables.Groups;
import com.foxminded.spring.console.tables.Students;
@ExtendWith(MockitoExtension.class)
class ConsoleMenuTest {
	
	Logger fooLogger = (Logger) LoggerFactory.getLogger(ConsoleMenu.class);
	
	ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
	List<ILoggingEvent> logsList;
	@Mock
	private StudentServiceImpl studentServiceImpl;
	@Mock
	private GroupsServiceImpl groupsServiceImpl;
	@Mock
	private Scanner scanner;
	@Mock
	private Logger logger;
	@Captor
	private ArgumentCaptor<LoggingEvent> loggingEventCaptor;

	@InjectMocks
	private ConsoleMenu consoleMenu;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		listAppender.start();

		fooLogger.addAppender(listAppender);
		logsList = listAppender.list;
	}

	@Test
	void displayMenuWhenNormalShouldGetChoice() throws SQLException {
		 when(scanner.nextInt()).thenReturn(6);
		 
		 consoleMenu.displayMenu();
		 
		 assertEquals("\n"
		 		+ "=== Console Menu ===\n"
		 		+ "1. Find all the groups with less or equal student count\n"
		 		+ "2. Find all the students related to the course with the given name\n"
		 		+ "3. Add a new student\n"
		 		+ "4. Delete student by STUDENT_ID\n"
		 		+ "5. Add a student to the course\n"
		 		+ "6. Remove the student from one of their courses\n"
		 		+ "Enter your choice:\n", logsList.get(0).getMessage());
		 assertEquals(Level.INFO, logsList.get(0)
                 .getLevel());

	        verify(scanner, times(2)).nextLong();
	        verify(scanner, times(1)).nextInt();
	        verify(scanner, times(3)).nextLine();
	}

	@Test
	void optionGetGroupsWithLessOrEqualStudentsWhenNormalMaxStudentsShouldReturnString() {

		int maxStudents = 12;
		when(scanner.nextInt()).thenReturn(maxStudents);

		List<Groups> groupsList = new ArrayList<>();
		Groups group1 = new Groups();
		group1.setGroupId(1L);
		group1.setGroupName("JR-59");

		Groups group2 = new Groups();
		group2.setGroupId(2L);
		group2.setGroupName("AD-62");
		groupsList.add(group1);
		groupsList.add(group2);

		when(groupsServiceImpl.findByMaxStudentsLessThanEqual(maxStudents)).thenReturn(groupsList);

		String result = consoleMenu.optionGetGroupsWithLessOrEqualStudents();

		String expectedResult = "Groups With Less Or Equal Students 12\r\n" + "Group ID: 1\r\n"
				+ "Group Name: JR-59\r\n" + "\r\n" + "Group ID: 2\r\n" + "Group Name: AD-62\r\n" + "\r\n" + "";
		assertEquals(expectedResult, result);
		assertEquals("Enter the maximum number of students: \n", logsList.get(0).getMessage());
	}

	@Test
	void optionGetStudentsByCourseNameWhenNormalMaxStudentsShouldReturnString() throws SQLException {

		String courseName = "Math";
		when(scanner.nextLine()).thenReturn(courseName);

		Set<Courses> coursesList = new HashSet<>();
		Courses course1 = new Courses();
		course1.setCourseId(1L);
		course1.setCourseName("Math");
		course1.setCourseDescription(" ");
		coursesList.add(course1);

		List<Students> studentsList = new ArrayList<>();
		Students student1 = new Students();
		student1.setStudentId(1L);
		student1.setFirstName("John");
		student1.setLastName("Doe");

		Groups group = new Groups();
		group.setGroupId(3L);
		student1.setGroup(group);

		student1.setCoursesList(coursesList);
		studentsList.add(student1);

		studentServiceImpl.addStudentToCourse(1L, 1L);

		when(studentServiceImpl.getStudentsByCourseName(courseName)).thenReturn(studentsList);

		String result = consoleMenu.optionGetStudentsByCourseName();

		String expectedResult = "Students By Math course\r\n"
				+ "Student ID: 1\r\n"
				+ "First Name: John\r\n"
				+ "Last Name: Doe\r\n"
				+ "Group ID: 3\r\n"
				+ "Course ID: 1\r\n"
				+ "\n";

		assertEquals(expectedResult, result);
	}

	@Test
	void optionAddStudentWhenNormalStudentShouldReturnString() {
		Optional<Groups> group = Optional.ofNullable(new Groups());
	    when(groupsServiceImpl.findOne(Mockito.anyLong())).thenReturn(group);
	    
		consoleMenu.optionAddStudent();
		assertEquals("\nEnter student ID: ", logsList.get(0).getMessage());
		assertEquals("\nEnter group ID: ", logsList.get(1).getMessage());
		assertEquals("\nEnter first name: ", logsList.get(2).getMessage());
		assertEquals("\nEnter last name: ", logsList.get(3).getMessage());
		assertEquals("\nStudent added successfully", logsList.get(4).getMessage());
	}

	@Test
	void optionDeleteStudentWhenNormalStudentShouldReturnString() {
		consoleMenu.optionDeleteStudent();
		assertEquals("\nEnter student ID: ", logsList.get(0).getMessage());
		assertEquals("\nStudent deleted successfully", logsList.get(1).getMessage());
	}

	@Test
	void optionAddStudentToCourseWhenNormalStudentShouldReturnString() {
		consoleMenu.optionAddStudentToCourse();
		assertEquals("\nEnter course ID: ", logsList.get(0).getMessage());
		assertEquals("\nEnter student ID: ", logsList.get(1).getMessage());
		assertEquals("\nStudent added to course successfully", logsList.get(2).getMessage());
	}

	@Test
	void optionDeleteStudentFromCourseWhenNormalStudentShouldReturnString() {
		consoleMenu.optionDeleteStudentFromCourse();
		assertEquals("\nEnter course ID: ", logsList.get(0).getMessage());
		assertEquals("\nEnter student ID: ", logsList.get(1).getMessage());
		assertEquals("\nStudent deleted from course successfully", logsList.get(2).getMessage());
	}
}
