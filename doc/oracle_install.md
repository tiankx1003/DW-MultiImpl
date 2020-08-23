```sh
sudo yum -y install binutils compat-libcap1 compat-libstdc++-33 gcc gcc-c++ glibc glibc-devel ksh libaio libaio-devel libgcc libstdc++ libstdc++-devel libXi libXtst make sysstat unixODBC unixODBC-devel

sudo rpm -q binutils compat-libcap1 compat-libstdc++-33 gcc gcc-c++ glibc glibc-devel ksh libaio libaio-devel libgcc libstdc++ libstdc++-devel libXi libXtst make sysstat unixODBC unixODBC-devel | grep "not installed"

groupadd oinstall
groupadd dba
groupadd oper
useradd -g oinstall -G dba oracle


passwd oracle 
# oracle

echo 'oracle  ALL=(ALL)   ALL' >> /etc/sudoers

id oracle

vim /etc/sysctl.conf
```

```properties
net.ipv4.icmp_echo_ignore_broadcasts = 1
net.ipv4.conf.all.rp_filter = 1
net.ipv4.conf.default.rp_filter = 1
fs.file-max = 6815744
fs.aio-max-nr = 1048576
kernel.shmall = 2097152
kernel.shmmax = 2147483648
kernel.shmmni = 4096
kernel.sem = 250 32000 100 128
net.ipv4.ip_local_port_range = 9000 65500
net.core.rmem_default = 262144
net.core.rmem_max= 4194304
net.core.wmem_default= 262144
net.core.wmem_max= 1048576
```



```sh
sysctl -p

vim /etc/security/limits.conf
```

```properties
oracle              soft    nproc   2047
oracle              hard    nproc   16384
oracle              soft    nofile  1024
oracle              hard    nofile  65536
```



```sh
vim /etc/pam.d/login
```

```sh
session required /lib64/security/pam_limits.so
session required pam_limits.so
```



```sh
vim /etc/profile
source /etc/profile
```

```sh
if [ $USER = "oracle" ]; then
   if [ $SHELL = "/bin/ksh" ]; then
       ulimit -p 16384
       ulimit -n 65536
    else
       ulimit -u 16384 -n 65536
   fi
fi
```



```sh
mkdir -p /data/app/
chown -R oracle:oinstall /data/app/
chmod -R 775 /data/app/
vim /home/oracle/.bash_profile
vim /home/oracle/.bashrc
```

```sh
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
```



```sh
vim /etc/hosts
# 127.0.0.1 后加自己的主机名
cat /sys/kernel/mm/transparent_hugepage/enabled
```



```sh
vim /etc/rc.d/rc.local
chmod +x /etc/rc.d/rc.local
setenforce 0 # 临时关闭
vim /etc/sysconfig/selinux # 重启，永久关闭
# SELINUX=disabled
cat /sys/kernel/mm/transparent_hugepage/enabled # 查看THP是否已关闭
# [always] madvise never
/usr/sbin/sestatus -v # 查看selinux状态
```

```sh
if test -f /sys/kernel/mm/transparent_hugepage/enabled; then
	echo never > /sys/kernel/mm/transparent_hugepage/enabled
fi
if test -f /sys/kernel/mm/transparent_hugepage/defrag; then
	echo never > /sys/kernel/mm/transparent_hugepage/defrag
fi
```



```sh
yum -y install lrzsz unzip zip
cd /data #进入数据库目录
rz #上传数据库压缩包
unzip linux.x64_11gR2_database_1of2.zip -d /data #解压包1到data
unzip linux.x64_11gR2_database_2of2.zip -d /data #解压包2到data
```

```sh
mkdir -p /data/etc
chown -R oracle:oinstall /data/etc
cp /data/database/response/* /data/etc/
vim /data/etc/db_install.rsp
```

