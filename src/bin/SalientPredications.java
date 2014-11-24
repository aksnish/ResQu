package bin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SalientPredications {

	public static void main(String[] args) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader("data/semrep/creutzfeldt_jakob_syndrome_S.semrep"));
		String line;
		try {
			while ((line = br.readLine())!= null){
				if(line.contains("TREATS")&&line.contains("Creutzfeldt-Jakob Syndrome")){
					System.out.println(line);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//			System.out.println("hello");
	}

}
