package com.hrycan.search;

import java.io.File;
import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author Nicholas Hrycan
 *
 * Simple Lucene indexer class to build an index to use with the Searcher
 * in order to demonstrate the Lucene Highlighter
 * 
 * usage: Indexer <index dir> <data dir>
 * 
 * index dir: where the index will be stored
 * data dir: dir containing plain txt files
 */
public class Indexer {
	protected static Logger log = Logger.getLogger(Indexer.class);
	
	public static void main(String[] args) throws Exception {
		
		if (args.length != 2) {
			log.warn("usage: Indexer <index dir> <data dir>");
			return;
		} 
		
		String indexDir = args[0];
		String dataDir = args[1];
		
		long start = System.currentTimeMillis();
		
		Directory dir = FSDirectory.getDirectory(new File(indexDir));
		IndexWriter writer = new IndexWriter(dir, new StandardAnalyzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
		
		File[] files = new File(dataDir).listFiles();
		for (File file : files) {
			if (file.getName().endsWith(".txt")) {
				Document doc = new Document();
				String contents = readFile(file);
				doc.add(new Field("contents", contents, Field.Store.COMPRESS, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
				doc.add(new Field("filepath", file.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				writer.addDocument(doc);     
			}
		}
		
		int numDocsIndexed = writer.numDocs();
		
		writer.optimize();
		writer.close();  
		
		long total = System.currentTimeMillis() - start;
		log.info("Indexing complete: indexed " + numDocsIndexed + " files in " + total + "ms.  " +
				"average=" + (total/numDocsIndexed) + "ms per file.");
		
	}
	
	public static String readFile(File file) throws Exception {
		String result = "";
		FileInputStream fin = new FileInputStream(file);

		byte fileContent[] = new byte[(int) file.length()];
		fin.read(fileContent);
		result = new String(fileContent);
		fin.close();
		
		return result;
	}
	
}
