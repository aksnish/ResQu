/******************************
* Name: 
* Date
* People: Nishita, Tom, Mike, Delroy, Amit
* Decription: 
*	The goal of this project is
*	to develop a methodology that automatically
*	determines the quality of a automatic, abstractive summary 
*	of MEDLINE articles.
******************************/

Codebase: 
	- svn co http://knoesis-svn.cs.wright.edu/svn/nishita/ResQu/

Prerequisites
	- Eclipse Kepler (3.2)
	- Ant 1.7
	- ??

Configuration
	- Establish a port redirect on port 3306 locally to khpco
	- 
Data:
	- All data for the project can be retrieved from the knoesis-hpco server
	- /usr/share/data/resqu/data/

Step 1
---------
	- Manually specify a list of query terms whose summaries must be generated
	- /data/disease_topic_list.txt
 
Step 2
---------
	- Get MEDLINE citations for the specified queries
	- run src/drivers/PubMedCitationRetriever 
	- INPUT: /data/disease_topic_list.txt
	- OUTPUT: /data/disease/
	- NB: Organize raw text according to prescribed MEDLINE format
		UI - 10026156
		TI - Not Available
		AB - The ectopic expression of the carboxyl-terminal ...
		
Step 3
--------
	- Extract semantic predications from the generated MEDLINE citations.
	- INPUT: 
	- OUTPUT: 
	
	- run src/drivers/PubMedCitationRetriever 
	- This code will read the passages file and create the Medline format concatenating to each document every relevant passage 

Step 2
---------


- alternatively these can be processed with the SemRep source code yourself (http://skr.nlm.nih.gov/)

		
HOW TO 

Step 1 - ??
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
