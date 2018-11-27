#!/usr/bin/env bash

apt-get update && apt-get install -y wget

# Compile Phase1
cd /tmp/data/asu_cse512-master/
sbt assembly

# Compile Phase2
cd /tmp/data/asu_cse512_phase2-master/
sbt clean assembly

# Download CVS
cd /tmp/data/asu_cse512_phase2-master/src/resources
wget --load-cookies /tmp/cookies.txt "https://docs.google.com/uc?export=download&confirm=$(wget --quiet --save-cookies /tmp/cookies.txt --keep-session-cookies --no-check-certificate 'https://docs.google.com/uc?export=download&id=1Frq7VM_Cb8G_vaQOJ6F3jR6loVlBPdAS' -O- | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1\n/p')&id=1Frq7VM_Cb8G_vaQOJ6F3jR6loVlBPdAS" -O yellow_tripdata_2009-01_point.csv && rm -rf /tmp/cookies.txt
