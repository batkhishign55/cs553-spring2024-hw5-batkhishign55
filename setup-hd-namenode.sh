sudo apt-get update
sudo apt install openssh-server openssh-client -y
sudo apt install openjdk-11-jdk -y
sudo apt-get install ssh
sudo apt-get install rsync

wget https://downloads.apache.org/hadoop/common/hadoop-3.3.6/hadoop-3.3.6.tar.gz
tar xzf hadoop-3.3.6.tar.gz
sudo mv hadoop-3.3.6 /usr/local/hadoop
echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64' | sudo tee -a /usr/local/hadoop/etc/hadoop/hadoop-env.sh

export HADOOP_HOME=/usr/local/hadoop
export HADOOP_INSTALL=$HADOOP_HOME
export HADOOP_MAPRED_HOME=$HADOOP_HOME
export HADOOP_COMMON_HOME=$HADOOP_HOME
export HADOOP_HDFS_HOME=$HADOOP_HOME
export HADOOP_YARN_HOME=$HADOOP_HOME

head -n -3 $HADOOP_HOME/etc/hadoop/core-site.xml > temp.txt && mv temp.txt $HADOOP_HOME/etc/hadoop/core-site.xml
echo "<configuration>" >> $HADOOP_HOME/etc/hadoop/core-site.xml
echo "  <property>" >> $HADOOP_HOME/etc/hadoop/core-site.xml
echo "      <name>fs.defaultFS</name>" >> $HADOOP_HOME/etc/hadoop/core-site.xml
echo "      <value>hdfs://10.208.93.242:9000/</value>" >> $HADOOP_HOME/etc/hadoop/core-site.xml
echo "  </property>" >> $HADOOP_HOME/etc/hadoop/core-site.xml
echo "</configuration>" >> $HADOOP_HOME/etc/hadoop/core-site.xml

head -n -3 $HADOOP_HOME/etc/hadoop/hdfs-site.xml > temp.txt && mv temp.txt $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "<configuration>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "  <property>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "      <name>dfs.replication</name>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "      <value>1</value>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "  </property>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "  <property>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "      <name>dfs.namenode.name.dir</name>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "      <value>file:///hdfs/namenode</value>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "  </property>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "</configuration>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml

head -n -5 $HADOOP_HOME/etc/hadoop/yarn-site.xml > temp.txt && mv temp.txt $HADOOP_HOME/etc/hadoop/yarn-site.xml
echo "<configuration>" >> $HADOOP_HOME/etc/hadoop/yarn-site.xml
echo "  <property>" >> $HADOOP_HOME/etc/hadoop/yarn-site.xml
echo "      <name>yarn.nodemanager.aux-services</name>" >> $HADOOP_HOME/etc/hadoop/yarn-site.xml
echo "      <value>mapreduce_shuffle</value>" >> $HADOOP_HOME/etc/hadoop/yarn-site.xml
echo "  </property>" >> $HADOOP_HOME/etc/hadoop/yarn-site.xml
echo "</configuration>" >> $HADOOP_HOME/etc/hadoop/yarn-site.xml

head -n -3 $HADOOP_HOME/etc/hadoop/mapred-site.xml > temp.txt && mv temp.txt $HADOOP_HOME/etc/hadoop/mapred-site.xml
echo "<configuration>" >> $HADOOP_HOME/etc/hadoop/mapred-site.xml
echo "  <property>" >> $HADOOP_HOME/etc/hadoop/mapred-site.xml
echo "      <name>mapreduce.framework.name</name>" >> $HADOOP_HOME/etc/hadoop/mapred-site.xml
echo "      <value>yarn</value>" >> $HADOOP_HOME/etc/hadoop/mapred-site.xml
echo "  </property>" >> $HADOOP_HOME/etc/hadoop/mapred-site.xml
echo "</configuration>" >> $HADOOP_HOME/etc/hadoop/mapred-site.xml

$HADOOP_HOME/bin/hdfs namenode -format
$HADOOP_HOME/bin/hdfs --daemon start namenode
$HADOOP_HOME/bin/yarn --daemon start resourcemanager
$HADOOP_HOME/bin/yarn --daemon start nodemanager
$HADOOP_HOME/bin/yarn --daemon start proxyserver
$HADOOP_HOME/bin/mapred --daemon start historyserver
jps