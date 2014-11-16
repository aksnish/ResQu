package gov.nih.nlm.utils;

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
	
	public String setFilename (String querystr, String topic){
		String filename;
		if(querystr.contains(Constants.DRUG_THERAPY))
			filename = topic+Constants.COMPLEX_EXTENSION;
		else 
			filename = topic+Constants.SIMPLE_EXTENSION;
		
		filename=filename.replaceAll(" ", "_");
		return filename;
	}
	
	public static void main(String[] args) {
		FilenameGenerator fg = new FilenameGenerator();
		String file = fg.normalizeFileName("data/disease/Migraine Disorders_S.txt");
		System.out.println(file);
	}

}
