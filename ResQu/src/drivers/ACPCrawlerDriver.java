package drivers;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Tag;
import gov.nih.nlm.crawler.ClinicalResourceCrawler;
import gov.nih.nlm.utils.Constants;

public class ACPCrawlerDriver {

	static PrintStream OUT;

	public static void main(String[] args) throws Exception {
		String abs_elements = null;
		//OUT = System.out;
		File folder = new File(Constants.CRAWLER_DATA_FOLDER);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				System.out.println("Files: "+ file.getName());
				String filename = Constants.METAMAP_INPUT_FOLDER+file.getName();
				OUT = new PrintStream(new File(filename.replaceAll(".html", "_indications.txt")));
				List<Tag> fontTagList = new ArrayList<Tag>();
				ClinicalResourceCrawler crawl = new ClinicalResourceCrawler();

				String startPage =Constants.CRAWLER_FILE_PREFIX+Constants.CRAWLER_DATA_FOLDER_PATH+file.getName();

				if (startPage.indexOf(':')==-1) startPage="file:"+startPage;

				int startIndex= crawl.getBoldTextIndex(startPage,"Uses");
				int endIndex= crawl.getBoldTextIndex(startPage,"Dosage and Administration");

				fontTagList = crawl.findFontBetween(startPage, startIndex,endIndex);
				OUT.append("PMID  - " +101254);
				OUT.append("\nTI  - " +"title");
				OUT.append("\nAB  - " );
				for(Tag font : fontTagList){
					if(("•").equalsIgnoreCase(abs_elements))
						continue;
					abs_elements = font.getElement().getTextExtractor().toString();
					OUT.append(abs_elements.replaceAll("[^\\x00-\\x7F]", "")+"\n\n");
				}

				startIndex= crawl.getBoldTextIndex(startPage,"Cautions");
				endIndex= crawl.getBoldTextIndex(startPage,"Interactions");
				fontTagList = crawl.findFontBetween(startPage, startIndex,endIndex);

				OUT = new PrintStream(new File(filename.replaceAll(".html", "_adverse_effects.txt")));
				OUT.append("PMID  - " +101254);
				OUT.append("\nTI  - " +"title");
				OUT.append("\nAB  - " );
				for(Tag font : fontTagList){
					abs_elements = font.getElement().getTextExtractor().toString();
					if(("•").equalsIgnoreCase(abs_elements))
						continue;
					OUT.append(abs_elements.replaceAll("[^\\x00-\\x7F]", "")+"\n\n");
				}
			}
		}
	}
}
