package drivers;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;

import gov.nih.nlm.core.lucene.LuceneIndexReader;
import gov.nih.nlm.core.similarity.measures.CosineSimilarity;
import gov.nih.nlm.model.DocVector;

public class LuceneIndexReaderDriver {
	public static void main(String[] args) throws IOException {
		String lucene_index_dir = "data/input_lucene/";
		LuceneIndexReader indexReader = new LuceneIndexReader(lucene_index_dir);
		CosineSimilarity cosine = new CosineSimilarity();
		List<Document> docList =indexReader.getDocs();
		List<DocVector> docVectrorList = indexReader.getDocVectors(docList); 
		double cosineSim = cosine.getCosineSimilarity(docVectrorList);
		indexReader.close();
	}
}
