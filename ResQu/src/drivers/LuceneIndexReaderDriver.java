package drivers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.lucene.document.Document;

import gov.nih.nlm.core.lucene.LuceneIndexReader;
import gov.nih.nlm.core.similarity.measures.DivergenceMetrics;
import gov.nih.nlm.core.similarity.measures.MetricsWriter;
import gov.nih.nlm.core.similarity.measures.SimilarityScorer;
import gov.nih.nlm.model.DocVector;
import gov.nih.nlm.utils.Constants;
import gov.nih.nlm.utils.DirectoryFileListIterator;
import gov.nih.nlm.utils.SerializeToFile;

public class LuceneIndexReaderDriver {

	public static void main(String[] args) throws IOException {
		String lucene_index_dir = Constants.LUCENE_INDEX_PATH;
		DirectoryFileListIterator dirFile = new DirectoryFileListIterator();
		List<String> files = dirFile.getDirectoryFolderList(lucene_index_dir);
		LuceneIndexReader indexReader = null;
		List<Document> docList = null;
		List<DocVector> docVectrorList = null;
		SimilarityScorer simScore = null;
		StringBuilder sb = new StringBuilder();
		List<Double> cosineSimList = new ArrayList<Double>();
		List<Double> eudDistList = new ArrayList<Double>();
		List<Double> jsdList = new ArrayList<Double>();
		DivergenceMetrics jsd = new DivergenceMetrics();
		String path_name = null;
		SerializeToFile serf = new SerializeToFile();
		double cosineSim = 0;

		for(String idxFile : files){
			path_name = idxFile.substring(idxFile.lastIndexOf("/")+1, idxFile.length());
			try {
				indexReader =  new LuceneIndexReader(idxFile);
				docList = indexReader.getDocs();
				docVectrorList = indexReader.getDocVectors(docList); 
				simScore = new SimilarityScorer(docVectrorList);
				cosineSim = simScore.getCosineSimilarity(docVectrorList);
				//				cosineSimList.add(cosineSim);
				double euclidean = simScore.getEuclideanDistance(docVectrorList);
				//				eudDistList.add(euclidean);
				double jsD = simScore.getJSDivergence(docVectrorList);
				jsdList.add(jsD);
				indexReader.close();
				//				break;
			}
			catch(Exception e){
				e.printStackTrace();
			}
			//			Double[] eudArr = eudDistList.toArray(new Double[eudDistList.size()]);
			//			double[] toNormArr = ArrayUtils.toPrimitive(eudArr);
			//			double [] normArr = simScore.normalizeArray(toNormArr);
			//
			//			Double [] dArr = ArrayUtils.toObject(normArr);
			//			List<Double> list = Arrays.asList(dArr);
			//			serf.serializeList(list, path_name+"_eud");
			serf.serializeList(cosineSim, path_name+"_cos");
		}


		serf.readdSerializedFile(Constants.DATA_FOLDER+"r-lists/");


		//		Double[] eudArr = eudDistList.toArray(new Double[eudDistList.size()]);
		//		double[] toNormArr = ArrayUtils.toPrimitive(eudArr);
		//		double [] normArr = simScore.normalizeArray(toNormArr);
		//
		//		Double[] jsdArr = jsdList.toArray(new Double[jsdList.size()]);
		//		double[] jsdArrArr = ArrayUtils.toPrimitive(jsdArr);
		//		double [] normArrjsd = simScore.normalizeArray(jsdArrArr);
		//
		//		System.out.println("Cosine sim list : "+ cosineSimList);
		//		System.out.println("Eud Dist list : "+ Arrays.toString(normArr));
		//		System.out.println("JS Div list : "+ Arrays.toString(normArrjsd));
	}
}
