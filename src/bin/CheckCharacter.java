package bin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CheckCharacter {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File("data/metamap/Ibuprofen.txt")));
		String line;
		while((line=br.readLine())!=null){
			if(("").equalsIgnoreCase(line)){
				System.out.println("NULL");
			}
			else{
				System.out.println("line :"+ line);
			}
		}
	}

}
