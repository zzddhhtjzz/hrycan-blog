package com.hrycan.prime.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.hrycan.prime.dao.EmployeeDao;
import com.hrycan.prime.entity.Employee;
import com.hrycan.prime.entity.Employee_;
import com.hrycan.prime.entity.Office_;
import com.hrycan.prime.util.EmployeeQueryData;
import com.hrycan.prime.util.QuerySortOrder;

@Repository("employeeDao")
public class EmployeeDaoJPA implements EmployeeDao {
	final Logger log = LoggerFactory.getLogger(EmployeeDaoJPA.class);
    
	protected EntityManager entityManager;
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }	

	public void create(Employee entity) {
		entityManager.persist(entity);
		
	}

	public void update(Employee entity) {
		Employee e = entityManager.merge(entity);
		int version = e.getVersion();
	}

	public Employee find(Object id) {
		Employee e = entityManager.find(Employee.class, id);
		return e;
	}

	public List<Employee> findAll() {
		TypedQuery<Employee> query = entityManager.createNamedQuery("Employee.findAll", Employee.class);

		List<Employee> list = query.getResultList();
		return list;
	}

	public List<Employee> findManagers() {
		TypedQuery<Employee> query = entityManager.createNamedQuery("Employee.manager", Employee.class);
		List<Employee> list = query.getResultList();
		return list;
	}
	
	public void delete(Employee entity) {
		entityManager.remove(entityManager.merge(entity));
		
	}
	public Employee getEmployeeWithCustomers(Integer id) {
		TypedQuery<Employee> query = entityManager.createNamedQuery("Employee.withCustomers", Employee.class);
		query.setParameter("id", id);
		Employee e = query.getSingleResult();
		return e;
	}
	
	
	
	public void findEmployees(EmployeeQueryData data) {
		log.info("findEmployees: " + data.toString());
		Map<String, String> filters = data.getFilters();
		int start = data.getStart();
		int end = data.getEnd();
		String sortField = data.getSortField();
		QuerySortOrder order = data.getOrder();
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> c = cb.createQuery(Employee.class);
		Root<Employee> emp = c.from(Employee.class);
		c.select(emp);
//		emp.fetch(Employee_.office);
//		emp.fetch(Employee_.reportsTo);
		
		CriteriaQuery<Long> countQ = cb.createQuery(Long.class);
		Root<Employee> empCount = countQ.from(Employee.class);
		countQ.select(cb.count(empCount));
		
		String jobTitle = filters.get("jobTitle");
		String officeCity = filters.get("office.city");
		
		List<Predicate> criteria = new ArrayList<Predicate>();
		if (jobTitle != null) {
			ParameterExpression<String> pexp = cb.parameter(String.class, "jobTitle");
			Predicate predicate = cb.equal(emp.get(Employee_.jobTitle), pexp);
			criteria.add(predicate);
		}
		if (officeCity != null) {
			ParameterExpression<String> pexp = cb.parameter(String.class, "office.city");
			Predicate predicate = cb.equal(emp.get(Employee_.office).get(Office_.city), pexp);
			criteria.add(predicate);
		}
		
		if (criteria.size() == 1) {
			c.where(criteria.get(0));
			countQ.where(criteria.get(0));
		} else if (criteria.size() > 1) {
			c.where(cb.and(criteria.toArray(new Predicate[0])));
			countQ.where(cb.and(criteria.toArray(new Predicate[0])));
		}
		
		
		if (sortField != null) {
			Path<String> path = emp.get(Employee_.firstName);
			if (sortField.equalsIgnoreCase("lastName")) {
					path = emp.get(Employee_.lastName);
			} else if(sortField.equalsIgnoreCase("email")) {
					path = emp.get(Employee_.email);
			} else if(sortField.equalsIgnoreCase("jobTitle")) {
					path = emp.get(Employee_.jobTitle);
			} else if(sortField.equalsIgnoreCase("firstName")) {
					path = emp.get(Employee_.firstName);
			}
			if (order == QuerySortOrder.ASC) {
				c.orderBy(cb.asc(path));
			} else {
				c.orderBy(cb.desc(path));
			}
		}
		
		TypedQuery<Employee> q = entityManager.createQuery(c);
//		q.setHint("org.hibernate.cacheable", true);
		TypedQuery<Long> countquery = entityManager.createQuery(countQ);
//		countquery.setHint("org.hibernate.cacheable", true);
		
		if (criteria.size() >= 1) {
			if (jobTitle != null) {
				q.setParameter("jobTitle", jobTitle);
				countquery.setParameter("jobTitle", jobTitle);
			}
			if (officeCity != null) {
				q.setParameter("office.city", officeCity);
				countquery.setParameter("office.city", officeCity);
			}
		}
		
		q.setMaxResults(end - start);
		q.setFirstResult(start);
		
		data.setResult(q.getResultList());
		Long totalResultCount = countquery.getSingleResult();
		data.setTotalResultCount(totalResultCount);
		
	}		
	
	public List<Employee> findSortedRange(int start, int end) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> emp = cq.from(Employee.class);
		cq.select(emp);
		cq.orderBy(cb.desc(emp.get(Employee_.email)));
		TypedQuery<Employee> q = entityManager.createQuery(cq);
		q.setMaxResults(end - start);
		q.setFirstResult(start);
		return q.getResultList();
	}
	
	
	public List<Employee> findRange(int start, int end) {
		TypedQuery<Employee> query = entityManager.createNamedQuery("Employee.findAll", Employee.class);
		query.setMaxResults(end - start);
		query.setFirstResult(start);
		
        return query.getResultList();
	}
	
	public int count() {
		Query query = entityManager.createNamedQuery("Employee.count");
		return ((Long) query.getSingleResult()).intValue();
	}

}