```rsp
oracle.install.option=INSTALL_DB_SWONLY
UNIX_GROUP_NAME=oinstall
ORACLE_HOSTNAME=omega
INVENTORY_LOCATION=/data/app/oracle/inventory
SELECTED_LANGUAGES=en,zh_CN
ORACLE_HOME=/data/app/oracle/product/11.2.0
ORACLE_BASE=/data/app/oracle
oracle.install.db.InstallEdition=EE
oracle.install.db.isCustomInstall=true
oracle.install.db.DBA_GROUP=dba
oracle.install.db.OPER_GROUP=dba
DECLINE_SECURITY_UPDATES=true
```



```sh
sudo chown -R oracle:oinstall app/
sudo chown -R oracle:oinstall database/
sudo chown -R oracle:oinstall etc/
```

```sh
su - oracle
cd /data/database
./runInstaller -silent -responseFile /data/etc/db_install.rsp -ignorePrereq
```

```sh
tail -f /data/app/oracle/inventory/logs/installActions2020-08-23_09-09-02PM.log


The following configuration scripts need to be executed as the "root" user. 
#!/bin/sh 
#Root scripts to run
 
/u01/app/oraInventory/orainstRoot.sh
/u01/app/oracle/product/11.2.0/db_1/root.sh
To execute the configuration scripts:
1. Open a terminal window 
2. Log in as "root" 
3. Run the scripts 
4. Return to this window and hit "Enter" key to continue
 
Successfully Setup Software.
```



```sh
su - root
sh /data/app/oracle/inventory/orainstRoot.sh
sh /data/app/oracle/product/11.2.0/root.sh
# 配置监听程序
su - oracle
netca /silent /responsefile /data/etc/netca.rsp
# netca: command not found
# 检查并修改 /home/oracle/.bash_profile
source /home/oracle/.bash_profile
```



```sh
netca /silent /responsefile /data/etc/netca.rsp
# 不是下面的返回结果则监察 /etc/hosts 本地回环地址是否添加主机名

Parsing command line arguments:
    Parameter "silent" = true
    Parameter "responsefile" = /data/etc/netca.rsp
Done parsing command line arguments.
Oracle Net Services Configuration:
Profile configuration complete.
Oracle Net Listener Startup:
    Running Listener Control:
      /data/app/oracle/product/11.2.0/bin/lsnrctl start LISTENER
    Listener Control complete.
    Listener started successfully.
Listener configuration complete.
Oracle Net Services configuration successful. The exit code is 0
```



```sh
# 查看监听端口
sudo yum install -y net-tools
netstat -tnpl | grep 1521
# tcp6       0      0 :::1521                 :::*                    LISTEN      31785/tnslsnr 
```

```sh
# 静默创建数据库
sudo vim /data/etc/dbca.rsp # 修改
```

```sh
[GENERAL]
RESPONSEFILE_VERSION = "11.2.0"
OPERATION_TYPE = "createDatabase"
[CREATEDATABASE]
GDBNAME = "orcl"
SID = "orcl"
SYSPASSWORD = "oracle"
SYSTEMPASSWORD = "oracle"
SYSMANPASSWORD = "oracle"
DBSNMPPASSWORD = "oracle"
DATAFILEDESTINATION =/data/app/oracle/oradata
RECOVERYAREADESTINATION=/data/app/oracle/fast_recovery_area
CHARACTERSET = "AL32UTF8"
TOTALMEMORY = "1638"
```

```sh
dbca -silent -responseFile /data/etc/dbca.rsp

Copying database files
1% complete
3% complete
11% complete
18% complete
26% complete
37% complete
Creating and starting Oracle instance
40% complete
45% complete
50% complete
55% complete
56% complete
60% complete
62% complete
Completing Database Creation
66% complete
70% complete
73% complete
85% complete
96% complete
100% complete
Look at the log file "/data/app/oracle/cfgtoollogs/dbca/orcl/orcl.log" for further details.
```

