hadoop_add_profile tez
function _tez_hadoop_classpath
{
    hadoop_add_classpath "$HADOOP_HOME/etc/hadoop" after
    hadoop_add_classpath "/opt/module/tez-0.10.1/*" after
    hadoop_add_classpath "/opt/module/tez-0.10.1/lib/*" after
}
