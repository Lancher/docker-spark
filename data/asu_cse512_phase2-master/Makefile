SRC=src/main/scala/cse512
spark-submit=/home/db/db_project/spark-2.3.2-bin-hadoop2.7/bin/spark-submit

compile: $(SRC)/*.scala
	sbt assembly

run_hotzone:
	rm -r test
	$(spark-submit) target/scala-2.11/CSE512-Hotspot-Analysis-Template-assembly-0.1.0.jar test/output hotzoneanalysis src/resources/point-hotzone.csv src/resources/zone-hotzone.csv

run_hotcell:
	rm -r test || true
	#spark-submit target/scala-2.11/CSE512-Hotspot-Analysis-Template-assembly-0.1.0.jar test/output hotcellanalysis src/resources/yellow_trip_sample_100000.csv
	$(spark-submit) target/scala-2.11/CSE512-Hotspot-Analysis-Template-assembly-0.1.0.jar test/output hotcellanalysis /apple_data/workspace/db/yellow_tripdata_2009-01_point.csv
