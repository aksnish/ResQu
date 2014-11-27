package gov.nih.nlm.pubmed;

import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceStub;
import gov.nih.nlm.utils.Constants;

import java.util.SortedSet;
import java.util.TreeSet;

public class PubMedGetPMID {

	public String[] getPmidsForKeyword(String keyword, String dbName,String numHits) {
		try{
			EUtilsServiceStub service = new EUtilsServiceStub();
			EUtilsServiceStub.ESearchRequest req = new EUtilsServiceStub.ESearchRequest();

			req.setDb(dbName);
			req.setTerm(keyword);
			req.setRetMax(numHits);
			EUtilsServiceStub.ESearchResult res = service.run_eSearch(req);

			String pmid = "";
			for (int i = 0; i < Integer.parseInt(res.getCount()); i++){
				if(i>0)pmid += ",";
				pmid += res.getIdList().getId()[i];

			}
			String[] pmids = pmid.split(",");

			return pmids;
		}catch (Exception e) { 
			e.printStackTrace();
			return null;
		}
	}
	
	
	//	public static void main(String[] args) {
	//		PubMedGetPMID gpm = new PubMedGetPMID();
	//		String query="Migraine Disorders";
	//		String[] list = gpm.getPmidsForKeyword(query,Constants.PUBMED, Constants.MAX_NUMBER);
	//		for(String str :list){
	//			System.out.println("PMID : " +str+" " + list.length);
	//		}
	//	}
}
