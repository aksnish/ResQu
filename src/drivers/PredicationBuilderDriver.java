package drivers;

import java.io.File;
import java.io.IOException;

import gov.nih.nlm.semrep.utils.PredicationBuilder;
import gov.nih.nlm.utils.Constants;
import gov.nih.nlm.utils.FilenameGenerator;

public class PredicationBuilderDriver {

	public static void main(String[] args) throws IOException {
		FilenameGenerator filegen = new FilenameGenerator();
		PredicationBuilder semrep = new PredicationBuilder();
		File directory = new File(Constants.SEMREP_FOLDER);
		File[] directoryListing = directory.listFiles();
		String filename, outputFile;
		int i=1;
		if (directoryListing != null) {
			for (File file : directoryListing) {
				
				String [] filesplit;
				if(file.getName().endsWith("salient_predications"))
				{
					filesplit = file.getName().toString().split("_S");
					filename = Constants.SEMREP_FOLDER+file.getName();
					System.out.println("querystr : " +filesplit[0]);
					System.out.println("filename : "+ file.getName().toString());
					outputFile= filegen.setFilename(Constants.SUMMARIZE_FOLDER,file.toString(),filesplit[0], i);
					System.out.println("generated name : "+ filename);
					//outputFile =file.getName();
					System.out.println("output file name : "+outputFile);
					semrep.getFilePredications(filename, outputFile);

				}
				i++;
				//break;
			}
		}

	}
}
