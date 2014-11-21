package drivers;

import java.util.Set;

import net.htmlparser.jericho.HTMLElements;
import gov.nih.nlm.crawler.ACPCrawler;
import gov.nih.nlm.crawler.SimpleJericho;

public class ACPCrawlerDriver {

	public static void main(String[] args) throws Exception {
		
//		String startPage = "http://online.statref.com/document.aspx?docAddress=nTcI4TcIBL6Efuupqj9DJg%3d%3d&QueryID=-1&SessionID=1E6386AQWOTXIVHN#H&d430&ChaptersTab&nTcI4TcIBL6Efuupqj9DJg%3d%3d&&539";
//		
//		ACPCrawler crawler = new ACPCrawler();
//		crawler.crawl(startPage);
//		
//		HTMLElements elements = null;
//		Set<String> blockTags =  elements.getBlockLevelElementNames();
		
			SimpleJericho sj = new SimpleJericho();
			sj.crawlURL();
	}
}
