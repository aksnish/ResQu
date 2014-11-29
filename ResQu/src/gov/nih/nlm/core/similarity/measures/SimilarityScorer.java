package gov.nih.nlm.core.similarity.measures;

import gov.nih.nlm.model.DocVector;

import java.util.Iterator;
import java.util.List;

public class SimilarityScorer {
	public List<DocVector> docVectrorList;
	public DocVector goldStdDocVec;

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
		double cosine = 0;
			for(DocVector d2 :docVectrorList){
				if(goldStdDocVec.id!=d2.id){
					cosine = (goldStdDocVec.matrix.dotProduct(d2.matrix)) / (goldStdDocVec.matrix.getNorm() * d2.matrix.getNorm());
					System.out.println("\t"+ cosine + "\t" + "{ doc:" +goldStdDocVec.id+", doc: "+d2.id+"}" );
				}
//				break;
		}
		return cosine;
	}


	public double getCosineSimilarity(DocVector d2) {
		return (goldStdDocVec.matrix.dotProduct(d2.matrix)) /
				(goldStdDocVec.matrix.getNorm() * d2.matrix.getNorm());
	}

}
