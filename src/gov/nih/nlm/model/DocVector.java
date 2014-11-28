package gov.nih.nlm.model;

import java.util.Map;

import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVectorFormat;
import org.apache.commons.math3.linear.SparseRealVector;

public class DocVector {

	public Map<String,Integer> dictionary;
	public SparseRealVector vector;
	public int id;
	public String content;

	public DocVector(int id, Map<String,Integer> dictionary) {
		this.id = id;
		this.dictionary = dictionary;
		this.vector = new OpenMapRealVector(dictionary.size());
	}

	public void setEntry(String term, double freq) {
		if (dictionary.containsKey(term)) {
			int pos = dictionary.get(term);
			vector.setEntry(pos, freq);
		}
	}

	public void normalize() {
		double sum = vector.getL1Norm();
		vector = (SparseRealVector) vector.mapDivide(sum);
	}

	public String toString() {
		RealVectorFormat formatter = new RealVectorFormat();
		return formatter.format(vector);
	}

	public void setContent(String content) {
		this.content = content;
	}


}


