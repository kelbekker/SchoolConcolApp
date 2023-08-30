package com.foxminded.spring.console.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.spring.console.repository.CourseRepository;
import com.foxminded.spring.console.tables.Courses;
@Service
public class CourseServiceImpl implements CourseService{
	
	@Autowired
	private CourseRepository courseRepository;

	@Override
	public Optional<Courses> findOne(Long courseId) {
		if (courseRepository.findById(courseId).isPresent()) {
            return courseRepository.findById(courseId);
        } else {
            return Optional.empty();
        }
	}

	@Override
	public List<Courses> findAll() {
		return courseRepository.findAll();
	}

	@Override
	public void delete(Long courseId) {
		courseRepository.deleteById(courseId);
	}

	@Override
	public Courses save(Courses course) {
		return courseRepository.save(course);
	}

	@Override
	public Courses update(Courses course) {
		return courseRepository.save(course);
	}
}
