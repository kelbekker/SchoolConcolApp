package com.foxminded.schoolconsoleapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foxminded.schoolconsoleapp.dao.CoursesDao;
import com.foxminded.schoolconsoleapp.service.CourseService;
import com.foxminded.spring.console.tables.Courses;

class CourseServiceTest {

	@Mock
	private CoursesDao coursesDao;
	@InjectMocks
	CourseService courseService = new CourseService();

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

		when(coursesDao.findAll()).thenReturn(coursesList);

		assertEquals(1, coursesDao.findAll().size());
		assertEquals(14, coursesDao.findAll().get(0).getCourseId());
		assertEquals("Math", coursesDao.findAll().get(0).getCourseName());
		assertEquals("Math!!", coursesDao.findAll().get(0).getCourseDescription());
	}

	@Test
	void proxyCoursesSaveStudentCourseWhenNormalShouldReturnVoid() {
		courseService.saveStudentCourse(12, 4);
		verify(coursesDao).saveStudentCourse(12, 4);
	}
}
