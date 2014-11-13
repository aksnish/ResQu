start=$(date +"%s")

echo "------------------------------------------"
echo "STEP1: GETTING 500 CITATIONS FROM MEDLINE..."
echo "------------------------------------------"
ant compile citation-driver
echo "------------------------------------------"

echo "------------------------------------------"
echo "STEP2: SEMREP THE MEDLINECITATIONS..."
echo "------------------------------------------"
ant compile semrep-driver
echo "------------------------------------------"

echo "------------------------------------------"
echo "STEP3: SUMMARIZING THE SEMREPED CITATIONS..."
echo "------------------------------------------"
./sum-script.sh
echo "------------------------------------------"

end=$(date +"%s")
rawextime=`expr $end - $start`
extime=$(($rawextime / 60))
echo "Total Execution Time: ($rawextime / 60) =  $extime minutes"
echo "-----------------DONE-------------------------"
