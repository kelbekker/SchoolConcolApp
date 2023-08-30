package com.foxminded.spring.console.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.foxminded.spring.console.tables.Groups;
@Repository
public interface GroupRepository extends JpaRepository<Groups, Long> {
	
	@Query("SELECT g FROM Groups g LEFT JOIN g.students s GROUP BY g.groupId, g.groupName HAVING COUNT(s) <= :maxStudents")
	List<Groups> findByMaxStudentsLessThanEqual(@Param("maxStudents")int maxStudents);
	
}
