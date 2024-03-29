package bin;

import gov.nih.nlm.pubmed.MetaMapConverter;
import gov.nih.nlm.utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetaMapWriter {

	public static void main(String[] args) throws IOException {
		MetaMapConverter mc = new MetaMapConverter();
		File folder = new File("data/metamap/");
		File[] listOfFiles = folder.listFiles();

		PrintWriter out = null;
		String filename = null;
		for (File file : listOfFiles) {
			if (file.isFile()) {
				filename= "data/"+file.getName();			
				out = new PrintWriter(new FileWriter(filename));
				String inputBuf = Constants.METAMAP_INPUT_FOLDER+file.getName();
				String results = mc.getMetaMapFormat(inputBuf);
				//	System.out.println(results);
				writeMetamapToFile(results, out);
			}
		}
		out.close();
		serializeHashMap("data/Acetominophen.txt");
	}

	public static void writeMetamapToFile(String result, PrintWriter out) throws FileNotFoundException{
		//System.out.println("writing to file: ");
		out.println(result);

	}

	private static void serializeHashMap(String filename) throws FileNotFoundException {
		Map<String, String> myVector = new HashMap<String,String>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		//System.out.println(filename);
		String line;
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile("^   [0-9]");
		try {
			while((line = br.readLine())!=null){
				Matcher matcher = pattern.matcher(line);
				if (matcher.lookingAt()) {
					//System.out.println("Adding to list");
					list.add(line);
					//System.out.println(line);
				}
			}
			//System.out.println("=========");
			//System.out.println("inside List : ");

			for(String li : list){
				String [] split = li.split("   ");
				for(int i=0 ; i <split.length;i++){

					String [] mapterms = (split[2]).split(":");
					for(int j = 0 ; j < mapterms.length;j++){
						mapterms[1] = (mapterms[1]).replaceAll("\\(.*\\)", "").replaceAll("\\[.*\\]","");
						myVector.put(mapterms[0], mapterms[1]);
					}
				}
				//System.out.println(li);
			}

			for(Map.Entry<String, String> entry : myVector.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				System.out.println(key +" : " + value);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
