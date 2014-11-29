package gov.nih.nlm.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
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
					//					break;
				}
			}
		}
		return fileList;
	}

	public List<String> getDirectoryFolderList(String dir){
		List<String> fileList = new ArrayList<String>();
		File directory = new File(dir);
		File [] listOfFile = directory.listFiles();
		for(File file : listOfFile){
			if(file.isDirectory()){
				fileList.add(file.toString());
			}
		}
		return fileList;
	}

	public static void main(String[] args) {
		//		DirectoryFileListIterator dc = new DirectoryFileListIterator();
		//		List<String> list = dc.getDirectoryFileList(Constants.SUM_DOC_DIR);
		//		List<String> listrem = null;
		//		Iterator<String> ite = list.iterator();
		//
		//		System.out.println("list "+list.size());
		//		String s = null;
		//
		//		while (ite.hasNext()){
		//			String l = ite.next();
		//			if(l.contains("GS")){
		//				s =l ;
		//				ite.remove();
		//
		//			}else{ 
		//				//System.out.println("S  : "+l);
		//			}
		//			listrem = list;
		//		}
		//		//System.out.println("gold std : " + s);
		//		for(String ls : listrem){
		//			System.out.println(ls);}
		//		System.out.println("list : "+listrem.size());
		DirectoryFileListIterator dir = new DirectoryFileListIterator();
		List<String> list =dir.getDirectoryFolderList(Constants.SUM_DOC_DIR);
		for(String l : list){
			System.out.println(l);
		}
	}
}
