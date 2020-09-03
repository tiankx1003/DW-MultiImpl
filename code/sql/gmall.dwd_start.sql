drop table if exists dwd_start_log;
create external table dwd_start_log
(
    `mid_id`       string,
    `user_id`      string,
    `version_code` string,
    `version_name` string,
    `lang`         string,
    `source`       string,
    `os`           string,
    `area`         string,
    `model`        string,
    `brand`        string,
    `sdk_version`  string,
    `gmail`        string,
    `height_width` string,
    `app_time`     string,
    `network`      string,
    `lng`          string,
    `lat`          string,
    `entry`        string,
    `open_ad_type` string,
    `action`       string,
    `loading_time` string,
    `detail`       string,
    `extend1`      string
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_start_log/'
    tblproperties ('parquet.compression' = 'lzo');


-- 向表中导入数据
insert overwrite table dwd_start_log partition (dt = '2020-09-03')
select get_json_object(line, '$.mid')          as mid_id,
       get_json_object(line, '$.uid')          as user_id,
       get_json_object(line, '$.vc')           as version_code,
       get_json_object(line, '$.vn')           as version_name,
       get_json_object(line, '$.l')            as lang,
       get_json_object(line, '$.sr')           as source,
       get_json_object(line, '$.os')           as os,
       get_json_object(line, '$.ar')           as area,
       get_json_object(line, '$.md')           as model,
       get_json_object(line, '$.ba')           as brand,
       get_json_object(line, '$.sv')           as sdk_version,
       get_json_object(line, '$.g')            as gmail,
       get_json_object(line, '$.hw')           as height_width,
       get_json_object(line, '$.t')            as app_time,
       get_json_object(line, '$.nw')           as network,
       get_json_object(line, '$.ln')           as lng,
       get_json_object(line, '$.la')           as lat,
       get_json_object(line, '$.entry')        as entry,
       get_json_object(line, '$.open_ad_type') as open_ad_type,
       get_json_object(line, '$.action')       as action,
       get_json_object(line, '$.loading_time') as loading_time,
       get_json_object(line, '$.detail')       as detail,
       get_json_object(line, '$.extend1')      as extend1
from ods_start_log
where dt = '2020-09-03';

-- 验证
select *
from dwd_start_log
limit 2;