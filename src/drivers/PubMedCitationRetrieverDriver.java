package drivers;

import gov.nih.nlm.pubmed.PubMedCitationRetriever;
import gov.nih.nlm.pubmed.SemRepPubMedCitations;
import gov.nih.nlm.semrep.utils.SemrepTripleExtractor;
import gov.nih.nlm.utils.Constants;
import gov.nih.nlm.utils.FilenameGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class PubMedCitationRetrieverDriver {

	public static void main(String[] args) throws Exception
	{

		FilenameGenerator filegen = new FilenameGenerator();
		BufferedReader br = new BufferedReader(new FileReader(Constants.DISEASE_LIST));
		String line, query;

		ArrayList<String> diseases = new ArrayList<String>();
		try {
			while((line = br.readLine())!= null)
			{
				diseases.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String file = null, topic;
		PubMedCitationRetriever pm = new PubMedCitationRetriever();
		for(int i = 0 ; i<diseases.size();i++){
			topic = diseases.get(i);
			query = topic+Constants.SIMPLE_MESH_HEADINGS;
			System.out.println("\nPubMed Query : "+query);

			file =filegen.setFilename(query, topic);


			StringBuffer ps = PubMedCitationRetriever.getPredications(query,Constants.NO_OF_CITATIONS);
			//StringBuffer ps = PubMedCitationRetriever.getPredications(query);

			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("Writing to file : " + file);
			try {
				FileWriter fw = new FileWriter(Constants.TOPIC_DISEASE_FILE+file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(ps.toString());

				bw.close();
			} catch (IOException e) {
				System.err.print("Unable to write to file ");
				e.printStackTrace();
			}
			break;
		}
	}
}

