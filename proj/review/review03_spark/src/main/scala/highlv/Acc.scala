package highlv

import org.apache.spark.util.AccumulatorV2

/**
 * @author tiankx
 * @date 2020/11/23 21:09
 * @version 1.0.0
 */
object Acc {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(20, 30, 40, 50, 20, 50), 2)
    var a = 1
    val acc = sc.longAccumulator
    val result = rdd1.map(x => {
      a += 1
      (a, 1)
    })
    val result2 = rdd1.map(x => {
      acc.add(1)
      (x, 1)
    })
    result.collect
    result2.collect
    println(a)
    println(acc.value) //
    result2.collect
    println(acc.value) //

    val acc2 = new MyAcc
    val result3 = rdd1.map(x => {
      acc2.add(1)
      (x, 1)
    })
    result3.collect
    println(acc2.value)
    result3.collect
    println(acc2.value)
    sc.stop()
  }
}

class MapAcc extends AccumulatorV2[Long, Map[String, Double]] {
  var map: Map[String, Double] = Map[String, Double]()
  var count = 0L

  override def isZero: Boolean = map.isEmpty

  override def copy(): AccumulatorV2[Long, Map[String, Double]] = {
    val acc = new MapAcc
    acc.map = map
    acc.count = count
    acc
  }

  override def reset(): Unit = {
    map = Map[String, Double]()
    count = 0
  }

  override def add(v: Long): Unit = {
    count += 1
    map += "sum" -> (map.getOrElse("sum", 0D) + v)
    map += "max" -> map.getOrElse("max", Long.MinValue.toDouble).max(v)
    map += "min" -> map.getOrElse("min", Long.MaxValue.toDouble).min(v)
  }

  override def merge(other: AccumulatorV2[Long, Map[String, Double]]): Unit = {
    other match {
      case o: MapAcc =>
        count += o.count
        map += "sum" -> (map.getOrElse("sum", 0D) + o.map.getOrElse("sum", 0D))
        map += "max" -> (map.getOrElse("max", Long.MinValue.toDouble).max(o.map.getOrElse("max", Long.MinValue.toDouble)))
        map += "min" -> (map.getOrElse("min", Long.MaxValue.toDouble).max(o.map.getOrElse("min", Long.MaxValue.toDouble)))
      case _ => throw new UnsupportedOperationException
    }
  }

  override def value: Map[String, Double] = {
    map += "avg" -> map.getOrElse("sum", 0D) / count
    map
  }
}

class MyAcc extends AccumulatorV2[Long, Long] {
  var sum = 0L

  override def isZero: Boolean = sum == 0

  /** 复制累加器，累加器从driver发送到executor */
  override def copy(): AccumulatorV2[Long, Long] = {
    val newAcc = new MyAcc
    newAcc.sum = sum
    newAcc
  }

  override def reset(): Unit = sum = 0

  override def add(v: Long): Unit = sum += v

  /** 合并累加器 */
  override def merge(other: AccumulatorV2[Long, Long]): Unit = {
    other match {
      case o: MyAcc => this.sum += o.sum
      case _ => throw new IllegalStateException()
    }
  }

  override def value: Long = this.sum
}