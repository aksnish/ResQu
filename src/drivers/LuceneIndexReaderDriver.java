package drivers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;

import gov.nih.nlm.core.lucene.LuceneIndexReader;
import gov.nih.nlm.core.similarity.measures.MetricsWriter;
import gov.nih.nlm.core.similarity.measures.SimilarityScorer;
import gov.nih.nlm.model.DocVector;
import gov.nih.nlm.utils.Constants;
import gov.nih.nlm.utils.DirectoryFileListIterator;

public class LuceneIndexReaderDriver {

	public static void main(String[] args) throws IOException {
		String lucene_index_dir = Constants.LUCENE_INDEX_PATH;
		DirectoryFileListIterator dirFile = new DirectoryFileListIterator();
		MetricsWriter mwriter = new MetricsWriter();
		List<String> files = dirFile.getDirectoryFolderList(lucene_index_dir);
		LuceneIndexReader indexReader = null;
		List<Document> docList = null;
		List<DocVector> docVectrorList = null;
		SimilarityScorer simScore;
		StringBuilder sb = new StringBuilder();
		
		List<String> cosineSimList = new ArrayList<String>();
		List<String> eudDistList = new ArrayList<String>();

		for(String idxFile : files){
			String path_name = idxFile.substring(idxFile.lastIndexOf("/")+1, idxFile.length());
			try {
				indexReader =  new LuceneIndexReader(idxFile);
				docList = indexReader.getDocs();
				docVectrorList = indexReader.getDocVectors(docList); 
				simScore = new SimilarityScorer(docVectrorList);
				double cosineSim = simScore.getCosineSimilarity(docVectrorList);
				cosineSimList.add(Double.toString(cosineSim));
				double euclidean = simScore.getEuclideanDistance(docVectrorList);
				eudDistList.add(Double.toString(euclidean));
				mwriter.writeMetrics(path_name, cosineSimList,eudDistList);
				indexReader.close();
				break;
			}
			catch(Exception e){
				e.printStackTrace();
			}

		}
		System.out.println("Cosine sim list : "+ cosineSimList);
		System.out.println("Eud Dist list : "+ eudDistList);
	}
}
