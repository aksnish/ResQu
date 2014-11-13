package gov.nih.nlm.utils;

public class Constants {
	
	public static final String PUBMED = "pubmed";
	public static final String SIMPLE_MESH_HEADINGS = "[MH] AND Humans[mh] AND Clinical Trial [PTYP] AND 1860/01:2014/08[DCOM] ";
	public static final String COMPLEX_MESH_HEADINGS = "/drug therapy"+SIMPLE_MESH_HEADINGS;
	public static final int  NO_OF_CITATIONS = 500;
	public static final String EMAIL = "nishita@knoesis.org";
	public static final String UTS_USER_NAME = "aksnish11";
	public static final String UTS_PASSWORD ="$Nih2014";
	public static String defaultCommand = "semrep -D -E";
	public static String metaMapCommand = "metamap -J dsyn,fndg,inpo,phsu -E";

	public static final String QUERY = "";
	public static final String TOPIC_DISEASE_FILE = "disease/";
	public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.9.0.11) Gecko/2009060215 Firefox/3.0.11 GTB6 (.NET CLR 3.5.30729)";
	
}
