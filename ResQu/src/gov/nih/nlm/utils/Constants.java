package gov.nih.nlm.utils;

public class Constants {

	public static final String PUBMED = "pubmed";
	public static final String SIMPLE_MESH_HEADINGS = "[MH] AND Humans[mh] AND Clinical Trial [PTYP] AND 1860/01:2014/08[DCOM] ";
	public static final String COMPLEX_MESH_HEADINGS = "/drug therapy"+SIMPLE_MESH_HEADINGS;
	public static final String DRUG_THERAPY="drug therapy";
	public static final String SIMPLE_EXTENSION="_S";
	public static final String COMPLEX_EXTENSION="_C";
	public static final int  NO_OF_CITATIONS = 500;
	public static final String MAX_NUMBER = "150000";
	
	public static final String EMAIL = "nishita@knoesis.org";
	public static final String UTS_USER_NAME = "aksnish11";
	public static final String UTS_PASSWORD ="$Nih2014";
	public static final String DEFAULT_COMMAND = "semrep -D -E";
	public static final String METAMAP_ALL_OPTIONS = "metamap -b";
	public static final String METAMAP_COMMAND = "metamap -J -I -Y dsyn,fndg,inpo,phsu";

	public static final String QUERY = "";
	public static final String DATA_FOLDER = "data/";//"/usr/share/data/resqu/data/";
	public static final String INPUT_FOLDER ="input/";
	public static final String DISEASE_LIST=DATA_FOLDER+"disease_topic_list.txt";
	public static final String DISEASE_FOLDER = DATA_FOLDER+INPUT_FOLDER+"disease/";
	public static final String DRUG_FOLDER = DATA_FOLDER+INPUT_FOLDER+"drug/";
	public static final String SEMREP_FOLDER = DATA_FOLDER+"semrep/";
	
	public static final String SUMMARIZE_FOLDER = DATA_FOLDER+"summarized-triples/";
	public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.9.0.11) Gecko/2009060215 Firefox/3.0.11 GTB6 (.NET CLR 3.5.30729)";
	

	public static final String CRAWLER_FILE_PREFIX="file:";
	public static final String CRAWLER_DATA_FOLDER="jericho/";
	public static final String CRAWLER_DATA_FOLDER_PATH =DATA_FOLDER+CRAWLER_DATA_FOLDER;

	public static final String METAMAP_INPUT_FOLDER =DATA_FOLDER+"metamap-input/";
	public static final String METAMAP_OUTPUT_FOLDER =DATA_FOLDER+"metamap-output/";
	
	public static final String SERIALZED_MAPS_FOLDER = DATA_FOLDER+"serializedMaps/";
	public static final String SUM_DOC_DIR = DATA_FOLDER+"summary-docs/";
	public static final String CONTENT_FIELD = "content";
	
	/*Core Constants*/
	
	//public static final  LUCENE_VERSION = 4.10.2;
}
