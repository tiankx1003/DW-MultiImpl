-- 商品点击表
drop table if exists dwd_display_log;
create external table dwd_display_log
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
    `action`       string,
    `goodsid`      string,
    `place`        string,
    `extend1`      string,
    `category`     string,
    `server_time`  string
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_display_log/'
    tblproperties ('parquet.compression' = 'lzo');

-- 商品详情页表
drop table if exists dwd_newsdetail_log;
create external table dwd_newsdetail_log
(
    `mid_id`        string,
    `user_id`       string,
    `version_code`  string,
    `version_name`  string,
    `lang`          string,
    `source`        string,
    `os`            string,
    `area`          string,
    `model`         string,
    `brand`         string,
    `sdk_version`   string,
    `gmail`         string,
    `height_width`  string,
    `app_time`      string,
    `network`       string,
    `lng`           string,
    `lat`           string,
    `entry`         string,
    `action`        string,
    `goodsid`       string,
    `showtype`      string,
    `news_staytime` string,
    `loading_time`  string,
    `type1`         string,
    `category`      string,
    `server_time`   string
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_newsdetail_log/'
    tblproperties ('parquet.compression' = 'lzo');

-- 商品列表页表
drop table if exists dwd_loading_log;
create external table dwd_loading_log
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
    `action`       string,
    `loading_time` string,
    `loading_way`  string,
    `extend1`      string,
    `extend2`      string,
    `type`         string,
    `type1`        string,
    `server_time`  string
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_loading_log/'
    tblproperties ('parquet.compression' = 'lzo');

-- 广告表
drop table if exists dwd_ad_log;
create external table dwd_ad_log
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
    `action`       string,
    `content`      string,
    `detail`       string,
    `ad_source`    string,
    `behavior`     string,
    `newstype`     string,
    `show_style`   string,
    `server_time`  string
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_ad_log/'
    tblproperties ('parquet.compression' = 'lzo');

-- 消息通知表
drop table if exists dwd_notification_log;
create external table dwd_notification_log
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
    `action`       string,
    `noti_type`    string,
    `ap_time`      string,
    `content`      string,
    `server_time`  string
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_notification_log/'
    tblproperties ('parquet.compression' = 'lzo');

-- 用户前台活跃表

drop table if exists dwd_active_foreground_log;
create external table dwd_active_foreground_log
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
    `push_id`      string,
    `access`       string,
    `server_time`  string
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_foreground_log/'
    tblproperties ('parquet.compression' = 'lzo');

-- 用户后台活跃表
drop table if exists dwd_active_background_log;
create external table dwd_active_background_log
(
    `mid_id`        string,
    `user_id`       string,
    `version_code`  string,
    `version_name`  string,
    `lang`          string,
    `source`        string,
    `os`            string,
    `area`          string,
    `model`         string,
    `brand`         string,
    `sdk_version`   string,
    `gmail`         string,
    `height_width`  string,
    `app_time`      string,
    `network`       string,
    `lng`           string,
    `lat`           string,
    `active_source` string,
    `server_time`   string
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_background_log/'
    tblproperties ('parquet.compression' = 'lzo');

-- 评论表
drop table if exists dwd_comment_log;
create external table dwd_comment_log
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
    `comment_id`   int,
    `userid`       int,
    `p_comment_id` int,
    `content`      string,
    `addtime`      string,
    `other_id`     int,
    `praise_count` int,
    `reply_count`  int,
    `server_time`  string
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_comment_log/'
    tblproperties ('parquet.compression' = 'lzo');

-- 收藏表
drop table if exists dwd_favorites_log;
create external table dwd_favorites_log
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
    `id`           int,
    `course_id`    int,
    `userid`       int,
    `add_time`     string,
    `server_time`  string
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_favorites_log/'
    tblproperties ('parquet.compression' = 'lzo');

-- 点赞表
drop table if exists dwd_praise_log;
create external table dwd_praise_log
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
    `id`           string,
    `userid`       string,
    `target_id`    string,
    `type`         string,
    `add_time`     string,
    `server_time`  string
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_praise_log/'
    tblproperties ('parquet.compression' = 'lzo');

-- 错误日志表
drop table if exists dwd_error_log;
create external table dwd_error_log
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
    `errorBrief`   string,
    `errorDetail`  string,
    `server_time`  string
)
    partitioned by (dt string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_error_log/'
    tblproperties ('parquet.compression' = 'lzo');


set hive.exec.dynamic.partition.mode=nonstrict;

insert overwrite table gmall.dwd_base_event_log partition(dt='2020-09-03')
select
    base_analizer(line,'mid') as mid_id,
    base_analizer(line,'uid') as user_id,
    base_analizer(line,'vc') as version_code,
    base_analizer(line,'vn') as version_name,
    base_analizer(line,'l') as lang,
    base_analizer(line,'sr') as source,
    base_analizer(line,'os') as os,
    base_analizer(line,'ar') as area,
    base_analizer(line,'md') as model,
    base_analizer(line,'ba') as brand,
    base_analizer(line,'sv') as sdk_version,
    base_analizer(line,'g') as gmail,
    base_analizer(line,'hw') as height_width,
    base_analizer(line,'t') as app_time,
    base_analizer(line,'nw') as network,
    base_analizer(line,'ln') as lng,
    base_analizer(line,'la') as lat,
    event_name,
    event_json,
    base_analizer(line,'st') as server_time
from  gmall.ods_event_log lateral view flat_analizer(base_analizer(line,'et')) tem_flat as event_name,event_json
where dt='2020-09-03'  and base_analizer(line,'et')<>'';
select * from gmall.dwd_base_event_log limit 10;