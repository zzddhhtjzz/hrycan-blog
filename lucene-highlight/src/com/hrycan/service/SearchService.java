package com.hrycan.service;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;

/**
 * @author Nicholas Hrycan
 *
 */
public interface SearchService {
	SearchResultContainer search(String searchTerm) throws IOException, ParseException;
	SearchResultContainer search(String searchTerm, int pageNumber, int pageSize) throws IOException, ParseException;
}	
