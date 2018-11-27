package cse512

import org.apache.spark.sql.SparkSession

object SpatialQuery extends App{
	def runRangeQuery(spark: SparkSession, arg1: String, arg2: String): Long = {
		val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
		pointDf.createOrReplaceTempView("point")

		// YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
		// // -93.63173,33.0183,-93.359203,33.219456
		val st_contains = (queryRectangle:String, pointString:String) => {
      		val q = queryRectangle.split(",").map(x=>x.toDouble)
      		val p = pointString.split(",").map(x=>x.toDouble)
    
			/*
			println("==============")
			q.foreach((num: Float)=>{printf("%f, ", num)})
			println()
			p.foreach((num: Float)=>{printf("%f, ", num)})
			*/
      		(p(0) >= q(0)) && (p(1) >= q(1)) && (p(0) <= q(2)) && (p(1) <= q(3))

    	}   
    	spark.udf.register("ST_Contains",st_contains)
  
    
    	// spark.udf.register("ST_Contains",(queryRectangle:String, pointString:String)=>((true)))


    	val resultDf = spark.sql("select * from point where ST_Contains('"+arg2+"',point._c0)")
    	resultDf.show()

    	return resultDf.count()
	}

	def runRangeJoinQuery(spark: SparkSession, arg1: String, arg2: String): Long = {

    	val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    	pointDf.createOrReplaceTempView("point")

    	val rectangleDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg2);
    	rectangleDf.createOrReplaceTempView("rectangle")

    	// YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
		val st_contains = (queryRectangle:String, pointString:String) => {
      		val q = queryRectangle.split(",").map(x=>x.toDouble)
      		val p = pointString.split(",").map(x=>x.toDouble)
    
      		(p(0) >= q(0)) && (p(1) >= q(1)) && (p(0) <= q(2)) && (p(1) <= q(3))
    	}   
    	spark.udf.register("ST_Contains",st_contains)
  
    	//spark.udf.register("ST_Contains",(queryRectangle:String, pointString:String)=>((true)))

    	val resultDf = spark.sql("select * from rectangle,point where ST_Contains(rectangle._c0,point._c0)")
    	resultDf.show()

    	return resultDf.count()
	}

	def runDistanceQuery(spark: SparkSession, arg1: String, arg2: String, arg3: String): Long = {

    	val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    	pointDf.createOrReplaceTempView("point")

    	// YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
        val st_within = (pointString1:String, pointString2:String, distance:Double) => {
            val p1 = pointString1.split(",").map(x=>x.toDouble)
            val p2 = pointString2.split(",").map(x=>x.toDouble)
            val d = scala.math.pow(scala.math.pow(p1(0)-p2(0), 2) + scala.math.pow(p1(1)-p2(1), 2), 0.5)
            d <= distance
        }
    	spark.udf.register("ST_Within", st_within)
        //spark.udf.register("ST_Within",(pointString1:String, pointString2:String, distance:Double)=>((true)))

    	val resultDf = spark.sql("select * from point where ST_Within(point._c0,'"+arg2+"',"+arg3+")")
    	resultDf.show()

    	return resultDf.count()
	}

  	def runDistanceJoinQuery(spark: SparkSession, arg1: String, arg2: String, arg3: String): Long = {

    	val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    	pointDf.createOrReplaceTempView("point1")

    	val pointDf2 = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg2);
    	pointDf2.createOrReplaceTempView("point2")

    	// YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
        val st_within = (pointString1:String, pointString2:String, distance:Double) => {
            val p1 = pointString1.split(",").map(x=>x.toDouble)
            val p2 = pointString2.split(",").map(x=>x.toDouble)
            val d = scala.math.pow(scala.math.pow(p1(0)-p2(0), 2) + scala.math.pow(p1(1)-p2(1), 2), 0.5)
            d <= distance
        }
    	spark.udf.register("ST_Within", st_within)
    	//spark.udf.register("ST_Within",(pointString1:String, pointString2:String, distance:Double)=>((true)))
    	val resultDf = spark.sql("select * from point1 p1, point2 p2 where ST_Within(p1._c0, p2._c0, "+arg3+")")
    	resultDf.show()

    	return resultDf.count()
  	}
}
