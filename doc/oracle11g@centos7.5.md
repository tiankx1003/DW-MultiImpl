yum install -y binutils compat-libstdc elfutils-libelf gcc glibc ksh libaio libgcc libstdc make sysstat libXp glibc-kernheaders
wget ftp://ftp.pbone.net/mirror/archive.fedoraproject.org/fedora/linux/core/updates/4/x86_64/xorg-x11-deprecated-libs-6.8.2-37.FC4.49.2.1.x86_64.rpm
sudo rpm -ivh xorg-x11-deprecated-libs-6.8.2-37.FC4.49.2.1.x86_64.rpm


rpm -q --queryformat %-{name}-%{version}-%{release}-%{arch}"\n" compat-libstdc++-33 glibc-kernheaders glibc-headers libaio libgcc glibc-devel xorg-x11-deprecated-libs


rpm -q --queryformat %-{name}-%{version}-%{release}-%{arch}"\n" compat-libstdc++-33 kernel-headers glibc-headers libaio libgcc glibc-devel xorg-x11-deprecated-libs


vim /etc/sysctl.conf
vim /etc/security/limits.conf


```conf
fs.aio-max-nr = 1048576
fs.file-max = 6815744
kernel.shmall = 2097152
kernel.shmmax = 536870912
kernel.shmmni = 4096
kernel.sem = 250 32000 100 128
net.ipv4.ip_local_port_range = 9000 65500
net.core.rmem_default = 262144
net.core.rmem_max = 4194304
net.core.wmem_default = 262144
net.core.wmem_max = 1048586
```

```conf
oracle           soft    nproc   2047
oracle           hard    nproc   16384
oracle           soft    nofile  1024
oracle           hard    nofile  65536
oracle           hard    stack   10240
```

检查异步io是否运行
cat /proc/slabinfo | grep kio


groupadd  -g 5000 oinstall
groupadd  -g 501 dba
useradd -g oinstall  -G  dba  oracle
passwd oracle

vim /home/oracle/.bash_profile


```sh
export ORACLE_BASE=/oracle/app/oracle
export ORACLE_HOME=/oracle/app/oracle/product/11.2.0/dbhome_1
export ORACLE_SID=powerdes
export PATH=$ORACLE_HOME/bin:$PATH
export ORACLE_TERM=xterm
export TNS_ADMIN=$ORACLE_HOME/network/admin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$ORACLE_HOME/oracm/lib:$ORACLE_HOME/lib
export CLASSPATH=$CLASSPATH:$ORACLE_HOME/rdbms/jlib:$ORACLE_HOME/jlib:$ORACLE_HOME/network/lib
export LANG=en_US.gbk
export NLS_LANG=american_america.ZHS16GBK
export EDITOR=vi
```

mkdir -p /oracle/app/oracle
chown -R oracle:oinstall /oracle/app/oracle
chmod -R 775 /oracle/app/oracle


yum install -y tigervnc tigervnc-server
rpm -q tigervnc tigervnc-server
vncserver

yum groupinstall "GNOME Desktop"
yum update grub2-common
yum install fwupdate-efi


systemctl status vncserver@:1.service
systemctl stop vncserver@:1.service
systemctl restart vncserver@:1.service

cp /lib/systemd/system/vncserver@.service /etc/systemd/system/vncserver@:1.service
vim /etc/systemd/system/vncserver@:1.service


systemctl enable vncserver@:1.service
vncpasswd
systemctl start vncserver@:1.service
systemctl status vncserver@:1.service

[Oracle支持自启动](../code/env_build_new/oracle.sh)]

$ORACLE_HOME/network/admin/listener.ora
$ORACLE_HOME/network/admin/tnsnames.ora

