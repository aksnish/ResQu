package gov.nih.nlm.core.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneIndexReader {

	private IndexReader indexReader = null;
	private Directory directory ;

	public LuceneIndexReader(String dir){
		try {
			if(indexReader==null){
				directory = FSDirectory.open(new File(dir));
				indexReader = DirectoryReader.open(directory);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void readDocs() throws IOException {
		int numOfDocs = indexReader.numDocs();
		System.out.println("number of documents : "+numOfDocs);
		for ( int i = 0; i < numOfDocs; i++)
		{
			Document document = indexReader.document(i);
			System.out.println("\n Document : " + document);
		}
		indexReader.close();
	}

	public int maxDocs (){
		return indexReader.maxDoc();
	}
}
