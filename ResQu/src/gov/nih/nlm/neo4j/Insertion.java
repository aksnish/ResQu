package gov.nih.nlm.neo4j;

/**
 * Represents the insertion of data in each
 * graph database
 * 
 * @author sotbeis
 * @email sotbeis@iti.gr
 */
public interface Insertion {
	
	/**
	 * Loads the data in each graph database
	 * @param datasetDir
	 */
	public void createGraph(String datasetDir);

}