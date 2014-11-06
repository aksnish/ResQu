package gov.nih.nlm.utils;

public class Constants {
	
	public static final String PUBMED = "pubmed";
	public static final String TOPIC = "Asthma";
	public static final String SIMPLE_MESH_HEADINGS = "[MH] AND Humans[mh] AND Clinical Trial [PTYP] AND 1860/01:2014/08[DCOM] ";
	public static final String COMPLEX_MESH_HEADINGS = "/drug therapy[MH] AND Humans[mh] AND Clinical Trial [PTYP] AND 1860/01:2014/08[DCOM] ";
	//public static final String SEARCH = "Anemia, Sickle Cell/drug therapy[MH] AND Humans[mh] AND Clinical Trial [PTYP] AND 1860/01:2014/08[DCOM] ";
	public static final int  NO_OF_CITATIONS = 500;
	public static final String EMAIL = "nishita@knoesis.org";
	public static final String UTS_USER_NAME = "aksnish11";
	public static final String UTS_PASSWORD ="$Nih2014";
	public static String defaultCommand = "semrep -D -E";

	//public static final String SEARCH = TOPIC+SIMPLE_MESH_HEADINGS;
	public static final String SEARCH = TOPIC+COMPLEX_MESH_HEADINGS;
	
	
}
