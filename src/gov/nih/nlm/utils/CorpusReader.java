package gov.nih.nlm.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CorpusReader {
	String corpus_dir;

	public CorpusReader(String input_dir) {
		this.corpus_dir = input_dir;
	}

	public List<String> getFiles() {
		System.out.println("Reading Directory: " + corpus_dir);
		DirectoryFileListIterator dirfile = new DirectoryFileListIterator();
		List<String> fileList = new ArrayList<String>();
		fileList = dirfile.getDirectoryFolderList(corpus_dir);
		return fileList;
	}

	public String getContent(String file_path) throws IOException {
		//System.out.println("Getting the content");
		BufferedReader bufReader = null;
		StringBuffer buf = new StringBuffer(" ");
		bufReader = new BufferedReader(new FileReader(file_path));
		while(bufReader.readLine()!=null){
			buf.append(bufReader.readLine());
			buf.append("\n");
		}
		bufReader.close();
		return buf.toString();
	}
}
