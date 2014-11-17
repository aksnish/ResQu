package gov.nih.nlm.pubmed;


import java.util.SortedSet;

import gov.nih.nlm.ncbi.www.soap.eutils.*;
import gov.nih.nlm.ncbi.www.soap.eutils.EFetchPubmedServiceStub.AbstractTextType;
import gov.nih.nlm.ncbi.www.soap.eutils.EFetchPubmedServiceStub.ArticleType;
public class PubMedCitationRetriever
{

	//private static StringBuilder OUT = new StringBuilder();
	private static StringBuffer OUT = new StringBuffer();
	//private static PrintStream OUT;
	private static int noOfArticles;

	static String fetchIds="";
	static String [] fetcharray={ "" };
	static String[] ids = { "" };


	public static StringBuffer getPredications(String query,int number)
	{
		try
		{
			EUtilsServiceStub service = new EUtilsServiceStub();

			EUtilsServiceStub.ESearchRequest req = new EUtilsServiceStub.ESearchRequest();
			req.setDb("pubmed");
			req.setTerm(query);
			req.setRetMax(Integer.toString(number));

			EUtilsServiceStub.ESearchResult res = service.run_eSearch(req);
			noOfArticles = res.getIdList().getId().length;
			ids[0] = "";
			for (int i = 0; i < noOfArticles; i++)
			{
				if (i > 0) ids[0] += ",";
				ids[0] += res.getIdList().getId()[i];
			}

		}

		catch (Exception e) { System.out.append(e.toString()); 
		}
		StringBuffer ps = printCitations(ids);
		return ps;
	}

	@Deprecated
	public static StringBuffer getPredications()
	{
		try
		{
			EUtilsServiceStub service = new EUtilsServiceStub();

			EUtilsServiceStub.ESearchRequest req = new EUtilsServiceStub.ESearchRequest();
			req.setDb("pubmed");
			req.setTerm("");
			req.setRetMax("10");

			EUtilsServiceStub.ESearchResult res = service.run_eSearch(req);
			noOfArticles = res.getIdList().getId().length;
			ids[0] = "";
			for (int i = 0; i < noOfArticles; i++)
			{
				if (i > 0) ids[0] += ",";
				ids[0] += res.getIdList().getId()[i];
			}

		}
		catch (Exception e) { System.out.println(e.toString()); }

		try
		{
			EUtilsServiceStub service = new EUtilsServiceStub();
			EUtilsServiceStub.ELinkRequest req = new EUtilsServiceStub.ELinkRequest();
			req.setDb("pubmed");
			req.setId(ids);
			EUtilsServiceStub.ELinkResult res = service.run_eLink(req);

			for (int i = 0; i < res.getLinkSet()[0].getLinkSetDb()[0].getLink().length; i++)
			{
				if (i > 0) fetchIds += ",";
				fetchIds += res.getLinkSet()[0].getLinkSetDb()[0].getLink()[i].getId().getString();
			}
			fetcharray = fetchIds.split(",");
		}
		catch (Exception e) { System.out.println(e.toString()); 
		}
		StringBuffer ps = printCitations(fetcharray);
		return ps;
	}

	public static StringBuffer getPredications(String query)
	{
		PubMedGetPMID pgp = new PubMedGetPMID();
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("Getting PMID List");
		String[] pmids = pgp.getPmidsForKeyword(query,"pubmed", "150000");

		System.out.println("Total number of citations for query : "+ pmids.length);
		StringBuffer OUT = printCitations(pmids);
		return OUT;
	}

	public static StringBuffer printCitations(String[] pmidList){

		try
		{
			EFetchPubmedServiceStub service = new EFetchPubmedServiceStub();
			EFetchPubmedServiceStub.EFetchRequest req = new EFetchPubmedServiceStub.EFetchRequest();

			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("Getting PubMed Article for each ciations");
			for(int i = 0 ; i<pmidList.length;i++){
				req.setId(pmidList[i]);

				EFetchPubmedServiceStub.EFetchResult res = service.run_eFetch(req);

				for (int k = 0; k < res.getPubmedArticleSet().getPubmedArticleSetChoice().length; k++)
				{
					EFetchPubmedServiceStub.PubmedArticleType art = res.getPubmedArticleSet().getPubmedArticleSetChoice()[k].getPubmedArticle();
					if(art!=null) {

						try {
							OUT.append("\nPMID  - " + (art.getMedlineCitation().getPMID()).toString().replaceAll("[^\\x00-\\x7F]", ""));
							OUT.append("\nTI  - " + (art.getMedlineCitation().getArticle().getArticleTitle()).toString().replaceAll("[^\\x00-\\x7F]", ""));
							OUT.append("\nAB  - " );

							ArticleType articleType = art.getMedlineCitation().getArticle();
							AbstractTextType[] abs	= articleType.getAbstract().getAbstractText();
							String ascii = null ;
							for (int j = 0; j < abs.length; j++)
								ascii = (abs[j]).toString().replaceAll("[^\\x00-\\x7F]", "");
							OUT.append(ascii);
							OUT.append("\n");

						} catch (Exception e) {
							OUT.append("\n");
						}
					} 
				}
			}
			System.out.println("Got all articles");
		}
		catch (Exception e) { 
			e.printStackTrace(); 
		}
		return OUT;
	}

	//	public static void main(String[] args) throws IOException {
	//		PubMedCitationRetriever pm = new PubMedCitationRetriever();
	//		String query="Testosterone[mh] AND prolactin AND sleep [mh] ";
	//		
	//		String result = getPredications(query).toString();
	//				try {
	//					FileWriter fw = new FileWriter("test.txt");
	//					BufferedWriter bw = new BufferedWriter(fw);
	//					bw.write(result);
	//		
	//					bw.close();
	//				} catch (IOException e) {
	//					System.err.print("Unable to write to file ");
	//					e.printStackTrace();
	//				}
	//	}
}
