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
mkdir -p $ZK_HOME/zkData
touch $ZK_HOME/zkData/myid
mv $ZK_HOME/conf/zoo_sample.cfg $ZK_HOME/conf/zoo.cfg
vim $ZK_HOME/conf/zoo.cfg
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
echo 'export JAVA_HOME=/opt/module/jdk1.6.0_144' >> $HBASE_HOME/conf/hbase-env.sh
echo 'export HBASE_MANAGES_ZK=false' >> $HBASE_HOME/conf/hbase-env.sh
vim $HBASE_HOME/conf/hbase-site.xml
vim $HBASE_HOME/conf/regionservers
ln -s /opt/module/hadoop-2.7.2/etc/hadoop/core-site.xml /opt/module/hbase/conf/core-site.xml
ln -s /opt/module/hadoop-2.7.2/etc/hadoop/hdfs-site.xml /opt/module/hbase/conf/hdfs-site.xml
xsync hbase/

hbase-daemon.sh start master
hbase-daemon.sh start regionserver

start-hbase.sh
stop-hbase.sh
```

 * [$HBASE_HOME/conf/hbase-site.xml](../code/env_build_new/hbase-site.xml)
 * [$HBASE_HOME/conf/regionservers](../code/env_build_new/regionservers)
 * [HBase页面http://server01:16010](http://server01:16010)