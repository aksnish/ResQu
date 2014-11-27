package bin;

import gov.nih.nlm.mappers.MapSorter;
import gov.nih.nlm.model.SummaryDocument;
import gov.nih.nlm.utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ConMapper {

	public SummaryDocument getConceptMap (String filename) throws IOException{
		PrintWriter OUT = new PrintWriter(new FileWriter(Constants.DATA_FOLDER+"dictionary.txt", true));
		BufferedReader br = new BufferedReader(new FileReader(filename));
		HashMap<String,Integer> conceptMap = new HashMap<String,Integer>();
		String line, concept;
		String [] predication;
		String str;
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
			Map<String, Integer> sortedMap = st.sortByValue(conceptMap);
			for(Entry<String, Integer> e : sortedMap.entrySet()){
				String key = e.getKey();
				int value =e.getValue();
				System.out.println(key+":"+value);
				str =key+":"+value+"\n";
				OUT.append(str);

			}
			OUT.close();
			//System.out.println(sortedMap.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
