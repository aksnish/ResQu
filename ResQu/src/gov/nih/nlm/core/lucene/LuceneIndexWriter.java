package gov.nih.nlm.core.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



public class LuceneIndexWriter {

	private IndexWriter indexWriter = null;
	private Analyzer analyzer = null;
	private int id = 0;
	private Directory directory;

	public LuceneIndexWriter(String dir){
		try {
			analyzer = new StandardAnalyzer(StopAnalyzer.ENGLISH_STOP_WORDS_SET.EMPTY_SET);
			//			analyzer = new KeywordAnalyzer();
			if (indexWriter == null) {
				IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
				indexConfig.setOpenMode( IndexWriterConfig.OpenMode.CREATE);
				directory = FSDirectory.open(new File(dir));
				indexWriter = new IndexWriter(directory, indexConfig);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String content, int type, String filename){
		try {
			FieldType fieldType = new FieldType();
			fieldType.setStored(true);
			fieldType.setIndexed(true);
			fieldType.setStoreTermVectors(true);
			Document doc = new Document();
			doc.add(new Field("content", content, fieldType)); 
			doc.add(new Field("docId", Integer.toString(id++), fieldType));
			doc.add(new Field("type", Integer.toString(type) , fieldType)); 
			doc.add(new Field("filename", filename , fieldType)); 
			indexWriter.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close(){
		try {
			indexWriter.commit();
			indexWriter.close();
			directory.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
