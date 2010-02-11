package com.hrycan.search;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.TermPositionVector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.SpanScorer;
import org.apache.lucene.search.highlight.TokenSources;

/**
 * @author Nicholas Hrycan
 *
 */
public class HighlighterUtil {

	/**
	 * Generates contextual fragments.  Assumes term vectors not stored in the index.
	 * @param analyzer - analyzer used for both indexing and searching
	 * @param query - query object created from user's input
	 * @param fieldName - name of the field in the lucene doc containing the text to be fragmented
	 * @param fieldContents - contents of fieldName
	 * @param fragmentNumber - max number of sentence fragments to return
	 * @param fragmentSize - the max number of characters for each fragment
	 * @return 
	 * 		array of fragments from fieldContents with terms used in query
	 * 		in <b> </b> tags
	 * @throws IOException 
	 */
	public String[] getFragmentsWithHighlightedTerms(Analyzer analyzer, Query query, 
			String fieldName, String fieldContents, int fragmentNumber, int fragmentSize) throws IOException {

		TokenStream stream = TokenSources.getTokenStream(fieldName, fieldContents, analyzer);
		SpanScorer scorer = new SpanScorer(query, fieldName,
				new CachingTokenFilter(stream));
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, fragmentSize);
		
		Highlighter highlighter = new Highlighter(scorer);
		highlighter.setTextFragmenter(fragmenter);
		highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
			
		String[] fragments = highlighter.getBestFragments(stream, fieldContents, fragmentNumber);
			
		return fragments;
	}
	

	/**
	 * Generates contextual fragments.
	 * @param termPosVector - Term Position Vector for fieldName
	 * @param query - query object created from user's input
	 * @param fieldName - name of the field containing the text to be fragmented
	 * @param fieldContents - contents of fieldName
	 * @param fragmentNumber - max number of sentence fragments to return
	 * @param fragmentSize - the max number of characters for each fragment
	 * @return 
	 * 		array of fragments from fieldContents with terms used in query
	 * 		in <b> </b> tags
	 * @return
	 * @throws IOException 
	 */
	public String[] getFragmentsWithHighlightedTerms(TermPositionVector termPosVector, Query query, 
			String fieldName, String fieldContents, int fragmentNumber, int fragmentSize) throws IOException  {
			
		TokenStream stream = TokenSources.getTokenStream(termPosVector);
		SpanScorer scorer = new SpanScorer(query, fieldName,
				new CachingTokenFilter(stream));
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, fragmentSize);
		Highlighter highlighter = new Highlighter(scorer);
		highlighter.setTextFragmenter(fragmenter);
		highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
				
		String[] fragments = highlighter.getBestFragments(stream, fieldContents, fragmentNumber);
			
		return fragments;
	}
	
}
