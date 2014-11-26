/******************************
* Name: 
* Date: 
*	Start Date: Summer 2014
	End Date: Summer 2015 (projected) 
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

Configuration
	- Establish a port redirect on port 3306 locally to knoesis-hpco
	- ssh w007dhc@knoesis-hpco.cs.wright.edu -L 3306:localhost:3306

Data:
	- All data for the project are available on the knoesis-hpco server
	- /usr/share/data/resqu/data/ (alias DATA_DIR)

Aliases:
--------
	- PROJ_NAME = ResQu
	- DATA_DIR = /usr/share/data/resqu/data/
	- SRC = $PROJ_NAME/src
Step 1
---------
	- Manually specify a list of query terms whose summaries must be generated
	- $DATA_DIR/disease_topic_list.txt
	- NB: For this initial application, we will use 
		+ A list of 20 Diseases
		+ A list of 20 Drugs
 
Step 2
---------
	- Get MEDLINE citations for the specified queries (i.e., Drugs and/or Diseases)
	- run $SRC/drivers/PubMedCitationRetriever.java (ant compile citation-driver)
	- INPUT: $DATA_DIR/disease_topic_list.txt
	- OUTPUT: $DATA_DIR/data/disease/
		NB: Organize raw text according to prescribed MEDLINE format
		PMID - 10026156
		TI - Not Available
		AB - The ectopic expression of the carboxyl-terminal ...
		
Step 3
--------
	- Extract semantic predications from the generated MEDLINE citations.
	- run $SRC/drivers/TripleBuilderDriver.java (ant compile semrep-driver)
	- INPUT: MEDLINE Citations in MEDLINE format
		+ $DATA_DIR/data/disease/ (in MEDLINE FORMAT)
	- OUTPUT: Extracted semantic predications in SemRep Relational format 
		+ $DATA_DIR/semrep
	- NB: Semantic predications can also be processed with the SemRep source code yourself (http://skr.nlm.nih.gov/) - CLARIFY

Step 4
---------
	- 



		
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
