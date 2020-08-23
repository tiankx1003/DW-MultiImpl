## hadoop-3.1.3

```bash
tar -zxvf /opt/software/hadoop-3.1.3.tar.gz -C /opt/module/
vim /etc/profile
```
```sh
export HADOOP_HOME=/opt/module/hadoop-3.1.3
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
```

 * [$HADOOP_HOME/etc/hadoop/core-site.xml](../code/env_build_new/core-site.xml)
 * [$HADOOP_HOME/etc/hadoop/hdfs-site.xml](../code/env_build_new/hdfs-site.xml)
 * [$HADOOP_HOME/etc/hadoop/yarn-site.xml](../code/env_build_new/yarn-site.xml)
 * [$HADOOP_HOME/etc/hadoop/mapred-site.xml](../code/env_build_new/mapred-site.xml)
 * [$HADOOP_HOME/etc/hadoop/workers](../code/env_build_new/workers)

## hive-3.1.2

```bash
tar -zxvf /opt/software/apache-hive-3.1.2-bin.tar.gz -C /opt/module/
# 解决log4j日志冲突
mv $HIVE_HOME/lib/log4j-slf4j-impl-2.10.0.jar $HIVE_HOME/lib/log4j-slf4j-impl-2.10.0.bak
cp /opt/software/mysql-connector-java-5.1.48.jar $HIVE_HOME/lib
vim $HIVE_HOME/conf/hive-site.xml
vim $HIVE_HOME/bin/hiveservices.sh
chmod +x $HIVE_HOME/bin/hiveservices.sh
```

 * [$HIVE_HOME/conf/hive-site.xml](../code/env_build_new/hive-site.xml)
 * [$HIVE_HOME/bin/hiveservices.sh](../code/env_build_new/hiveservices.sh)

## tez-0.10.1

```bash
mkdir /opt/module/tez
tar -zxvf /opt/software/tez-0.10.1-SNAPSHOT-minimal.tar.gz -C /opt/module/tez
hadoop fs -mkdir /tez
hadoop fs -put /opt/software/tez-0.10.1-SNAPSHOT.tar.gz /tez
vim $HADOOP_HOME/etc/hadoop/tez-site.xml
vim $HADOOP_HOME/etc/hadoop/shellprofile.d/tez.sh
# 修改hive引擎
vim $HIVE_HOME/conf/hive-site.xml 
```

 * [$HADOOP_HOME/etc/hadoop/tez-site.xml](../code/env_build_new/tez-site.xml)
 * [$HADOOP_HOME/etc/hadoop/shellprofile.d/tez.sh](../code/env_build_new/tez.sh)

## spark-2.4.5

```bash
tar -zxf /opt/software/spark-2.4.5-bin-without-hive.tgz -C /opt/module
mv /opt/module/spark-2.4.5-bin-without-hive /opt/module/spark-2.4.5
mv $SPARK_HOME/conf/spark-env.sh.template $SPARK_HOME/conf/spark-env.sh
echo 'export SPARK_DIST_CLASSPATH=$(hadoop classpath)' >> $SPARK_HOME/conf/spark-env.sh
# Hive on Spark
ln -s $SPARK_HOME/jars/scala-library-2.11.12.jar /opt/module/hive/lib/scala-library-2.11.12.jar
#如果以下两个文件存在，就跳过
ln -s $SPARK_HOME/jars/spark-core_2.11-2.4.5.jar $HIVE_HOME/lib/spark-core_2.11-2.4.5.jar
ln -s $SPARK_HOME/jars/spark-network-common_2.11-2.4.5.jar $HIVE_HOME/lib/spark-network-common_2.11-2.4.5.jar
vim $HIVE_HOME/conf/spark-defaults.conf
hadoop fs -mkdir /spark-history
hadoop fs -mkdir /spark-jars
hadoop fs -put $SPARK_HOME/jars/* /spark-jars
vim $HIVE_HOME/conf/hive-site.xml
vim $SPARK_HOME/conf/spark-env.sh
# export SPARK_DIST_CLASSPATH=$(hadoop classpath)
# SPARK_MASTER_HOST=server01
vim $SPARK_HOME/conf/slaves
# Yanr模式
vim $HADOOP_HOME/etc/hadoop/yarn-site.xml
vim $SPARK_HOME/conf/spark-env.sh
# YARN_CONF_DIR=/opt/module/hadoop-3.1.3/etc/hadoop
xsync $HADOOP_HOME/etc/hadoop/yarn-site.xml
xsync $SPARK_HOME/conf/spark-env.sh
$SPARK_HOME/bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--master yarn \
--deploy-mode client \
$SPARK_HOME/examples/jars/spark-examples_2.11-2.4.5.jar \
100
# server02:8088
```
 * [$SPARK_HOME/conf/spark-defaults.conf](../code/env_build_new/spark-defaults.conf)
 * [$SPARK_HOME/conf/slaves](../code/env_build_new/slaves)

## zookeeper-3.5.7

```bash
tar -zxvf /opt/software/zookeeper-3.5.7.tar.gz -C /opt/module/
xsync zookeeper-3.5.7/
mkdir -p $ZOOKEEPER_HOME/zkData
touch $ZOOKEEPER_HOME/zkData/myid
mv $ZOOKEEPER_HOME/conf/zoo_sample.cfg $ZOOKEEPER_HOME/conf/zoo.cfg
vim $ZOOKEEPER_HOME/conf/zoo.cfg
xsync zoo.cfg

xcall zkServer.sh start
xcall zkServer.sh status
xcall zkServer.sh stop
```
```conf
dataDir=/opt/module/zookeeper-3.5.7/zkData
#######################cluster##########################
server.1=server01:2888:3888
server.2=server02:2888:3888
server.3=server03:2888:3888
```

