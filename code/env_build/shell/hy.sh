#!/bin/bash
case $1 in
# 传参为1时开启，0关闭
"1"){ 
    mr-jobhistory-daemon.sh start historyserver
    start-dfs.sh
    ssh server02 'start-yarn.sh'
    jpsall
};;
"0"){
    stop-dfs.sh
    mr-jobhistory-daemon.sh stop historyserver
    ssh server02 'stop-yarn.sh'
    jpsall
};;
esac