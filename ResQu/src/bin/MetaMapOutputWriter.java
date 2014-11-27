package bin;

import gov.nih.nlm.pubmed.MetaMapConverter;
import gov.nih.nlm.utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetaMapOutputWriter {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		MetaMapConverter mc = new MetaMapConverter();
		serializeHashMap(Constants.METAMAP_OUTPUT_FOLDER);
	}
	public static void serializeHashMap(String fname) throws IOException, ClassNotFoundException {
		Map<String, String> topicMap;// = new HashMap<String,String>();
		BufferedReader br;
		String hfile, filename;

		File folder = new File(fname);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				topicMap = new HashMap<String,String>();
				filename = Constants.METAMAP_OUTPUT_FOLDER+file.getName();
				hfile = file.getName();

				System.out.println("Reading file : "+filename);
				br = new BufferedReader(new FileReader(filename));
				String line;
				List<String> list = new ArrayList<String>();
				Pattern pattern = Pattern.compile("^   [0-9]");
				while((line = br.readLine())!=null){
				
					Matcher matcher = pattern.matcher(line);
					if (matcher.lookingAt()||line.contains("Phrase:")) {
						if(line.equals("Phrase: title"))
							continue;
						
						System.out.println("line :"+ line);
						//list.add(line);
					}

				}
			}
		}
	}
}
