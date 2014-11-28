package drivers;

import java.util.List;

import gov.nih.nlm.core.lucene.LuceneIndexWriter;
import gov.nih.nlm.utils.CorpusReader;

public class LuceneIndexWriterDriver {

	public static void main(String[] args) {
		String input_dir ="data/input_lucene/";
		CorpusReader reader = new CorpusReader(input_dir); 
		String dir = null;
		LuceneIndexWriter writer = new LuceneIndexWriter(dir);
		List<String> files = reader.getFiles();
		String content;
		for(String file_path: files){
			content = reader.getContent(file_path);
			writer.write(content);
		}
		writer.close();
	}

}
