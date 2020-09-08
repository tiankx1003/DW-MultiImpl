-- 3.3.1 创建订单表
drop table if exists ods_order_info;
create external table ods_order_info
(
    `id`           string comment '订单编号',
    `total_amount` decimal(10, 2) comment '订单金额',
    `order_status` string comment '订单状态',
    `user_id`      string comment '用户id',
    `payment_way`  string comment '支付方式',
    `out_trade_no` string comment '支付流水号',
    `create_time`  string comment '创建时间',
    `operate_time` string comment '操作时间'
) comment '订单表'
    partitioned by (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ods/ods_order_info/'
;
-- 3.3.2 创建订单详情表
drop table if exists ods_order_detail;
create external table ods_order_detail
(
    `id`          string comment '订单编号',
    `order_id`    string comment '订单号',
    `user_id`     string comment '用户id',
    `sku_id`      string comment '商品id',
    `sku_name`    string comment '商品名称',
    `order_price` string comment '商品价格',
    `sku_num`     string comment '商品数量',
    `create_time` string comment '创建时间'
) comment '订单明细表'
    partitioned by (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ods/ods_order_detail/'
;
-- 3.3.3 创建商品表
drop table if exists ods_sku_info;
create external table ods_sku_info
(
    `id`           string comment 'skuid',
    `spu_id`       string comment 'spuid',
    `price`        decimal(10, 2) comment '价格',
    `sku_name`     string comment '商品名称',
    `sku_desc`     string comment '商品描述',
    `weight`       string comment '重量',
    `tm_id`        string comment '品牌id',
    `category3_id` string comment '品类id',
    `create_time`  string comment '创建时间'
) comment '商品表'
    partitioned by (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ods/ods_sku_info/'
;
-- 3.3.4 创建用户表
drop table if exists ods_user_info;
create external table ods_user_info
(
    `id`          string comment '用户id',
    `name`        string comment '姓名',
    `birthday`    string comment '生日',
    `gender`      string comment '性别',
    `email`       string comment '邮箱',
    `user_level`  string comment '用户等级',
    `create_time` string comment '创建时间'
) comment '用户信息'
    partitioned by (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ods/ods_user_info/'
;
-- 3.3.5 创建商品一级分类表
drop table if exists ods_base_category1;
create external table ods_base_category1
(
    `id`   string comment 'id',
    `name` string comment '名称'
) comment '商品一级分类'
    partitioned by (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ods/ods_base_category1/'
;
-- 3.3.6 创建商品二级分类表
drop table if exists ods_base_category2;
create external table ods_base_category2
(
    `id`         string comment ' id',
    `name`       string comment '名称',
    category1_id string comment '一级品类id'
) comment '商品二级分类'
    partitioned by (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ods/ods_base_category2/'
;
-- 3.3.7 创建商品三级分类表
drop table if exists ods_base_category3;
create external table ods_base_category3
(
    `id`         string comment ' id',
    `name`       string comment '名称',
    category2_id string comment '二级品类id'
) comment '商品三级分类'
    partitioned by (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ods/ods_base_category3/'
;
-- 3.3.8 创建支付流水表
drop table if exists ods_payment_info;
create external table ods_payment_info
(
    `id`              bigint comment '编号',
    `out_trade_no`    string comment '对外业务编号',
    `order_id`        string comment '订单编号',
    `user_id`         string comment '用户编号',
    `alipay_trade_no` string comment '支付宝交易流水编号',
    `total_amount`    decimal(16, 2) comment '支付金额',
    `subject`         string comment '交易内容',
    `payment_type`    string comment '支付类型',
    `payment_time`    string comment '支付时间'
) comment '支付流水表'
    partitioned by (`dt` string)
    row format delimited fields terminated by '\t'
    location '/warehouse/gmall/ods/ods_payment_info/'
;
