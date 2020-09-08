-- 3.4.1 创建订单表
drop table if exists dwd_order_info;
create external table dwd_order_info
(
    `id`           string comment '',
    `total_amount` decimal(10, 2) comment '',
    `order_status` string comment ' 1 2 3 4 5',
    `user_id`      string comment 'id',
    `payment_way`  string comment '',
    `out_trade_no` string comment '',
    `create_time`  string comment '',
    `operate_time` string comment ''
)
    partitioned by (`dt` string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_order_info/'
    tblproperties ("parquet.compression" = "snappy")
;
-- 3.4.2 创建订单详情表
drop table if exists dwd_order_detail;
create external table dwd_order_detail
(
    `id`          string comment '',
    `order_id`    decimal(10, 2) comment '',
    `user_id`     string comment 'id',
    `sku_id`      string comment 'id',
    `sku_name`    string comment '',
    `order_price` string comment '',
    `sku_num`     string comment '',
    `create_time` string comment ''
)
    partitioned by (`dt` string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_order_detail/'
    tblproperties ("parquet.compression" = "snappy")
;
-- 3.4.3 创建用户表
drop table if exists dwd_user_info;
create external table dwd_user_info
(
    `id`          string comment 'id',
    `name`        string comment '',
    `birthday`    string comment '',
    `gender`      string comment '',
    `email`       string comment '',
    `user_level`  string comment '',
    `create_time` string comment ''
)
    partitioned by (`dt` string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_user_info/'
    tblproperties ("parquet.compression" = "snappy")
;
-- 3.4.4 创建支付流水表
drop table if exists dwd_payment_info;
create external table dwd_payment_info
(
    `id`              bigint comment '',
    `out_trade_no`    string comment '',
    `order_id`        string comment '',
    `user_id`         string comment '',
    `alipay_trade_no` string comment '',
    `total_amount`    decimal(16, 2) comment '',
    `subject`         string comment '',
    `payment_type`    string comment '',
    `payment_time`    string comment ''
)
    partitioned by (`dt` string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_payment_info/'
    tblproperties ("parquet.compression" = "snappy")
;
-- 3.4.5 创建商品表（增加分类）

drop table if exists dwd_sku_info;
create external table dwd_sku_info
(
    `id`             string comment 'skuid',
    `spu_id`         string comment 'spuid',
    `price`          decimal(10, 2) comment '',
    `sku_name`       string comment '',
    `sku_desc`       string comment '',
    `weight`         string comment '',
    `tm_id`          string comment 'id',
    `category3_id`   string comment '1id',
    `category2_id`   string comment '2id',
    `category1_id`   string comment '3id',
    `category3_name` string comment '3',
    `category2_name` string comment '2',
    `category1_name` string comment '1',
    `create_time`    string comment ''
)
    partitioned by (`dt` string)
    stored as parquet
    location '/warehouse/gmall/dwd/dwd_sku_info/'
    tblproperties ("parquet.compression" = "snappy")
;
