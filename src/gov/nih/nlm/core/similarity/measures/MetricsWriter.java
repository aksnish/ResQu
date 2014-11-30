package gov.nih.nlm.core.similarity.measures;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class MetricsWriter {
	public void writeMetrics(String path_name, List<String> cosineSimList, List<String> eudDistList){
		String metricsFolder = "data/metrics/";

		String list1 = buildString(cosineSimList); 
		String list2 = buildString(eudDistList);
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(new File(metricsFolder+path_name));
			writer.println("# Define 2 vectors");
			writer.println("cosine <- c("+list1.replaceAll(" ", ",")+")");
			writer.println("eucliedian <- c("+list2.replaceAll(" ", ",")+")");
			writer.println("# Graph cars using a y axis that ranges from 0 to 12");
			writer.println("plot(cosine, type=\"o\",  pch=20, col=\"blue\", ylim=c(0,4), ann=FALSE)");
			writer.println("# Graph trucks with red dashed line and square points");
			writer.println("lines(eucliedian, type=\"o\", pch=20, lty=2, col=\"red\")");
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

	public String buildString (List<String> str){
		StringBuilder sb = new StringBuilder();
		for (String s : str)
		{
			sb.append(s).append(" ");
		}
		return sb.toString().trim();
	}
}
