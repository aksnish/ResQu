package gov.nih.nlm.mappers;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PredicationMapper {

	public void getConceptMap (String filename) throws FileNotFoundException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		Map<String,Integer> predicationMap = new HashMap<String,Integer>();
		String line;

		try {
			while((line =br.readLine())!=null){
	
					if(!(predicationMap.containsKey(line))){
						predicationMap.put(line, 1);
					}
					else
					{
						predicationMap.put(line, predicationMap.get(line)+1);
					}
				}
			MapSorter st = new MapSorter();
			Map<String, Integer> sortedMap = st.sortByValue(predicationMap);
			int c = 1;
			for(Entry<String, Integer> e : sortedMap.entrySet()){
				String key = e.getKey();
				int value =e.getValue();
				System.out.println( c++ +"  " + "Predications: "+key+"        Value:"+value);

			}
			System.out.println(sortedMap.size());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws FileNotFoundException {
		PredicationMapper cp = new PredicationMapper();
		cp.getConceptMap("Migraine_Disorders_S.txt");
	}

}

