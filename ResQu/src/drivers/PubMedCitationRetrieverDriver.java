package drivers;

import gov.nih.nlm.pubmed.PubMedCitationRetriever;
import gov.nih.nlm.pubmed.SemRepPredicationExtractor;
import gov.nih.nlm.semrep.utils.PredicationBuilder;
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
import java.io.Writer;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class PubMedCitationRetrieverDriver {

	public static void main(String[] args)
	{
		PubMedCitationRetriever pm = new PubMedCitationRetriever();
		StringBuffer ps = new StringBuffer();
		FilenameGenerator filegen = new FilenameGenerator();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(Constants.DISEASE_LIST));
			String line, query;
			File file;
			String topic, filename;
			int i = 1;
			String j;
			while((line = br.readLine())!= null)
			{
				topic = line;
				query = topic+Constants.SIMPLE_MESH_HEADINGS;
				System.out.println("PubMed Query : "+query);
				filename = filegen.setFilename(Constants.DISEASE_FOLDER ,query, topic,i);
				PubMedCitationRetriever.getPredications(query,Constants.NO_OF_CITATIONS, filename);
				query = topic+Constants.COMPLEX_DISEASE_MESH_HEADINGS;
				System.out.println("PubMed Query : "+query);
				filename = filegen.setFilename(Constants.DISEASE_FOLDER ,query, topic,i);
				PubMedCitationRetriever.getPredications(query,Constants.NO_OF_CITATIONS, filename);
				System.out.println("------------------------------------------------------");
				//			break;
				i++;
			}
			br.close();

			i= 1;
			br = new BufferedReader(new FileReader(Constants.DRUG_LIST));
			while((line = br.readLine())!= null)
			{
				topic = line;
				query = topic+Constants.SIMPLE_MESH_HEADINGS;
				System.out.println("PubMed Query : "+query);
				filename = filegen.setFilename(Constants.DRUG_FOLDER ,query, topic,i);
				PubMedCitationRetriever.getPredications(query,Constants.NO_OF_CITATIONS, filename);
				query = "\""+topic+Constants.COMPLEX_DRUG_INDICATIONS_MESH_HEADINGS;
				System.out.println("PubMed Query : "+query);
				filename = filegen.setFilename(Constants.DRUG_FOLDER ,query, topic,i);
				PubMedCitationRetriever.getPredications(query,Constants.NO_OF_CITATIONS, filename);
				query = "\""+topic+Constants.COMPLEX_DRUG_EFFECTS_MESH_HEADINGS;
				System.out.println("PubMed Query : "+query);
				filename = filegen.setFilename(Constants.DRUG_FOLDER ,query, topic,i);
				PubMedCitationRetriever.getPredications(query,Constants.NO_OF_CITATIONS, filename);
				System.out.println("------------------------------------------------------");
				//break;
				i++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

