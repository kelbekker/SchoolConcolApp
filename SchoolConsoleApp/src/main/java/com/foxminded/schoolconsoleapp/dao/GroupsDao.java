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

import com.foxminded.spring.console.tables.Groups;

public class GroupsDao implements SchoolDao<Groups> {

	private DataSource datasource = new DataSource();

	@Override
	public Groups save(Groups group) {
		String sql = "INSERT INTO groups (group_id, group_name) VALUES (?, ?)";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, group.getGroupId());
			statement.setString(2, group.getGroupName());
			statement.executeUpdate();

			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				int groupId = generatedKeys.getInt(1);
				group.setGroupId(groupId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return group;
	}

	@Override
	public Groups update(Groups group) {
		String sql = "UPDATE groups SET group_name = ? WHERE group_id = ?";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, group.getGroupName());
			statement.setInt(2, group.getGroupId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return group;
	}

	@Override
	public Groups findOne(int id) {
		String sql = "SELECT group_id, group_name FROM groups WHERE group_id = ?";
		Groups group = new Groups();

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {
					int groupId = resultSet.getInt("group_id");
					String groupName = resultSet.getString("group_name");

					group.setGroupId(groupId);
					group.setGroupName(groupName);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return group;
	}

	@Override
	public List<Groups> findAll() {
		List<Groups> groupsList = new ArrayList<>();
		String sql = "SELECT group_id, group_name FROM groups";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				int groupId = resultSet.getInt("group_id");
				String groupName = resultSet.getString("group_name");

				Groups group = new Groups();
				group.setGroupId(groupId);
				group.setGroupName(groupName);

				groupsList.add(group);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groupsList;
	}

	@Override
	public void delete(int id) {
		String sql = "DELETE FROM groups WHERE group_id = ?";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void createTable() throws IOException {
		final String SQL_SCRIPT_FILE = "groups creator.sql";
		String scriptContent = Files.readString(Paths.get(SQL_SCRIPT_FILE));

		try (Connection connection = datasource.createConnection()) {
			Statement statement = connection.createStatement();

			statement.executeUpdate(scriptContent);
			System.out.println("Database initialization successful.");

		} catch (SQLException e) {
			System.err.println("Failed to initialize the database: " + e.getMessage());
		}
	}

	public List<Groups> getGroupsWithLessOrEqualStudents(int maxStudents) {
		List<Groups> groups = new ArrayList<>();

		String sql = "SELECT g.group_id, g.group_name, COUNT(s.student_id) AS student_count "
				+ "FROM groups g LEFT JOIN students s ON g.group_id = s.group_id "
				+ "GROUP BY g.group_id, g.group_name " + "HAVING COUNT(s.student_id) <= ?";

		try (Connection connection = datasource.createConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, maxStudents);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Groups group = new Groups();
				group.setGroupId(resultSet.getInt("group_id"));
				group.setGroupName(resultSet.getString("group_name"));

				groups.add(group);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return groups;
	}

}