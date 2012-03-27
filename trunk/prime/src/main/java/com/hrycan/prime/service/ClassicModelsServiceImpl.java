package com.hrycan.prime.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import com.hrycan.prime.dao.CustomerDao;
import com.hrycan.prime.dao.EmployeeDao;
import com.hrycan.prime.dao.OfficeDao;
import com.hrycan.prime.entity.Employee;
import com.hrycan.prime.entity.Office;
import com.hrycan.prime.exception.DataNotFoundException;
import com.hrycan.prime.exception.StaleDataException;
import com.hrycan.prime.util.EmployeeQueryData;

@Transactional 
@Service(value="classicModelsService")
public class ClassicModelsServiceImpl implements ClassicModelsService {
	final Logger log = LoggerFactory.getLogger(ClassicModelsServiceImpl.class);
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	public int getOfficeCount() {
		int cnt = officeDao.count();
		return cnt;
	}

	public List<Office> findOffices(int start, int end) {
		return officeDao.findRange(start, end);
	}

	public Office findOffice(Object id) {
		return officeDao.find(id);
	}

	public List<Office> getAllOffices() {

		List<Office> offices = officeDao.findAll();	

		return offices;
	}

	public void create(Office o) {
		log.info("create office");
		officeDao.create(o);
		
	}

	public void edit(Office o) {
		
		officeDao.update(o);
	}

	public void remove(Office o) {
		officeDao.delete(o);
	}

	public int getEmployeeCount() {
		return employeeDao.count();
	}

	public List<Employee> findEmployees(int start, int end) {
		return employeeDao.findRange(start, end);
	}
	
	public void findEmployees(EmployeeQueryData data) {
		StopWatch sw = new StopWatch("findEmployees");
		sw.start();
		employeeDao.findEmployees(data);
		sw.stop();
		log.info(sw.shortSummary());
	}

	public Employee findEmployee(Object id) {
		return employeeDao.find(id);
	}

	public List<Employee> getAllEmployees() {
		return employeeDao.findAll();
	}

	public void create(Employee o) {
		log.info("create employee");
		
		employeeDao.create(o);
		
	}

	public void edit(Employee o) throws StaleDataException, DataNotFoundException {

		try {
			log.info("finding emp id " + o.getEmployeeNumber());
			//find
			Employee emp = employeeDao.find(o.getEmployeeNumber());
			if (emp == null) {
				log.warn("edit employee: cant find employeeNumber=" + o.getEmployeeNumber());
				throw new DataNotFoundException("cant find employeeNumber=" + o.getEmployeeNumber());
			} else {
				employeeDao.update(o);
				log.info("done updating emp id " + o.getEmployeeNumber());
			}
		} catch (JpaOptimisticLockingFailureException e) {
			log.warn(e.toString(), e);
			StaleDataException ste = new StaleDataException("edit Employee: id=" + o.getEmployeeNumber());
			throw ste;
		} 
	}

	public void remove(Employee o) throws StaleDataException {
		try {
			Employee emp = employeeDao.find(o.getEmployeeNumber());
			if (emp == null) {
				log.warn("remove employee: cant find employeeNumber=" + o.getEmployeeNumber());
				//do nothing, already deleted
			} else {
				employeeDao.delete(o);
			}
		} catch (JpaOptimisticLockingFailureException e) {
			log.warn(e.toString(), e);
			StaleDataException ste = new StaleDataException("remove Employee: id=" + o.getEmployeeNumber());
			throw ste;
		}
		
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	public void setEmployeeDao(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}

	public void setOfficeDao(OfficeDao officeDao) {
		this.officeDao = officeDao;
	}

	@Override
	public List<Employee> findManagers() {
		List<Employee> result = employeeDao.findManagers();
		return result;
	}

}
