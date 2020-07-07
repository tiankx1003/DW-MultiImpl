#!/bin/bash
pcount=$#
if ((pcount==0)); then
echo no args;
exit;
fi
p1=$1
fname=`basename $p1`
echo fname=$fname
pdir=`cd -P $(dirname $p1); pwd`
echo pdir=$pdir
user=`whoami`
for i in `cat $HADOOP_HOME/etc/hadoop/slaves`
do
	echo -e "\033[31m ---------- $i ---------- \033[0m"
	rsync -av $pdir/$fname $user@$i:$pdir
done