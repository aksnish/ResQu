start=$(date +"%s")

echo "------------------------------------------"
echo "STEP1: GETTING 500 CITATIONS FROM MEDLINE..."
echo "------------------------------------------"
#ant compile citation-driver
echo "------------------------------------------"

echo "------------------------------------------"
echo "STEP2: SEMREPPING THE MEDLINE CITATIONS..."
echo "------------------------------------------"
#ant compile semrep-driver
echo "------------------------------------------"

echo "------------------------------------------"
echo "STEP3: SUMMARIZING THE SEMREPED MEDLINE CITATIONS."
echo "------------------------------------------"
./sum-script.sh
echo "------------------------------------------"

echo "-----------------------------------------------------"
echo "STEP4: EXTRACTING TRIPLES FROM THE SUMMARIZED DOCUMENTS."
echo "-----------------------------------------------------"
ant compile triple-extractor-driver
echo "-----------------------------------------------------"

echo "------------------------------------------"
echo "STEP5: NEO4J STUFF..."
echo "------------------------------------------"
#ant compile neo4j-driver
echo "------------------------------------------"

echo "------------------------------------------"
echo "STEP6: GETTING GOLD STANDARD DATA USING JERICHO HTML CRAWLER..."
echo "------------------------------------------"
#ant compile jericho-driver
echo "------------------------------------------"

echo "------------------------------------------"
echo "STEP7: METAMAPPING THE CRAWLED DATA..."
echo "------------------------------------------"
#ant compile metamap-driver
echo "------------------------------------------"

end=$(date +"%s")
rawextime=`expr $end - $start`
extime=$(($rawextime / 60))
echo "Total Execution Time: ($rawextime / 60) =  $extime minutes"
echo "-----------------DONE-------------------------"
