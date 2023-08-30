package com.foxminded.spring.console.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.foxminded.spring.console.tables.Students;
public interface StudentService {
	
	public List<Students> getStudentsByCourseName(String courseName) throws SQLException;

	public void deleteStudent(Long studentId);

	public void deleteStudentFromCourse(Long studentId, Long courseId);

	public void addStudentToCourse(Long courseId, Long studentId);
	
	public List<Students> findAll();
	
	public Optional<Students> findOne(Long id);
	
	public Students update(Students student);
	
	public Students save(Students student);
}
