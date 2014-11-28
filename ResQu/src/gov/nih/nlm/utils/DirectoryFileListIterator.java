package gov.nih.nlm.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryFileListIterator {

	public List<String> getDirectoryFileList(String dir){
		List<String> fileList = new ArrayList<String>();
		File directory = new File(dir);
		File [] innerDir;
		File [] listOfFile = directory.listFiles();
		for(File file : listOfFile){
			if(file.isDirectory()){
				innerDir = file.listFiles();
				for(File innerFile : innerDir){
					if(innerFile.isFile()){
						fileList.add(innerFile.toString());
					}
				}
			}
		}
		return fileList;
	}
}
