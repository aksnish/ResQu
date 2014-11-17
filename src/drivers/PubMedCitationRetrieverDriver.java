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

		String file, topic;
		PubMedCitationRetriever pm = new PubMedCitationRetriever();
		for(int i = 0 ; i<diseases.size();i++){
			topic = diseases.get(i);
			query = topic+Constants.SIMPLE_MESH_HEADINGS;
			System.out.println(query);
			FileWriter fw = null;
			try {
				file =filegen.setFilename(query, topic);
				System.out.println("Write to file : "+file);
				
				fw = new FileWriter(Constants.TOPIC_DISEASE_FILE+file,false);
			} catch (IOException e) {
				e.printStackTrace();
			}
			PrintWriter pw = new PrintWriter(fw,false);
			StringBuilder ps = PubMedCitationRetriever.getPredications(query,Constants.NO_OF_CITATIONS);
			pw.write(ps.toString());
			pw.flush();
			pw.close();
			break;

		}
	}
}
