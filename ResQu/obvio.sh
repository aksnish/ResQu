start=$(date +"%s")

svn update
echo "------------------------------------------"
echo "STEP1: GETTING CORPUS STATISTICS FROM MEDLINE..."
echo "------------------------------------------"
ant compile medline-corpus-selector
echo "------------------------------------------"

echo "STEP2: GENERATING THE MEDLINE TO MESH MAPPINGS..."
echo "------------------------------------------"
cd scripts
./medline-to-mesh.sh
cd .. 
echo "------------------------------------------"

echo "STEP3: GETTING PREDICATIONS FOR EACH MEDLINE ARTICLE. ALSO CREATES THE PREDICATIONS GRAPH..."
ant compile medline-to-predication
echo "------------------------------------------"

echo "STEP4: MAPPING EACH PREDICATION TO ITS MEDLINE ARTICLES..."
ant compile predication-to-medline
echo "------------------------------------------"

echo "STEP5: CREATING THE MESH CONTEXT VECTORS FOR EACH PREDICATION..."
ant compile predication-to-mesh
echo "------------------------------------------"

echo "STEP6: GENERATING PATHS BETWEEN SOURCE AND TARGET..."
ant compile path-generator-driver
echo "------------------------------------------"

echo "STEP7: CREATING THE RELEVANT SUBSET OF MESH SEMANTIC SIMILARITY DESCRIPTOR PAIRS..."
ant compile similarity-subset-creator
echo "------------------------------------------"

echo "STEP8: OBTAINING LABELS FOR CUIS AND ENTREZ GENES FROM THE BKR..."
ant compile cui-to-label-map
echo "------------------------------------------"

echo "STEP9: BUILDING THE PATH VECTORS..."
ant compile path-vector-builder
echo "------------------------------------------"

echo "STEP10: GENERATING SUBGRAPHS..."
ant compile subgraph-generator
echo "------------------------------------------"

echo "STEP11: RANKING RARITY OF SUBGRAPHS "
ant compile rarity-ranker-driver

end=$(date +"%s")
rawextime=`expr $end - $start`
extime=$(($rawextime / 60))
echo "Total Execution Time: ($rawextime / 60) =  $extime minutes"
echo "-----------------DONE-------------------------"
