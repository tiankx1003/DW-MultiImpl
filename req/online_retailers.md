# 电商数仓

## 环境搭建

[hive安装部署](../doc/env_build_new.md##hive-3.1.2)

 * 元数据维护在mysql，支持tez、spark引擎，支持lzo压缩

<!-- TODO mysql主从复制 -->

## 数仓分层

#### ODS
 * original data storage

原始数据，保持原貌不作处理

#### DWD
data warehouse detail

结构和粒度不变，对空值、脏数据、超限数据作清洗


#### DWS
data warehouse service

轻度汇总，针对主体聚合到当日的粒度
以某个维度为线索，组成跨主题的宽表

#### ADS
application data storage

存放最终指标数据



## 需求


### 用户行为数仓

[ods建表](../code/sql/gmall.ods.sql)
[ods加载数据](../scripts/shell/gmall.ods_load.sh)

json字符串展开成每个字段
[dwd_start建表](../code/sql/gmall.dwd_start.sql)
[dwd_base建表](../code/sql/gmall.dwd_base.sql)
[dwd_event建表](../code/sql/gmall.dwd_event.sql)
[UDF自定义函数](../proj/hivefunction/src/main/java/com/tian/udf/BaseFieldUDF.java)
[UDTF自定义函数](../proj/hivefunction/src/main/java/com/tian/udtf/EventJsonUDTF.java)

```sh
# 上传jar包到hdfs
hadoop fs -mkdir /user/hive/jars
hadoop fs -put /opt/files/hivefunction-1.0-SNAPSHOT-jar-with-dependencies.jar /user/hive/jars
hadoop fs -put /opt/files/hivefunction-1.0-SNAPSHOT.jar /user/hive/jars
```
```sql
-- 注册函数（永久）， 注册时注意库名
use gmall;
create function base_analizer 
as 'com.tian.udf.BaseFieldUDF' 
using jar 'hdfs://server01:8020/user/hive/jars/hivefunction-1.0-SNAPSHOT.jar';

create function flat_analizer 
as 'com.tian.udtf.EventJsonUDTF' 
using jar 'hdfs://server01:8020/user/hive/jars/hivefunction-1.0-SNAPSHOT.jar'; 
```

[dwd_start调度脚本](../scripts/shell/gmall.dwd_start.sh)
[dwd_base调度脚本](../scripts/shell/gmall.dwd_base.sh)
[dwd_event调度脚本](../scripts/shell/gmall.dwd_event.sh)


```xml
    <!-- 2020-9-4 14:55:41 添加udf -->
    <property>
        <name>hive.aux.jars.path</name>
        <value>file:///opt/module/hive-3.1.2/lib/hivefunction-1.0-SNAPSHOT.jar,file:///opt/module/hive-3.1.2/lib/hivefunction-1.0-SNAPSHOT-jar-with-dependencies.jar</value>
    </property>
```
```sh
# hive-env.sh
export HIVE_AUX_JARS_PATH=$HIVE_HOME/lib2
```

[活跃设备明细 dws_uv](../code/sql/gmall.dws_uv.sql)
[每日新增设备明细](../code/sql/gmall.dws_new_mid.sql)
[每日留存用户明细](../code/sql/gmall.dws_user_retation.sql)


活跃设备数
每日新增设备
留存用户数、比例
沉默用户数
流失用户数
本周回流用户数
时间区间内连续n天登陆



### 系统业务数仓

[数仓理论基础](../doc/data_warehouse_theory.md)

hadoop支持snappy压缩
Sqoop环境部署

MySQL存储过程生成虚拟数据



ODS建表
Sqoop同步脚本

DWD建表
DWD调度脚本

DWS用户行为宽表
DWS用户购买明细宽表

<!-- 拉链表 -->

GMV成交额
漏斗分析
品牌复购率


数据可视化

MySQL建表
Sqoop导出
echarts

Azkaban调度





