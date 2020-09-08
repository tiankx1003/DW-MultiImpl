-- ADS活跃设备数

drop table if exists ads_uv_count;
create external table ads_uv_count
(
    dt         string comment '统计日期',
    day_count  bigint comment '日活总数',
    wk_count   bigint comment '周活总数',
    is_weekend string comment '是否是周末'
) comment '活跃设备数'
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/dws/ads_uv_count';

-- 插入数据
insert overwrite table ads_uv_count
select '2019-08-28' dt,
       daycount.ct,
       wkcount.ct,
       if(date_add(next_day('2019-08-28', 'MO'), -1) = '2019-08-28', 'Y', 'N') -- 是否是周末
from (select '2019-08-28' dt, count(*) ct
      from dws_uv_day
      where dt = '2019-08-28') daycount
         join
     (select '2019-08-28' dt, count(*) ct
      from dws_uv_wk
      where wk_dt =
            concat(date_add(next_day('2019-08-28', 'MO'), -7), '_', date_add(next_day('2019-08-28', 'MO'), -1))) wkcount
     on daycount.dt = wkcount.dt;