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


## flume-1.7.0
```bash
tar -zxf /opt/software/apache-flume-1.7.0-bin.tar.gz -C /opt/module/
mv apache-flume-1.7.0-bin/
# 替换guava
rm -fr $FLUME_HOME/lib/guava-11.0.2.jar #兼容hadoop3
cp $HADOOP_HOME/share/hadoop/common/lib/guava-27.0-jre.jar $FLUME_HOME/lib
xsync $FLUME_HOME/lib
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

kafka-topics.sh --zookeeper server01:2181/kafka --delete --topic first
#需要server.properties中设置delete.topic.enable=true否则只是标记删除

kafka-console-producer.sh \
--broker-list server01:9092 --topic first

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

## lzo-0.4.21

#### 下载、安装、编译LZO
```sh
# 配置maven

sudo yum -y install gcc-c++ lzo-devel zlib-devel autoconf automake libtool

# 
wget http://www.oberhumer.com/opensource/lzo/download/lzo-2.10.tar.gz
tar -zxvf lzo-2.10.tar.gz
cd lzo-2.10
./configure -prefix=/usr/local/hadoop/lzo/
make
make install
```

#### 编译hadoop-lzo源码
```sh
wget https://github.com/twitter/hadoop-lzo/archive/master.zip
unzip master.zip
vim hadoop-lzo-master/pom.xml
# 修改hadoop.current.version为对应版本 3.1.3

# 声明两个临时环境变量
export C_INCLUDE_PATH=/usr/local/hadoop/lzo/include
export LIBRARY_PATH=/usr/local/hadoop/lzo/lib 

cd hadoop-lzo-master
mvn package -Dmaven.test.skip=true

ll target/hadoop-lzo-0.4.21-SNAPSHOT.jar
```


#### 配置支持lzo
```sh
mv hadoop-lzo-0.4.21-SNAPSHOT.jar hadoop-lzo-0.4.21.jar
cp hadoop-lzo-0.4.21.jar $HADOOP_HOME/share/hadoop/common/
xsync $HADOOP_HOME/share/hadoop/common/
vim $HADOOP_HOME/etc/hadoop/core-site.xml
# 添加配置
xsync $HADOOP_HOME/etc/hadoop/core-site.xml
```

```xml
    <property>
        <name>io.compression.codecs</name>
        <value>
            org.apache.hadoop.io.compress.GzipCodec,
            org.apache.hadoop.io.compress.DefaultCodec,
            org.apache.hadoop.io.compress.BZip2Codec,
            org.apache.hadoop.io.compress.SnappyCodec,
            com.hadoop.compression.lzo.LzoCodec,
            com.hadoop.compression.lzo.LzopCodec
        </value>
    </property>

    <property>
        <name>io.compression.codec.lzo.class</name>
        <value>com.hadoop.compression.lzo.LzoCodec</value>
    </property>
```

#### 测试
```sh
# 手动为压缩文件创建索引
hadoop jar $HADOOP_HOME/share/hadoop/common/hadoop-lzo-0.4.21.jar com.hadoop.compression.lzo.DistributedLzoIndexer big_file.lzo



hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-client-jobclient-3.1.3-tests.jar TestDFSIO -clean

```

```sql
use dw;
create table testLZO(id bigint, time bigint, uid string, keyword string, url_rank int, click_num int, click_url string) row format delimited fields terminated by '\t' STORED AS
INPUTFORMAT 'com.hadoop.mapred.DeprecatedLzoTextInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat';



```



core-site.xml

```xml
<!-- 2020-9-1 14:18:36 支持LZO -->
<!-- 声明可用的压缩算法的编/解码器 -->
    <property>
        <name>io.compression.codecs</name>
        <value>
            org.apache.hadoop.io.compress.GzipCodec,
            org.apache.hadoop.io.compress.DefaultCodec,
            org.apache.hadoop.io.compress.DeflateCodec,
            org.apache.hadoop.io.compress.BZip2Codec,
            org.apache.hadoop.io.compress.SnappyCodec,
            org.apache.hadoop.io.compress.Lz4Codec,
            com.hadoop.compression.lzo.LzoCodec,
            com.hadoop.compression.lzo.LzopCodec
        </value>
        <description>
            A comma-separated list of the compression codec classes that can
            be used for compression/decompression. In addition to any classes specified
            with this property (which take precedence), codec classes on the classpath
            are discovered using a Java ServiceLoader.
        </description>
    </property>

    <!-- 配置lzo编解码器相关参数 -->
    <property>
        <name>io.compression.codec.lzo.class</name>
        <value>com.hadoop.compression.lzo.LzoCodec</value>
    </property>
