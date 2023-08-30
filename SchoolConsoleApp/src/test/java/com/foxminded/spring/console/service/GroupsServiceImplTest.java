package com.foxminded.spring.console.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.spring.console.repository.GroupRepository;
import com.foxminded.spring.console.tables.Groups;
@ExtendWith(MockitoExtension.class)
class GroupsServiceImplTest {
	@Mock
	private GroupRepository groupRepository;

	@InjectMocks
	private GroupsServiceImpl groupsServiceImpl;

	@BeforeEach
	public void setUp() throws IOException {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getGroupsWithLessOrEqualStudentsWhenNormalShouldReturnVoid() {
		List<Groups> groupsList = new ArrayList<>();
		Groups group1 = new Groups();
		group1.setGroupId(1L);
		group1.setGroupName("JR-59");
		groupRepository.save(group1);

		Groups group2 = new Groups();
		group2.setGroupId(2L);
		group2.setGroupName("AD-62");
		groupRepository.save(group2);
		groupsList.add(group1);
		groupsList.add(group2);

		when(groupRepository.findByMaxStudentsLessThanEqual(5)).thenReturn(groupsList);
		List<Groups> result = groupsServiceImpl.findByMaxStudentsLessThanEqual(5);

		assertEquals(2, result.size());
		assertEquals(1, result.get(0).getGroupId());
		assertEquals(2, result.get(1).getGroupId());
		assertEquals("AD-62", result.get(1).getGroupName().trim());
		assertEquals("JR-59", result.get(0).getGroupName().trim());
	}

	@Test
	void findOneWhenNormalShouldReturnOptionalGroups() {
		long groupId = 1L;
		Groups group = new Groups();
		group.setGroupId(groupId);
		group.setGroupName("Math Group");

		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

		Optional<Groups> result = groupsServiceImpl.findOne(groupId);

		assertEquals(Optional.of(group), result);
	}

	@Test
	void saveWhenNormalShouldReturnGroups() {
		long groupId = 1L;
		String groupName = "Math Group";
		Groups group = new Groups();
		group.setGroupId(groupId);
		group.setGroupName(groupName);

		when(groupRepository.save(group)).thenReturn(group);

		Groups savedGroup = groupsServiceImpl.save(group);

		assertEquals(groupId, savedGroup.getGroupId());
		assertEquals(groupName, savedGroup.getGroupName());

		verify(groupRepository).save(group);
	}

	@Test
	void updateWhenNormalShouldReturnGroups() {
		long groupId = 1L;
		String groupName = "Math Group";
		Groups group = new Groups();
		group.setGroupId(groupId);
		group.setGroupName(groupName);

		when(groupRepository.save(group)).thenReturn(group);

		Groups updatedGroup = groupsServiceImpl.update(group);

		assertEquals(groupId, updatedGroup.getGroupId());
		assertEquals(groupName, updatedGroup.getGroupName());

		verify(groupRepository).save(group);
	}

	@Test
	void findAllWhenNormalShouldReturnListGroups() {
		List<Groups> groupsList = new ArrayList<>();
		long groupId = 1L;
		String groupName = "Math Group";
		Groups group = new Groups();
		group.setGroupId(groupId);
		group.setGroupName(groupName);
		groupsList.add(group);
		
		Groups group2 = new Groups();
		group.setGroupId(2L);
		group.setGroupName("Science Group");
		groupsList.add(group2);

		when(groupRepository.findAll()).thenReturn(groupsList);

		List<Groups> result = groupsServiceImpl.findAll();

		assertEquals(groupsList.size(), result.size());
		assertEquals(groupsList.get(0).getGroupId(), result.get(0).getGroupId());
		assertEquals(groupsList.get(0).getGroupName(), result.get(0).getGroupName());
		assertEquals(groupsList.get(1).getGroupId(), result.get(1).getGroupId());
		assertEquals(groupsList.get(1).getGroupName(), result.get(1).getGroupName());
	}

	@Test
	void deleteWhenNormalShouldReturnVoid() {
		Long groupId = 1L;

		groupsServiceImpl.delete(groupId);

		verify(groupRepository).deleteById(groupId);
	}
}
