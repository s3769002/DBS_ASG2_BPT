#!/bin/bash
#sed '1d' data.csv | cat | cut -d"," -f8,9 | sort | uniq > sensor_unique.csv
#cut -d, -f-4,6-6,8-
sed '1d' data.csv | cat | awk -F "\"*,\"*" '{$11=$8$2; print $0,$11;}' OFS=, | sort -t"," -k11  | cut -d"," -f-10 > sorted.csv

#sed '1d' data.csv | cat | awk -F "\"*,\"*" '{$11=$7$2; print $0,$11;}' OFS=, | sort -t"," -k11  | cut -d"," -f11 > testsorted.csv
#sed '1d' data.csv | cat | awk -F "\"*,\"*" '{$11=$7$2; print $0,$11;}' OFS=, | cut -d"," -f11 > testunsorted.csv