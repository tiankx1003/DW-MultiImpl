#! /bin/bash

for i in server01 server02
do
	ssh $i "java -classpath /opt/files/logcollector-1.0-SNAPSHOT-jar-with-dependencies.jar com.tian.appclient.AppMain $1 $2 >/opt/files/test.log &"
done
