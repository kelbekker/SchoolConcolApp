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

import com.foxminded.spring.console.repository.CourseRepository;
import com.foxminded.spring.console.tables.Courses;
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        CourseRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest {

	@Autowired
	private CourseRepository courseRepository;
	    
	@Test
	void saveWhenNormalCourseShoudReternCourse() throws SQLException {
		
		Courses courses = new Courses();
		courses.setCourseId(4L);
		courses.setCourseName("Physics");
		courses.setCourseDescription("Course Number One!");

		Courses savedCourse = courseRepository.save(courses);

		assertEquals(4, savedCourse.getCourseId());
		assertEquals("Physics", savedCourse.getCourseName());
		assertEquals("Course Number One!", savedCourse.getCourseDescription());
	}

	@Test
	void updateWhenNormalCourseShoudReternCourse() throws SQLException {
		
		Courses courses = new Courses();
		courses.setCourseId(10L);
		courses.setCourseName("Physics");
		courses.setCourseDescription("Course Number One!");

		Courses updatedCourse = courseRepository.save(courses);

		assertEquals(10, updatedCourse.getCourseId());
	}

	@Test
	void findOneWhenNormalCourseShoudReternCourse() throws SQLException {

		Optional<Courses> oneCourse = courseRepository.findById(101L);

		assertEquals("Math", oneCourse.get().getCourseName());
	}

	@Test
	void findAllWhenNormalCourseShoudReternListCourses() throws SQLException {

		List<Courses> allCourses = courseRepository.findAll();
		assertEquals(3, allCourses.size());
	}

	@Test
	void deleteWhenNormalCourseShoudReternDeleteCourse() throws SQLException {

		courseRepository.deleteById(101L);
		assertEquals(2, courseRepository.findAll().size());
	}
}
