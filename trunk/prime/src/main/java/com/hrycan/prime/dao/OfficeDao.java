package com.hrycan.prime.dao;

import com.hrycan.prime.entity.Office;

public interface OfficeDao extends ClassicModelsDao<Office> {
	public Office getOfficeWithEmployees(Integer id);
}
