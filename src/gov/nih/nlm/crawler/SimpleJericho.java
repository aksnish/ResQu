package gov.nih.nlm.crawler;

import gov.nih.nlm.utils.Constants;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import scala.xml.dtd.ELEMENTS;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.HTMLElements;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

public class SimpleJericho {

	//static String htmlText = "<meter value=\"2\" min=\"0\" max=\"10\">2 out of 10</meter><br><meter value=\"0.6\">60%</meter>";

	static String startPage = "http://online.statref.com.ezproxy.libraries.wright.edu:2048/Document.aspx?FxId=539&SessionID=1E78E43GMGISTWWS#H&0&ChaptersTab&95uax5V0Y2R40OdOPr3SFg%3d%3d&&539";


	public void crawlURL() throws Exception {
				URL url = new URL(startPage);
				URLConnection conn = url.openConnection();
				conn.setRequestProperty("User-agent", Constants.USER_AGENT);
				Source source=new Source(conn);

		//Source source = new Source("<HTML><HEAD><TITLE>Your Title Here</TITLE></HEAD><BODY BGCOLOR=\"FFFFFF\"><CENTER><IMG SRC=\"clouds.jpg\"ALIGN=\"BOTTOM\"> </CENTER><HR><a href=\"http://somegreatsite.com\">Link Name</a>is a link to another nifty site H1>This is a Header</H1><H2>This is a Medium Header</H2>Send me mail at <a href=\"mailto:support@yourcompany.com\">support@yourcompany.com</a>.<P> This is a new paragraph!<P><B>This is a new paragraph!</B><BR> <B><I>This is a new sentence without a paragraph break, in bold italics.</I></B><HR></BODY></HTML>");

		Element table = source.getFirstElement();
		String tablecont = table.getContent().toString();
		System.out.println(tablecont);
		
		Segment htmlseg = new Segment(source, 0,source.length());
		
		System.out.println("seg : "+htmlseg);

		List<Element> para = source.getAllElements(HTMLElementName.P);
		for (Element element1 : para) {
			if (element1.getAttributes() != null) {
				String td = element1.getAllElements().toString();

				System.out.println("\n"+"P: " + td);

			}
		}


		List<Element> elementListTd = source.getAllElements(HTMLElementName.TD);

		//Scroll through the list of elements "td" page
		for (Element element : elementListTd) {
			if (element.getAttributes() != null) {
				String td = element.getAllElements().toString();
				String tag = "td";
				System.out.println("TD: " + td);
				//System.out.println(element.getContent());
				String conteudoAtributo = element.getTextExtractor().toString();
				System.out.println(conteudoAtributo);

				//	               if (td.contains(palavraCompara)) {
				//	                   List<Element> tabela;
				//					tabela.add(conteudoAtributo);
			}

		}

	}
}