package com.foxminded.spring.console.tables;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
@Entity
public class Courses {

	@Id
	@Column(name = "course_id")
	private Long courseId;

	@Column(name = "course_name", length = 20)
	private String courseName;

	@Column(name = "course_description", length = 20)
	private String courseDescription;

	@ManyToMany(mappedBy = "coursesList")
	private Set<Students> students;

	public Set<Students> getStudents() {
		return students;
	}

	public void setStudents(Set<Students> students) {
		this.students = students;
	}

	public Courses() {
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public void addStudent(Students student) {
		if (students == null) {
			students = new HashSet<>();
		}
		students.add(student);
	}

	public void removeStudent(Students student) {
		if (students != null) {
			students.remove(student);
		}
	}
}
