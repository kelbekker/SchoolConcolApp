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

import com.foxminded.spring.console.repository.CourseRepository;
import com.foxminded.spring.console.tables.Courses;
@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

	@Mock
	private CourseRepository courseRepository;

	@InjectMocks
	private CourseServiceImpl courseServiceImpl;

	@BeforeEach
	public void setUp() throws IOException {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void findOneWhenNormalShouldReturnOptionalCourses() {
		long courseId = 1L;
		Courses course = new Courses();
		course.setCourseId(courseId);
		course.setCourseName("HT-83");

		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

		Optional<Courses> result = courseServiceImpl.findOne(courseId);

		assertEquals(Optional.of(course), result);
	}

	@Test
	void saveWhenNormalShouldReturnCourses() {
		Courses course = new Courses();
		course.setCourseId(1L);
		course.setCourseName("HT-83");

		when(courseRepository.save(course)).thenReturn(course);

		courseServiceImpl.save(course);
		verify(courseRepository).save(course);
	}

	@Test
	void updateWhenNormalShouldReturnCourses() {
		Courses course = new Courses();
		course.setCourseId(1L);
		course.setCourseName("HT-83");

		when(courseRepository.save(course)).thenReturn(course);

		Courses updatedCourse = courseServiceImpl.update(course);

		assertEquals(1L, updatedCourse.getCourseId());
		assertEquals("HT-83", updatedCourse.getCourseName());
	}

	@Test
	void findAllWhenNormalShouldReturnListCourses() {
		Courses course1 = new Courses();
		course1.setCourseId(1L);
		course1.setCourseName("HT-83");

		Courses course2 = new Courses();
		course2.setCourseId(2L);
		course2.setCourseName("YT-83");

		List<Courses> coursesList = new ArrayList<>();
		coursesList.add(course1);
		coursesList.add(course2);

		when(courseRepository.findAll()).thenReturn(coursesList);

		List<Courses> result = courseServiceImpl.findAll();

		assertEquals(2, result.size());
		assertEquals(1L, result.get(0).getCourseId());
		assertEquals("HT-83", result.get(0).getCourseName());
		assertEquals(2L, result.get(1).getCourseId());
		assertEquals("YT-83", result.get(1).getCourseName());
	}

	@Test
	void deleteCourseWhenNormalShouldReturnVoid() {
		Courses course = new Courses();
		course.setCourseId(1L);
		course.setCourseName("HT-83");

		courseServiceImpl.delete(1L);
		verify(courseRepository).deleteById(1L);
	}
}
