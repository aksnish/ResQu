package gov.nih.nlm.semrep.utils;

import java.io.File;
import java.io.PrintWriter;
import java.util.Set;

/***
 * This class reads the SemRep output from the SKR API
 * that uses SemRep to parse the input text, and produces
 * and flat file of all predications in the input text
 */

public class SemrepTripleExtractor {

	String predication ="";
	public String getPreds(String ip){
		boolean str = new File(ip).isFile();
		String ret;
		if(str==true){
			ret = getFilePredications(ip);
		}
		else{
			ret = getInputPredications(ip);
		}
		return ret;

	}
	@SuppressWarnings("unused")
	public String getFilePredications (String inputFile){ //, String outputFile){


		FlatFileContentIterator ffcit = new FlatFileContentIterator(inputFile);
		try{

			//PrintWriter out = new PrintWriter(outputFile);
			while (ffcit.hasNext())
			{
				String line = ffcit.next();
				if(line.contains("----"))
					continue;
				if(line == null)
					continue;
				if(line.split("\\|").length != 13 ){
					continue;
				}
				if(line.contains("|")){

					String subject = getSubjects(line);
					String predicate = getPredicate(line);
					String object = getObjects(line);
					if(object.equalsIgnoreCase("Migraine Disorders")&&(predicate.equalsIgnoreCase("TREATS")||predicate.equalsIgnoreCase("PREVENTS")))
						//						System.out.println(subject +"-" + predicate + "-" + object);
						//serialize(subject, predicate, object, out);
						predication +=subject+"-"+predicate+"-"+object+"\n";
				}
			}
			ffcit.close();
			//			out.close();
		}
		catch (Exception e){

		}
		return predication;
	}


	public String getInputPredications (String predString){ //, String outputFile){
		String [] preds = predString.split("\n");
		try{

			//PrintWriter out = new PrintWriter(outputFile);
			for(String pred : preds)
			{

				if(pred.contains("----"))
					continue;
				if(pred == null)
					continue;
				if(pred.split("\\|").length != 13 ){
					continue;
				}
				if(pred.contains("|")){

					String subject = getSubjects(pred);
					String predicate = getPredicate(pred);
					String object = getObjects(pred);
					if(object.equalsIgnoreCase("Migraine Disorders")&&(predicate.equals("TREATS")||predicate.equalsIgnoreCase("PREVENTS")))
						//						System.out.println(subject +"-" + predicate + "-" + object);
						//serialize(subject, predicate, object, out);
						predication += subject+"-"+predicate+"-"+object+"\n";
				}
			}
		}
		catch (Exception e){

		}
		return predication;
	}



	public void serialize(String subject, String predicate, String object, PrintWriter out) {
		String delimeter = "-";
		out.println(subject+delimeter+predicate+delimeter+object);
	}

	private String getSubjects(String line) {
		return line.split("\\|")[3].trim();
	}

	private String getPredicate(String line) {
		return line.split("\\|")[8].trim();
	}

	private String getObjects(String line) {


		return line.split("\\|")[10].trim();
	}

	//	public static void main (String args [])
	//	{
	//
	//		SemrepTripleExtractor semrep = new SemrepTripleExtractor();
	//		String inputFile= "semrep.txt";
	//		String outputFile= "nsemtest1.txt";
	//		semrep.getPredications(inputFile, outputFile);
	//	}

}
