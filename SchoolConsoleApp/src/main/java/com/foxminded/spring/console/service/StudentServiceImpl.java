package com.foxminded.spring.console.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.spring.console.repository.CourseRepository;
import com.foxminded.spring.console.repository.StudentRepository;
import com.foxminded.spring.console.tables.Courses;
import com.foxminded.spring.console.tables.Students;
@Service
public class StudentServiceImpl implements StudentService {	
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private CourseRepository courseRepository;

	private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
	
	@Override
	public List<Students> getStudentsByCourseName(String courseName) throws SQLException {
		List<Students> result = null;
		if (studentRepository.getStudentsByCourseName(courseName) != null) {
			result = studentRepository.getStudentsByCourseName(courseName);
		}
		return result;
	}

	@Override
	public void deleteStudent(Long studentId) {
		studentRepository.deleteById(studentId);
	}

	@Override
	public void deleteStudentFromCourse(Long studentId, Long courseId) {
		try {
			logger.debug("Going to delete the student from the course.");

			Optional<Students> student = studentRepository.findById(studentId);
			Optional<Courses> course = courseRepository.findById(courseId);

			if (student.isPresent() && course.isPresent()) {
				student.get().removeCourse(course.get());
				studentRepository.save(student.get());
			} else {
				logger.error("Failed to delete the student from the course."
						+ " Invalid studentId or courseId.");
			}
		} catch (Exception e) {
			logger.error("Failed to delete the student from the course.");
			e.printStackTrace();
		}
	}

	public void addStudentToCourse(Long courseId, Long studentId) {
		try {
			logger.debug("Going to add the student to the course.");

			Optional<Students> student = studentRepository.findById(studentId);
			Optional<Courses> course = courseRepository.findById(courseId);

			if (student.isPresent() && course.isPresent()) {
				student.get().addCourse(course.get());
				studentRepository.save(student.get());
			} else {
				logger.error("Failed to add the student to the course. "
						+ "Invalid studentId or courseId.");
			}
		} catch (Exception e) {
			logger.error("Failed to add the student to the course.");
			e.printStackTrace();
		}
	}

	@Override
	public List<Students> findAll() {
		return studentRepository.findAll();
	}

	@Override
	public Optional<Students> findOne(Long id) {
		Optional<Students> studentOptional = studentRepository.findById(id);
		return studentOptional.isPresent() ? studentOptional : Optional.empty();
	}

	@Override
	public Students update(Students student) {
		return studentRepository.save(student);
	}

	@Override
	public Students save(Students student) {
		return studentRepository.save(student);
	}
}
