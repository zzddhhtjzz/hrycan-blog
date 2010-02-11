package com.hrycan.search;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.TermPositionVector;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

/**
 * @author Nicholas Hrycan
 * 
 * Simple class to demonstrate the Lucene Highlighter
 * 
 * usage: Searcher <index dir> <search term>
 * 
 * index dir: same place where Indexer stored its output
 * search term: word you are searching for in the index
 * 
 * assumes documents in the index contain 2 fields: contents and filename
 *
 */
public class Searcher {
	protected static Logger log = Logger.getLogger(Searcher.class);
	
	private static int fragmentNumber = 4;
	private static int fragmentSize = 100;
	private static String fieldName = "contents";
	
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			log.warn("usage: Searcher <index dir> <search term>");
			return;
		} 
		
		String indexDir = args[0];
		String searchTerm = args[1];
		
		long start = System.currentTimeMillis();
		
		HighlighterUtil hlu = new HighlighterUtil();
		IndexSearcher searcher = new IndexSearcher(indexDir);
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser qp = new QueryParser(fieldName, analyzer);
		
		log.info("Searching index for: " + searchTerm);
		
		Query query = qp.parse(searchTerm);
		TopDocs hits = searcher.search(query, 10);
		
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			int docId = hits.scoreDocs[i].doc;
			Document doc = searcher.doc(docId);
			String filename = doc.get("filename");
			String contents =  doc.get(fieldName);
			
			log.info("RESULT" + i + "-------------");
			log.info("\t" + filename);
			
			//TermPositionVector termPosVector = (TermPositionVector) searcher.getIndexReader().getTermFreqVector(docId, fieldName);
			//String[] fragments = hlu.getFragmentsWithHighlightedTerms(termPosVector, query, fieldName, contents, fragmentNumber, fragmentSize);
			
			String[] fragments = hlu.getFragmentsWithHighlightedTerms(analyzer, query, fieldName, contents, fragmentNumber, fragmentSize);
			for (String fragment : fragments) {
				fragment = fragment.replaceAll("\\n", " ");
				log.info("\t\t" + fragment + "...");
			}
			log.info("END" + i + "-------------");
		}
		
		long runtime = System.currentTimeMillis() - start;
		log.info("Search complete in " + runtime + "ms");
	}
}
