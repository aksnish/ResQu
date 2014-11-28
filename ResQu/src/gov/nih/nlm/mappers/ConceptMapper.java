package gov.nih.nlm.mappers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConceptMapper {

	public List<String> getConceptMap(String filename) throws IOException {
		

		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		String [] predication;
		List<String> list = new ArrayList<String>();
		try {
			while((line =br.readLine())!=null){
				predication = line.split("-");
				for(int i = 0 ; i<predication.length;i++)
				{
					list.add(predication[0]);
					//System.out.println(predication[0]);
				}
			}
			br.close();

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
//	public static void main(String[] args) throws IOException {
//		ConceptMapper cm = new ConceptMapper();
//		cm.getConceptMap("data/summarized-triples/18-Hypertensive_disease/Hypertensive_disease_S");
//	}
}
