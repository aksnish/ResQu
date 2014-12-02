package gov.nih.nlm.core.similarity.measures;

import com.aliasi.stats.Statistics;

public class DivergenceMetrics {
	public static final double log2 = Math.log(2);

	public static double klDivergence(double[] P, double[] Q) {
		double klDiv = 0.0;
		for (int i = 0; i < P.length; ++i) {
			if (P[i] == 0.0) { 
				continue; 
			}
			if (Q[i] == 0.0) { 
				continue; 
			} 
			klDiv += P[i] * Math.log( P[i] / Q[i] );
		}
		return klDiv/ log2;
	}

	public static double jsDivergence(double[] P, double[] Q) {
		assert(P.length == Q.length);
		double[] modKLArr = new double[P.length];
		for (int i = 0; i < P.length; ++i) {
			modKLArr[i] += (P[i] + Q[i])/2;
		}
		return (klDivergence(P, modKLArr) + klDivergence(Q, modKLArr))/2;
	}

//	public static void main(String[] args) {
//		double [] arr1 ={0.3, 0.6, 0.5, 0.8, 0.1};
//		double [] arr2 ={ 0.8, 0.1,0.3, 0.6, 0.5,};
//		System.out.println(jsDivergence(arr1, arr2));
//		System.out.println(Statistics.jsDivergence(arr1, arr2));
//		System.out.println(klDivergence(arr1, arr2));
//	}
}
