package drivers;

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
		List<String> files = reader.getFiles();
		String content;

		LuceneIndexWriter writer = new LuceneIndexWriter(lucene_index_dir);
		for(String file_path: files){
			content = reader.getContent(file_path);
			writer.write(content);
		}
		writer.close();
	}

}
