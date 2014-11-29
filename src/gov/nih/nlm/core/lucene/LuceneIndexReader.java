package gov.nih.nlm.core.lucene;

import gov.nih.nlm.model.DocVector;
import gov.nih.nlm.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class LuceneIndexReader {

	private IndexReader indexReader = null;
	private Directory directory ;
	private Map<String,Integer> dictionary;

	public LuceneIndexReader(String dir){
		try {
			if(indexReader==null){
				directory = FSDirectory.open(new File(dir));
				indexReader = DirectoryReader.open(directory);
				createDictionary();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public List<Document> getDocs() {
		List<Document> docList = new ArrayList<Document>();
		Document doc;
		try {
			int numOfDocs = indexReader.maxDoc();
			for ( int i = 0; i < numOfDocs; i++)
			{
				doc = indexReader.document(i);
				docList.add(doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return docList;
	}

	public List<DocVector> getDocVectors(List<Document> docList) {
		List<DocVector> docVecList = new ArrayList<DocVector>();
		Document doc;
		DocVector docVec = null;
		String content;
		try {
			int numOfDocs = indexReader.maxDoc();
			for ( int i = 0; i < numOfDocs; i++)
			{
				doc = indexReader.document(i);
				int id = Integer.parseInt(doc.get("docId"));
				int type = Integer.parseInt(doc.get("type"));
				content = doc.get(Constants.CONTENT_FIELD);
				Terms terms = indexReader.getTermVector(id,Constants.CONTENT_FIELD); 
				if (terms != null && terms.size() > 0) {
					docVec = new DocVector(id, dictionary, type);
					docVec.setContent(content);
					TermsEnum termsEnum = terms.iterator(null);
					BytesRef term = null;
					while ((term = termsEnum.next()) != null) {
						docVec.setEntry(term.utf8ToString(), Double.parseDouble(Long.toString(termsEnum.totalTermFreq())));
						//docVec.setEntry(term.utf8ToString(), Double.parseDouble(Long.toString(termsEnum.totalTermFreq())), indexReader, Constants.CONTENT_FIELD);
					}
					docVecList.add(docVec);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return docVecList;
	}



	public void createDictionary(){
		dictionary = new HashMap<String,Integer>();
		try {
			int pos = 0;
			Terms indexTerms = SlowCompositeReaderWrapper.wrap(indexReader).terms(Constants.CONTENT_FIELD);
			TermsEnum termsEnum = indexTerms.iterator(null);
			while ((termsEnum.next()) != null) {
				dictionary.put(termsEnum.term().utf8ToString(), pos++);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void close(){
		try {
			indexReader.close();
			directory.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
