#!/bin/bash
cd /home/nishita/Documents/Projects/Summarization2006-AR/bin/
base_dir="/home/nishita/workspace/ResQu/semrep/"
base_dir_files="$base_dir*"
for file in $base_dir_files
do
    if [[ $file == *semrep ]]
    then
	IFS='/' read -a ary <<< "${file}"
	filename=${ary[6]};
	IFS='_' read -a myary <<< "${filename}"
	#preferred_name=${myary[0]}
	preferred_name=${filename/_[A-Z].semrep/ }
	myFile="$base_dir$filename"
	perl summarize_treatment.pl $myFile $preferred_name
	#echo "$filename $myFile $preferred_name"
	break;
    fi	
done
