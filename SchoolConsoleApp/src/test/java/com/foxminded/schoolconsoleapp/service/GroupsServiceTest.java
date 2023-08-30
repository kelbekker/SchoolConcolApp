package com.foxminded.schoolconsoleapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foxminded.schoolconsoleapp.dao.GroupsDao;
import com.foxminded.schoolconsoleapp.service.GroupsService;
import com.foxminded.spring.console.tables.Groups;

class GroupsServiceTest {
	@Mock
	private GroupsDao groupsDao;
	@InjectMocks
	GroupsService groupsService = new GroupsService();

	@BeforeEach
	public void setUp() throws IOException {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void generateTablesWhenTablesDoNotExistShoudGenerateTables() throws IOException {
		List<Groups> groupsList = new ArrayList<>();
		Groups groups = new Groups();
		groups.setGroupId(12);
		groups.setGroupName("HR-45");
		groupsList.add(groups);
		
		when(groupsDao.findAll()).thenReturn(groupsList);
		
		assertEquals(1, groupsDao.findAll().size());
		assertEquals(12, groupsDao.findAll().get(0).getGroupId());
		assertEquals("HR-45", groupsDao.findAll().get(0).getGroupName());
	}
	
	@Test
	void proxyGroupsGetGroupsWithLessOrEqualStudentsWhenNormalShouldReturnVoid() {
		List<Groups> groupsList = new ArrayList<>();
		Groups group1 = new Groups();
		group1.setGroupId(1);
		group1.setGroupName("JR-59");
		groupsDao.save(group1);

		Groups group2 = new Groups();
		group2.setGroupId(2);
		group2.setGroupName("AD-62");
		groupsDao.save(group2);
		groupsList.add(group1);
		groupsList.add(group2);
		
		when(groupsDao.getGroupsWithLessOrEqualStudents(5)).thenReturn(groupsList);
		List<Groups> result = groupsService.getGroupsWithLessOrEqualStudents(5);

		assertEquals(2, result.size());
		assertEquals(1, result.get(0).getGroupId());
		assertEquals(2, result.get(1).getGroupId());
		assertEquals("AD-62", result.get(1).getGroupName().trim());
		assertEquals("JR-59", result.get(0).getGroupName().trim());
	}

}
