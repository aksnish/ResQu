package drivers;

import gov.nih.nlm.semrep.utils.SemrepTripleExtractor;
import gov.nih.nlm.utils.Constants;
import gov.nlm.nih.pubmed.PubMedCitationRetriever;
import gov.nlm.nih.pubmed.PubMedCitationToSemRep;
import gov.nlm.nih.pubmed.PubMedCitationToSemRep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings("unused")
public class PubMedSemRep {

	public static void main(String[] args) throws Exception
	{

		PubMedCitationRetriever pm = new PubMedCitationRetriever();
		System.out.println("Start process");
				StringBuilder ps = pm.getPredications(Constants.SEARCH,Constants.NO_OF_CITATIONS);
				File filename = new File(setFilename(Constants.SEARCH));
		
				@SuppressWarnings("resource")
				PrintWriter writer = new PrintWriter(filename);
		
				writer.write(ps.toString());
				try {
					FileWriter fw = new FileWriter(filename);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(ps.toString());
		
					bw.close();
				} catch (IOException e) {
					System.err.print("Unable to write to file ");
					e.printStackTrace();
				}
				System.out.println("Written to File process"+filename);
				

		//getSRDocs();
	}

	public static void getSRDocs () throws FileNotFoundException{


		String filename;
		String results;
		PrintWriter writer;

		File directory = new File("diseases/");
		File[] directoryListing = directory.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {

				results = PubMedCitationToSemRep.getSemrepedDocuments(child.toString());

				filename = child.toString().substring(child.toString().indexOf("/"), child.toString().indexOf("."));
				
				writer = new PrintWriter("semrep/" + filename +".semrep");

				writer.write(results);
				try {
					FileWriter fw = new FileWriter("semrep/" + filename +".semrep");
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(results);

					bw.close();
				} catch (IOException e) {
					System.err.print("Unable to write to file ");
					e.printStackTrace();
				}
			}
		}else {
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
		}
	}



	//
	//		//System.out.println(results);
	//
	//
	//		SemrepTripleExtractor sp = new SemrepTripleExtractor();
	//
	//		String inputFile = "semrep.txt";
	//		//		String outputFile= "nsemtest1.txt";
	//		String str = sp.getInputPredications(results);
	//		sp.getPreds(inputFile);
	//		System.out.println(str);



	public static String setFilename (String querystr){

		String query = null; 

		if(querystr.contains("[MH]"))
			query = querystr.substring(0, Constants.SEARCH.indexOf("[MH]"));
		else
			query=querystr;
			String split;


			if(query.contains("/"))
			{
				split = query.substring(0, query.indexOf('/'))+"_C";

			}else
			{
				split = query+"_S";
			}

			split.replaceAll("// ", "_");
			String filename =split+".txt";

		
		return filename;
	}
}
