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
import com.foxminded.schoolconsoleapp.dao.StudentsDao;
import com.foxminded.spring.console.tables.Courses;
import com.foxminded.spring.console.tables.Students;

class StudentsDaoTest {

	private StudentsDao studentsDao;
	private CoursesDao coursesDao;
	private DataSource dataSource;
	private Connection connection;

	@BeforeEach
	public void setup() throws IOException, SQLException {
		studentsDao = new StudentsDao();
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
	void getStudentsByCourseNameWhenNormalCourseNameShoudBeListStudents() throws SQLException {

		Courses course1 = new Courses();
		course1.setCourseId(1);
		course1.setCourseName("Math");
		course1.setCourseDescription(" ");
		coursesDao.save(course1);

		Students student1 = new Students();
		student1.setStudentId(1);
		student1.setGroupId(3);
		student1.setFirstName("John");
		student1.setLastName("Doe");
		studentsDao.save(student1);

		studentsDao.addStudentToCourse(student1.getStudentId(), course1.getCourseId());

		List<Students> students = studentsDao.getStudentsByCourseName("Math");

		assertEquals(1, students.size());
		assertEquals(1, students.get(0).getStudentId());
		assertEquals("John", students.get(0).getFirstName());
		assertEquals("Doe", students.get(0).getLastName());
		assertEquals(3, students.get(0).getGroupId());

		List<Courses> coursesList1 = students.get(0).getCoursesList();

		assertEquals(1, coursesList1.size());

		assertEquals(1, coursesList1.get(0).getCourseId());
		assertEquals("Math", coursesList1.get(0).getCourseName());
	}

	@Test
	void saveWhenAddStudentShoudBeSaveStudentTable() throws SQLException {
		Students student = new Students();
		student.setStudentId(1);
		student.setGroupId(1);
		student.setFirstName("John");
		student.setLastName("Doe");

		Students actualStudent = studentsDao.save(student);

		assertEquals(1, actualStudent.getStudentId());
	}

	@Test
	void findOneWhenNormalIdShoudBeStudentTable() throws SQLException {
		Students student = new Students();
		student.setStudentId(2);
		student.setGroupId(2);
		student.setFirstName("Mark");
		student.setLastName("Doe");
		studentsDao.save(student);

		Courses course1 = new Courses();
		course1.setCourseId(3);
		course1.setCourseName("Math");
		course1.setCourseDescription(" ");
		coursesDao.save(course1);

		studentsDao.addStudentToCourse(student.getStudentId(), course1.getCourseId());

		Students actualStudent = studentsDao.findOne(2);

		assertEquals(2, actualStudent.getStudentId());
		assertEquals("Mark", actualStudent.getFirstName());
		assertEquals("Doe", actualStudent.getLastName());
	}

	@Test
	void updateWhenUpdateStudentShoudBeStudentTable() throws SQLException {
		Students student = new Students();
		student.setStudentId(2);
		student.setGroupId(2);
		student.setFirstName("Mark");
		student.setLastName("Doe");

		studentsDao.save(student);

		student.setFirstName("James");
		Students updatedStudent = studentsDao.update(student);

		assertEquals(student, updatedStudent);
		assertEquals(2, updatedStudent.getStudentId());
		assertEquals("James", updatedStudent.getFirstName());
	}

	@Test
	void findAllWhenNormalShoudBeStudentsList() throws SQLException {
		Students student = new Students();
		student.setStudentId(2);
		student.setGroupId(2);
		student.setFirstName("Mark");
		student.setLastName("Doe");
		studentsDao.save(student);

		Students student1 = new Students();
		student1.setStudentId(3);
		student1.setGroupId(2);
		student1.setFirstName("Mark");
		student1.setLastName("Doe");
		studentsDao.save(student1);

		List<Students> StudentsList = studentsDao.findAll();

		assertEquals(2, StudentsList.size());
		assertEquals(2, StudentsList.get(0).getStudentId());
		assertEquals(3, StudentsList.get(1).getStudentId());
		assertEquals(2, StudentsList.get(0).getGroupId());
		assertEquals(2, StudentsList.get(1).getGroupId());
		assertEquals("Mark", StudentsList.get(1).getFirstName());
	}

	@Test
	void deleteWhenNormalIdShoudDeleteStudent() throws SQLException {
		Students student = new Students();
		student.setStudentId(2);
		student.setGroupId(2);
		student.setFirstName("Mark");
		student.setLastName("Doe");
		studentsDao.save(student);

		assertEquals(2, student.getGroupId());
		studentsDao.delete(2);

		int deletedGroupId = studentsDao.findOne(2).getGroupId();

		assertEquals(0, deletedGroupId);
	}

	@Test
	void addStudentToCourseWhenNormalStudentIdAndCourseIdShoudAddStudentToCourse() throws SQLException {

		Courses course1 = new Courses();
		course1.setCourseId(3);
		course1.setCourseName("Math");
		course1.setCourseDescription(" ");
		coursesDao.save(course1);

		Students student1 = new Students();
		student1.setStudentId(1);
		student1.setGroupId(3);
		student1.setFirstName("John");
		student1.setLastName("Doe");
		studentsDao.save(student1);

		studentsDao.addStudentToCourse(student1.getStudentId(), course1.getCourseId());

		Students student = studentsDao.findOne(student1.getStudentId());

		assertEquals(3, student.getCoursesList().get(0).getCourseId());
		assertEquals("Math", student.getCoursesList().get(0).getCourseName());
	}

	@Test
    void deleteStudentFromCourseWhenNormalIdShouldDeleteStudent() throws SQLException {
		Students student1 = new Students();
		student1.setStudentId(1);
		student1.setGroupId(3);
		student1.setFirstName("John");
		student1.setLastName("Doe");
		studentsDao.save(student1);

		Courses course1 = new Courses();
		course1.setCourseId(3);
		course1.setCourseName("Math");
		course1.setCourseDescription(" ");
		coursesDao.save(course1);
		
		studentsDao.addStudentToCourse(student1.getStudentId(), course1.getCourseId());
		
		studentsDao.deleteStudentFromCourse(student1.getStudentId(), course1.getCourseId());
		
		int deletedGroupId = studentsDao.findAll().get(0).getGroupId();

		assertEquals(3, deletedGroupId);
	}

}
