package drivers;

import java.io.IOException;

import gov.nih.nlm.neo4j.UniqueNodes;

public class Neo4jDriver {
	
	public static void main (String args []) throws IOException{

		System.out.println("Start");
		UniqueNodes un = new UniqueNodes();
		un.createNodespace();

	}

}
