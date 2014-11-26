package drivers;

import gov.nih.nlm.semrep.utils.TripleBuilder;

import java.io.File;

public class SummarizedPredicationExtractorDriver {


	public static void main (String args [])
	{
		TripleBuilder semrep = new TripleBuilder();
		String inputFile;
		String outputFile;
		File folder = new File("semrep/");
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (accept(file.toString())) {
				inputFile = file.toString();
				outputFile = file.toString().substring(0, file.toString().indexOf(".semrep"));
				semrep.getFilePredications(inputFile, outputFile);
			}
		}
	}

	public static boolean accept(final String name) {
		if(name.endsWith("salient_predications"))
			return true;
		else
			return false;
	}

}
