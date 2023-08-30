package com.foxminded.schoolconsoleapp.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.foxminded.schoolconsoleapp.dao.CoursesDao;
import com.foxminded.schoolconsoleapp.dao.StudentsDao;
import com.foxminded.spring.console.tables.Courses;
import com.foxminded.spring.console.tables.Students;

public class CourseService {

	private static Random random = new Random();
	private CoursesDao coursesDao = new CoursesDao();
	private StudentsDao studentsDao = new StudentsDao();

	public void generateTables() throws IOException {
		coursesDao.createTable();
		List<Courses> courses = generateCourses(10);
		List<Students> students = studentsDao.findAll();
		assignCoursesToStudents(students, courses);
	}

	private List<Courses> generateCourses(int count) {
		List<Courses> courses = new ArrayList<>();

		for (int i = 1; i < count; i++) {
			Courses course = new Courses();
			int courseId = i;
			course.setCourseName(getRandomCourseName());
			course.setCourseId(courseId);
			course.setCourseDescription("Course Number One!");
			coursesDao.save(course);
			courses.add(course);
		}

		return courses;
	}

	private String getRandomCourseName() {
		String[] courseNames = { "Math", "Biology", "Physics", "Chemistry", "History", "English", "Computer Science",
				"Art", "Music", "Geography" };
		int index = random.nextInt(courseNames.length);
		return courseNames[index];
	}

	private void assignCoursesToStudents(List<Students> students, List<Courses> allCourses) {

		for (Students student : students) {
			List<Courses> courses = new ArrayList<>();
			int numCourses = random.nextInt(3);

			for (int i = 0; i < numCourses; i++) {
				int courseIndex = random.nextInt(allCourses.size());
				Courses course = allCourses.get(courseIndex);
				courses.add(course);
				coursesDao.saveStudentCourse(allCourses.get(courseIndex).getCourseId(), student.getStudentId());
			}
		}
	}

	public void saveStudentCourse(int coursesId, int studentId) {
		coursesDao.saveStudentCourse(coursesId, studentId);
	}
}
