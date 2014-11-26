package drivers;

import gov.nih.nlm.pubmed.SemRepPredicationExtractor;
import gov.nih.nlm.utils.Constants;
import gov.nih.nlm.utils.FilenameGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SemRepPredicationExtractorDriver {

	public static void main(String[] args) throws FileNotFoundException{


		SemRepPredicationExtractor pubSem = new SemRepPredicationExtractor();
		FilenameGenerator filegen = new FilenameGenerator();
		String filename;
		String results;
		PrintWriter writer;
		File directory = new File(Constants.TOPIC_DISEASE_FOLDER);
		File[] directoryListing = directory.listFiles();
		if (directoryListing != null) {
			for (File file : directoryListing) {

				filename = filegen.normalizeFileName(file.toString());

				System.out.println("----------------------");
				System.out.println("Converting PubMed Citations to SemRep format");
				results = pubSem.getSemrepedDocuments(file.toString());
				//				writer = new PrintWriter(Constants.SEMREP_FOLDER+ filename +".semrep");
				//				writer.write(results);

				System.out.println("Writing to file : "+filename);
				try {
					FileWriter fw = new FileWriter(Constants.SEMREP_FOLDER+ filename +".semrep");
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(results);
					bw.flush();
					bw.close();
				} catch (IOException e) {
					System.err.print("Unable to write to file ");
					e.printStackTrace();
				}
				//break;
			}
		}else {
			System.out.println("not a directory");
		}
	}
}
