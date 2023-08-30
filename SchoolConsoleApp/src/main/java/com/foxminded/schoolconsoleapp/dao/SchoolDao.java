package com.foxminded.schoolconsoleapp.dao;

import java.util.List;

public interface SchoolDao<T> {
	
	T save(T model);

	T update(T model);

	T findOne(int id);

	List<T> findAll();

	void delete(int id);
}