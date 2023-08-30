package com.foxminded.spring.console.service;

import java.util.List;
import java.util.Optional;
import com.foxminded.spring.console.tables.Courses;
public interface CourseService {
	
	public Optional<Courses> findOne(Long courseId);

	public List<Courses> findAll();

	public void delete(Long courseId);

	public Courses save(Courses course);

	public Courses update(Courses course);
	
}
