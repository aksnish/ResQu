package gov.nih.nlm.neo4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

//import com.personal.neo4j.Main.RelTypes;

public class UniqueNodes {

	public void createNodespace() throws IOException {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( "db/graphDB" );

		try (Transaction tx = graphDb.beginTx()) {

			FileReader fr = new FileReader("RelTypes");
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(fr);
			String line = "";

			Map<String, Node> check = new HashMap<String, Node>();
			while ((line = br.readLine()) != null) {

				String s = line;
				String[] sa = s.split("-");



				if (!check.containsKey(sa[0])) {
					Node node1 = graphDb.createNode();
					node1.setProperty("id", sa[0]);
					check.put(sa[0], node1);
				}
				if (!check.containsKey(sa[1])) {
					Node node2 = graphDb.createNode();
					node2.setProperty("id", sa[1]);
					check.put(sa[1], node2);
				}

				//check.get(sa[0]).createRelationshipTo(check.get(sa[1]), RelTypes.TREATS);


			}

			tx.success();
		}
	}

	public static void main (String args []) throws IOException{

		UniqueNodes un = new UniqueNodes();
		un.createNodespace();

	}
}
