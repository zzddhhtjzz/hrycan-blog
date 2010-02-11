package com.hrycan.service;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

import com.hrycan.search.HighlighterUtil;
import com.hrycan.utils.ArrayLocation;
import com.hrycan.utils.Paginator;

/**
 * @author Nicholas Hrycan
 *
 */
public class SearchServiceImpl implements SearchService {

	protected static Logger log = Logger.getLogger(SearchServiceImpl.class);
	
	private int fragmentNumber;
	private int fragmentSize;
	private int maxNumberOfResults;
	private String searchField;
	
	private IndexSearcher searcher;
	private Paginator paginator;
	
	public SearchServiceImpl(String indexDirectory) throws CorruptIndexException, IOException {
		searcher = new IndexSearcher(indexDirectory);
	}
	
	
	/**
	 * @param searchTerm - user is searching the index for this item
	 * @param pageNumber - page of results requested by the user
	 * @param pageSize - number of results displayed per page
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public SearchResultContainer search(String searchTerm, int pageNumber, int pageSize) throws IOException, ParseException {
	
		SearchResultContainer container = new SearchResultContainer();
		ArrayList<SearchResult> result = new ArrayList<SearchResult>();
		
		long start = System.currentTimeMillis();
		
		HighlighterUtil hlu = new HighlighterUtil();
		
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser qp = new QueryParser(searchField, analyzer);
		
		log.debug("Searching index for: " + searchTerm);
		
		Query query = qp.parse(searchTerm);
		TopDocs hits = searcher.search(query, maxNumberOfResults);
		ArrayLocation arrayLocation = paginator.calculateArrayLocation(hits.scoreDocs.length, pageNumber, pageSize);
		
		for (int i = arrayLocation.getStart(); i < arrayLocation.getEnd(); i++) {
			SearchResult sr = new SearchResult();
			
			int docId = hits.scoreDocs[i].doc;
			
			//load the document
			Document doc = searcher.doc(docId);
			String filename = doc.get("filename");
			String contents =  doc.get(searchField);
			
			sr.setFilename(filename);
			
			log.debug("RESULT" + i + "-------------");
			log.debug("\t" + filename);
			
			//TermPositionVector termPosVector = (TermPositionVector) searcher.getIndexReader().getTermFreqVector(docId, fieldName);
			//String[] fragments = hlu.getFragmentsWithHighlightedTerms(termPosVector, query, fieldName, contents, fragmentNumber, fragmentSize);
			
			String[] fragments = hlu.getFragmentsWithHighlightedTerms(analyzer, query, searchField, contents, fragmentNumber, fragmentSize);
			sr.setFragments(fragments);
			result.add(sr);				
			
			log.debug("END" + i + "-------------");
			
		}
		
		long runtime = System.currentTimeMillis() - start;
		
		container.setUserInput(searchTerm);
		container.setTotalHitCount(hits.scoreDocs.length);
		container.setExecutionTime(runtime);
		container.setSearchResults(result);
		
		log.debug("Search complete in " + runtime + "ms");
		return container;
	}
	
	public SearchResultContainer search(String searchTerm) throws IOException, ParseException {
		SearchResultContainer container = new SearchResultContainer();
		ArrayList<SearchResult> result = new ArrayList<SearchResult>();
		
		long start = System.currentTimeMillis();
		
		HighlighterUtil hlu = new HighlighterUtil();
		
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser qp = new QueryParser(searchField, analyzer);
		
		log.debug("Searching index for: " + searchTerm);
		
		Query query = qp.parse(searchTerm);
		TopDocs hits = searcher.search(query, maxNumberOfResults);
		
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			SearchResult sr = new SearchResult();
			
			int docId = hits.scoreDocs[i].doc;
			Document doc = searcher.doc(docId);
			String filename = doc.get("filename");
			String contents =  doc.get(searchField);
			
			sr.setFilename(filename);
			
			log.debug("RESULT" + i + "-------------");
			log.debug("\t" + filename);
			
			//TermPositionVector termPosVector = (TermPositionVector) searcher.getIndexReader().getTermFreqVector(docId, fieldName);
			//String[] fragments = hlu.getFragmentsWithHighlightedTerms(termPosVector, query, fieldName, contents, fragmentNumber, fragmentSize);
			
			String[] fragments = hlu.getFragmentsWithHighlightedTerms(analyzer, query, searchField, contents, fragmentNumber, fragmentSize);
			sr.setFragments(fragments);
			result.add(sr);				
			
			log.debug("END" + i + "-------------");
			
		}
		
		long runtime = System.currentTimeMillis() - start;
		container.setUserInput(searchTerm);
		container.setTotalHitCount(hits.scoreDocs.length);
		container.setExecutionTime(runtime);
		container.setSearchResults(result);
		
		log.debug("Search complete in " + runtime + "ms");
		return container;
	}


	public void setFragmentNumber(int fragmentNumber) {
		this.fragmentNumber = fragmentNumber;
	}

	public void setFragmentSize(int fragmentSize) {
		this.fragmentSize = fragmentSize;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public void setMaxNumberOfResults(int maxNumberOfResults) {
		this.maxNumberOfResults = maxNumberOfResults;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
}
