package com.hrycan.prime.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.hrycan.prime.dao.CustomerDao;
import com.hrycan.prime.entity.Customer;

@Repository("customerDao")
public class CustomerDaoJPA implements CustomerDao {
	final Logger log = LoggerFactory.getLogger(CustomerDaoJPA.class);
    protected EntityManager entityManager;
	
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
	public void create(Customer entity) {
		entityManager.persist(entity);
		
	}

	public void update(Customer entity) {
		entityManager.merge(entity);
		
	}

	public Customer find(Object id) {
		Customer c = entityManager.find(Customer.class, id);
		return c;
	}

	public List<Customer> findAll() {
		TypedQuery<Customer> query = entityManager.createNamedQuery("Customer.findAll", Customer.class);
		List<Customer> list = query.getResultList();
		return list;
	}

	public void delete(Customer entity) {
		entityManager.remove(entityManager.merge(entity));
		
	}
	
	public List<Customer> findRange(int start, int end) {
		TypedQuery<Customer> query = entityManager.createNamedQuery("Customer.findAll", Customer.class);
		query.setMaxResults(end - start);
		query.setFirstResult(start);
        return query.getResultList();
	}
	
	public int count() {
		Query query = entityManager.createNamedQuery("Customer.count");
		return ((Long) query.getSingleResult()).intValue();
	}

}
