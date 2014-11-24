package drivers;

import gov.nih.nlm.pubmed.MetaMapConverter;
import gov.nih.nlm.utils.Constants;

public class MetaMapCitationsDriver {

	public static void main(String[] args) {

		MetaMapConverter mc = new MetaMapConverter();
		String inputBuf = Constants.METAMAP_FOLDER_PATH+"/Acetominophen.txt";
		String results = mc.getMetaMapFormat(inputBuf);
		System.out.println(results);

	}

}
