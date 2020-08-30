# 数据采集通道

### 一、生成虚拟数据

[java生成虚拟数据](../proj/logcollector/src/main/java/com/tian/appclient/AppMain.java)
[python生成虚拟数据](../scripts/python/data_gen.py)

```bash
java -classpath /opt/files/logcollector-1.0-SNAPSHOT-jar-with-dependencies.jar com.tian.appclient.AppMain > /opt/files/test.log
# /tmp/logs目录生成日志文件
```



**埋点日志样式**

```json
{
    "ap": "xxxxx", //项目数据来源 app pc
    "cm": { //common 公共字段
        "mid": "", // (String) 设备唯一标识
        "uid": "", // (String) 用户标识
        "vc": "1", // (String) versionCode，程序版本号
        "vn": "1.0", // (String) versionName，程序版本名
        "l": "zh", // (String) language系统语言
        "sr": "", // (String) 渠道号，应用从哪个渠道来的。
        "os": "7.1.1", // (String) Android系统版本
        "ar": "CN", // (String) area区域
        "md": "BBB100-1", // (String) model手机型号
        "ba": "blackberry", // (String) brand手机品牌
        "sv": "V2.2.1", // (String) sdkVersion
        "g": "", // (String) gmail
        "hw": "1620x1080", // (String) heightXwidth，屏幕宽高
        "t": "1506047606608", // (String) 客户端日志产生时的时间
        "nw": "WIFI", // (String) 网络模式
        "ln": 0, // (double) lng经度
        "la": 0 // (double) lat 纬度
    },
    "et": [ //事件
        {
            "ett": "1506047605364", //客户端事件产生时间
            "en": "display", //事件名称
            "kv": { //事件结果，以key-value形式自行定义
                "goodsid": "236",
                "action": "1",
                "extend1": "1",
                "place": "2",
                "category": "75"
            }
        }
    ]
}
```

**示例日志**
```json
1540934156385|{ //时间戳|日志
    "ap": "gmall",
    "cm": {
        "uid": "1234",
        "vc": "2",
        "vn": "1.0",
        "la": "EN",
        "sr": "",
        "os": "7.1.1",
        "ar": "CN",
        "md": "BBB100-1",
        "ba": "blackberry",
        "sv": "V2.2.1",
        "g": "abc@gmail.com",
        "hw": "1620x1080",
        "t": "1506047606608",
        "nw": "WIFI",
        "ln": 0
    },
    "et": [
        {
            "ett": "1506047605364", //客户端事件产生时间
            "en": "display", //事件名称
            "kv": { //事件结果，以key-value形式自行定义
                "goodsid": "236",
                "action": "1",
                "extend1": "1",
                "place": "2",
                "category": "75"
            }
        },
        {
            "ett": "1552352626835",
            "en": "active_background",
            "kv": {
                "active_source": "1"
            }
        }
    ]
}
```


### 二、Flume#1采集日志数据

[Flume搭建](../doc/env_build_new.md##flume-1.9.0)

Tialdir Source 支持断点续传
Kafka Channel 省略了Sink的配置


[Flume#1配置](../code/acquisition_channel/flume01.conf)


Flume拦截器
**ETL**用于过滤时间戳不合法或者Json不完整的日志
**日志类型区分**用于将启动日志和事件日志区分开，发往Kafka不同的Topic
拦截器的Maven工程编译打包放入server01:$FLUME_HOME/lib，分发


[Flume-ETL拦截器](../proj/flume-interceptor/src/main/java/com/tian/flume/interceptor/LogETLInterceptor.java)

[Flume-ETL拦截器](../proj/flume-interceptor/src/main/java/com/tian/flume/interceptor/LogTypeInterceptor.java)

[Flume#1启动脚本](../scripts/shell/flume01.sh)



### 三、Kafka消息队列

[Kafka搭建](../doc/env_build_new.md##kafka_2.11-2.4.1)

```sh
# 新增topic， 启动与事件
kafka-topics.sh --create --bootstrap-server server01:9092 \
--topic topic_start --partitions 2 --replication-factor 2

kafka-topics.sh --create --bootstrap-server server01:9092 \
--topic topic_event --partitions 2 --replication-factor 2

kafka-topics.sh --list --bootstrap-server server01:9092

kafka-console-consumer.sh \
--bootstrap-server server01:9092 --from-beginning --topic topic_event

kafka-console-consumer.sh \
--bootstrap-server server01:9092 --from-beginning --topic topic_start

kafka-topics.sh --zookeeper server01:2181/kafka \
--delete --topic topic_start
```


[Kafka集群启动脚本](../scripts/shell/kaf.sh)

<!-- Kafka压力测试于集群规划 -->

### 四、Flume#2消费Kafka数据导入到Hive


[Flume#2配置](../code/acquisition_channel/flume02.conf)

[Flume#2启动脚本](../scripts/shell/flume02.sh)

```sh
# 配置内存参数防止FGC
echo `JAVA_OPTS="-Xms100m -Xmx2000m -Dcom.sun.management.jmxremote"` >> $FLUME_HOME/conf/flume-env.sh
```

日志数据采集通道脚本

### 五、Sqoop导入导出数据

[Sqoop搭建](../doc/env_build_new.md##Sqoop-1.4.6)


