package com.hrycan.prime.util;

import java.util.List;
import java.util.Map;


import com.hrycan.prime.entity.Employee;

public class EmployeeQueryData {

	private List<Employee> result;
	private Long totalResultCount;
	
	private int start;
	private int end;
	private String sortField;
	private QuerySortOrder order;
	private Map<String,String> filters;
	
	public EmployeeQueryData(int start, int end, String field, QuerySortOrder order, Map<String, String> filters) {
	
		this.start = start;
		this.end = end;
		this.sortField = field;
		this.order = order;
		this.filters = filters;
	}
	
	public List<Employee> getResult() {
		return result;
	}

	public Long getTotalResultCount() {
		return totalResultCount;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public QuerySortOrder getOrder() {
		return order;
	}

	public Map<String, String> getFilters() {
		return filters;
	}

	public void setResult(List<Employee> result) {
		this.result = result;
	}

	public void setTotalResultCount(Long totalResultCount) {
		this.totalResultCount = totalResultCount;
	}

	public String getSortField() {
		return sortField;
	}

	@Override
	public String toString() {
		return "EmployeeQueryData [start=" + start + ", end=" + end + ", sortField=" + sortField + ", order=" + order + ", filters="
				+ filters + "]";
	}
	
}
