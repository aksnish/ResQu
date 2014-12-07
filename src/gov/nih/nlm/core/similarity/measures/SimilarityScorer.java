package gov.nih.nlm.core.similarity.measures;

import gov.nih.nlm.model.DocVector;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import com.aliasi.cluster.KMeansClusterer;
import com.aliasi.matrix.SvdMatrix;
import com.aliasi.stats.Statistics;

public class SimilarityScorer {
	public List<DocVector> docVectrorList;
	public DocVector goldStdDocVec;
	private EuclideanDistance eud;
	public SimilarityScorer(List<DocVector> docVectrorList) {	
		Iterator<DocVector> list_ite = docVectrorList.iterator();
		while(list_ite.hasNext()){
			DocVector docs = list_ite.next();
			if(docs.type == 0){
				this.goldStdDocVec = docs;

				list_ite.remove();
			}
			this.docVectrorList=docVectrorList;
		}
	}

	public double getCosineSimilarity (List<DocVector> docVectrorList){
		double cosine = 0.0;
		for(DocVector d2 :docVectrorList){
			if(goldStdDocVec.id!=d2.id){
				cosine = (goldStdDocVec.matrix.dotProduct(d2.matrix)) / (goldStdDocVec.matrix.getNorm() * d2.matrix.getNorm());
				//				System.out.println("\t"+ cosine + "\t" + "{ gold_doc:"+goldStdDocVec.filename +goldStdDocVec.id+", doc: "+d2.id+"}" );
			}
			//				break;
		}
		return cosine;
	}

	public double getCosineSimilarity(DocVector d2) {
		return (goldStdDocVec.matrix.dotProduct(d2.matrix)) /
				(goldStdDocVec.matrix.getNorm() * d2.matrix.getNorm());
	}

	public double getEuclideanDistance(List<DocVector> docVectorList){
		eud = new EuclideanDistance();
		double [] array1 = goldStdDocVec.matrix.toArray(); 
		double [] array2 = null;
		for(DocVector docVec : docVectorList){
			array2 = docVec.matrix.toArray();
		}
		eud.compute(array1, array2);
		//		DocVecToArray [] dcarr = {new DocVecToArray(goldStdDocVec.id, goldStdDocVec.filename, goldStdDocVec.type)};
		//		System.out.println("\t"+ eudu + "\t" + "{ gold_doc:"+goldStdDocVec.filename +goldStdDocVec.id+", doc:}" );
		return eud.compute(array1, array2);
	}

	public double[] normalizeArray (double [] array){
		double max =0.0;
		for(int i=0; i<array.length;i++){
			if(max<=array[i]){
				max = array[i];
			}
		}

		//		System.out.println("max :"+max);
		final double[] result = new double[array.length];
		for(int i=0; i<array.length;i++){
			result[i]=array[i]/max;	
			//			System.out.println("after div arr : "+ result[i]);
		}
		return result;
	}

	public void printArray(double [] array){
		for(int i=0; i<array.length;i++){
			System.out.println(array[i]);
		}
	}

	public double getJSDivergence(List<DocVector> docVectorList){
		double [] array1 = goldStdDocVec.matrix.toArray(); 
		double [] array2 = null;
		for(DocVector docVec : docVectorList){
			array2 = docVec.matrix.toArray();
		}
		double [] resultG = normalizeArray(array1);
		double [] result = normalizeArray(array2);

		return Statistics.jsDivergence(resultG,result);
	}
}
