package com.hrycan.search;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hrycan.service.SearchResult;
import com.hrycan.service.SearchResultContainer;
import com.hrycan.service.SearchService;

/**
 * @author Nicholas Hrycan
 * 
 * 
 *         usage: Searcher <search term>
 * 
 */
public class Searcher {
	protected static Logger log = Logger.getLogger(Searcher.class);

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			log.warn("usage: Searcher <search term>");
			return;
		}

		String searchTerm = args[0];

		ApplicationContext context = new ClassPathXmlApplicationContext("clientcontext.xml");
		SearchService service = (SearchService) context.getBean("searchService");
		log.info("Searching index for: " + searchTerm);
		long start = System.currentTimeMillis();
		
		SearchResultContainer resultContainer = service.search(searchTerm);
		long time = resultContainer.getExecutionTime();
		int hitCount = resultContainer.getTotalHitCount();
		String input = resultContainer.getUserInput();
		log.info(hitCount + " matches for " + input + " in " + time + "ms");
		
		ArrayList<SearchResult> result = resultContainer.getSearchResults();
		
		for (SearchResult sr : result) {
			String filename = sr.getFilename();
			log.info("\t" + filename);

			String[] fragments = sr.getFragments();
			for (String fragment : fragments) {
				fragment = fragment.replaceAll("\\n", " ");
				log.info("\t\t" + fragment + "...");
			}
		}
		
		long runtime = System.currentTimeMillis() - start;
	    log.info("Search complete in " + runtime + "ms");
		
	}
}
