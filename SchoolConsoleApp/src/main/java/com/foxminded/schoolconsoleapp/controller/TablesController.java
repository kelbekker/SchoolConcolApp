package com.foxminded.schoolconsoleapp.controller;

import java.sql.SQLException;
import java.util.List;

import com.foxminded.schoolconsoleapp.service.CourseService;
import com.foxminded.schoolconsoleapp.service.GroupsService;
import com.foxminded.schoolconsoleapp.service.StudentService;
import com.foxminded.spring.console.tables.Groups;
import com.foxminded.spring.console.tables.Students;

public class TablesController {

	StudentService studentService = new StudentService();
	CourseService courseService = new CourseService();
	GroupsService groupsService = new GroupsService();

	public String formattedGetGroupsWithLessOrEqualStudentsResult(int maxStudents) {
		List<Groups> groups = groupsService.getGroupsWithLessOrEqualStudents(maxStudents);

		StringBuilder result = new StringBuilder();
		result.append(String.format("Groups With Less Or Equal Students %s%n", maxStudents));
		for (Groups group : groups) {
			result.append(String.format("Group ID: %s%nGroup Name: %s%n%n", group.getGroupId(), group.getGroupName()));
		}

		return result.toString();
	}

	public String formattedGetStudentsByCourseNameResult(String courseName) throws SQLException {
		List<Students> students = studentService.getStudentsByCourseName(courseName);

		StringBuilder result = new StringBuilder();
		result.append(String.format("Students By %s course%n", courseName));

		for (Students student : students) {
			result.append(
					String.format("Student ID: %s%nFirst Name: %s%nLast Name: %s%n" + "Group ID: %s%nCourse ID: %s%n%n",
							student.getStudentId(), student.getFirstName(), student.getLastName(), student.getGroupId(),
							student.getCoursesList().get(0).getCourseId()));
		}

		return result.toString();
	}

	public String formattedDeleteStudentResult(int studentId) {
		studentService.deleteStudent(studentId);

		StringBuilder result = new StringBuilder();
		result.append(String.format("Student number %s deleted%n", studentId));
		return result.toString();
	}

	public String formattedAddStudentToCourseResult(int studentId, int courseId) {
		courseService.saveStudentCourse(studentId, courseId);

		StringBuilder result = new StringBuilder();
		result.append(String.format("Student number %s added to course number %s%n", studentId, courseId));
		return result.toString();
	}

	public String formattedDeleteStudentFromCourseResult(int studentId, int courseId) {
		studentService.deleteStudentFromCourse(studentId, courseId);

		StringBuilder result = new StringBuilder();
		result.append(String.format("Student number %s deleted from course number %s%n", studentId, courseId));
		return result.toString();
	}

	public String formattedAddStudentResult(Students student) {
		studentService.addStudent(student);

		StringBuilder result = new StringBuilder();
		result.append(String.format("Student number %s added%n", student.getStudentId()));
		return result.toString();
	}
}
