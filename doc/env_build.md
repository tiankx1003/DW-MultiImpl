### node0
 * install CentOS 6.8 minimal config network setting
```bash
yum install -y vim tar rsync openssh openssh-clients libaio net-tools git ntp ntpdate ntp-doc zip unzip
service iptables status
service iptables stop
chkconfig iptables --list
chkconfig iptables off
chkconfig ntpd on
ntpdate pool.ntp.org
/etc/init.d/ntpd start
vim /etc/vimrc
vim /etc/hosts
useradd tian
passwd tian
mkdir /opt/module/ /opt/software/
chown tian:tian /opt/module/ /opt/software/ -R
su tian
tar -zxvf /opt/software/jdk-8u144-linux-x64.tar.gz -C /opt/module
tar -zxvf /opt/software/scala-2.11.8.tgz -C /opt/module
sed 's/timeout=5/timeout=1/' /etc/grub.conf
echo 'tiankx ALL=(ALL)    NOPASSWD:ALL' >> /etc/sudoers
echo 'export JAVA_HOME=/opt/module/jdk1.8.0_144' >> /etc/profile
echo 'export PATH=$PATH:$JAVA_HOME/bin' >> /etc/profile
echo 'export SCALA_HOME=/opt/module/scala-2.11.8' >> /etc/profile
echo 'export PATH=$PATH:$SCALA_HOME/bin' >> /etc/profile
echo 'source /etc/profile' >> ~/.bashrc
source /etc/profile
java -version
scala -version
mkdir -p ~/bin
```
```vim
colorscheme default
syntax on
set showmode
set showcmd
set t_Co=256
filetype on
filetype indent on
filetype plugin on
filetype plugin indent on
set autoindent
set cindent
set tabstop=4
set shiftwidth=4
set softtabstop=4
set cursorline
set linebreak
set wrapmargin=0
set incsearch
set smartcase
set nobackup
set noswapfile
set undofile
set noerrorbells
set visualbell
set history=1000
set autoread
set fileencodings=utf-8,ucs-bom,gb18030,gbk,gb2312,cp936
set termencoding=utf-8
set encoding=utf-8
au FileType c,cpp setlocal comments-=:// comments+=f://
set paste
```
```host
192.168.2.200 node0
192.168.2.201 node1
192.168.2.202 node2
192.168.2.203 node3
```
### clone vm
```bash
vim /etc/udev/rules.d/70-persistent-net.rules
vim /etc/sysconfig/network-scripts/ifcfg-eth0
vim /etc/sysconfig/network
vim ~/bin/xsync
vim ~/bin/xcall
vim ~/bin/copy-ssh
vim ~/bin/jpsall
vim ~/bin/hy
chmod 777 ~/bin/*
```
### hadoop
```bash
tar -zxvf hadoop-2.7.2.tar.gz -C /opt/module/
vim slaves
# node1
# node2
# node3
vim core-site.xml
vim hdfs-site.xml
vim yarn-site.xml
vim mapred-site.xml
echo 'export JAVA_HOME=/opt/module/jdk1.8.0_144' >> /opt/module/hadoop-2.7.2/etc/hadoop/mapred-env.sh
echo 'export JAVA_HOME=/opt/module/jdk1.8.0_144' >> /opt/module/hadoop-2.7.2/etc/hadoop/hadoop-env.sh
echo 'export JAVA_HOME=/opt/module/jdk1.8.0_144' >> /opt/module/hadoop-2.7.2/etc/hadoop/yarn-env.sh
# export JAVA_HOME=/opt/module/jdk1.8.0_144
xsync /opt/module/hadoop-2.7.2
sudo su root
/home/tiankx/bin/xcall echo 'export HADOOP_HOME=/opt/module/hadoop-2.7.2' >> /etc/profile
/home/tiankx/bin/xcall echo 'export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin' >> /etc/profile
exit
xcall hadoop version
echo 'log4j.logger.org.apache.hadoop.util.NativeCodeLoader=ERROR' >> $HADOOP_HOME/etc/hadoop/log4j.properties
hdfs namenode -format
hy 1
jpsall
hy 0
jpsall
```
### hive
```bash
tar -zxvf apache-hive-2.3.0-bin.tar.gz -C /opt/module/
mv apache-hive-2.3.0-bin/ hive-2.3.0/
vim /etc/profile
# export HIVE_HOME=/opt/module/hive-2.3.0
# export PATH=$PATH:$HIVE_HOME/bin
source /etc/profile
cp /opt/module/hive-2.3.0/conf/hive-env.sh.template /opt/module/hive-2.3.0/conf/hive-env.sh
echo 'export HADOOP_HOME=/opt/module/hadoop-2.7.2' >> /opt/module/hive-2.3.0/conf/hive-env.sh
echo 'export HIVE_CONF_DIR=/opt/module/hive-2.3.0/conf' >> /opt/module/hive-2.3.0/conf/hive-env.sh
hadoop fs -mkdir /tmp
hadoop fs -mkdir -p /user/hive/warehouse
hadoop fs -chmod g+w /tmp
hadoop fs -chmod g+w /user/hive/warehouse
tar -zxvf mysql-connector-java-5.1.27.tar.gz
cp mysql-connector-java-5.1.27-bin.jar /opt/module/hive-2.3.0/lib/
vim $HIVE_HOME/conf/hive-site.xml
schematool -dbType mysql -initSchema
hive --service metastore &
hy 1
hive
```
### mysql
```bash
rpm -qa|grep -i mysql
sudo rpm -e --nodeps mysql-libs-5.1.73-7.el6.x86_64
sudo rpm -ivh MySQL-server-5.6.24-1.el6.x86_64.rpm
sudo cat /root/.mysql_secret
sudo service mysql status
sudo service mysql start
sudo rpm -ivh MySQL-client-5.6.24-1.el6.x86_64.rpm
mysql -uroot -pTFQsVMblhG6JoxhB
```
```sql
SET PASSWORD=PASSWORD('root');
-- alter user 'root'@'localhost'IDENTIFIED BY 'root';
use mysql;
update user set host='%' where host='localhost';
delete from user where Host='node1';
delete from user where Host='127.0.0.1';
delete from user where Host='::1';
flush privileges;
\q;
```
### Hive on Tez
```bash
tar -zxvf apache-tez-0.9.1-bin.tar.gz -C /opt/module/
vim $HIVE_HOME/conf/hive-env.sh
vim $HIVE_HOME/conf/hive-site.xml
vim $HIVE_HOME/conf/tez-site.xml
vim $HADOOP_HOME/etc/hadoop/yarn-site.xml
xsync $HADOOP_HOME/etc/hadoop/yarn-site.xml
hadoop fs -mkdir /tez
hadoop fs -put /opt/module/tez-0.9.1/ /tez
hadoop fs -ls /tez /tez/tez-0.9.1
hive # 执行验证sql
```
```sh
export TEZ_HOME=/opt/module/tez-0.9.1    #是你的tez的解压目录
export TEZ_JARS=""
for jar in `ls $TEZ_HOME |grep jar`; do
    export TEZ_JARS=$TEZ_JARS:$TEZ_HOME/$jar
done
for jar in `ls $TEZ_HOME/lib`; do
    export TEZ_JARS=$TEZ_JARS:$TEZ_HOME/lib/$jar
done
export HIVE_AUX_JARS_PATH=/opt/module/hadoop-2.7.2/share/hadoop/common/hadoop-lzo-0.4.20.jar$TEZ_JARS
```
```xml
<!-- hive-site.xml -->
<property>
    <name>hive.execution.engine</name>
    <value>tez</value>
</property>
<!-- yarn-site.xml -->
<property>
    <name>yarn.nodemanager.vmem-check-enabled</name>
    <value>false</value>
</property>
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
  <property>
    <name>tez.lib.uris</name>
    <value>${fs.defaultFS}/tez/tez-0.9.1,${fs.defaultFS}/tez/tez-0.9.1/lib</value>
  </property>
  <property>
    <name>tez.lib.uris.classpath</name>
    <value>${fs.defaultFS}/tez/tez-0.9.1,${fs.defaultFS}/tez/tez-0.9.1/lib</value>
  </property>
  <property>
    <name>tez.use.cluster.hadoop-libs</name>
    <value>true</value>
  </property>
  <property>
    <name>tez.history.logging.service.class</name>
    <value>org.apache.tez.dag.history.logging.ats.ATSHistoryLoggingService</value>
  </property>
</configuration>
```
```sql
create table stu(id int,name string);
insert into stu values(1,'zhangshan');
select * from stu;
drop table stu;
```
### spark compile
```bash
tar -zxvf apache-maven-3.5.4-bin.tar.gz -C ../module/
mv apache-maven-3.5.4/ maven-3.5.4/
sudo vim /etc/profile
# 添加maven环境变量
vim /opt/module/maven-3.5.4/conf/settings.xml
export MAVEN_OPTS="-Xmx2g -XX:MaxPermSize=512M -XX:ReservedCodeCacheSize=512m"
echo $MAVEN_OPTS
# ./make-distribution.sh --name "hadoop2-without-hive" --tgz "-Pyarn,hadoop-provided,hadoop-2.6,parquet-provided"
./dev/make-distribution.sh \
--name "hadoop272-without-hive" \
--tgz "-Pyarn,hadoop-provided,hadoop-2.7,parquet-provided,orc-provided" \
-Dhadoop.version=2.7.2
```

```bash
export MAVEN_OPTS="-Xmx2g -XX:ReservedCodeCacheSize=512m"
echo $MAVEN_OPTS
./dev/make-distribution.sh --name "hadoop272-without-hive" --tgz "-Pyarn,hadoop-provided,hadoop-2.7,parquet-provided,orc-provided" -Dhadoop.version=2.7.2"
```
```xml
<repositories>
  <repository>
    <id>central</id>
    <!-- This should be at top, it makes maven try the central repo first and then others and hence faster dep resolution -->
    <name>Maven Repository</name>
    <!--<url>https://repo.maven.apache.org/maven2</url>!-->
    <url>https://maven.aliyun.com/nexus/content/groups/public/</url>
    <releases>
      <enabled>true</enabled> 
    </releases>
    <snapshots>
      <enabled>false</enabled>
    </snapshots>
  </repository>
</repositories>
<pluginRepositories>
  <pluginRepository>
    <id>central</id>
    <!--<url>https://repo.maven.apache.org/maven2</url>-->
    <url>https://maven.aliyun.com/nexus/content/groups/public/</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>false</enabled>
    </snapshots>
  </pluginRepository>
</pluginRepositories>
```
