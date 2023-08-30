package com.foxminded.spring.console.service;

import java.util.List;
import java.util.Optional;
import com.foxminded.spring.console.tables.Groups;
public interface GroupsService {
	
	public List<Groups> findByMaxStudentsLessThanEqual(int maxStudents);
	
	public Optional<Groups> findOne(Long groupId);
	
	public Groups save(Groups group);

	public Groups update(Groups group);

	public List<Groups> findAll();

	public void delete(Long id);
}
