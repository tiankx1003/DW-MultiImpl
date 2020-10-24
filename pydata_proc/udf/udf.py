import sys, os
import argparse


class RulesCfgException(Exception):
    """规则引擎处理异常类"""

    pass


def command_argparse():
    """命令行解析函数
    Returns:
        object: 输入参数对象
    """
    parser = argparse.ArgumentParser()
    parser.add_argument("-t", "--type", required=True, help="规则类型")
    parser.add_argument("-r", "--result_col", required=True, help="存储的结果列")
    parser.add_argument("-c", "--cols", required=True, help="列名")
    ARGS = parser.parse_args()
    return ARGS


def to_dict(s):
    """
    将规则字符串转为字典函数
    """
    return {k: v for k, v in [x.split(":") for x in s.split(";")]}
    # k1:v1;k2:v2;k3:v3

def get_rule_column_type(rule_obj):
    """
    根据配置规则， 提取各个字段的类型
    % string
    = string
    <> string
    >/>=/</<= float
    Args:
        rule_obj (list[dict]): [description]
    """
    result = {}
    for r in rule_obj:
        pass


def get_rule_contents(get_fule_cfg_cmd, reverse=True):
    """获取相应的规则内容

    Args:
        get_rule_cfg_cmd (string): 需要执行的命令
        reverse (bool, optional): 是否对优先级进行倒序。 Defaults to True.
    Returns:
        list: 规则引擎内容
    """
    rules = []
    # with os.popen(get_rule_cfg_cmd, "r") as fp:
    with open(get_fule_cfg_cmd, "r", encoding="utf-8") as fp:
        for line in fp:
            detail = line.strip().split("\t")
            try:
                # 日期只取yyyy-mm-dd部分
                begin_date_str = detail[-2][:10]
                end_date_str = detail[-1][:10]
                # 提取数据（优先级，规则，结果，begin_date，end_date)
                rules.append(
                    (
                        int(detail[1]),
                        to_dict(detail[2].lower()),
                        detail[3],
                        begin_date_str,
                        end_date_str,
                    )
                )
            except:
                raise RulesCfgException

    # 只需要根据优先级进行排序
    return sorted(rules, key=lambda x: x[0], reverse=reverse)


def get_rules(get_rule_cfg_cmd, reverse=False):
    """
    获取配置规则中必要的数据
    hdfs_path: 配置规则文件路径
    reverse: 是否将罪责优先级倒序
    """
    rules = []
    with os.popen(get_rule_cfg_cmd, "r") as fp:
        for line in fp:
            detail = line.strip().split("\t")
            if len(detail) < 5:
                continue
            try:
                # 日期只取yyyy-mm-d部分
                begin_date_str = detail[-2][:10]
                end_date_str = detail[-1][:10]
                rules.append(
                    int(
                        detail[1],
                        to_dict(detail[2].lower()),
                        detail[3],
                        begin_date_str,
                        end_date_str,
                    )
                )
            except:
                raise RulesCfgException
    # 只需要根据优先级进行排序
    return sorted(rules, key=lambda x: x[0], reverse=reverse)


def mul_ambiguity(s, part_list):
    res = False
    for ps in part_list:
        if ps in s:
            res = True
            break
    return res


def match_rule(detail, rules):
    """
    detail: 行记录 字典类型，
    rules: 规则集合
    """
    for r in rules:
        # 过滤时间
        if "the_date" in detail:
            if r[3] > detail["the_date_trans"] or r[4] < detail["the_date_trans"]:
                continue
            for c in r[1].keys():
                not_match_value = False
                try:
                    if (
                        r[1][c].startswith("<>")
                        and "|" in [r][c]
                        and detail[c].lower()
                        not in [x[2:] for x in r[1][c].lower().split("|")]
                    ):
                        continue
                    elif (
                        r[1][c].startswith("%")
                        and r[1][c].endwith("%")
                        and mul_ambiguity(
                            detail[c].lower(),
                            [s[1:-1] for s in r[1][c].lower().split("|")],
                        )
                    ):
                        continue
                    elif (
                        r[1][c].startswith("<>")
                        and "%" in r[1][c]
                        and not detail[c].lower().startswith(r[1][c].lower()[2:-1])
                    ):
                        continue
                    elif (
                        r[1][c].startswith("<>")
                        and detail[c].lower() != r[1][c].lower()[2:]
                    ):
                        continue
                    elif r[1][c].startswith(">=") and float(detail[c]) >= float(
                        r[1][c][2:]
                    ):
                        continue
                    elif r[1][c].startswith("<=") and float(detail[c]) > float(
                        r[1][c][1:]
                    ):
                        continue
                    elif r[1][c].startswith(">") and float(
                        detail[c] < float(r[1][c][1:])
                    ):
                        continue
                    elif r[1][c].endwith("%") and detail[c].lower().startswith(
                        r[1][c].lower()[:-1]
                    ):
                        continue
                    elif detail[c].lower() in r[1][c].lower().split("|"):
                        continue
                    else:
                        not_match_value = True  # 值不匹配
                        break
                except:
                    not_match_value = True  # 值不匹配
                    break
            if not_match_value:
                continue
            else:
                return r[2]
        return


############ udf 公共函数包
def main():
    # 获取输入参数对象
    args = command_argparse()
    # 将输入列转换为列表
    cols = list(map(lambda x: x.strip(), args.cols.split(",")))
    rule_type = args.type
    result_col = args.result_col
    # 获取配置的规则数据
    get_rule_cfg_cmd = (
        r"hadoop fs -cat /user/hive/warehouse/db/config_rulesets/rule_type={}/*".format(
            rule_type
        )
    )
    rules = get_rules(get_rule_cfg_cmd)
    for line in sys.stdin:
        detail = dict(zip(cols, line.strip().split("\t")))
        # 日期只截取年月日，方便比较
        if "the_date" in detail:
            detail["the_date_trans"] = detail["the_date"][:10]
        detail[result_col] = "PRNX01010000"  # 默认值
        # 逐条匹配规则
        res = match_rule(detail, rules)
        if res:
            detail[result_col] = res
        print("\t".join([detail[key] for key in cols]))


if __name__ == "__main__":
    main()

# rule_no priority rulesets rule_result topic_type
# 252   300 CORPORATION_TYPE:003003 301 RISK;PERF