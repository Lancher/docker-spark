#!/usr/bin/env bash

# Run Phase 1
/usr/spark-2.3.1/bin/spark-submit \
/tmp/data/asu_cse512-master/target/scala-2.11/CSE512-Project-Phase1-Template-assembly-0.1.0.jar \
/tmp/data/asu_cse512-master/result/output \
rangequery /tmp/data/asu_cse512-master/src/resources/arealm10000.csv -93.63173,33.0183,-93.359203,33.219456 \
rangejoinquery /tmp/data/asu_cse512-master/src/resources/arealm10000.csv /tmp/data/asu_cse512-master/src/resources/zcta10000.csv \
distancequery /tmp/data/asu_cse512-master/src/resources/arealm10000.csv -88.331492,32.324142 1 \
distancejoinquery /tmp/data/asu_cse512-master/src/resources/arealm10000.csv /tmp/data/asu_cse512-master/src/resources/arealm10000.csv 0.1

# Run Phase 2
/usr/spark-2.3.1/bin/spark-submit \
/tmp/data/asu_cse512_phase2-master/target/scala-2.11/CSE512-Hotspot-Analysis-Template-assembly-0.1.0.jar \
/tmp/data/asu_cse512_phase2-master/result/output \
hotzoneanalysis \
/tmp/data/asu_cse512_phase2-master/src/resources/point-hotzone.csv \
/tmp/data/asu_cse512_phase2-master/src/resources/zone-hotzone.csv

/usr/spark-2.3.1/bin/spark-submit \
/tmp/data/asu_cse512_phase2-master/target/scala-2.11/CSE512-Hotspot-Analysis-Template-assembly-0.1.0.jar \
/tmp/data/asu_cse512_phase2-master/result/output \
hotcellanalysis \
/tmp/data/asu_cse512_phase2-master/src/resources/yellow_tripdata_2009-01_point.csv