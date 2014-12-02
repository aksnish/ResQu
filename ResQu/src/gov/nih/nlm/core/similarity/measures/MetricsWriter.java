package gov.nih.nlm.core.similarity.measures;

import gov.nih.nlm.utils.Constants;
import gov.nih.nlm.utils.DirectoryFileListIterator;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MetricsWriter {
	public void writeMetrics(String path_name){
		String metricsFolder = "data/metrics/";
		String cosstr = null;
		String eudstr = null;
		PrintWriter writer = null;
		DirectoryFileListIterator dir = new DirectoryFileListIterator();
		List<String> files = dir.getDirectoryFolderList(path_name);
		for(String file : files){
			Path p = Paths.get(file);
			String filename= p.getFileName().toString();
			if(file.contains("_cos")){
				cosstr = readdSerializedFile(file);
			}
			else if(file.contains("_eud")){
				eudstr = readdSerializedFile(file);
			}
			try{
				writer = new PrintWriter(new File(metricsFolder+filename+".R"));
				writer.println("# Define 2 vectors");
				writer.println("cosine <- c("+cosstr+")");
				writer.println("#eucliedian <- c("+eudstr+")");
				writer.println("# Graph cars using a y axis that ranges from 0 to 12");
				writer.println("plot(cosine, type=\"o\",  pch=20, col=\"blue\", ylim=c(0,4), ann=FALSE)");
				writer.println("# Graph trucks with red dashed line and square points");
				writer.println("#lines(eucliedian, type=\"o\", pch=20, lty=2, col=\"red\")");
				writer.println("# Create a title with a red, bold/italic font");
				writer.println("box()");
				writer.println("title(xlab=\"Y-axis\", col.lab=rgb(0,0.5,0))");
				writer.println("title(ylab=\"X-axis\", col.lab=rgb(0,0.5,0))");
				writer.println("title(main=\"Diseases\", col.main=\"blue\", font.main=4)");
			}catch(Exception e){
				e.printStackTrace();
			}
			writer.close();
		}

	}


	public String readdSerializedFile (String filename){
		double acard = 0;
		try {
			FileInputStream in = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(in);
			acard = (double) (ois.readObject());
			ois.close();
		} catch (Exception e) {
			System.out.println("Problem serializing: " + e);
		}
		//			System.out.println("File is: "+file+"  :  " + acard);
		return Double.toString(acard);
	}

	public String buildString (List<String> str){
		StringBuilder sb = new StringBuilder();
		for (String s : str)
		{
			sb.append(s).append(" ");
		}
		return sb.toString().trim();
	}

	public static void main(String[] args) {
		MetricsWriter mw = new MetricsWriter();
		mw.writeMetrics(Constants.DATA_FOLDER+"r-lists/");
		//		mw.readdSerializedFile(Constants.DATA_FOLDER+"r-lists/");
	}
}
