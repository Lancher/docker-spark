
## Setup
1. Install Docker and Docker-Compose on Ubuntu 16.104
2. Git checkout different GitB branches with different CPU/Mem/Nodes settings.
3. Run `docker exec -it` to get into master node.

## Run Phase 1

```bash
cd /tmp/data/asu_cse512-master/
sbt assembly
/usr/spark-2.3.1/bin/spark-submit \
/tmp/data/asu_cse512-master/target/scala-2.11/CSE512-Project-Phase1-Template-assembly-0.1.0.jar \
/tmp/data/asu_cse512-master/result/output \
rangequery /tmp/data/asu_cse512-master/src/resources/arealm10000.csv -93.63173,33.0183,-93.359203,33.219456 \
rangejoinquery /tmp/data/asu_cse512-master/src/resources/arealm10000.csv /tmp/data/asu_cse512-master/src/resources/zcta10000.csv \
distancequery /tmp/data/asu_cse512-master/src/resources/arealm10000.csv -88.331492,32.324142 1 \
distancejoinquery /tmp/data/asu_cse512-master/src/resources/arealm10000.csv /tmp/data/asu_cse512-master/src/resources/arealm10000.csv 0.1
```

## Run Phase 2

```bash
cd /tmp/data/asu_cse512_phase2-master/
sbt clean assembly

/usr/spark-2.3.1/bin/spark-submit \
/tmp/data/asu_cse512_phase2-master/target/scala-2.11/CSE512-Hotspot-Analysis-Template-assembly-0.1.0.jar \
/tmp/data/asu_cse512_phase2-master/result/output \
hotzoneanalysis \
/tmp/data/asu_cse512_phase2-master/src/resources/point-hotzone.csv \
/tmp/data/asu_cse512_phase2-master/src/resources/zone-hotzone.csv

cd /tmp/data/asu_cse512_phase2-master/src/resources
wget --load-cookies /tmp/cookies.txt "https://docs.google.com/uc?export=download&confirm=$(wget --quiet --save-cookies /tmp/cookies.txt --keep-session-cookies --no-check-certificate 'https://docs.google.com/uc?export=download&id=1Frq7VM_Cb8G_vaQOJ6F3jR6loVlBPdAS' -O- | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1\n/p')&id=1Frq7VM_Cb8G_vaQOJ6F3jR6loVlBPdAS" -O yellow_tripdata_2009-01_point.csv && rm -rf /tmp/cookies.txt

/usr/spark-2.3.1/bin/spark-submit \
/tmp/data/asu_cse512_phase2-master/target/scala-2.11/CSE512-Hotspot-Analysis-Template-assembly-0.1.0.jar \
/tmp/data/asu_cse512_phase2-master/result/output \
hotcellanalysis \
/tmp/data/asu_cse512_phase2-master/src/resources/yellow_tripdata_2009-01_point.csv

```


# Testing Setting

Node 1, 2, 4
Core 2, 4
Mem 4, 6

Hence, there ate 12 possibility.