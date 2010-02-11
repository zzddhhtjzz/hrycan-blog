package com.hrycan.service;

import java.io.Serializable;

/**
 * @author Nicholas Hrycan
 *
 */
public class SearchResult implements Serializable {
	private String[] fragments;
	private String filename;
	public String[] getFragments() {
		return fragments;
	}
	public void setFragments(String[] fragments) {
		this.fragments = fragments;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
