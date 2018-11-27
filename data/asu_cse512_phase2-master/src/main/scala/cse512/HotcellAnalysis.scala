package cse512

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql._

object HotcellAnalysis {
  Logger.getLogger("org.spark_project").setLevel(Level.WARN)
  Logger.getLogger("org.apache").setLevel(Level.WARN)
  Logger.getLogger("akka").setLevel(Level.WARN)
  Logger.getLogger("com").setLevel(Level.WARN)

def runHotcellAnalysis(spark: SparkSession, pointPath: String): DataFrame =
{
  // Load the original data from a data source
  var pickupInfo = spark.read.format("com.databricks.spark.csv").option("delimiter",";").option("header","false").load(pointPath);
  pickupInfo.createOrReplaceTempView("nyctaxitrips")
  //pickupInfo.show()

  // Assign cell coordinates based on pickup points
  spark.udf.register("CalculateX",(pickupPoint: String)=>((
    HotcellUtils.CalculateCoordinate(pickupPoint, 0)
    )))
  spark.udf.register("CalculateY",(pickupPoint: String)=>((
    HotcellUtils.CalculateCoordinate(pickupPoint, 1)
    )))
  spark.udf.register("CalculateZ",(pickupTime: String)=>((
    HotcellUtils.CalculateCoordinate(pickupTime, 2)
    )))
  pickupInfo = spark.sql("select CalculateX(nyctaxitrips._c5),CalculateY(nyctaxitrips._c5), CalculateZ(nyctaxitrips._c1) from nyctaxitrips")
  var newCoordinateName = Seq("x", "y", "z")
  pickupInfo = pickupInfo.toDF(newCoordinateName:_*)
  //pickupInfo.show()

  // Define the min and max of x, y, z
  val minX = -74.50/HotcellUtils.coordinateStep
  val maxX = -73.70/HotcellUtils.coordinateStep
  val minY = 40.50/HotcellUtils.coordinateStep
  val maxY = 40.90/HotcellUtils.coordinateStep
  val minZ = 1
  val maxZ = 31
  val numCells = (maxX - minX + 1)*(maxY - minY + 1)*(maxZ - minZ + 1)

  // YOU NEED TO CHANGE THIS PART
  val pickup_count = pickupInfo.groupBy("x", "y", "z").count()
  pickup_count.show()
  pickup_count.createOrReplaceTempView("pickup_count")
  println(numCells)
  

  //val n_table = spark.sql("select count(a.count) from pickup_count as a")
  val sum_over_x_table = spark.sql("select sum(a.count) from pickup_count as a")
  val sum_over_x = sum_over_x_table.first().getLong(0).toDouble
  //val n = n_table.first().getLong(0).toDouble
  val n = numCells
  val X = sum_over_x / n
  println(n)
  println(X)
  
  val sum_over_x_squre_table = spark.sql("select sum(a.count*a.count) from pickup_count as a")
  val sum_over_x_squre = sum_over_x_squre_table.first().getLong(0).toDouble
  val S = math.sqrt((sum_over_x_squre/n)-X*X)
  println(S)
  
  val select_distance = (x1:Int, y1:Int, z1:Int, x2:Int, y2:Int, z2:Int)=> {
    val d = HotcellUtils.distance(x1, y1, z1, x2, y2, z2)
    d
  }
  spark.udf.register("select_distance", select_distance)
  
  var temp = spark.sql(s"""
    select a1.x as x1, a1.y as y1, a1.z as z1, a2.count as c2
    from pickup_count as a1, pickup_count as a2
    where select_distance(a1.x, a1.y, a1.z, a2.x, a2.y, a2.z) < 1.8
  """)
  temp.createOrReplaceTempView("neighbor_table")
  temp.show()
  
  temp.groupBy("x1", "y1", "z1").count().show()

  temp = spark.sql(s"""
    select t.x1, t.y1, t.z1, sum(t.c2) as s
    from neighbor_table as t
    group by t.x1, t.y1, t.z1
  """)
  temp.createOrReplaceTempView("result_with_sum")
  
  val compute_g = (x:Int, y:Int, z:Int, v:Double) => {
    val w = HotcellUtils.w_value(x, y, z)
    val u = (v - X*w)
    val d = S * math.sqrt((n*w - w*w)/(n-1))
    u/d 
  }
  spark.udf.register("compute_g", compute_g)
  temp = spark.sql("select t.x1, t.y1, t.z1, compute_g(t.x1, t.y1, t.z1, t.s) as g from result_with_sum as t")
  temp.createOrReplaceTempView("result_with_g")
  temp = spark.sql("select count(*) from result_with_g")
  temp.show()

  // show result with g-score
  //temp = spark.sql("select t.x1 as x, t.y1 as y, t.z1 as z, t.g as g from result_with_g as t order by t.g DESC limit 50")
  //return temp.coalesce(1)
  
  // show result without g-score
  temp = spark.sql("select t.x1 as x, t.y1 as y, t.z1 as z from result_with_g as t order by t.g DESC limit 50")
  return temp.coalesce(1)

}
}
