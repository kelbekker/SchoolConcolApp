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

public class CoursesDao implements SchoolDao<Courses> {

	private DataSource datasource = new DataSource();

	@Override
	public Courses save(Courses course) {
		String sql = "INSERT INTO Courses (course_id, course_name, course_description) VALUES (?, ?, ?)";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, course.getCourseId());
			statement.setString(2, course.getCourseName());
			statement.setString(3, course.getCourseDescription());
			statement.executeUpdate();

			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				int courseId = generatedKeys.getInt(1);
				course.setCourseId(courseId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return course;
	}

	@Override
	public Courses update(Courses courses) {
		String sql = "UPDATE courses SET course_name = ? WHERE course_id = ?";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, courses.getCourseName());
			statement.setInt(2, courses.getCourseId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courses;
	}

	@Override
	public Courses findOne(int id) {
		String sql = "SELECT course_id, course_name, course_description FROM courses WHERE course_id = ?";
		Courses courses = new Courses();

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {

					courses.setCourseId(resultSet.getInt("course_id"));
					courses.setCourseName(resultSet.getString("course_name"));
					courses.setCourseDescription(resultSet.getString("course_description"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courses;
	}

	@Override
	public List<Courses> findAll() {
		List<Courses> coursesList = new ArrayList<>();
		String sql = "SELECT course_id, course_name, course_description FROM courses";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				int courseId = resultSet.getInt("course_id");
				String courseName = resultSet.getString("course_name");
				String courseDescription = resultSet.getString("course_description");

				Courses course = new Courses();
				course.setCourseId(courseId);
				course.setCourseName(courseName);
				course.setCourseDescription(courseDescription);

				coursesList.add(course);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return coursesList;
	}

	@Override
	public void delete(int id) {
		String sql = "DELETE FROM courses WHERE course_id = ?";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createTable() throws IOException {
		final String SQL_SCRIPT_FILE = "courses creator.sql";

		String scriptContent = Files.readString(Paths.get(SQL_SCRIPT_FILE));

		try (Connection connection = datasource.createConnection();
				Statement statement = connection.createStatement()) {

			statement.executeUpdate(scriptContent);
		} catch (SQLException e) {
			System.err.println("Failed to initialize the database: " + e.getMessage());
		}
	}

	public void saveStudentCourse(int coursesId, int studentId) {
		String sql = "INSERT INTO student_course (course_id, student_id) VALUES (?, ?)";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, coursesId);
			statement.setInt(2, studentId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}