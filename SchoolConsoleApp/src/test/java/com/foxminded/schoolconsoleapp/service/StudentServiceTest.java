package com.foxminded.schoolconsoleapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foxminded.schoolconsoleapp.dao.StudentsDao;
import com.foxminded.schoolconsoleapp.service.StudentService;
import com.foxminded.spring.console.tables.Courses;
import com.foxminded.spring.console.tables.Groups;
import com.foxminded.spring.console.tables.Students;

class StudentServiceTest {
	@Mock
	private StudentsDao studentsDao;
	@InjectMocks
	StudentService studentService = new StudentService();

	@BeforeEach
	public void setUp() throws IOException {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void generateTablesWhenTablesDoNotExistShoudGenerateTables() throws IOException {
		List<Courses> coursesList = new ArrayList<>();
		Courses courses = new Courses();
		courses.setCourseId(14);
		courses.setCourseName("Math");
		courses.setCourseDescription("Math!!");
		coursesList.add(courses);
		
		List<Students> studentsList = new ArrayList<>();
		Students students = new Students();
		students.setStudentId(45);
		students.setFirstName("Michael");
		students.setLastName("Taylor");
		students.setGroupId(3);
		students.setCoursesList(coursesList);
		studentsList.add(students);
		
		when(studentsDao.findAll()).thenReturn(studentsList);
		
		assertEquals(45, studentsDao.findAll().get(0).getStudentId());
		assertEquals("Michael", studentsDao.findAll().get(0).getFirstName());
		assertEquals("Taylor", studentsDao.findAll().get(0).getLastName());
		assertEquals(3, studentsDao.findAll().get(0).getGroupId());
		assertEquals(14, studentsDao.findAll().get(0).getCoursesList().get(0).getCourseId());
	}

	@Test
	void proxyStudentsGetStudentsByCourseNameWhenNormailShouldBeStudentsList() throws SQLException {
		List<Courses> coursesList = new ArrayList<>();
		Courses course1 = new Courses();
		course1.setCourseId(1);
		course1.setCourseName("Math");
		course1.setCourseDescription(" ");
		coursesList.add(course1);

		List<Students> studentsList = new ArrayList<>();
		Students student1 = new Students();
		student1.setStudentId(1);
		student1.setGroupId(3);
		student1.setFirstName("John");
		student1.setLastName("Doe");
		student1.setCoursesList(coursesList);
		studentsDao.save(student1);
		studentsList.add(student1);
		
		when(studentsDao.getStudentsByCourseName("Math")).thenReturn(studentsList);
		List<Students> result = studentService.getStudentsByCourseName("Math");

		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getStudentId());
		assertEquals("John", result.get(0).getFirstName());
		assertEquals("Doe", result.get(0).getLastName());
		assertEquals(3, result.get(0).getGroupId());
	}
	
	@Test
	void proxyStudentsAddStudentWhenNormalShouldReturnVoid() {
		Students student1 = new Students();
		student1.setStudentId(1);
		student1.setGroupId(3);
		student1.setFirstName("John");
		student1.setLastName("Doe");
		studentService.addStudent(student1);
		verify(studentsDao).addStudent(student1);
	}
	
	@Test
	void proxyStudentsDeleteStudentWhenNormalShouldReturnVoid() {
		Students student1 = new Students();
		student1.setStudentId(1);
		student1.setGroupId(3);
		student1.setFirstName("John");
		student1.setLastName("Doe");
		studentService.deleteStudent(1);
		verify(studentsDao).deleteStudent(1);
	}
	
	@Test
	void proxyStudentsDeleteStudentFromCourseWhenNormalShouldReturnVoid() {
		List<Courses> coursesList = new ArrayList<>();
		Courses courses = new Courses();
		courses.setCourseId(14);
		courses.setCourseName("Math");
		courses.setCourseDescription("Math!!");
		coursesList.add(courses);
		
		List<Students> studentsList = new ArrayList<>();
		Students students = new Students();
		students.setStudentId(45);
		students.setFirstName("Michael");
		students.setLastName("Taylor");
		students.setGroupId(3);
		students.setCoursesList(coursesList);
		studentsList.add(students);
		
		studentService.deleteStudentFromCourse(students.getStudentId(), courses.getCourseId());
		verify(studentsDao).deleteStudentFromCourse(students.getStudentId(), courses.getCourseId());
	}
}
