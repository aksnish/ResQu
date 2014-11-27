package gov.nih.nlm.utils;

import java.io.File;
import java.io.IOException;

public class FilenameGenerator {

	public static final char FILE_SEPERATOR = '/';

	public  int indexofSeperator(String filename){
		if(filename == null)
		{
			return -1;
		}
		int lastSepPos = filename.lastIndexOf(FILE_SEPERATOR);
		return lastSepPos;
	}

	public  String normalizeFileName(String filename){
		if(filename == null){
			return null;
		}
		int bpos = indexofSeperator(filename)+1;
		int epos = filename.length();
		String newName = filename.substring(bpos, epos).replaceAll(".txt", "");
		return newName;
	}

	@Deprecated
	public String setFilename (String querystr, String topic){
		String filename;
		if(querystr.contains(Constants.DRUG_THERAPY))
			filename = topic+Constants.COMPLEX_EXTENSION;
		else 
			filename = topic+Constants.SIMPLE_EXTENSION;

		filename=filename.replaceAll(" ", "_");
		return filename;
	}

	public String setFilename (String directoryPrefix, String querystr, String topic, int i) throws IOException{
		String filename = null;
		String format , result = null;

		if(i<10){
			format = String.format("%%0%dd", 2);
			result = String.format(format, i);
		}
		else{
			result = Integer.toString(i);
		}
		
		//Constants.DISEASE_FOLDER
		String dir = directoryPrefix+result+"-"+topic;
		dir=dir.replaceAll(" ", "_");
		File directory = new File(dir);

		if (!directory.exists()) {
			directory.mkdirs();

			System.out.println("Dir : "+directory);
			if(querystr.contains(Constants.DRUG_THERAPY)){
				filename = directory+"/"+topic+Constants.COMPLEX_EXTENSION;
			}else{ 
				filename = directory+"/"+topic+Constants.SIMPLE_EXTENSION;
			}
			filename=filename.replaceAll(" ", "_");
		}
		else{
			System.out.println("directory already exists"+directory);
			if(querystr.contains(Constants.DRUG_THERAPY))
			{
				filename = directory+"/"+topic+Constants.COMPLEX_EXTENSION;
			}else{ 
				filename = directory+"/"+topic+Constants.SIMPLE_EXTENSION;
			}
			filename=filename.replaceAll(" ", "_");
		}
		return filename;
	}


	//	public static void main(String[] args) {
	//		FilenameGenerator fg = new FilenameGenerator();
	//		String file = fg.normalizeFileName("data/disease/Migraine Disorders_S.txt");
	//		System.out.println(file);
	//	}

}
