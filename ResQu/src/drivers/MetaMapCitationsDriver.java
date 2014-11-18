package drivers;

import gov.nih.nlm.pubmed.MetaMapConverter;

public class MetaMapCitationsDriver {

	public static void main(String[] args) {
		
		MetaMapConverter mc = new MetaMapConverter();
		String results = mc.getMetaMapFormat("data/disease/Migraine_Disorders_S");
		System.out.println(results);

	}

}
