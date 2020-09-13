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
use $APP;
set hive.exec.dynamic.partition.mode=nonstrict;
-- hive
insert into table ads_gmv_sum_day
select 
'$do_date' dt,
    sum(order_count) gmv_count,
    sum(order_amount) gmv_amount,
    sum(payment_amount) payment_amount 
from dws_user_action
where dt ='$do_date'
group by dt
;

insert into table ads_user_action_convert_day
select 
    '$do_date',
    uv.day_count,
    ua.order_count,
    cast(ua.order_count/uv.day_count as  decimal(10,2)) visitor2order_convert_ratio,
    ua.payment_count,
    cast(ua.payment_count/ua.order_count as  decimal(10,2)) order2payment_convert_ratio
from  
    (
        select 
            dt,
            sum(if(order_count>0,1,0)) order_count,
            sum(if(payment_count>0,1,0)) payment_count
        from dws_user_action
        where dt='$do_date'
        group by dt
    )ua 
    join ads_uv_count  uv 
    on uv.dt=ua.dt
;

insert into table ads_sale_tm_category1_stat_mn
select   
    mn.sku_tm_id,
    mn.sku_category1_id,
    mn.sku_category1_name,
    sum(if(mn.order_count>=1,1,0)) buycount,
    sum(if(mn.order_count>=2,1,0)) buyTwiceLast,
    sum(if(mn.order_count>=2,1,0))/sum( if(mn.order_count>=1,1,0)) buyTwiceLastRatio,
    sum(if(mn.order_count>=3,1,0))  buy3timeLast  ,
    sum(if(mn.order_count>=3,1,0))/sum( if(mn.order_count>=1,1,0)) buy3timeLastRatio ,
    date_format('$do_date' ,'yyyy-MM') stat_mn,
    '$do_date' stat_date
from 
(
    select 
        user_id, 
        sd.sku_tm_id,
        sd.sku_category1_id,
        sd.sku_category1_name,
        sum(order_count) order_count
    from dws_sale_detail_daycount sd 
    where date_format(dt,'yyyy-MM')=date_format('$do_date' ,'yyyy-MM')
    group by user_id, sd.sku_tm_id, sd.sku_category1_id, sd.sku_category1_name
) mn
group by mn.sku_tm_id, mn.sku_category1_id, mn.sku_category1_name
;

"

$hive -e "$sql"
