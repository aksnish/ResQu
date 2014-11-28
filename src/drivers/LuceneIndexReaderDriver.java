package drivers;

import java.io.IOException;

import gov.nih.nlm.core.lucene.LuceneIndexReader;

public class LuceneIndexReaderDriver {
	public static void main(String[] args) throws IOException {
		String lucene_index_dir = "data/input_lucene";
		LuceneIndexReader indexReader = new LuceneIndexReader(lucene_index_dir);
		indexReader.readDocs();
		System.out.println("Total number of documents : "+indexReader.maxDocs());
	}
}
