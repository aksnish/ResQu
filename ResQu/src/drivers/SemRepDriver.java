package drivers;

import gov.nih.nlm.pubmed.PubMedCitationToSemRep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SemRepDriver {

	public static void main(String[] args) throws FileNotFoundException{


		PubMedCitationToSemRep psm = new PubMedCitationToSemRep();
		String filename;
		String results;
		PrintWriter writer;

		File directory = new File("disease/");
		File[] directoryListing = directory.listFiles();
		if (directoryListing != null) {
			for (File file : directoryListing) {
				filename = file.toString().substring(file.toString().indexOf("/")+1, file.toString().indexOf("."));
				filename=filename.replaceAll(" ","_");
				
				results = psm.getSemrepedDocuments(file.toString());
				writer = new PrintWriter("semrep/" + filename +".semrep");
				writer.write(results);
				try {
					FileWriter fw = new FileWriter("semrep/" + filename +".semrep");
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
