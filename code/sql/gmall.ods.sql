drop database if exists gmall cascade
;
create database gmall
;

drop table if exists ods_start_log
;

use gmall;

drop table if exists ods_start_log;
create external table if not exists ods_start_log (line string)
partitioned by (`dt` string)
stored as
  inputformat 'com.hadoop.mapred.DeprecatedLzoTextInputFormat'
  outputformat 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
location '/warehouse/gmall/ods/ods_start_log'
;

drop table if exists ods_event_log;
create external table if not exists ods_event_log (line string)
partitioned by (`dt` string)
stored as
  inputformat 'com.hadoop.mapred.DeprecatedLzoTextInputFormat'
  outputformat 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
location '/warehouse/gmall/ods/ods_event_log'
;

