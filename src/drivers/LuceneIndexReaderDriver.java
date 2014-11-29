package drivers;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;

import gov.nih.nlm.core.lucene.LuceneIndexReader;
import gov.nih.nlm.core.similarity.measures.SimilarityScorer;
import gov.nih.nlm.model.DocVector;

public class LuceneIndexReaderDriver {
	public static void main(String[] args) throws IOException {
		String lucene_index_dir = "data/input_lucene/";
		LuceneIndexReader indexReader = new LuceneIndexReader(lucene_index_dir);

		List<Document> docList =indexReader.getDocs();
		List<DocVector> docVectrorList = indexReader.getDocVectors(docList); 

		SimilarityScorer cosine = new SimilarityScorer(docVectrorList);

		for(DocVector docVec : docVectrorList){
			//			System.out.println("DocVec id :"+docVec.id);
			//			System.out.println("DocVec contents :"+docVec.content);

		}

		double cosineSim = cosine.getCosineSimilarity(docVectrorList);
		//System.out.println(""+ cosineSim);


		indexReader.close();
	}
}
