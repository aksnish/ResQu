package bin;

import java.io.File;
import java.io.IOException;


/*Script implemented in sum-script.sh */
public class SummarizerDriverTest {


	public static void main(String[] args) throws IOException {

		Process pr = null;
		Runtime rt = Runtime.getRuntime();
		StringBuilder command = new StringBuilder();
		String filename = null, preferred_name = null;
		
		String base_path= "/home/nishita/workspace/ResQu/sem/";
		String repo ="/home/nishita/Documents/Projects/Summarization-2012-SPA/bin/";
		
		String[] cmd = { "/bin/bash", "cd /home/nishita/Documents/Projects/Summarization-2012-SPA/bin/", "ls -l"};
		
		File directory = new File("sem/");
		File[] directoryListing = directory.listFiles();

		if (directoryListing != null) {
			for (File file : directoryListing) {

				filename = file.toString().substring(file.toString().indexOf("/")+1,file.toString().length());
				preferred_name = file.toString().substring(file.toString().indexOf("/")+1,file.toString().indexOf("_"));

				System.out.println(filename + "\t" + preferred_name);
				
				pr = rt.exec(cmd);
				System.out.println(pr.getOutputStream().toString());
				
				command = new StringBuilder();
				command.append("perl summarize_test.pl " +base_path + filename +" "+ preferred_name);
				pr = rt.exec(command.toString());
				//System.out.println(command);

				//go back to project directory
				break;
			} 
		}
		pr.destroy();
	}
}


//perl summarize_test.pl Test.txt Asthma
