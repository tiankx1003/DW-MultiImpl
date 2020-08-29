#!/bin/bash
#
#################FUNCTION#############
#
# AutoStart Oracle and listener
# AutoStop Oracle and listener
#
#####################################
#
# Created by ZhouYS 2003-11-26
#
case "$1" in
start)
echo "Starting Oracle Databases ... "
echo "-------------------------------------------------" >> /var/log/oracle
date +" %T %a %D : Starting Oracle Databasee as part of system up." >> /var/log/oracle
echo "-------------------------------------------------" >> /var/log/oracle
su - oracle -c "dbstart" >> /var/log/oracle
echo "Done."
echo "Starting Oracle Listeners ... "
echo "-------------------------------------------------" >> /var/log/oracle
date +" %T %a %D : Starting Oracle Listeners as part of system up." >> /var/log/oracle
echo "-------------------------------------------------" >> /var/log/oracle
su - oracle -c "lsnrctl start" >> /var/log/oracle
echo "Done."
echo ""
echo "-------------------------------------------------" >> /var/log/oracle
date +" %T %a %D : Finished." >> /var/log/oracle
echo "-------------------------------------------------" >> /var/log/oracle
touch /var/lock/subsys/oracle

;;
stop)
echo "Stoping Oracle Listeners ... "
echo "-------------------------------------------------" >> /var/log/oracle
date +" %T %a %D : Stoping Oracle Listener as part of system down." >> /var/log/oracle
echo "-------------------------------------------------" >> /var/log/oracle
su - oracle -c "lsnrctl stop" >> /var/log/oracle
echo "Done."
rm -f /var/lock/subsys/oracle
echo "Stoping Oracle Databases ... "
echo "-------------------------------------------------" >> /var/log/oracle
date +" %T %a %D : Stoping Oracle Databases as part of system down." >> /var/log/oracle
echo "-------------------------------------------------" >> /var/log/oracle
su - oracle -c "dbshut" >>/var/log/oracle
echo "Done."
echo ""
echo "-------------------------------------------------" >> /var/log/oracle
date +" %T %a %D : Finished." >> /var/log/oracle
echo "-------------------------------------------------" >> /var/log/oracle

;;
restart)
$0 stop
$0 start

;;
*)
echo "Usage: oracle "
exit 1
esac








#!/bin/sh
# chkconfig: 345 61 61
# description: Oracle 11g AutoRun Services
# /etc/init.d/oracle
#
# Run-level Startup script for the Oracle Instance, Listener, and
# Web Interface

# oracle start                                                                                                                
umask 022
export ORACLE_HOSTNAME=omega
export ORACLE_BASE=/data/app/oracle
export ORACLE_HOME=$ORACLE_BASE/product/11.2.0/
export ORACLE_SID=orcl
export PATH=.:$ORACLE_HOME/bin:$ORACLE_HOME/OPatch:$ORACLE_HOME/jdk/bin:$PATH
export LC_ALL="en_US"
export LANG="en_US"                                                                                                                     
export NLS_LANG="AMERICAN_AMERICA.ZHS16GBK"
export NLS_DATE_FORMAT="YYYY-MM-DD HH24:MI:SS"
# oracle end
export PATH=$PATH:$ORACLE_HOME/bin

ORA_OWNR="oracle"

# if the executables do not exist -- display error

if [ ! -f $ORACLE_HOME/bin/dbstart -o ! -d $ORACLE_HOME ]
then
     echo "Oracle startup: cannot start"
     exit 1
fi

# depending on parameter -- startup, shutdown, restart
# of the instance and listener or usage display

case "$1" in
 start)
     # Oracle listener and instance startup
     su $ORA_OWNR -lc $ORACLE_HOME/bin/dbstart
     echo "Oracle Start Succesful!OK."
     ;;
 stop)
     # Oracle listener and instance shutdown
     su $ORA_OWNR -lc $ORACLE_HOME/bin/dbshut
     echo "Oracle Stop Succesful!OK."
     ;;
 reload|restart)
     $0 stop
     $0 start
     ;;
 *)
     echo $"Usage: `basename $0` {start|stop|reload|reload}"
     exit 1
esac
exit 0