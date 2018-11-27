#!/usr/bin/env bash

apt-get update && apt-get install -y wget

# Compile Phase1
cd /tmp/data/asu_cse512-master/
sbt assembly
chmod 755 /tmp/data/asu_cse512-master/target/scala-2.11/CSE512-Project-Phase1-Template-assembly-0.1.0.jar

# Compile Phase2
cd /tmp/data/asu_cse512_phase2-master/
sbt clean assembly
chmod 755 /tmp/data/asu_cse512_phase2-master/target/scala-2.11/CSE512-Hotspot-Analysis-Template-assembly-0.1.0.jar

# Download CVS
cd /tmp/data/asu_cse512_phase2-master/src/resources
wget --load-cookies /tmp/cookies.txt "https://docs.google.com/uc?export=download&confirm=$(wget --quiet --save-cookies /tmp/cookies.txt --keep-session-cookies --no-check-certificate 'https://docs.google.com/uc?export=download&id=1Frq7VM_Cb8G_vaQOJ6F3jR6loVlBPdAS' -O- | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1\n/p')&id=1Frq7VM_Cb8G_vaQOJ6F3jR6loVlBPdAS" -O yellow_tripdata_2009-01_point.csv && rm -rf /tmp/cookies.txt
