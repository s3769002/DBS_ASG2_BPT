#!/bin/bash

sed '1d' data.csv | cat | awk -F "\"*,\"*" '{$11=$8$2; print $0,$11;}' OFS=, | sort -t"," -k11  | cut -d"," -f-10 > sorted.csv
