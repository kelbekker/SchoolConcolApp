package com.foxminded.spring.console.dao;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.foxminded.spring.console.repository.GroupRepository;
import com.foxminded.spring.console.tables.Groups;
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		GroupRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GroupRepositoryTest {

	@Autowired
	private GroupRepository groupRepository;

	@Test
	void saveWhenNormalGroupShoudReternGroup() throws SQLException {

		Groups groups = new Groups();
		groups.setGroupId(4L);
		groups.setGroupName("PH-89");

		Groups savedGroup = groupRepository.save(groups);

		assertEquals(4, savedGroup.getGroupId());
		assertEquals("PH-89", savedGroup.getGroupName());
	}

	@Test
	void updateWhenNormalGroupShoudReternGroup() throws SQLException {

		Groups groups = new Groups();
		groups.setGroupId(5L);
		groups.setGroupName("PH-89");

		Groups updatedGroup = groupRepository.save(groups);

		assertEquals(5, updatedGroup.getGroupId());
	}

	@Test
	void findOneWhenNormalGroupShoudReternGroup() throws SQLException {

		Optional<Groups> oneGroup = groupRepository.findById(102L);

		assertEquals("YN-93", oneGroup.get().getGroupName());
	}

	@Test
	void findAllWhenNormalGroupShoudReternListGroups() throws SQLException {

		List<Groups> allGroups = groupRepository.findAll();
		assertEquals(3, allGroups.size());
	}

	@Test
	void deleteWhenNormalStudentShoudReternDeleteStudent() throws SQLException {

		groupRepository.deleteById(101L);
		assertEquals(2, groupRepository.findAll().size());
	}

	@Test
	void findByMaxStudentsLessThanEqualWhenNormalStudentShoudReternDeleteStudent() throws SQLException {
		Groups group1 = new Groups();
		group1.setGroupId(1L);
		group1.setGroupName("JR-59");
		groupRepository.save(group1);

		Groups group2 = new Groups();
		group2.setGroupId(2L);
		group2.setGroupName("AD-62");
		groupRepository.save(group2);

		List<Groups> groupsList = groupRepository.findByMaxStudentsLessThanEqual(2);

		assertEquals(5, groupsList.size());
		assertEquals(1, groupsList.get(0).getGroupId());
		assertEquals(2, groupsList.get(1).getGroupId());
		assertEquals("AD-62", groupsList.get(1).getGroupName().trim());
		assertEquals("JR-59", groupsList.get(0).getGroupName().trim());
	}
}
