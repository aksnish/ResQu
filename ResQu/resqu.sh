start=$(date +"%s")

echo "------------------------------------------"
echo "STEP1: GETTING 500 CITATIONS FROM MEDLINE..."
echo "------------------------------------------"
ant compile citation-driver
echo "------------------------------------------"


end=$(date +"%s")
rawextime=`expr $end - $start`
extime=$(($rawextime / 60))
echo "Total Execution Time: ($rawextime / 60) =  $extime minutes"
echo "-----------------DONE-------------------------"
