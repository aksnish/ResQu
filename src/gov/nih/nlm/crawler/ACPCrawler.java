package gov.nih.nlm.crawler;

import gov.nih.nlm.utils.Constants;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

public class ACPCrawler {

	public void crawl() {}

	public void crawl(String startPage) {

		try {
			URL url = new URL(startPage);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-agent", Constants.USER_AGENT);
			Source source = new Source(conn);
			List<StartTag> startTagSet = source.getAllStartTags("a");
			System.out.println(startTagSet.size());
			for(StartTag stag : startTagSet){
				System.out.println("Tag is: " + stag.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

}
