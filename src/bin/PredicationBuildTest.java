package bin;

import gov.nih.nlm.semrep.utils.PredicationBuilder;

import java.io.File;

public class PredicationBuildTest {


	public static void main (String args [])
	{
		PredicationBuilder semrep = new PredicationBuilder();
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
