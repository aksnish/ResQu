package drivers;

import gov.nih.nlm.pubmed.MetaMapConverter;

public class MetaMapCitationsDriver {

	public static void main(String[] args) {
		
		MetaMapConverter mc = new MetaMapConverter();
		String results = mc.getMetaMapFormat("jericho/jeri-out.txt");
		System.out.println(results);

	}

}
