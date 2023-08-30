package com.foxminded.schoolconsoleapp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foxminded.schoolconsoleapp.controller.TablesController;
import com.foxminded.schoolconsoleapp.service.CourseService;
import com.foxminded.schoolconsoleapp.service.GroupsService;
import com.foxminded.schoolconsoleapp.service.StudentService;
import com.foxminded.spring.console.tables.Courses;
import com.foxminded.spring.console.tables.Groups;
import com.foxminded.spring.console.tables.Students;

class TablesControllerTest {

	@InjectMocks
	private TablesController tablesController;
	@Mock
	private CourseService courseService;
	@Mock
	private GroupsService groupsService;
	@Mock
	private StudentService studentService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void formattedGetGroupsWithLessOrEqualStudentsResultWhenNormalSouldReternString() throws SQLException {
		List<Groups> mockGroups = new ArrayList<>();

		Groups group1 = new Groups();
		group1.setGroupId(15);
		group1.setGroupName("SR-67");
		mockGroups.add(group1);

		Groups group2 = new Groups();
		group2.setGroupId(11);
		group2.setGroupName("PR-87");
		mockGroups.add(group2);

		when(groupsService.getGroupsWithLessOrEqualStudents(5)).thenReturn(mockGroups);
		String result = tablesController.formattedGetGroupsWithLessOrEqualStudentsResult(5);

		assertEquals("Groups With Less Or Equal Students 5\r\n" + "Group ID: 15\r\n" + "Group Name: SR-67\r\n" + "\r\n"
				+ "Group ID: 11\r\n" + "Group Name: PR-87\r\n" + "\r\n", result);
	}

	@Test
	void formattedGetStudentsByCourseNameResultWhenNormalSouldReternString() throws SQLException {
		List<Students> students = new ArrayList<>();
		Students student1 = new Students();
		student1.setStudentId(1);
		student1.setFirstName("John");
		student1.setLastName("Doe");
		student1.setGroupId(123);
		Courses course1 = new Courses();
		course1.setCourseId(1);
		course1.setCourseName("Math");
		List<Courses> coursesList1 = new ArrayList<>();
		coursesList1.add(course1);
		student1.setCoursesList(coursesList1);
		students.add(student1);

		when(studentService.getStudentsByCourseName("Math")).thenReturn(students);
		String result = tablesController.formattedGetStudentsByCourseNameResult("Math");

		StringBuilder expectedResult = new StringBuilder();
		expectedResult.append(String.format("Students By %s course%n", "Math"));

		for (Students student : students) {
			expectedResult.append(
					String.format("Student ID: %s%nFirst Name: %s%nLast Name: %s%n" + "Group ID: %s%nCourse ID: %s%n%n",
							student.getStudentId(), student.getFirstName(), student.getLastName(), student.getGroupId(),
							student.getCoursesList().get(0).getCourseId()));
		}

		assertEquals(expectedResult.toString(), result);

	}

	@Test
	void formattedDeleteStudentResultWhenNormalSouldReternString() throws SQLException {

		Students student1 = new Students();
		student1.setStudentId(1);
		student1.setFirstName("John");
		student1.setLastName("Doe");
		student1.setGroupId(123);
		Courses course1 = new Courses();
		course1.setCourseId(1);
		course1.setCourseName("Math");
		List<Courses> coursesList1 = new ArrayList<>();
		coursesList1.add(course1);
		student1.setCoursesList(coursesList1);

		String result = tablesController.formattedDeleteStudentResult(student1.getStudentId());
		verify(studentService).deleteStudent(1);

		StringBuilder expectedResult = new StringBuilder();
		expectedResult.append(String.format("Student number %s deleted%n", student1.getStudentId()));

		assertEquals(expectedResult.toString(), result);
	}

	@Test
	void formattedAddStudentToCourseResultWhenNormalSouldReternString() throws SQLException {
		Students student1 = new Students();
		student1.setStudentId(1);
		student1.setFirstName("John");
		student1.setLastName("Doe");
		student1.setGroupId(123);
		Courses course1 = new Courses();
		course1.setCourseId(1);
		course1.setCourseName("Math");
		List<Courses> coursesList1 = new ArrayList<>();
		coursesList1.add(course1);
		student1.setCoursesList(coursesList1);

		String result = tablesController.formattedAddStudentToCourseResult(course1.getCourseId(),
				student1.getStudentId());
		verify(courseService).saveStudentCourse(course1.getCourseId(), student1.getStudentId());
		;

		StringBuilder expectedResult = new StringBuilder();
		expectedResult.append(String.format("Student number %s added to course number %s%n", student1.getStudentId(),
				course1.getCourseId()));

		assertEquals(expectedResult.toString(), result);
	}

	@Test
	void formattedDeleteStudentFromCourseResultWhenNormalSouldReternString() throws SQLException {
		Students student1 = new Students();
		student1.setStudentId(1);
		student1.setFirstName("John");
		student1.setLastName("Doe");
		student1.setGroupId(123);
		Courses course1 = new Courses();
		course1.setCourseId(1);
		course1.setCourseName("Math");
		List<Courses> coursesList1 = new ArrayList<>();
		coursesList1.add(course1);
		student1.setCoursesList(coursesList1);

		String result = tablesController.formattedDeleteStudentFromCourseResult(student1.getStudentId(),
				course1.getCourseId());
		verify(studentService).deleteStudentFromCourse(student1.getStudentId(), course1.getCourseId());

		StringBuilder expectedResult = new StringBuilder();
		expectedResult.append(String.format("Student number %s deleted from course number %s%n",
				student1.getStudentId(), course1.getCourseId()));

		assertEquals(expectedResult.toString(), result);
	}

	@Test
	void formattedAddStudentResultWhenNormalSouldReternString() throws SQLException {
		Students student1 = new Students();
		student1.setStudentId(1);
		student1.setFirstName("John");
		student1.setLastName("Doe");
		student1.setGroupId(123);
		Courses course1 = new Courses();
		course1.setCourseId(1);
		course1.setCourseName("Math");
		List<Courses> coursesList1 = new ArrayList<>();
		coursesList1.add(course1);
		student1.setCoursesList(coursesList1);

		String result = tablesController.formattedAddStudentResult(student1);
		verify(studentService).addStudent(student1);

		StringBuilder expectedResult = new StringBuilder();
		expectedResult.append(String.format("Student number %s added%n", student1.getStudentId()));

		assertEquals(expectedResult.toString(), result);
	}
}
