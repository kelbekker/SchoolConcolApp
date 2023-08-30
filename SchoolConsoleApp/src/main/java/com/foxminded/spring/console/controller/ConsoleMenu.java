package com.foxminded.spring.console.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.foxminded.spring.console.service.GroupsServiceImpl;
import com.foxminded.spring.console.service.StudentServiceImpl;
import com.foxminded.spring.console.tables.Courses;
import com.foxminded.spring.console.tables.Groups;
import com.foxminded.spring.console.tables.Students;
@Controller
public class ConsoleMenu implements CommandLineRunner{
	private static final Logger logger = LoggerFactory.getLogger(ConsoleMenu.class);
	@Autowired
	private GroupsServiceImpl groupsServiceImpl = new GroupsServiceImpl();
	@Autowired
	private StudentServiceImpl studentServiceImpl;
	private Scanner scanner = new Scanner(System.in);
	private StringBuilder result = new StringBuilder();

	@Override
	public void run(String... args) throws Exception {
		displayMenu();
	}

	public void displayMenu() throws SQLException {
		int choice = 0;

		while (choice != 6) {
			logger.info("""
					\n=== Console Menu ===
					1. Find all the groups with less or equal student count
					2. Find all the students related to the course with the given name
					3. Add a new student
					4. Delete student by STUDENT_ID
					5. Add a student to the course
					6. Remove the student from one of their courses
					Enter your choice:
					""");
			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				logger.info("\nYou selected Option Get Groups With Less Or Equal Students.");
				System.out.print(optionGetGroupsWithLessOrEqualStudents());
				break;
			case 2:
				logger.info("\nYou selected Option Get Students By Course Name.");
				System.out.print(optionGetStudentsByCourseName());
				break;
			case 3:
				logger.info("\nYou selected Option Add Student.");
				optionAddStudent();
				break;
			case 4:
				logger.info("\nYou selected Option Delete Student.");
				optionDeleteStudent();
				break;
			case 5:
				logger.info("\nYou selected Option Add Student To Course.");
				optionAddStudentToCourse();
				break;
			case 6:
				logger.info("\nYou selected Option Delete Student From Course.");
				optionDeleteStudentFromCourse();
				break;
			default:
				logger.warn("\nInvalid choice! Please enter a number between 1 and 6.");
			}
		}

		scanner.close();
	}

	@GetMapping("/groups/less-or-equal-students")
	protected String optionGetGroupsWithLessOrEqualStudents() {

		logger.info("Enter the maximum number of students: \n");
		int maxStudents = scanner.nextInt();
		scanner.nextLine();

		List<Groups> groups = groupsServiceImpl.findByMaxStudentsLessThanEqual(maxStudents);

		result.append(String.format("Groups With Less Or Equal Students %s%n", maxStudents));
		for (Groups group : groups) {
			result.append(String.format("Group ID: %s%nGroup Name: %s%n%n", group.getGroupId(), group.getGroupName()));
		}
		logger.info("Groups with less or equal students retrieved successfully\n");
		return result.toString();
	}

	protected String optionGetStudentsByCourseName() throws SQLException {

		logger.info("Enter the course name: \n");
		String courseName = scanner.nextLine();

		List<Students> students = studentServiceImpl.getStudentsByCourseName(courseName);

		result.append(String.format("Students By %s course%n", courseName));

		for (Students student : students) {
			result.append(
					String.format("Student ID: %s%nFirst Name: %s%nLast Name: %s%n" + "Group ID: %s%n",
							student.getStudentId(), student.getFirstName(), student.getLastName(),
							student.getGroup().getGroupId()
					));
			Set<Courses> coursesList = student.getCoursesList();
			Courses corseId = coursesList.iterator().next();
			if (!coursesList.isEmpty()) {
				result.append(String.format("Course ID: %s%n", corseId.getCourseId()));
			}
			result.append("\n");
		}
		logger.info("\nGet students by course name retrieved successfully");
		return result.toString();
	}

	protected void optionAddStudent() {
		Students student = new Students();

		logger.info("\nEnter student ID: ");
		Long studentId = scanner.nextLong();
		scanner.nextLine();
		student.setStudentId(studentId);

		logger.info("\nEnter group ID: ");
		Long groupId = scanner.nextLong();
		scanner.nextLine();

		Optional<Groups> group = groupsServiceImpl.findOne(groupId);
		if (group.isEmpty()) {
			throw new IllegalArgumentException("Group does not exist.");
		}
		Long groupIdLong = groupId;
		
		group.get().setGroupId(groupIdLong);
		student.setGroup(group.get());


		logger.info("\nEnter first name: ");
		String firstName = scanner.nextLine();
		student.setFirstName(firstName);

		logger.info("\nEnter last name: ");
		String lastName = scanner.nextLine();
		student.setLastName(lastName);
		try {
			studentServiceImpl.save(student);

			logger.info("\nStudent added successfully");
		} catch (Exception e) {
			logger.error("Failed to add the student.");
			e.printStackTrace();
		}
	}

	protected void optionDeleteStudent() {
		logger.info("\nEnter student ID: ");
		Long studentId = scanner.nextLong();
		scanner.nextLine();
		try {
			studentServiceImpl.deleteStudent(studentId);

			logger.info("\nStudent deleted successfully");
		} catch (Exception e) {
			logger.error("Failed to delete the student.");
			e.printStackTrace();
		}
	}

	protected void optionAddStudentToCourse() {
		logger.info("\nEnter course ID: ");
		Long courseId = scanner.nextLong();
		scanner.nextLine();

		logger.info("\nEnter student ID: ");
		Long studentId = scanner.nextLong();
		scanner.nextLine();
		try {
			studentServiceImpl.addStudentToCourse(courseId, studentId);

			logger.info("\nStudent added to course successfully");
		} catch (Exception e) {
			logger.error("Failed to add the student to the course.");
			e.printStackTrace();
		}
	}

	protected void optionDeleteStudentFromCourse() {
		logger.info("\nEnter course ID: ");
		Long courseId = scanner.nextLong();
		scanner.nextLine();

		logger.info("\nEnter student ID: ");
		Long studentId = scanner.nextLong();
		scanner.nextLine();

		try {
			studentServiceImpl.deleteStudentFromCourse(studentId, courseId);

			logger.info("\nStudent deleted from course successfully");
		} catch (Exception e) {
			logger.error("Failed to delete the student from the course.");
			e.printStackTrace();
		}
	}
}