## hbase-1.3.1

```bash
tar -zxvf /opt/software/hbase-1.3.1-bin.tar.gz -C /opt/module
# export HBASE_HOME=/opt/module/hbase-1.3.1
# export PATH=$PATH:$HBASE_HOME/bin
mv $HBASE_HOME/lib/slf4j-log4j12-1.7.5.jar $HBASE_HOME/lib/slf4j-log4j12-1.7.5.jar.bak
echo 'export JAVA_HOME=/opt/module/jdk1.8.0_144' >> $HBASE_HOME/conf/hbase-env.sh
echo 'export HBASE_MANAGES_ZK=false' >> $HBASE_HOME/conf/hbase-env.sh
vim $HBASE_HOME/conf/hbase-site.xml
vim $HBASE_HOME/conf/regionservers
ln -s $HADOOP_HOME/etc/hadoop/core-site.xml $HBASE_HOME/conf/core-site.xml
ln -s $HADOOP_HOME/etc/hadoop/hdfs-site.xml $HBASE_HOME/conf/hdfs-site.xml
xsync $HBASE_HOME/

hbase-daemon.sh start master
xcall hbase-daemon.sh start regionserver

export HBASE_HOME=/opt/module/hbase-1.3.1
export PATH=$PATH:$HBASE_HOME/bin

start-hbase.sh
stop-hbase.sh
```

 * [$HBASE_HOME/conf/hbase-site.xml](../code/env_build_new/hbase-site.xml)
 * [$HBASE_HOME/conf/regionservers](../code/env_build_new/regionservers)
 * [HBase页面http://server01:16010](http://server01:16010)


## flume-1.9.0
```bash
tar -zxf /opt/software/apache-flume-1.9.0-bin.tar.gz -C /opt/module/
mv apache-flume-1.9.0-bin/
rm -fr $FLUME_HOME/lib/guava-11.0.2.jar #兼容hadoop3
```

```bash
sudo yum install -y nc
sudo netstat -tunlp | grep 44444
#开启Flume监听端口
#第一种写法
flume-ng agent --conf $FLUME_HOME/conf/ --name a1 --conf-file $FLUME_HOME/job/flume-netcat-logger.conf -Dflume.root.logger=INFO,console
#第二种写法
flume-ng agent -c $FLUME_HOME/conf/ -n a1 -f $FLUME_HOME/job/flume-netcat-logger.conf -Dflume.root.logger=INFO,console
nc localhost 44444
```

## kafka_2.11-2.4.1

```bash
tar -zxvf /opt/software/kafka_2.11-2.4.1.tgz -C /opt/module/
# export KAFKA_HOME=/opt/module/kafka_2.11-2.4.1
# export PATH=$PATH:$KAFKA_HOME/bin
mkdir $KAFKA_HOME/logs
vim $KAFKA_HOME/config/server.properties
xsync $KAFKA_HOME/ #修改broker.id
```

 * [$KAFKA_HOME/config/server.properties](../code/env_build_new/server.properties)

```bash
xcall kafka-server-start.sh -daemon $KAFKA_HOME/config/server.properties
xcall kafka-server-stop.sh
```

```bash
kafka-topics.sh --list --bootstrap-server server01:9092

kafka-topics.sh --create --bootstrap-server server01:9092 \
--topic first --partitions 2 --replication-factor 2

kafka-topics.sh --zookeeper server01:2181/kafka \
--delete --topic first
#需要server.properties中设置delete.topic.enable=true否则只是标记删除

kafka-console-producer.sh \
--broker-list server01:9092 --topic first

kafka-console-consumer.sh \
--bootstrap-server server01:9092 --from-beginning --topic first

kafka-console-consumer.sh \
--bootstrap-server server01:9092 --from-beginning --topic first
#--from-beginning：会把主题中以往所有的数据都读取出来

kafka-topics.sh --bootstrap-server server01:9092 --describe --topic first

kafka-topics.sh --zookeeper server01:2181/kafka --alter --topic first --partitions 6
```

## Sqoop-1.4.6

```bash
tar -zxf /opt/software/sqoop-1.4.6.bin__hadoop-2.0.4-alpha.tar.gz -C /opt/module/
mv sqoop-1.4.6.bin__hadoop-2.0.4-alpha sqoop-1.4.6
# SQOOP_HOME=/opt/module/sqoop-1.4.6
mv $SQOOP_HOME/conf/sqoop-env-template.sh $SQOOP_HOME/conf/sqoop-env.sh
vim $SQOOP_HOME/conf/sqoop-env.sh
# jdbc驱动
cp mysql-connector-java-5.1.48-bin.jar $SQOOP_HOME/lib/
sqoop help
sqoop list-databases --connect jdbc:mysql://server01:3306/ --username root --password root
```
```sh
export HADOOP_COMMON_HOME=/opt/module/hadoop-3.1.3
export HADOOP_MAPRED_HOME=/opt/module/hadoop-3.1.3
export HIVE_HOME=/opt/module/hive-3.1.2
export ZOOKEEPER_HOME=/opt/module/zookeeper-3.5.7
export ZOOCFGDIR=/opt/module/zookeeper-3.5.7/conf
export HBASE_HOME=/opt/module/hbase-1.3.1
export HCAT_HOME=/opt/module/sqoop-1.4.6/hcatalog
export ACCUMULO_HOME=/opt/module/sqoop-1.4.6/accumulo
```



## Oracle 11g

[Oracle 11g](oracle_install.md)