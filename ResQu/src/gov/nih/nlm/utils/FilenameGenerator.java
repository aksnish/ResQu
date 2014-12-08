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

			//System.out.println("Dir : "+directory);
			if(querystr.contains(Constants.COMPLEX_DISEASE_MESH_HEADINGS)||querystr.contains("_C")){
				filename = directory+"/"+topic+Constants.COMPLEX_EXTENSION;
			}else if(querystr.contains(Constants.COMPLEX_DRUG_INDICATIONS_MESH_HEADINGS))
			{
				filename = directory+"/"+topic+Constants.COMPLEX_DRUG_INDICATION_EXTENSION;
			}else if(querystr.contains(Constants.COMPLEX_DRUG_EFFECTS_MESH_HEADINGS))
			{
				filename = directory+"/"+topic+Constants.COMPLEX_DRUG_EFFECTS_EXTENSION;
			}else // if(querystr.contains("_S")){ 
			{
				filename = directory+"/"+topic+Constants.SIMPLE_EXTENSION;
			}
			filename=filename.replaceAll(" ", "_");
		}
		else{
			//			System.out.println("directory already exists"+directory);
			if(querystr.contains(Constants.COMPLEX_DISEASE_MESH_HEADINGS)||querystr.contains("_C"))
			{
				filename = directory+"/"+topic+Constants.COMPLEX_EXTENSION;
			}else if(querystr.contains(Constants.COMPLEX_DRUG_INDICATIONS_MESH_HEADINGS))
			{
				filename = directory+"/"+topic+Constants.COMPLEX_DRUG_INDICATION_EXTENSION;
			}else if(querystr.contains(Constants.COMPLEX_DRUG_EFFECTS_MESH_HEADINGS))
			{
				filename = directory+"/"+topic+Constants.COMPLEX_DRUG_EFFECTS_EXTENSION;
			}else  //if(querystr.contains("_S")){ 
			{
				filename = directory+"/"+topic+Constants.SIMPLE_EXTENSION;
			}
			filename=filename.replaceAll(" ", "_");
		}
		return filename;
	}




	public String setFilename (String directoryPrefix , String parentFolder , String file){
		String parentfile = parentFolder.toString().substring(parentFolder.toString().lastIndexOf("/")+1, parentFolder.toString().length());
		String dir = directoryPrefix+parentfile;
		File directory = new File(dir);
		String filename;
		if (!directory.exists()) {
			directory.mkdirs();
			filename = directory+"/"+file+".semrep";
		}
		else{
			filename = directory+"/"+file+".semrep";
		}
		return filename;
	}




	public static void main(String[] args) throws IOException {
		FilenameGenerator fg = new FilenameGenerator();
		//			String file = fg.normalizeFileName("data/disease/Migraine Disorders_S.txt");
		//			System.out.println(file);
//		System.out.println(fg.setFilename(Constants.DATA_FOLDER+Constants.INPUT_FOLDER, "Migraine_S","Migraine", 1));
		System.out.println(fg.setFilename(Constants.SUMMARIZE_FOLDER+"disease/","data/semrep/disease/03-Asthma","Asthma"));
	}

}
