package drivers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nih.nlm.pubmed.MetaMapConverter;
import gov.nih.nlm.utils.Constants;

public class MetaMapCitationsDriver {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		MetaMapConverter mc = new MetaMapConverter();
		File folder = new File(Constants.METAMAP_INPUT_FOLDER_PATH);
		File[] listOfFiles = folder.listFiles();
		String filename = null;
		for (File file : listOfFiles) {
			if (file.isFile()) {
				filename= Constants.METAMAP_OUTPUT_FOLDER_PATH+file.getName();
				System.out.println("output file name  :"+ filename);				
				String inputBuf = Constants.METAMAP_INPUT_FOLDER_PATH+file.getName();
				String results =mc.getMetaMapFormat(inputBuf);
				writeMetaMapToFile(results, filename);
			}
		}
	}

	/**
	 * Write a small string to a File - Use a FileWriter
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void writeMetaMapToFile(String content, String filePath) throws IOException, ClassNotFoundException {
		Writer writer = null;

		try {

			writer = new FileWriter(filePath);
			writer.write(content);

		} catch (IOException e) {

			System.err.println("Error writing the file : ");
			e.printStackTrace();

		} finally {

			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {

					System.err.println("Error closing the file : ");
					e.printStackTrace();
				}
			}

		}
		serializeHashMap(Constants.METAMAP_OUTPUT_FOLDER_PATH);
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
				filename = Constants.METAMAP_OUTPUT_FOLDER_PATH+file.getName();
				hfile = file.getName();

				System.out.println("Reading file : "+filename);
				br = new BufferedReader(new FileReader(filename));
				String line;
				List<String> list = new ArrayList<String>();
				Pattern pattern = Pattern.compile("^   [0-9]");
				while((line = br.readLine())!=null){
					Matcher matcher = pattern.matcher(line);
					if (matcher.lookingAt()) {
						list.add(line);
					}
				}
				for(String li : list){
					String [] split = li.split("   ");
					for(int i=0 ; i <split.length;i++){
						String [] mapterms = (split[2]).split(":");
						for(int j = 0 ; j < mapterms.length;j++){
							mapterms[1] = (mapterms[1]).replaceAll("\\(.*\\)", "").replaceAll("\\[.*\\]","");
							topicMap.put(mapterms[0], mapterms[1]);
						}
					}
				}
				FileOutputStream fos =	new FileOutputStream("data/serializedMaps/"+hfile.replaceAll(".txt","")+".ser");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(topicMap);
				oos.close();
				fos.close();
			}
		}
		//deSerialize();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void deSerialize () throws IOException, ClassNotFoundException
	{
		HashMap<Integer, String> readSerialMap = null;
		String filename;
		File folder = new File(Constants.SERIALZED_MAPS_FOLDER);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				filename = Constants.SERIALZED_MAPS_FOLDER+file.getName();
				FileInputStream fis = new FileInputStream(filename);
				ObjectInputStream ois = new ObjectInputStream(fis);
				readSerialMap = (HashMap<Integer, String>) ois.readObject();
				ois.close();
				fis.close();

				System.out.println("Deserialized HashMap.." + filename);
				Set<Entry<Integer, String>> set = readSerialMap.entrySet();
				Iterator<Entry<Integer, String>> iterator = set.iterator();
				System.out.println("--------------------------");
				while(iterator.hasNext()) {
					Map.Entry mentry = (Map.Entry)iterator.next();
					System.out.println(mentry.getKey() + " : "+mentry.getValue());
				}
				System.out.println("--------------------------");
			}
		}
	}
}