```


mapred-site.xml
```xml
<!-- 2020-9-1 14:18:36 支持LZO -->
<!-- map输出是否压缩 -->
    <!-- 默认值:false -->
    <property>
        <name>mapreduce.map.output.compress</name>
        <value>true</value>
        <description>
            Should the outputs of the maps be compressed before being
            sent across the network. Uses SequenceFile compression.
        </description>
    </property>
    <!-- 设置map输出压缩所使用的对应压缩算法的编解码器,此处设置为LzoCodec,生成的文件后缀为.lzo_deflate -->
    <!-- 默认值:org.apache.hadoop.io.compress.DefaultCodec -->
    <property>
        <name>mapreduce.map.output.compress.codec</name>
        <value>com.hadoop.compression.lzo.LzoCodec</value>
        <description>
            If the map outputs are compressed, how should they be compressed?
        </description>
    </property>
    <!-- 设置MR job最终输出文件是否压缩 -->
    <!-- 默认值:false -->
    <property>
        <name>mapreduce.output.fileoutputformat.compress</name>
        <value>true</value>
        <description>Should the job outputs be compressed?
        </description>
    </property>
    <!-- 设置MR job最终输出文件所使用的压缩算法对应的编解码器,此处设置为LzoCodec,生成的文件后缀为.lzo_deflate -->
    <!-- 默认值:org.apache.hadoop.io.compress.DefaultCodec -->
    <property>
        <name>mapreduce.output.fileoutputformat.compress.codec</name>
        <value>com.hadoop.compression.lzo.LzoCodec</value>
        <description>If the job outputs are compressed, how should they be compressed?
        </description>
    </property>
    <!-- 设置序列文件的压缩格式 -->
    <!-- 默认值:RECORD -->
    <property>
        <name>mapreduce.output.fileoutputformat.compress.type</name>
        <value>BLOCK</value>
        <description>If the job outputs are to compressed as SequenceFiles, how should
               they be compressed? Should be one of NONE, RECORD or BLOCK.
        </description>
    </property>
```

hive-site.xml

```xml
<!-- 2020-9-1 14:18:36 支持LZO -->
<!-- 设置hive语句执行输出文件是否开启压缩,具体的压缩算法和压缩格式取决于hadoop中
    设置的相关参数 -->
    <!-- 默认值:false -->
    <property>
        <name>hive.exec.compress.output</name>
        <value>true</value>
        <description>
            This controls whether the final outputs of a query (to a local/HDFS file or a Hive table) 
            is compressed. 
            The compression codec and other options are determined from Hadoop config variables 
            mapred.output.compress*
        </description>
    </property>
    <!-- 控制多个MR Job的中间结果文件是否启用压缩,具体的压缩算法和压缩格式取决于hadoop中
    设置的相关参数 -->
    <!-- 默认值:false -->
    <property>
        <name>hive.exec.compress.intermediate</name>
        <value>true</value>
        <description>
            This controls whether intermediate files produced by Hive between multiple map-reduce jobs are compressed. 
            The compression codec and other options are determined from Hadoop config variables mapred.output.compress*
        </description>
    </property>

    <!-- 解决inert数据无法查询问题 -->
    <property>
        <name>mapred.output.compression.codec</name>
        <value>com.hadoop.compression.lzo.LzopCodec</value>
        <description>
            解决insert查询为空的问题
        </description>
    </property>
```

set hive.exec.compress.output=true;
set mapred.output.compression.codec=com.hadoop.compression.lzo.LzopCodec;


```xml
<property>
    <name>mapreduce.map.output.compress</name>
    <value>true</value>
</property>
<property>
    <name>mapreduce.map.output.compress.codec</name>
    <value>org.apache.hadoop.io.compress.SnappyCodec</value>
</property>
<property>
    <name>mapreduce.output.fileoutputformat.compress</name>
    <value>true</value>
</property>
<property>
    <name>mapreduce.output.fileoutputformat.compress.codec</name>
    <value>org.apache.hadoop.io.compress.BZip2Codec</value>
</property>
```

```xml
<!--启用map中间文件压缩-->
<property>
    <name>mapreduce.map.output.compress</name>
    <value>true</value>
</property>
    <!--启用map中间压缩类-->
    <property>
        <name>mapred.map.output.compression.codec</name>
        <value>com.hadoop.compression.lzo.LzopCodec</value>
    </property>
<!--启用mapreduce文件压缩-->
<property>
    <name>mapreduce.output.fileoutputformat.compress</name>
    <value>true</value>
</property>
<!--启用mapreduce压缩类-->
<property>
   <name>mapreduce.output.fileoutputformat.compress.codec</name>
   <value>com.hadoop.compression.lzo.LzopCodec</value>
</property>
    <!--配置Jar包-->
    <property>
        <name>mapred.child.env</name>
        <value>LD_LIBRARY_PATH=/usr/local/hadoop/lzo/lib</value>
    </property>
```