package gov.nih.nlm.core.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneIndexWriter {


	private IndexWriter indexWriter = null;
	private Analyzer analyzer = null;

	public LuceneIndexWriter(String dir){
		try {
			analyzer = new StandardAnalyzer();
			if (indexWriter == null) {
				IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);

				Directory directory = FSDirectory.open(new File(dir));
				indexWriter = new IndexWriter(directory, indexConfig);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write (String content){
		//		  Document doc = new Document();
		//		    String text = "This is the text to be indexed.";
		//		    doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
		//		    iwriter.addDocument(doc)
	}

	public void close(){
		try {
			indexWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
