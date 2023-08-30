package com.foxminded.spring.console.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.spring.console.repository.GroupRepository;
import com.foxminded.spring.console.tables.Groups;
@Service
public class GroupsServiceImpl implements GroupsService {

	@Autowired
	private GroupRepository groupRepository;

	@Override
	public List<Groups> findByMaxStudentsLessThanEqual(int maxStudents) {
		List<Groups> result = null;
		if (groupRepository.findByMaxStudentsLessThanEqual(maxStudents) != null) {
			result = groupRepository.findByMaxStudentsLessThanEqual(maxStudents);
		}
		return result;
	}

	@Override
	public Optional<Groups> findOne(Long groupId) {
		if (groupRepository.findById(groupId).isPresent()) {
			return groupRepository.findById(groupId);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Groups save(Groups group) {
		return groupRepository.save(group);
	}

	@Override
	public Groups update(Groups group) {
		return groupRepository.save(group);
	}

	@Override
	public List<Groups> findAll() {
		return groupRepository.findAll();
	}

	@Override
	public void delete(Long id) {
		groupRepository.deleteById(id);
	}
}