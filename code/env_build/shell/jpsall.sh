#!/bin/bash
for i in `cat $HADOOP_HOME/etc/hadoop/slaves`
do
    echo -e "\033[31m ======== $i ======== \033[0m"
    ssh $i 'jps'
done