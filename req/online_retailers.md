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


## 数仓理论基础

 * [数仓理论基础](../doc/data_warehouse_theory.md)

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
        <name>hive.aux.jars.path</name>w
        <value>file:///opt/module/hive-3.1.2/lib/hivefunction-1.0-SNAPSHOT.jar,file:///opt/module/hive-3.1.2/lib/hivefunction-1.0-SNAPSHOT-jar-with-dependencies.jar</value>
    </property>
```
```sh
# hive-env.sh
export HIVE_AUX_JARS_PATH=$HIVE_HOME/lib2
```

[活跃设备明细 dws_uv](../code/sql/gmall.dws_uv.sql)
[每日新增设备明细 dws_new_mid](../code/sql/gmall.dws_new_mid.sql)
[每日留存用户明细 dws_user_retention](../code/sql/gmall.dws_user_retention.sql)

[dws_uv调度脚本](../scripts/shell/gmall.dws_uv.sh)
[dws_new_mid调度脚本](../scripts/shell/gmall.dws_new_mid.sh)
[dws_user_retention调度脚本](../scripts/shell/gmall.dws_user_retention.sh)


[活跃设备数 ads_uv](../code/sql/gmall.ads_uv.sql)
[每日新增设备 ads_new_mid](../code/sql/gmall.ads_new_mid.sql)
[留存用户数、比例 ads_user_retention](../code/sql/gmall.ads_user_retention.sql)
[沉默用户数 ads_silent](../code/sql/gmall.ads_silent.sql)
[流失用户数 ads_wastage](../code/sql/gmall.ads_wastage.sql)
[本周回流用户数 ads_back](../code/sql/gmall.ads_back.sql)
[时间区间内连续n天登陆 ads_continuty_wk](../code/sql/gmall.ads_continuty_wk.s3ql)

[ads_uv调度脚本](../scripts/shell/gmall.ads_uv.sh)
[ads_new_mid调度脚本](../scripts/shell/gmall.ads_new_mid.sh)
[ads_user_retention调度脚本](../scripts/shell/gmall.ads_user_retention.sh)
[ads_silent调度脚本](../scripts/shell/gmall.ads_silent.sh)
[ads_wastage调度脚本](../scripts/shell/gmall.ads_wastage.sh)
[ads_back调度脚本](../scripts/shell/gmall.ads_back.sh)
[ads_continuty_wk](../scripts/shell/gmall.ads_continuty_wk.sh)


### 系统业务数仓

[MySQL建表](../code/sql/gmall.mysql_create.sql)
[MySQL函数](../code/sql/gmall.mysql_func.sql)
[MySQL插入数据](../code/sql/gmall.mysql_insert.sql)
[MySQL存储过程生成虚拟数据](../code/sql/gmall.mysql_proc.sql)

```sql
-- 生成mysql业务数据
init_data(do_date_string VARCHAR(20), order_incr_num INT,user_incr_num INT, sku_num INT, if_truncate BOOLEAN);
-- do_date_string 生成数据日期
-- order_incr_num 订单id个数
-- user_incr_num 用户id个数
-- sku_num 商品sku个数
-- if_truncate 是否删除数据

-- 案例测试
-- 生成日期2019年2月10日的数据、订单1000个、用户200个、商品sku300个、删除原始数据
call init_data('2020-09-04',1000,200,300,true);

-- 查看结果
select * from base_category1;
select * from base_category2;
select * from base_category3;

select * from order_info;
select * from order_detail;

select * from sku_info;
select * from user_info;

select * from payment_info;
```


[hadoop支持snappy压缩](../doc/env_build_new.md##Snappy)
[Sqoop环境部署](../doc/env_build_new.md##Sqoop-1.4.6)

[ODS建表](../code/sql/gmall.ods_db.sql)
[Sqoop同步脚本](../scripts/shell/gmall.ods_db.sh)

 * Sqoop导入到hdfs目录，load数据到表
 * 尝试直接Sqoop直接导入到Hive表

[DWD建表](../code/sql/gmall.dwd_db.sql)
[DWD调度脚本](../scripts/shell/gmall.dwd_db.sh)

[DWS用户行为宽表](../code/sql/gmall.dws_db.sql)
[DWS用户购买明细宽表](../code/sql/gmall.dws_db.sql)

<!-- 拉链表 -->

[GMV成交额](../code/sql/gmall.ads_gmv.sql)
[漏斗分析](../code/sql/gmall.ads_act_convert.sql)
[品牌复购率](ads.../code/sql/gmall.ads_rebuy.sql)


### 数据可视化

MySQL建表
Sqoop导出
echarts

### Azkaban调度





