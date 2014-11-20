package gov.nih.nlm.neo4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

//import com.personal.neo4j.Main.RelTypes;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


public class UniqueNodes {

	public void createNodespace() throws IOException {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( "data/neo2" );
		try (Transaction tx = graphDb.beginTx()) {

			FileReader fr = new FileReader("test.txt");
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(fr);
			String line = "";

			Map<String, Node> check = new HashMap<String, Node>();
			while ((line = br.readLine()) != null) {
				if(line.contains("|")){
					line = line.replaceAll("\\|\\|\\|", "\\|").replaceAll("\\|\\|", "");


					String s = line;
					String[] sa = s.split("\\|");

					if (!check.containsKey(sa[2])) {
						Node node1 = graphDb.createNode();
						node1.setProperty("CUI", sa[2]);
						node1.setProperty("name",sa[3]);
						node1.setProperty("semantic_type",sa[4]);
						sa[0]=sa[0].substring(0, sa[0].indexOf("."));
						node1.setProperty("PMID",sa[0]);
						check.put(sa[2], node1);
					}

					if (!check.containsKey(sa[7])) {
						Node node2 = graphDb.createNode();
						node2.setProperty("CUI", sa[7]);
						node2.setProperty("name", sa[8]);
						node2.setProperty("semantic_type",sa[9]);
						check.put(sa[7], node2);
					}

					//check.get(sa[2]).createRelationshipTo(check.get(sa[7]),RelTypes.TREATS);
				}
			}

			tx.success();
			System.out.println("Done");
		}

	}

//	public static void main (String args []) throws IOException{

//		UniqueNodes un = new UniqueNodes();
//		un.createNodespace();

//	}
}