```sh
ps -ef | grep ora_ | grep -v grep


oracle    17496      1  0 18:48 ?        00:00:00 ora_pmon_orcl
oracle    17498      1  0 18:48 ?        00:00:00 ora_vktm_orcl
oracle    17502      1  0 18:48 ?        00:00:00 ora_gen0_orcl
oracle    17504      1  0 18:48 ?        00:00:00 ora_diag_orcl
oracle    17506      1  0 18:48 ?        00:00:00 ora_dbrm_orcl
oracle    17508      1  0 18:48 ?        00:00:00 ora_psp0_orcl
oracle    17510      1  0 18:48 ?        00:00:00 ora_dia0_orcl
oracle    17512      1  0 18:48 ?        00:00:00 ora_mman_orcl
oracle    17514      1  0 18:48 ?        00:00:00 ora_dbw0_orcl
oracle    17516      1  0 18:48 ?        00:00:00 ora_lgwr_orcl
oracle    17518      1  0 18:48 ?        00:00:00 ora_ckpt_orcl
oracle    17520      1  0 18:48 ?        00:00:00 ora_smon_orcl
oracle    17522      1  0 18:48 ?        00:00:00 ora_reco_orcl
oracle    17524      1  0 18:48 ?        00:00:00 ora_mmon_orcl
oracle    17526      1  0 18:48 ?        00:00:00 ora_mmnl_orcl
oracle    17528      1  0 18:48 ?        00:00:00 ora_d000_orcl
oracle    17530      1  0 18:48 ?        00:00:00 ora_s000_orcl
oracle    17541      1  0 18:48 ?        00:00:00 ora_qmnc_orcl
oracle    17557      1  0 18:48 ?        00:00:00 ora_cjq0_orcl
oracle    17566      1  0 18:48 ?        00:00:00 ora_q000_orcl
oracle    17568      1  0 18:48 ?        00:00:00 ora_q001_orcl
```

```sh
lsnrctl status


LSNRCTL for Linux: Version 11.2.0.1.0 - Production on 15-MAR-2019 18:50:22
 
Copyright (c) 1991, 2009, Oracle.  All rights reserved.
 
Connecting to (DESCRIPTION=(ADDRESS=(PROTOCOL=IPC)(KEY=EXTPROC1521)))
STATUS of the LISTENER
------------------------
Alias                     LISTENER
Version                   TNSLSNR for Linux: Version 11.2.0.1.0 - Production
Start Date                15-MAR-2019 17:55:11
Uptime                    0 days 0 hr. 55 min. 10 sec
Trace Level               off
Security                  ON: Local OS Authentication
SNMP                      OFF
Listener Parameter File   /data/app/oracle/product/11.2.0/network/admin/listener.ora
Listener Log File         /data/app/oracle/diag/tnslsnr/lhk_oracle/listener/alert/log.xml
Listening Endpoints Summary...
  (DESCRIPTION=(ADDRESS=(PROTOCOL=ipc)(KEY=EXTPROC1521)))
  (DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=localhost)(PORT=1521)))
Services Summary...
Service "orcl" has 1 instance(s).
  Instance "orcl", status READY, has 1 handler(s) for this service...
Service "orclXDB" has 1 instance(s).
  Instance "orcl", status READY, has 1 handler(s) for this service...
The command completed successfully
```



```sh
su oracle # 切换到oracle用户
sqlplus / as sysdba # 进入数据库
```

```sql
SQL> select status from v$instance; # 查数据库状态
SQL> select status from v$instance;
select status from v$instance
*
ERROR at line 1:
ORA-01034: ORACLE not available
Process ID: 0
Session ID: 0 Serial number: 0

# 解决方法
SQL> startup

ORA-00845: MEMORY_TARGET not supported on this system
调节内存大小


ORA-01078: failure in processing system parameters
LRM-00109: could not open parameter file '/data/app/oracle/product/11.2.0/dbs/initORCL.ora'
```

```sh
# ORA-01078
cd /data/app/oracle/admin/orcl/pfile/
ls 
# init.ora.2152019184812
sudo cp /data/app/oracle/admin/orcl/pfile/init.ora.2152019184812
/data/app/oracle/admin/orcl/pfile/init.ora
$ORACLE_HOME/dbs/initORCL.ora
```


```sql
create pfile='pfile=/data/app/oracle/product/11.2.0/db_1/dbs/initorcl.ora' from spfile;
create pfile='pfile=/data/app/oracle/admin/orcl/pfile/initorcl.ora' from spfile;
```



