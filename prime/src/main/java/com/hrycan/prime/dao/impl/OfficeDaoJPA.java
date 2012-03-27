package com.hrycan.prime.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.hrycan.prime.dao.OfficeDao;
import com.hrycan.prime.entity.Office;


@Repository("officeDao")
public class OfficeDaoJPA implements OfficeDao {
	final Logger log = LoggerFactory.getLogger(OfficeDaoJPA.class);
    protected EntityManager entityManager;
	
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
	public void create(Office entity) {
		entityManager.persist(entity);
		
	}

	public void update(Office entity) {
		entityManager.merge(entity);
		
	}

	public Office find(Object id) {
		Office o = entityManager.find(Office.class, id);
		return o;
	}

	public List<Office> findAll() {
		TypedQuery<Office> query = entityManager.createNamedQuery("Office.findAll", Office.class);
		List<Office> list = query.getResultList();
		return list;
	}

	public void delete(Office entity) {
		entityManager.remove(entityManager.merge(entity));
		
	}

	public Office getOfficeWithEmployees(Integer id) {
		TypedQuery<Office> query = entityManager.createNamedQuery("Office.withEmployees", Office.class);
		query.setParameter("officeCode", id);
		Office o = query.getSingleResult();
		return o;
	}

	public List<Office> findRange(int start, int end) {
		TypedQuery<Office> query = entityManager.createNamedQuery("Office.findAll", Office.class);
		query.setMaxResults(end - start);
		query.setFirstResult(start);
        return query.getResultList();
	}
	
	public int count() {
		Query query = entityManager.createNamedQuery("Office.count");
		return ((Long) query.getSingleResult()).intValue();
	}

}
