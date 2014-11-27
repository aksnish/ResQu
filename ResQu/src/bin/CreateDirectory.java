package bin;

import gov.nih.nlm.utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CreateDirectory {

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

	public static void main(String[] args) throws IOException {
		int i = 1; 
		CreateDirectory cd = new  CreateDirectory();
		//BufferedReader br = new BufferedReader(new FileReader(Constants.))
		String filename = cd.setFilename(Constants.SUMMARIZE_FOLDER ,"Asthma_S.semrep.salient_predications", "Asthma", i);
		File file = new File(filename);
		file.createNewFile();
		System.out.println("File : "+filename);


	}

}
