package com.foxminded.schoolconsoleapp.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.schoolconsoleapp.dao.DataSource;
import com.foxminded.schoolconsoleapp.dao.GroupsDao;
import com.foxminded.spring.console.tables.Groups;

class GroupsDaoTest {

	private GroupsDao groupsDao;
	private DataSource dataSource;
	private Connection connection;

	@BeforeEach
	public void setup() throws IOException, SQLException {
		groupsDao = new GroupsDao();
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
	void getGroupsWithLessOrEqualStudentsWhenmaxStudentsShouldBeListGroup() throws Exception {
		Groups group1 = new Groups();
		group1.setGroupId(1);
		group1.setGroupName("JR-59");
		groupsDao.save(group1);

		Groups group2 = new Groups();
		group2.setGroupId(2);
		group2.setGroupName("AD-62");
		groupsDao.save(group2);

		List<Groups> groupsList = groupsDao.getGroupsWithLessOrEqualStudents(5);

		assertEquals(2, groupsList.size());
		assertEquals(1, groupsList.get(0).getGroupId());
		assertEquals(2, groupsList.get(1).getGroupId());
		assertEquals("AD-62", groupsList.get(1).getGroupName().trim());
		assertEquals("JR-59", groupsList.get(0).getGroupName().trim());
	}

	@Test
	void saveWhenAddStudentShouldSaveGroupTable() throws SQLException {
		Groups group = new Groups();
		group.setGroupId(1);
		group.setGroupName("JS-59");

		Groups actualGroup = groupsDao.save(group);

		assertEquals(1, actualGroup.getGroupId());
		assertEquals("JS-59", actualGroup.getGroupName());
	}

	@Test
	void updateWhenUpdateStudentShouldUpdateGroupTable() throws SQLException {
		Groups group = new Groups();
		group.setGroupId(3);
		group.setGroupName("JS-59");

		groupsDao.save(group);

		group.setGroupName("JR-59");
		Groups updatedGroup = groupsDao.update(group);

		assertEquals(group, updatedGroup);
		assertEquals(3, updatedGroup.getGroupId());
		assertEquals("JR-59", updatedGroup.getGroupName());
	}

	@Test
	void findOneWhenNormalIdShouldRetrieveGroupFromTable() throws SQLException {
		Groups group = new Groups();
		group.setGroupId(1);
		group.setGroupName("JR-59");

		groupsDao.save(group);

		Groups actualGroup = groupsDao.findOne(group.getGroupId());

		assertEquals(1, actualGroup.getGroupId());
		assertEquals("JR-59", actualGroup.getGroupName());
	}

	@Test
	void findAllWhenNormalShouldRetrieveAllGroupsFromTable() throws SQLException, IOException {
		groupsDao = new GroupsDao();
		Groups group1 = new Groups();
		group1.setGroupId(1);
		group1.setGroupName("JR-59");
		groupsDao.save(group1);

		Groups group2 = new Groups();
		group2.setGroupId(2);
		group2.setGroupName("AD-62");
		groupsDao.save(group2);

		List<Groups> groupsList = groupsDao.findAll();

		assertEquals(2, groupsList.size());
		assertEquals(1, groupsList.get(0).getGroupId());
		assertEquals(2, groupsList.get(1).getGroupId());
		assertEquals("JR-59", groupsList.get(0).getGroupName());
		assertEquals("AD-62", groupsList.get(1).getGroupName());
	}

	@Test
	void deleteWhenNormalIdShouldDeleteGroup() throws SQLException {
		Groups group = new Groups();
		group.setGroupId(1);
		group.setGroupName("JR-59");
		groupsDao.save(group);

		groupsDao.delete(1);

		int deletedGroupId = groupsDao.findOne(1).getGroupId();

		assertEquals(0, deletedGroupId);
	}
}
