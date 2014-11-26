package gov.nih.nlm.semrep.utils;

import java.io.PrintWriter;

/***
 * This class reads the SemRep output from the SKR API
 * that uses SemRep to parse the input text, and produces
 * a flat file of all predications in the input text
 */

public class PredicationBuilder {

	@SuppressWarnings("unused")
	public void getFilePredications (String inputFile, String outputFile){
		Predication pred = new Predication();
		String preferred_name = outputFile.replaceAll("_S", " ").replaceAll("_", " ");
		preferred_name = preferred_name.substring(preferred_name.indexOf("/")+1, preferred_name.length());
		
		FlatFileContentIterator ffcit = new FlatFileContentIterator(inputFile);
		try{

			PrintWriter out = new PrintWriter(outputFile);
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
					String subject = pred.getSubject(line);
					String predicate = pred.getPredicate(line);
					String object = pred.getObject(line);
					serialize(subject, predicate, object, out);
				}
			}
			ffcit.close();
			out.close();
		}
		catch (Exception e){
			//FIXME - shouldnt you print a stacktrace?
		}
	}

	public void serialize(String subject, String predicate, String object, PrintWriter out) {
		String delimeter = "-";
		out.println(subject+delimeter+predicate+delimeter+object);
	}

}
