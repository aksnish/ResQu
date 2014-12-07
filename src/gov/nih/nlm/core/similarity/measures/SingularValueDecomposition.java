package gov.nih.nlm.core.similarity.measures;

import com.aliasi.matrix.SvdMatrix;

public class SingularValueDecomposition {


	static final double[][] TERM_DOCUMENT_MATRIX = {{16, 21, 27, 9, 21, 21, 0, 21, 21, 27, 9, 21, 48, 102, 24, 15, 24, 21, 21, 36, 72, 24, 15, 21, 24}, 
													{15, 16, 30, 8, 8, 9, 9, 9, 15, 12, 21, 24,93, 18, 18, 42, 12, 66, 48, 12, 9, 54, 21, 0, 21},
													{16, 21, 27, 9, 21, 21, 0, 21, 21, 27, 9, 21, 48, 16, 21, 27, 9, 21, 21, 0, 21, 21, 27, 9, 21}};
		
		//{-30, 0, 66,30}, {42, 30, 63, 188},{66, 60, 174, 15}};//, {3, 1, 1}, {-1, 3, 1},  {3, 1, 1}, {3, 1, 1}, {3, 1, 1}, {3, 1, 1}, {1, 1, 1}};

	//	static final String[] TERMS = new String[] { };
	//	static final String[] DOCS = new String[] {	};

	static final int NUM_FACTORS = 2;

	public static void main(String[] args) throws Exception {
		double featureInit = 0.01;
		double initialLearningRate = 0.005;
		int annealingRate = 1000;
		double regularization = 0.00;
		double minImprovement = 0.0000;
		int minEpochs = 10;
		int maxEpochs = 50000;

		System.out.println("  Computing SVD");
		System.out.println("    maxFactors=" + NUM_FACTORS);
		System.out.println("    featureInit=" + featureInit);
		System.out.println("    initialLearningRate=" + initialLearningRate);
		System.out.println("    annealingRate=" + annealingRate);
		System.out.println("    regularization" + regularization);
		System.out.println("    minImprovement=" + minImprovement);
		System.out.println("    minEpochs=" + minEpochs);
		System.out.println("    maxEpochs=" + maxEpochs);

		SvdMatrix matrix
		= SvdMatrix.svd(TERM_DOCUMENT_MATRIX,
				NUM_FACTORS,
				featureInit,
				initialLearningRate,
				annealingRate,
				regularization,
				null,
				minImprovement,
				minEpochs,
				maxEpochs);

		double[] scales = matrix.singularValues();
		double[][] termVectors = matrix.leftSingularVectors();

		System.out.println("\nSCALES : Diagnol Matrix");
		for (int k = 0; k < NUM_FACTORS; ++k)
			System.out.printf("%d  %4.2f\n",k,scales[k]);


				System.out.println("\nDOCUMENT VECTORS" + " : Right Singular matrix : Number of Documents : " + termVectors.length);
		for (int i = 0; i < termVectors.length; ++i) {
			System.out.print("(");
			for (int k = 0; k < NUM_FACTORS; ++k) {
				if (k > 0) System.out.print(", ");
								System.out.printf("% 5.2f",termVectors[i][k]);
			}
			System.out.print(")  ");
		}



				System.out.println("\nDOCUMENT COSINE SIMILARITY");
		double score = 0;
		for (int j = 0; j < termVectors.length; ++j) {
			if(termVectors[0]!=termVectors[j]){
				//								double score = dotProduct(termVec1,termVec2,scales);
				score = cosine(termVectors[0],termVectors[j],scales);
								System.out.printf("%d  % 5.2f\n",j,score);
			}
		}	
	}

	static double dotProduct(double[] xs, double[] ys, double[] scales) {
		double sum = 0.0;
		for (int k = 0; k < xs.length; ++k)
			sum += xs[k] * ys[k] * scales[k];
		return sum;
	}

	static double cosine(double[] xs, double[] ys, double[] scales) {
		double product = 0.0;
		double xsLengthSquared = 0.0;
		double ysLengthSquared = 0.0;
		for (int k = 0; k < xs.length; ++k) {
			double sqrtScale = Math.sqrt(scales[k]);
			double scaledXs = sqrtScale * xs[k];
			double scaledYs = sqrtScale * ys[k];
			xsLengthSquared += scaledXs * scaledXs;
			ysLengthSquared += scaledYs * scaledYs;
			product += scaledXs * scaledYs;
		}
		return product / Math.sqrt(xsLengthSquared * ysLengthSquared);
	}
}