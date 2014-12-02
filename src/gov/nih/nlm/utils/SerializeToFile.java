package gov.nih.nlm.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SerializeToFile {
	public void serializeList (List<Double> list, String filename){
		String data_folder =Constants.DATA_FOLDER+"r-lists/";
		try {
			FileOutputStream out = new FileOutputStream(data_folder+filename+".ser");
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(list);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			System.out.println("Problem serializing: " + e);
		}
	}

	public void serializeList (double list, String filename){
		String data_folder =Constants.DATA_FOLDER+"r-lists/";
		try {
			FileOutputStream out = new FileOutputStream(data_folder+filename+".ser");
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(list);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			System.out.println("Problem serializing: " + e);
		}
	}

	@SuppressWarnings("unchecked")
	public void readSerializedFile (String filename){
		List<Double> acard = new ArrayList<Double>();
		DirectoryFileListIterator dir = new DirectoryFileListIterator();
		List<String>files = dir.getDirectoryFolderList(filename);
		for(String file : files){	
			try {
				FileInputStream in = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(in);
				acard = (List<Double>) (ois.readObject());
				ois.close();
			} catch (Exception e) {
				System.out.println("Problem serializing: " + e);
			}
			System.out.println("File is: "+file+"  :  " + acard);
		}
	}
	
	public void readdSerializedFile (String filename){
		double acard = 0;
		DirectoryFileListIterator dir = new DirectoryFileListIterator();
		List<String>files = dir.getDirectoryFolderList(filename);
		for(String file : files){	
			try {
				FileInputStream in = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(in);
				acard = (double) (ois.readObject());
				ois.close();
			} catch (Exception e) {
				System.out.println("Problem serializing: " + e);
			}
			System.out.println("File is: "+file+"  :  " + acard);
		}
	}
}
