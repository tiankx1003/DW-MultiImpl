#! /bin/bash
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
        for i in server01 server02
        do
                echo " --------启动 $i 采集flume-------"
                ssh $i "nohup flume-ng agent -n a1 -c /opt/module/flume-1.7.0/conf -f /opt/module/flume-1.7.0/job/flume01.conf -Dflume.root.logger=INFO,LOGFILE > /opt/module/flume-1.7.0/logs/flume.log 2>&1 &"
        done
};;	
"0"){
        for i in server01 server02
        do
                echo " --------停止 $i 采集flume-------"
                ssh $i "ps -ef | grep flume01 | grep -v grep |awk '{print $2}' | xargs kill -9"
        done

};;
esac
