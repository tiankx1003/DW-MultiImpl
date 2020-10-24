-- add file .py
select 
transform(
    -- cols1 从cols4中拿出需要使用的字段
) using 'udf.py -t COUNTER_PARTY_FINC_CLASSIFY - r finc_classify_2 -c cols2' -- cols2 传参给py脚本的字段， 用于切分
as (
    -- cols3 最终结果字段
)
from (
    select -- cols4 子查询的字段
    from tab0
) t;