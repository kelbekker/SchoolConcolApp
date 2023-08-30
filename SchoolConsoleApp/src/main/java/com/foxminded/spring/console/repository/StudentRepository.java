package com.foxminded.spring.console.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.foxminded.spring.console.tables.Students;
@Repository
public interface StudentRepository extends JpaRepository<Students, Long> {

	@Query("SELECT s FROM Students s JOIN s.coursesList c WHERE c.courseName = :courseName")
	List<Students> getStudentsByCourseName(@Param("courseName") String courseName);
}
