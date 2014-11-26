package drivers;

import java.io.File;

import gov.nih.nlm.semrep.utils.SemrepTripleExtractor;
import gov.nih.nlm.utils.Constants;

public class TripleBuilderDriver {

	public static void main(String[] args) {

		SemrepTripleExtractor semrep = new SemrepTripleExtractor();
		File directory = new File(Constants.SEMREP_FOLDER);
		File[] directoryListing = directory.listFiles();
		String filename, outputFile;
		if (directoryListing != null) {
			for (File file : directoryListing) {
				if(file.getName().endsWith("salient_predications"))
				{
					filename = Constants.SEMREP_FOLDER+file.getName();
					outputFile =Constants.SUMMARIZE_FOLDER+file.getName();
					System.out.println("output file name : "+outputFile);
					semrep.getFilePredications(filename, outputFile);

				}
				//break;

			}
		}

	}
}
