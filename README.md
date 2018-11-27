
# Setup
1. Install Docker and Docker-Compose on Ubuntu 16.104
2. Git checkout different branches with different CPU/Mem/Nodes settings.
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

