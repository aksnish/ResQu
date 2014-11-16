package drivers;

import gov.nih.nlm.pubmed.PubMedCitationToSemRep;
import gov.nih.nlm.utils.Constants;
import gov.nih.nlm.utils.FilenameGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SemRepDriver {

	public static void main(String[] args) throws FileNotFoundException{


		PubMedCitationToSemRep pubSem = new PubMedCitationToSemRep();
		FilenameGenerator filegen = new FilenameGenerator();
		String filename;
		String results;
		PrintWriter writer;

		File directory = new File(Constants.TOPIC_DISEASE_FILE);
		File[] directoryListing = directory.listFiles();
		if (directoryListing != null) {
			for (File file : directoryListing) {
				System.out.println(file);
				//filename = file.toString().substring(file.toString().indexOf("/")+1, file.toString().indexOf("."));
				filename = filegen.normalizeFileName(file.toString());
				System.out.println(filename);
				//filename=filename.replaceAll(" ","_");
				
				results = pubSem.getSemrepedDocuments(file.toString());
				writer = new PrintWriter(Constants.SEMREP_FOLDER+ filename +".semrep");
				writer.write(results);
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
				break;
			}
		}else {
			System.out.println("not a directory");
		}
	}
}
