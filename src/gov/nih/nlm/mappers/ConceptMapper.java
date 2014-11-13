package gov.nih.nlm.mappers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class ConceptMapper {

	public void getConceptMap (String filename) throws FileNotFoundException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		HashMap<String,Integer> conceptMap = new HashMap<String,Integer>();
		String line, concept;
		String [] predication;
		try {
			while((line =br.readLine())!=null){

				predication = line.split("-");

				for(int i = 0 ; i<predication.length;i++)
				{
					concept = predication[0];
					if(!(conceptMap.containsKey(concept))){
						conceptMap.put(concept, 1);
					}
					else
					{
						conceptMap.put(concept, conceptMap.get(concept)+1);
					}
				}

			}
			MapSorter st = new MapSorter();
			HashMap<String, Integer> sortedMap = st.sortByValues(conceptMap);
			int c = 1;
			for(Entry<String, Integer> e : sortedMap.entrySet()){
				String key = e.getKey();
				int value =e.getValue();
				System.out.println( c++ + "Concept: "+key+"        Value:"+value);

			}
			System.out.println(sortedMap.size());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws FileNotFoundException {
		ConceptMapper cp = new ConceptMapper();
		cp.getConceptMap("Migraine_Disorders_S.txt");
	}

}
