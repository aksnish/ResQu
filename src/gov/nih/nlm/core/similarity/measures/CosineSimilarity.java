package gov.nih.nlm.core.similarity.measures;

import gov.nih.nlm.model.DocVector;

import java.util.List;

public class CosineSimilarity {

	public double getCosineSimilarity (List<DocVector> docVectrorList){
		double cosine = 0;
		for(DocVector d1 :docVectrorList){
			for(DocVector d2 :docVectrorList){
				if(d1.id!=d2.id){
					cosine = (d1.vector.dotProduct(d2.vector)) / (d1.vector.getNorm() * d2.vector.getNorm());
					System.out.println(""+ cosine);
				}
			}
		}
		return cosine;
	}

	public double getCosineSimilarity(DocVector d1, DocVector d2) {
		return (d1.vector.dotProduct(d2.vector)) /
				(d1.vector.getNorm() * d2.vector.getNorm());
	}

}
