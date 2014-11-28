/**********************************
 * @(#)				ConceptMapDriver.java
 * @author			Delroy Cameron
 * @version			1.0 Nov 26, 2014
 */
package drivers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import gov.nih.nlm.mappers.ConceptMapper;
import gov.nih.nlm.model.SummaryDocument;
import gov.nih.nlm.utils.Constants;
import gov.nih.nlm.utils.FilenameGenerator;

public class ConceptMapDriver {

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		ConceptMapper cp = new ConceptMapper();
		FilenameGenerator filegen = new FilenameGenerator();

		File dir = new File(Constants.SUMMARIZE_FOLDER);
		File [] list = dir.listFiles();
		String filename, querystr = null, topic = null;
		SummaryDocument sumDoc = new SummaryDocument();
		String [] file_elements;
		String outputFile = null;
		List<String> conceptList = new ArrayList<String>();
		int i=1;
		for(File file : list){
			if(file.isDirectory()){
				File innerDir = new File(file.toString());
				File [] innerList = innerDir.listFiles();
				for(File innerFile : innerList){
					filename = innerFile.toString();
					file_elements = filename.split("/");
					querystr = file_elements[3];
					topic = file_elements[3].replaceAll("_S", "");
					conceptList= cp.getConceptMap(filename);

					sumDoc.setContentList(conceptList);
					sumDoc.setId(i);
					sumDoc.setTopic(topic);
					outputFile = filegen.setFilename(Constants.SUM_DOC_DIR, querystr, topic, i);
					System.out.println("--------------------");
					System.out.println(outputFile);
					serialize(conceptList,outputFile );
				}
			}
			i++;
		}
		//	deSerialize();
	}

	private static void serialize(SummaryDocument sumDoc, String outputFile) throws IOException {
		FileOutputStream fos =	new FileOutputStream(outputFile+".ser", true);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(sumDoc);
		oos.close();
		fos.close();

	}

	private static void serialize(List<String> sumDoc, String outputFile) throws IOException {
	PrintWriter writer;
	writer = new PrintWriter(outputFile+".txt");
	for(String str : sumDoc){
	writer.println(str);
	}
	writer.close();
	}

	public static void deSerialize () throws IOException, ClassNotFoundException
	{
		SummaryDocument results = new SummaryDocument();
		String filename;
		File dir = new File(Constants.SUM_DOC_DIR);
		File [] list = dir.listFiles();
		for(File file : list){
			if(file.isDirectory()){
				System.out.println();
				File innerDir = new File(file.toString());
				File [] innerList = innerDir.listFiles();
				for(File innerFile : innerList){
					System.out.println("------------------");
					filename =Constants.SUM_DOC_DIR+file.getName()+"/"+innerFile.getName();
					//System.out.println("file: "+filename);
					FileInputStream fis = new FileInputStream(filename);
					ObjectInputStream ois = new ObjectInputStream(fis);
					results = (SummaryDocument) ois.readObject();

					System.out.println("Summay_Doc_ID : " + results.getId());
					System.out.println("Summay_Doc_Topic : " + results.getTopic());
					for(String concept: results.getContentList()){
						System.out.println(concept);
					}
					ois.close();
					fis.close();
				}

			}
		}
	}
}