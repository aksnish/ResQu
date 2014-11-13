package gov.nih.nlm.pubmed;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import gov.nih.nlm.ncbi.www.soap.eutils.*;
import gov.nih.nlm.ncbi.www.soap.eutils.EFetchPubmedServiceStub.AbstractTextType;
import gov.nih.nlm.ncbi.www.soap.eutils.EFetchPubmedServiceStub.ArticleType;
import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceStub.ObjUrlType;
public class PMWriter
{

	//private static StringBuilder OUT = new StringBuilder();
	private static PrintStream OUT;
	private static int noOfArticles;

	static String fetchIds="";
	static String [] fetcharray={ "" };
	static String[] ids = { "" };

	
	public static void getPredications(String query,int number) throws FileNotFoundException
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

		catch (Exception e) { System.out.println(e.toString()); 
		}
		printCitations(ids);

	}

	public static void getAllPredications(String query) throws FileNotFoundException
	{
		try
		{
			EUtilsServiceStub service = new EUtilsServiceStub();

			EUtilsServiceStub.ESearchRequest req = new EUtilsServiceStub.ESearchRequest();
			req.setDb("pubmed");
			req.setTerm(query);
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

		printCitations(fetcharray);
	}

	public static void printCitations(String[] pmidList) throws FileNotFoundException{
		OUT = new PrintStream(new FileOutputStream("Engineer.txt", true)); ;

		try
		{
			EFetchPubmedServiceStub service = new EFetchPubmedServiceStub();
			EFetchPubmedServiceStub.EFetchRequest req = new EFetchPubmedServiceStub.EFetchRequest();

			for(int i = 0 ; i<pmidList.length;i++){
				req.setId(pmidList[i]);

				EFetchPubmedServiceStub.EFetchResult res = service.run_eFetch(req);

				for (int k = 0; k < res.getPubmedArticleSet().getPubmedArticleSetChoice().length; k++)
				{
					EFetchPubmedServiceStub.PubmedArticleType art = res.getPubmedArticleSet().getPubmedArticleSetChoice()[k].getPubmedArticle();
					if(art!=null) {

						try {
							OUT.print("\nPMID  - " + (art.getMedlineCitation().getPMID()).toString().replaceAll("[^\\x00-\\x7F]", ""));
							OUT.print("\nTI  - " + (art.getMedlineCitation().getArticle().getArticleTitle()).toString().replaceAll("[^\\x00-\\x7F]", ""));
							OUT.print("\nAB  - " );

							ArticleType articleType = art.getMedlineCitation().getArticle();
							AbstractTextType[] abs	= articleType.getAbstract().getAbstractText();
							String ascii = null ;
							for (int j = 0; j < abs.length; j++)
								ascii = (abs[j]).toString().replaceAll("[^\\x00-\\x7F]", "");
							OUT.print(ascii);
							OUT.print("\n");

						} catch (Exception e) {
							OUT.print("\n");
						}
					} 
				}
			}
		}
		catch (Exception e) { 
			e.printStackTrace(); 
		}
		//System.out.println(OUT);

	}

	public static void main(String[] args) throws FileNotFoundException {
		//getPredications("Asthma", 10);
		getAllPredications("Asthma");
	}
}

