package gov.nih.nlm.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.Tag;

public class ClinicalResourceCrawler {

	public List<Tag> findFontBetween(String startPage, int startIndex, int endIndex ){
		if(startIndex == -1 || endIndex == -1){
			System.out.println("startIndex == -1 || endIndex == -1");
			return null;
		}

		List<Tag> fontTagList = new ArrayList<Tag>();
		try{
			Source source=new Source(new URL(startPage));
			int pos = startIndex;
			//			System.out.println("start :"+startIndex+" end :" + endIndex);
			while(pos != -1 && pos <= endIndex){
				StartTag font_tag = source.getNextStartTag(pos,HTMLElementName.FONT);
				if(font_tag != null){
					if(pos < endIndex){
						fontTagList.add(font_tag);
						//						System.out.println("\t\tUses: "+font_tag.getElement().getTextExtractor());
					}else{
						System.out.println("\t\tOther: " +font_tag.getElement().getTextExtractor());
					}
					pos = font_tag.getEnd();
				}
			}
		}catch(Exception e){
			System.out.println("Error while finding font tag between" + "<body>" + " and "+ "</body>"+ " in "+ startPage);
		}
		return fontTagList;
	}

	public int getBoldTextIndex(String startPage, String text){
		try {
			Source source=new Source(new URL(startPage));
			List<Element> bold = source.getAllElements(HTMLElementName.B);
			for(Element ele: bold){
				String bold_ele = ele.getContent().toString();
				if(bold_ele.equals(text)){
					return ele.getBegin();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@SuppressWarnings("unused")
	private void getBoldTextIndex(String startPage){
		try {
			Source source=new Source(new URL(startPage));
			List<Element> bold = source.getAllElements(HTMLElementName.B);
			for(Element ele: bold){
				String bold_ele = ele.getContent().toString();
				System.out.println("Acetominophen : "+bold_ele);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
