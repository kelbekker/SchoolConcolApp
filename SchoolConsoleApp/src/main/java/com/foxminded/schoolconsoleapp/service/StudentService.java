package com.foxminded.schoolconsoleapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.foxminded.schoolconsoleapp.dao.StudentsDao;
import com.foxminded.spring.console.tables.Courses;
import com.foxminded.spring.console.tables.Students;

import java.io.IOException;
import java.sql.SQLException;

public class StudentService {

	private static Random random = new Random();
	private StudentsDao studentsDao = new StudentsDao();

	public List<Students> generateTables() throws IOException {
		studentsDao.createTable();
		final String[] FIRST_NAMES = { "John", "Alice", "Michael", "Emma", "David", "Olivia", "James", "Sophia",
				"William", "Emily" };
		final String[] LAST_NAMES = { "Smith", "Johnson", "Brown", "Taylor", "Miller", "Wilson", "Anderson", "Clark",
				"Walker", "Young" };

		return generateStudents(20, FIRST_NAMES, LAST_NAMES);
	}

	private List<Students> generateStudents(int count, String[] firstNames, String[] lastNames) {

		List<Students> students = new ArrayList<>();
		List<Courses> courses = new ArrayList<>();

		for (int i = 1; i < count + 1; i++) {
			String firstName = firstNames[random.nextInt(firstNames.length)];
			String lastName = lastNames[random.nextInt(lastNames.length)];
			int studentId = i;
			Students student = new Students();
			student.setStudentId(studentId);
			student.setGroupId(3);
			student.setFirstName(firstName);
			student.setLastName(lastName);
			student.setCoursesList(courses);
			Students actualStudent = studentsDao.save(student);
			students.add(actualStudent);
		}
		return students;
	}

	public List<Students> getStudentsByCourseName(String courseName) throws SQLException {
		List<Students> result = null;
		if (studentsDao.getStudentsByCourseName(courseName) != null) {
			result = studentsDao.getStudentsByCourseName(courseName);
		}
		return result;
	}

	public void addStudent(Students student) {
		studentsDao.addStudent(student);
	}

	public void deleteStudent(int studentId) {
		studentsDao.deleteStudent(studentId);
	}

	public void deleteStudentFromCourse(int studentId, int courseId) {
		studentsDao.deleteStudentFromCourse(studentId, courseId);
	}
}