package com.foxminded.spring.console.tables;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
@Entity
public class Students {

	@Id
	@Column(name = "student_id")
	private Long studentId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@ManyToMany
	(fetch = FetchType.EAGER)
	@JoinTable(
			name = "student_course",
			joinColumns = @JoinColumn(name = "student_id"),
			inverseJoinColumns = @JoinColumn(name = "course_id")
			)
	private Set<Courses> coursesList;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private Groups group;
	
	public Students() {
	}

	public Groups getGroup() {
		return group;
	}

	public void setGroup(Groups group) {
		this.group = group;
	}
	
	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<Courses> getCoursesList() {
		return coursesList != null ? coursesList : Collections.emptySet();
	}

	public void setCoursesList(Set<Courses> coursesList) {
		this.coursesList = coursesList;
	}

	public void addCourse(Courses course) {
		if (coursesList == null) {
			coursesList = new HashSet<>();
		}
		coursesList.add(course);
	}

	public void removeCourse(Courses course) {
		if (coursesList != null) {
			coursesList.remove(course);
		}
	}
}
