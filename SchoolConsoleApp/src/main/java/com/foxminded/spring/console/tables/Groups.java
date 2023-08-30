package com.foxminded.spring.console.tables;

import java.util.Set;
import jakarta.persistence.*;
@Entity
public class Groups {

	@Id
	@Column(name = "group_id")
	private Long groupId;
	
	@Column(name = "group_name")
	private String groupName;

	@OneToMany(mappedBy = "group")
	private Set<Students> students;
	
	public Groups() {
    }
	
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
