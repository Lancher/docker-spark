package cse512

object HotzoneUtils {

  def ST_Contains(queryRectangle: String, pointString: String ): Boolean = {
    val q = queryRectangle.split(",").map(x=>x.toDouble)
    val p = pointString.split(",").map(x=>x.toDouble)
    return (p(0) >= q(0)) && (p(1) >= q(1)) && (p(0) <= q(2)) && (p(1) <= q(3))
  }

  // YOU NEED TO CHANGE THIS PART

}
