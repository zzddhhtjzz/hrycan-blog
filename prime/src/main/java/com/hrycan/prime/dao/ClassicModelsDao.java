package com.hrycan.prime.dao;

import java.util.List;

import com.hrycan.prime.entity.Customer;
import com.hrycan.prime.entity.Employee;
import com.hrycan.prime.entity.Office;

public interface ClassicModelsDao<T> {
	public void create(T entity);

	public void update(T entity);

	public T find(Object id);

	public List<T> findAll();

	public void delete(T entity);

	public List<T> findRange(int start, int end);

	public int count();

}
