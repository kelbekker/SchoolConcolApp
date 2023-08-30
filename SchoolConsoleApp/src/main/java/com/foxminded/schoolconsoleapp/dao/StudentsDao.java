package com.foxminded.schoolconsoleapp.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.spring.console.tables.Courses;
import com.foxminded.spring.console.tables.Students;

public class StudentsDao implements SchoolDao<Students> {
	private DataSource datasource = new DataSource();
	
	@Override
	public Students save(Students student) {
		String sql = "INSERT INTO students (student_id, group_id, first_name, last_name) VALUES (?, ?, ?, ?)";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			statement.setInt(1, student.getStudentId());
			statement.setInt(2, student.getGroupId());
			statement.setString(3, student.getFirstName());
			statement.setString(4, student.getLastName());
			statement.executeUpdate();

			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				int studentId = generatedKeys.getInt(1);
				student.setStudentId(studentId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return student;
	}

	@Override
	public Students update(Students student) {
		String sql = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, student.getGroupId());
			statement.setString(2, student.getFirstName());
			statement.setString(3, student.getLastName());
			statement.setInt(4, student.getStudentId());

			int rowsAffected = statement.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Student updated successfully.");
			} else {
				System.out.println("Failed to update student.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return student;
	}

	@Override
	public Students findOne(int id) {
		String sql = "SELECT s.group_id, s.first_name, s.last_name, c.course_id, c.course_name FROM students s "
				+ "JOIN student_course sc ON s.student_id = sc.student_id "
				+ "JOIN courses c ON sc.course_id = c.course_id " + "WHERE s.student_id = ?";

		Students student = new Students();
		List<Courses> coursesList = new ArrayList<>();

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					student = new Students();
					student.setStudentId(id);
					student.setGroupId(resultSet.getInt("group_id"));
					student.setFirstName(resultSet.getString("first_name"));
					student.setLastName(resultSet.getString("last_name"));

					Courses course = new Courses();
					course.setCourseId(resultSet.getInt("course_id"));
					course.setCourseName(resultSet.getString("course_name"));
					coursesList.add(course);

					student.setCoursesList(coursesList);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return student;
	}

	@Override
	public List<Students> findAll() {
		List<Students> studentsList = new ArrayList<>();
		String sql = "SELECT student_id, group_id, first_name, last_name FROM students";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				int studentId = resultSet.getInt("student_id");
				int groupId = resultSet.getInt("group_id");
				String firstName = resultSet.getString("first_name");
				String lastName = resultSet.getString("last_name");

				Students student = new Students();
				student.setStudentId(studentId);
				student.setGroupId(groupId);
				student.setFirstName(firstName);
				student.setLastName(lastName);

				studentsList.add(student);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return studentsList;
	}

	@Override
	public void delete(int id) {
		String deleteStudentSql = "DELETE FROM students WHERE student_id = ?";
		String deleteStudentCourseSql = "DELETE FROM student_course WHERE student_id = ?";

		try (Connection connection = datasource.createConnection();
				PreparedStatement deleteStudentStatement = connection.prepareStatement(deleteStudentSql);
				PreparedStatement deleteStudentCourseStatement = connection.prepareStatement(deleteStudentCourseSql)) {

			deleteStudentStatement.setInt(1, id);
			deleteStudentCourseStatement.setInt(1, id);

			deleteStudentCourseStatement.executeUpdate();
			deleteStudentStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createTable() throws IOException {
		final String SQL_SCRIPT_FILE = "students creator.sql";

		String scriptContent = Files.readString(Paths.get(SQL_SCRIPT_FILE));

		try (Connection connection = datasource.createConnection();
				Statement statement = connection.createStatement()) {

			statement.executeUpdate(scriptContent);
		} catch (SQLException e) {
			System.err.println("Failed to initialize the database: " + e.getMessage());
		}
	}

	public List<Students> getStudentsByCourseName(String courseName) throws SQLException {
		List<Students> students = new ArrayList<>();
		String sql = "SELECT s.student_id, s.group_id, s.first_name, s.last_name, c.course_id, c.course_name "
				+ "FROM students s " + "JOIN student_course sc ON s.student_id = sc.student_id "
				+ "JOIN courses c ON sc.course_id = c.course_id " + "WHERE c.course_name = ?";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, courseName);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Students student = new Students();
					student.setStudentId(resultSet.getInt("student_id"));
					student.setGroupId(resultSet.getInt("group_id"));
					student.setFirstName(resultSet.getString("first_name"));
					student.setLastName(resultSet.getString("last_name"));

					Courses course = new Courses();
					course.setCourseId(resultSet.getInt("course_id"));
					course.setCourseName(resultSet.getString("course_name"));

					List<Courses> coursesList = new ArrayList<>();
					coursesList.add(course);
					student.setCoursesList(coursesList);

					students.add(student);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return students;
		}
	}

	public void addStudent(Students student) {
		save(student);
	}

	public void deleteStudent(int studentId) {
		delete(studentId);
	}
	
	public void addStudentToCourse(int studentId, int courseId) {
		String sql = "INSERT INTO student_course (student_id, course_id) VALUES (?, ?)";

		Students student = new Students();
		List<Courses> coursesList = new ArrayList<>();
		Courses course = new Courses();

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, studentId);
			statement.setInt(2, courseId);
			statement.executeUpdate();

			course.setCourseId(courseId);
			coursesList.add(course);

			student.setCoursesList(coursesList);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void deleteStudentFromCourse(int studentId, int courseId) {
		String sql = "DELETE FROM student_course WHERE student_id = ? AND course_id = ?";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, studentId);
			statement.setInt(2, courseId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}