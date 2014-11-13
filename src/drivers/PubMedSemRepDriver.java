package drivers;

import gov.nih.nlm.pubmed.PubMedCitationRetriever;
import gov.nih.nlm.pubmed.PubMedCitationToSemRep;
import gov.nih.nlm.semrep.utils.SemrepTripleExtractor;
import gov.nih.nlm.utils.Constants;

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
public class PubMedSemRepDriver {

	public static void main(String[] args) throws Exception
	{

		BufferedReader br = new BufferedReader(new FileReader("disease_topic_list.txt"));
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

		String file;
		PubMedCitationRetriever pm = new PubMedCitationRetriever();
		for(int i = 0 ; i<diseases.size();i++){
			query = diseases.get(i)+Constants.SIMPLE_MESH_HEADINGS;
			System.out.println(query);
			FileWriter fw = null;
			try {
				file = setFilename(query);
				fw = new FileWriter("data/"+file,false);
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

	public static String setFilename (String querystr){
		String query = null; 
		if(querystr.contains("[MH]"))
			query = querystr.substring(0, querystr.indexOf("[MH]"));
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
