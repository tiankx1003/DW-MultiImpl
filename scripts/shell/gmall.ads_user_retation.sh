#!/bin/bash

# 定义变量方便修改
APP=gmall
hive=$HIVE_HOME/bin/hive

# 如果是输入的日期按照取输入日期；如果没输入日期取当前时间的前一天
if [ -n "$1" ] ;then
    do_date=$1
else 
    do_date=`date -d "-0 day" +%F`  
fi 


sql="
set hive.exec.dynamic.partition.mode=nonstrict;
-- hive
insert overwrite table "$APP".dws_user_retention_day partition (dt = "$do_date")
select nm.mid_id,
       nm.user_id,
       nm.version_code,
       nm.version_name,
       nm.lang,
       nm.source,
       nm.os,
       nm.area,
       nm.model,
       nm.brand,
       nm.sdk_version,
       nm.gmail,
       nm.height_width,
       nm.app_time,
       nm.network,
       nm.lng,
       nm.lat,
       nm.create_date,
       1 retention_day
from "$APP".dws_uv_detail_day ud
join "$APP".dws_new_mid_day nm on ud.mid_id = nm.mid_id
where ud.dt = '$do_date'
  and nm.create_date = date_add('$do_date', -1);
;
"

$hive -e "$sql"
