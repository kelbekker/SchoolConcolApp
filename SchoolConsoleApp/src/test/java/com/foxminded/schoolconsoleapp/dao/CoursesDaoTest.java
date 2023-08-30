package com.foxminded.schoolconsoleapp.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.schoolconsoleapp.dao.CoursesDao;
import com.foxminded.schoolconsoleapp.dao.DataSource;
import com.foxminded.spring.console.tables.Courses;

class CoursesDaoTest {

	private CoursesDao coursesDao;
	private DataSource dataSource;
	private Connection connection;

	@BeforeEach
	public void setup() throws IOException, SQLException{
		coursesDao = new CoursesDao();
		dataSource = new DataSource();
        connection = dataSource.createConnection();
	}
	
	@AfterEach
    public void teardown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
	
	@Test
	void saveWhenAddCoursesShoudBeCoursesTable() throws SQLException {
		Courses course = new Courses();
		course.setCourseId(1);
		course.setCourseName("Math");

		Courses actualCourse = coursesDao.save(course);

		assertEquals(1, actualCourse.getCourseId());
		assertEquals("Math", actualCourse.getCourseName());
		coursesDao.delete(course.getCourseId());
	}

	@Test
	void updateWhenUpdateCourseShoudBeCourseTable() throws SQLException {
		Courses course = new Courses();
		course.setCourseId(1);
		course.setCourseName("Math 2");

		coursesDao.save(course);
		course.setCourseName("Math");

		Courses updatedCourse = coursesDao.update(course);

		assertEquals(course, updatedCourse);
		assertEquals(1, updatedCourse.getCourseId());
		coursesDao.delete(course.getCourseId());
	}

	@Test
	void findOneWhenNormalIdShoudBeCourseTable() throws SQLException {
		Courses course = new Courses();
		course.setCourseId(1);
		course.setCourseName("Math");
		course.setCourseDescription("Math");
		coursesDao.save(course);

		Courses actualCourse = coursesDao.findOne(1);

		assertEquals(1, actualCourse.getCourseId());
		assertEquals("Math", actualCourse.getCourseName());
		assertEquals("Math", actualCourse.getCourseDescription());
		coursesDao.delete(course.getCourseId());
	}

	@Test
	void findAllWhenNormalShoudBeCoursesList() throws SQLException {
		Courses course = new Courses();
		course.setCourseId(2);
		course.setCourseName("Math");
		course.setCourseDescription("Course Nuber One!");
		coursesDao.save(course);

		Courses course1 = new Courses();
		course1.setCourseId(3);
		course1.setCourseName("Physics");
		course1.setCourseDescription("Course Nuber One!");
		coursesDao.save(course1);

		List<Courses> CoursesList = coursesDao.findAll();

		assertEquals(2, CoursesList.size());
		assertEquals(2, CoursesList.get(0).getCourseId());
		assertEquals(3, CoursesList.get(1).getCourseId());
		assertEquals("Math", CoursesList.get(0).getCourseName());
		assertEquals("Physics", CoursesList.get(1).getCourseName());
		assertEquals("Course Nuber One!", CoursesList.get(1).getCourseDescription());
		
		coursesDao.delete(course.getCourseId());
		coursesDao.delete(course1.getCourseId());
	}

	@Test
	void deleteWhenNormalIdShoudDeleteCourse() throws SQLException {
		Courses course = new Courses();
		course.setCourseId(2);
		course.setCourseName("Math");
		course.setCourseDescription("Course Nuber One!");
		coursesDao.save(course);
		
		coursesDao.delete(course.getCourseId());

		int deletedGroupId = coursesDao.findOne(1).getCourseId();

		assertEquals(0, deletedGroupId);
	}
}
