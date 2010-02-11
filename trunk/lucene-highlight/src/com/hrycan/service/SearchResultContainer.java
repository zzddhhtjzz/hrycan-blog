package com.hrycan.service;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Nicholas Hrycan
 *
 */
public class SearchResultContainer implements Serializable {
	private ArrayList<SearchResult> searchResults;
	private int totalHitCount;
	private String userInput;
	private long executionTime;
	
	public SearchResultContainer() {
		
	}
	
	public ArrayList<SearchResult> getSearchResults() {
		return searchResults;
	}
	public void setSearchResults(ArrayList<SearchResult> searchResults) {
		this.searchResults = searchResults;
	}
	public String getUserInput() {
		return userInput;
	}
	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
	public long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}
	public int getTotalHitCount() {
		return totalHitCount;
	}
	public void setTotalHitCount(int totalHitCount) {
		this.totalHitCount = totalHitCount;
	}
}
