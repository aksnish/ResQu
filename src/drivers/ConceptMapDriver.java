/**********************************
 * @(#)				ConceptMapDriver.java
 * @author			Delroy Cameron
 * @version			1.0 Nov 26, 2014
 */
package drivers;

import java.io.File;
import java.io.IOException;

import gov.nih.nlm.mappers.ConceptMapper;
import gov.nih.nlm.model.SummaryDocument;
import gov.nih.nlm.utils.Constants;

public class ConceptMapDriver {

	public static void main(String[] args) throws IOException{
		String outputDir = Constants.SUM_DOC_DIR;
		ConceptMapper cp = new ConceptMapper();
		File dir = new File(Constants.SUMMARIZE_FOLDER);
		File [] list = dir.listFiles();
		String filename;
		
		int i=0;
		for(File file : list){
			filename =Constants.SUMMARIZE_FOLDER+file.getName();
			//System.out.println("--------------------");
			SummaryDocument sumDoc = cp.getConceptMap(filename);
			//break;
			i++;
			
			serialize(sumDoc, outputDir+"topic-"+i);
		}
	}

	private static void serialize(SummaryDocument sumDoc, String string) {
		// TODO Auto-generated method stub
		
	}
}