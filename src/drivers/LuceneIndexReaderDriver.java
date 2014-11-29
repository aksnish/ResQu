package drivers;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;

import gov.nih.nlm.core.lucene.LuceneIndexReader;
import gov.nih.nlm.core.similarity.measures.SimilarityScorer;
import gov.nih.nlm.model.DocVector;
import gov.nih.nlm.utils.Constants;
import gov.nih.nlm.utils.DirectoryFileListIterator;

public class LuceneIndexReaderDriver {
	public static void main(String[] args) throws IOException {

		String lucene_index_dir = Constants.LUCENE_INDEX_PATH;
		DirectoryFileListIterator dirFile = new DirectoryFileListIterator();
		List<String> files = dirFile.getDirectoryFolderList(lucene_index_dir);
		LuceneIndexReader indexReader = null;
		List<Document> docList = null;
		List<DocVector> docVectrorList = null;
		SimilarityScorer simScore;

		for(String idxFile : files){
			System.out.println("File: "+idxFile);
			try {
				indexReader =  new LuceneIndexReader(idxFile);
				docList = indexReader.getDocs();
				docVectrorList = indexReader.getDocVectors(docList); 

				System.out.println("Read: === "+docList.size());
				for(Document doc : docList){
					System.out.println("\t\t\t"+doc.get("docId"));
				}

				for(DocVector docVec : docVectrorList){
					System.out.println("DocVec id :"+docVec.id);
					//					System.out.println("DocVec contents :"+docVec.content);
				}
				simScore = new SimilarityScorer(docVectrorList);
				double cosineSim = simScore.getCosineSimilarity(docVectrorList);
				double euclidean = simScore.getEuclideanDistance(docVectrorList);
				indexReader.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		//end
	}
}
