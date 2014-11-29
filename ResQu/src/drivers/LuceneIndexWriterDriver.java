package drivers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import gov.nih.nlm.core.lucene.LuceneIndexWriter;
import gov.nih.nlm.utils.Constants;
import gov.nih.nlm.utils.CorpusReader;

public class LuceneIndexWriterDriver {

	public static void main(String[] args) throws IOException {
		String lucene_index_dir ="data/input_lucene/";
		String corpus_dir = Constants.SUM_DOC_DIR;
		CorpusReader reader = new CorpusReader(corpus_dir);
		List<String> folder = reader.getFiles();
		String content;

		File list;
		File [] files;
		for(String folder_path: folder){
			list = new File(folder_path);
			files = list.listFiles();
			System.out.println("------------------------------------");
			System.out.println("Serializing Index: " + folder_path);
			LuceneIndexWriter writer = null;
			String idxfile = null;
			try {
				for(File file_path : files){
					if(idxfile == null){
						idxfile = lucene_index_dir+file_path.getName().replaceAll("_[A-Z]*.txt", "");
						writer = new LuceneIndexWriter(idxfile);
					}
					if(file_path.getName().contains("_GS")){
						System.out.println("GS File: "+ file_path.getName());
						content = reader.getContent(file_path.toString());
						writer.write(content, 0, file_path.getName());
					}else {
						System.out.println("Reg File: "+ file_path.getName());
						content = reader.getContent(file_path.toString());
						writer.write(content, 1, file_path.getName());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			writer.close();
		}

	}
}
