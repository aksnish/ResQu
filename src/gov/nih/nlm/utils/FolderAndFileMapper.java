package gov.nih.nlm.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FolderAndFileMapper {

	public static Map<String,Integer> addToMap(String foldername) throws IOException{
		BufferedReader  br = new BufferedReader(new FileReader(Constants.DISEASE_LIST));
		Map<String, Integer> folderNumberMap = new HashMap<String, Integer>();
		String line;
		int i=1;
		try {
			while((line = br.readLine())!= null)
			{
				folderNumberMap.put(line, i);
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		br.close();
		return folderNumberMap;
	}
}
