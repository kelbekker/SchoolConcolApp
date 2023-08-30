package com.foxminded.schoolconsoleapp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.foxminded.schoolconsoleapp.dao.DataSource;
import com.foxminded.schoolconsoleapp.dao.GroupsDao;
import com.foxminded.spring.console.tables.Groups;
import com.foxminded.spring.console.tables.Students;

public class GroupsService {

	private static Random random = new Random();
	private GroupsDao groupsDao = new GroupsDao();

	public void generateTables() throws IOException {
		groupsDao.createTable();
		generateGroups(10);
	}

	private List<Groups> generateGroups(int count) {
		List<Groups> groups = new ArrayList<>();

		for (int i = 1; i < count + 1; i++) {
			String groupName = generateRandomGroupName(random);
			int groupId = i;
			Groups group = new Groups();
			group.setGroupName(groupName);
			group.setGroupId(groupId);
			groupsDao.save(group);
			groups.add(group);
		}
		return groups;
	}

	private String generateRandomGroupName(Random random) {
		StringBuilder groupName = new StringBuilder();
		groupName.append(getRandomUpperCaseLetter(random));
		groupName.append(getRandomUpperCaseLetter(random));
		groupName.append('-');
		groupName.append(getRandomDigit(random));
		groupName.append(getRandomDigit(random));
		return groupName.toString();
	}

	private char getRandomUpperCaseLetter(Random random) {
		return (char) (random.nextInt(26) + 'A');
	}

	private char getRandomDigit(Random random) {
		return (char) (random.nextInt(10) + '0');
	}

	public List<Groups> getGroupsWithLessOrEqualStudents(int maxStudents) {
		List<Groups> result = null;
		if (groupsDao.getGroupsWithLessOrEqualStudents(maxStudents) != null) {
			result = groupsDao.getGroupsWithLessOrEqualStudents(maxStudents);
		}
		return result;
	}
}
