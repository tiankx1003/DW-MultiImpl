#! /bin/bash

case $1 in
"1"){
        for i in server03
        do
                echo " --------启动 $i 消费flume-------"
                ssh $i "nohup /opt/module/flume-1.9.0/bin/flume-ng agent -n a1 -c /opt/module/flume-1.9.0/conf/ -f /opt/module/flume-1.9.0/job/flume02.conf -Dflume.root.logger=INFO,LOGFILE > /opt/module/flume-1.9.0/logs/flume.log 2>&1 &"
        done
};;
"0"){
        for i in server03
        do
                echo " --------停止 $i 消费flume-------"
                ssh $i "ps -ef | grep flume02 | grep -v grep |awk '{print $2}' | xargs kill"
                
        done
};;
esac

# nohup /opt/module/flume-1.9.0/bin/flume-ng agent -n a1 -c /opt/module/flume-1.9.0/conf/ -f /opt/module/flume-1.9.0/job/flume02.conf -Dflume.root.logger=INFO,LOGFILE > /opt/module/flume-1.9.0/logs/flume.log 2>&1 &