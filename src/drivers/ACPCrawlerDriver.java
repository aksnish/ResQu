package drivers;

import java.util.ArrayList;
import java.util.List;
import net.htmlparser.jericho.Tag;
import gov.nih.nlm.crawler.ClinicalResourceCrawler;

public class ACPCrawlerDriver {

	public static void main(String[] args) throws Exception {
		List<Tag> fontTagList = new ArrayList<Tag>();
		ClinicalResourceCrawler crawl = new ClinicalResourceCrawler();
		String startPage ="file:jericho/Acetominophen.html";
		if (startPage.indexOf(':')==-1) startPage="file:"+startPage;

		//crawl.getBoldTextIndex(startPage);

				System.out.println("Source Start page : "+startPage);
				int startIndex= crawl.getBoldTextIndex(startPage,"Uses");
				int endIndex= crawl.getBoldTextIndex(startPage,"Dosage and Administration");
				System.out.println(startIndex +" "+endIndex);
				fontTagList = crawl.findFontBetween(startPage, startIndex,endIndex);
		
				for(Tag font : fontTagList){
					//System.out.println("----------------");
					//System.out.println(font.getElement());
					System.out.println("USES : "+font.getElement().getTextExtractor());
				}
		
				startIndex= crawl.getBoldTextIndex(startPage,"Cautions");
				endIndex= crawl.getBoldTextIndex(startPage,"Interactions");
		
				fontTagList = crawl.findFontBetween(startPage, startIndex,endIndex);
				System.out.println("----------------");
				for(Tag font : fontTagList){
		
					//System.out.println(font.getElement());
					System.out.println("CAUTIONS : "+font.getElement().getTextExtractor());
				}
	}
}
