#!/bin/bash
# 说明1：nohup，该命令可以在你退出帐户/关闭终端之后继续运行相应的进程。
#       nohup就是不挂起的意思，不挂断地运行命令。
# 说明2：/dev/null代表linux的空设备文件，所有往这个文件里面写入的内容都会丢失，俗称“黑洞”。
# 标准输入0：从键盘获得输入 /proc/self/fd/0 
# 标准输出1：输出到屏幕（即控制台） /proc/self/fd/1 
# 错误输出2：输出到屏幕（即控制台） /proc/self/fd/2

# 启动 f1 1
# 停止 f1 0
case $1 in
"1"){
    echo -e "\033[31m -------- 启动 clickhouse-server ------- \033[0m"
    nohup clickhouse-server --config-file=/etc/clickhouse-server/config.xml >null 2>&1 &
};;    
"0"){
    echo -e "\033[31m -------- 停止 clickhouse-server ------- \033[0m"
    ps -ef | grep 'clickhouse-server' | grep -v grep |awk '{print $2}' | xargs kill -9
};;
esac
