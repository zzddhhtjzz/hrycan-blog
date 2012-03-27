package com.hrycan.prime.dao;

import java.util.List;

import com.hrycan.prime.entity.Employee;
import com.hrycan.prime.util.EmployeeQueryData;

public interface EmployeeDao extends ClassicModelsDao<Employee> {
	public Employee getEmployeeWithCustomers(Integer id);
	public List<Employee> findRange(int start, int end);
	public void findEmployees(EmployeeQueryData data);
	public List<Employee> findManagers();
}
