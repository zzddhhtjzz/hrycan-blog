package com.hrycan.search;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *  @author Nicholas Hrycan
 *  </br>
 *	Simple example to demonstrate the searchAndUpdateDocument method of UpdateUtil
 *  usage: Updater indexdir filepath
 *  filepath is the identifier field of the documents in the index located in indexdir
 *
 */
public class Updater {
	protected static Logger log = Logger.getLogger(Updater.class);
	
	public static void main(String[] args) throws Exception {
		
		if (args.length != 2) {
			log.warn("usage: Updater <indexdir> <filepath>");
			return;
		}
		
		String indexDir = args[0];
		String filepath = args[1];
		
		Directory dir = FSDirectory.getDirectory(new File(indexDir));
		IndexWriter writer = null;
		IndexSearcher searcher = null;
		UpdateUtil updateUtil = new UpdateUtil();
		try {
			writer = new IndexWriter(dir, new StandardAnalyzer(), false, IndexWriter.MaxFieldLength.UNLIMITED);
			searcher = new IndexSearcher(writer.getDirectory());
		
			Document updateDoc = new Document();
			updateDoc.add(new Field("author", "Nicholas", Field.Store.YES, Field.Index.NOT_ANALYZED));
			updateDoc.add(new Field("source", "Reuters", Field.Store.YES, Field.Index.NOT_ANALYZED));
			updateDoc.add(new Field("section", "Small Business", Field.Store.YES, Field.Index.NOT_ANALYZED));
			updateDoc.add(new Field("date", DateTools.dateToString(new Date(), DateTools.Resolution.MINUTE), 
					Field.Store.YES, Field.Index.NOT_ANALYZED));
			
			Term term = new Term("filepath", filepath);
			updateUtil.searchAndUpdateDocument(writer, searcher, updateDoc, term);

		} finally {
			if (searcher != null) {
				searcher.close();
			}
			if (writer != null) {
				writer.commit();
				writer.optimize();
				writer.close();
			}
		}
	}
}
