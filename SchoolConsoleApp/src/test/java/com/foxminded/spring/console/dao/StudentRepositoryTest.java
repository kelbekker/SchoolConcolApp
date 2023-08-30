package com.foxminded.spring.console.dao;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.foxminded.spring.console.repository.GroupRepository;
import com.foxminded.spring.console.repository.StudentRepository;
import com.foxminded.spring.console.tables.Courses;
import com.foxminded.spring.console.tables.Groups;
import com.foxminded.spring.console.tables.Students;
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
		StudentRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryTest {

	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private GroupRepository groupRepository;

	@Test
	void saveWhenNormalStudentShoudReternStudent() throws SQLException {

		Students student = new Students();
		student.setStudentId(4L);
		student.setFirstName("Emma");
		student.setLastName("Johnson");
		student.setCoursesList(new HashSet<>());

		Optional<Groups> group = groupRepository.findById(101L);
		student.setGroup(group.get());

		Students savedStudent = studentRepository.save(student);

		assertEquals(4, savedStudent.getStudentId());
		assertEquals(group.get(), savedStudent.getGroup());
		assertEquals("Emma", savedStudent.getFirstName());
		assertEquals("Johnson", savedStudent.getLastName());
	}

	@Test
	void updateWhenNormalStudentShoudReternStudent() throws SQLException {

		Students student = new Students();
		student.setStudentId(10L);
		student.setFirstName("Emma");
		student.setLastName("Johnson");
		student.setCoursesList(new HashSet<>());

		Optional<Groups> group = groupRepository.findById(101L);
		student.setGroup(group.get());

		Students updatedStudent = studentRepository.save(student);

		assertEquals(10, updatedStudent.getStudentId());
	}

	@Test
	void findOneWhenNormalStudentShoudReternStudent() throws SQLException {
		Optional<Students> oneStudent = studentRepository.findById(101L);

		assertEquals("Doe", oneStudent.get().getLastName());
	}

	@Test
	void findAllWhenNormalStudentShoudReternListStudents() throws SQLException {

		List<Students> allStudents = studentRepository.findAll();
		assertEquals(3, allStudents.size());
	}

	@Test
	void deleteWhenNormalStudentShoudReternDeleteStudent() throws SQLException {

		studentRepository.deleteById(101L);
		assertEquals(2, studentRepository.findAll().size());
	}

	@Test
	void getStudentsByCourseNameWhenNormalStudentShoudReternDeleteStudent() throws SQLException {
		List<Students> students = studentRepository.getStudentsByCourseName("Math");

		Optional<Groups> group = groupRepository.findById(101L);

		assertEquals(1, students.size());
		assertEquals(101, students.get(0).getStudentId());
		assertEquals("Mohan", students.get(0).getFirstName());
		assertEquals("Doe", students.get(0).getLastName());
		assertEquals(group.get(), students.get(0).getGroup());

		Set<Courses> coursesList1 = students.get(0).getCoursesList();

		assertEquals(1, coursesList1.size());

		Courses course = coursesList1.iterator().next();
		assertEquals(101, course.getCourseId());
		assertEquals("Math", course.getCourseName());
	}
}