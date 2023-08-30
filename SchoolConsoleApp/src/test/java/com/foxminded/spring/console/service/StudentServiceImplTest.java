package com.foxminded.spring.console.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.spring.console.repository.CourseRepository;
import com.foxminded.spring.console.repository.StudentRepository;
import com.foxminded.spring.console.tables.Courses;
import com.foxminded.spring.console.tables.Groups;
import com.foxminded.spring.console.tables.Students;
@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

	@Mock
	private StudentRepository studentRepository;
	
	@Mock
	private CourseRepository courseRepository;

	@Mock
	private Students student;
	
	@Mock
	private Courses course;  
    
	@InjectMocks
	private StudentServiceImpl studentServiceImpl;

	@BeforeEach
	public void setUp() throws IOException {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getStudentsByCourseNameWhenNormailShouldBeStudentsList() throws SQLException {
		Set<Courses> coursesList = new HashSet<>();
		Courses course1 = new Courses();
		course1.setCourseId(1L);
		course1.setCourseName("Math");
		course1.setCourseDescription(" ");
		coursesList.add(course1);

		List<Students> studentsList = new ArrayList<>();
		Students student1 = new Students();
		student1.setStudentId(1L);
		student1.setFirstName("John");
		student1.setLastName("Doe");
		
		Groups group = new Groups();
		group.setGroupId(3L);
		student1.setGroup(group);
		
		studentRepository.save(student1);
		studentsList.add(student1);
		
		when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
		when(courseRepository.findById(101L)).thenReturn(Optional.of(course));
		studentServiceImpl.addStudentToCourse(101L, 1L);

		when(studentRepository.getStudentsByCourseName("Math")).thenReturn(studentsList);

		List<Students> result = studentServiceImpl.getStudentsByCourseName("Math");

		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getStudentId());
		assertEquals("John", result.get(0).getFirstName());
		assertEquals("Doe", result.get(0).getLastName());
		assertEquals(group, result.get(0).getGroup());
	}

	@Test
	void saveWhenNormalShouldReturnVoid() {
		Students student1 = new Students();
		student1.setStudentId(1L);
		student1.setFirstName("John");
		student1.setLastName("Doe");

		Groups group = new Groups();
		group.setGroupId(3L);
		student1.setGroup(group);

		studentRepository.save(student1);
		verify(studentRepository).save(student1);
	}

	@Test
	void deleteStudentWhenNormalShouldReturnVoid() {
		Students student1 = new Students();
		student1.setStudentId(1L);
		student1.setFirstName("John");
		student1.setLastName("Doe");

		Groups group = new Groups();
		group.setGroupId(3L);
		student1.setGroup(group);

		studentServiceImpl.deleteStudent(1L);
		verify(studentRepository).deleteById(1L);
	}

	@Test
	void deleteStudentFromCourseWhenNormalShouldReturnVoid() {
		Set<Courses> coursesSet = new HashSet<>();
		Courses courses = new Courses();
		courses.setCourseId(14L);
		courses.setCourseName("Math");
		courses.setCourseDescription("Math!!");
		coursesSet.add(courses);

		when(studentRepository.findById(45L)).thenReturn(Optional.of(student));
		when(courseRepository.findById(14L)).thenReturn(Optional.of(course));

		studentServiceImpl.deleteStudentFromCourse(45L, courses.getCourseId());

		assertEquals(14, courses.getCourseId());
		assertEquals(0, student.getCoursesList().size());
	}

	@Test
	void addStudentToCourseWhenNormalShouldReturnVoid() {
		Set<Courses> coursesSet = new HashSet<>();
		Courses courses = new Courses();
		courses.setCourseId(14L);
		courses.setCourseName("Math");
		courses.setCourseDescription("Math!!");
		coursesSet.add(courses);

		when(student.getCoursesList()).thenReturn(coursesSet);
		when(studentRepository.findById(45L)).thenReturn(Optional.of(student));
		when(courseRepository.findById(14L)).thenReturn(Optional.of(course));

		studentServiceImpl.addStudentToCourse(14L, 45L);

		assertEquals(14, courses.getCourseId());
		assertEquals(1, student.getCoursesList().size());
	}

	@Test
	void findOneWhenNormalShouldReturnOptionalStudents() {
		Students student = new Students();
		student.setStudentId(1L);
		when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

		Optional<Students> result = studentServiceImpl.findOne(1L);

		assertTrue(result.isPresent());
		assertEquals(1L, result.get().getStudentId());
		verify(studentRepository, times(1)).findById(1L);
	}

	@Test
	void findAllWhenNormalShouldReturnListStudents() {
		Students student1 = new Students();
		student1.setStudentId(1L);
		student1.setFirstName("John");
		student1.setLastName("Doe");

		Students student2 = new Students();
		student2.setStudentId(2L);
		student2.setFirstName("Jane");
		student2.setLastName("Smith");

		List<Students> studentsList = new ArrayList<>();
		studentsList.add(student1);
		studentsList.add(student2);

		when(studentRepository.findAll()).thenReturn(studentsList);

		List<Students> result = studentServiceImpl.findAll();

		assertEquals(2, result.size());

		Students resultStudent1 = result.get(0);
		assertEquals(1L, resultStudent1.getStudentId());
		assertEquals("John", resultStudent1.getFirstName());
		assertEquals("Doe", resultStudent1.getLastName());

		Students resultStudent2 = result.get(1);
		assertEquals(2L, resultStudent2.getStudentId());
		assertEquals("Jane", resultStudent2.getFirstName());
		assertEquals("Smith", resultStudent2.getLastName());
	}

	@Test
	void updateWhenNormalShouldReturnStudents() {
		Students student = new Students();
		student.setStudentId(1L);
		student.setFirstName("John");
		student.setLastName("Doe");

		when(studentRepository.save(student)).thenReturn(student);

		Students updatedStudent = studentServiceImpl.update(student);

		assertEquals(1L, updatedStudent.getStudentId());
		assertEquals("John", updatedStudent.getFirstName());
		assertEquals("Doe", updatedStudent.getLastName());
	}
}
