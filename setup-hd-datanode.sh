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
echo "      <name>dfs.datanode.data.dir</name>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "      <value>file:///hdfs/datanode</value>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "  </property>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml
echo "</configuration>" >> $HADOOP_HOME/etc/hadoop/hdfs-site.xml

$HADOOP_HOME/bin/hdfs --daemon start datanode
jps