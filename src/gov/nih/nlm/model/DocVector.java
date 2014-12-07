package gov.nih.nlm.model;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVectorFormat;
import org.apache.commons.math3.linear.SparseRealVector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

public class DocVector {

	public Map<String,Integer> dictionary;
	public SparseRealVector matrix;
	public int id;
	public String content;
	public int type;
	public String filename;

	public DocVector(int id, Map<String,Integer> dictionary, int type, String filename) {
		this.id = id;
		this.type = type;
		this.filename = filename;
		this.dictionary = dictionary;
//		System.out.println("Dictional matrix :"+dictionary.toString());
		this.matrix = new OpenMapRealVector(dictionary.size());
	}

	public void setEntry(String term, double freq) {
		if (dictionary.containsKey(term)) {
			int pos = dictionary.get(term);
			matrix.setEntry(pos, freq);
//			System.out.println("Term : "+ term + "  Pos: " + pos + " Freq : " + freq );//+ "matrix : " + matrix.toString().replaceAll(";", ","));
			
		}
	}

	public void setEntry(String term, double tf , IndexReader reader, String field) {
		double idf, df;
		int N = reader.numDocs();
		if (dictionary.containsKey(term)) {
			int pos = dictionary.get(term);
			try {
				df = reader.docFreq(new Term(term, field));
				idf = Math.log(N/df);
				double TFIDF = tf*idf;
				matrix.setEntry(pos, TFIDF);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void normalize() {
		double sum = matrix.getL1Norm();
		matrix = (SparseRealVector) matrix.mapDivide(sum);
	}

	public String toString() {
		RealVectorFormat formatter = new RealVectorFormat();
		return formatter.format(matrix);
	}

	public void setContent(String content) {
		this.content = content;
	}
}


