Step 1
---------
	- Process Documents to obtain predications from text.
	- organize raw text according to prescribed Medline format
		UI - 10026156
		TI - Not Available
		AB - The ectopic expression of the carboxyl-terminal ...
	
	- run src/drivers/PubMedCitationRetriever 
	- This code will read the passages file and create the Medline format concatenating to each document every relevant passage 

Step 2
---------


- alternatively these can be processed with the SemRep source code yourself (http://skr.nlm.nih.gov/)

		
HOW TO RUN VARIOUS TASKS

Step 1 - THE LSMDFS ALGORITHM
---------
	- ant compile citation-driver

Step 2 - SEMREP THE MEDLINE CITATIONS
---------
	- ant compile semrep-driver
	
Step 3 - SUMMARIZE THE SEMREPED CIATIONS
---------
	- sh ./sum-script.sh 
	
Step 4 - NEO4J
---------
	- sh ./sum-script.sh 
	
Step 5 - JERICHO CRAWLER
---------
	- sh ./sum-script.sh 