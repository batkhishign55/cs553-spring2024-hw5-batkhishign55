

lxc config device override <cont-name> root size=250GiB
lxc storage set default volume.size 100GB
sudo systemctl reload snap.lxd.daemon
lxc storage info default

/usr/local/hadoop/bin/hdfs dfs -ls
/usr/local/hadoop/bin/hdfs dfsadmin -report
/usr/local/hadoop/bin/hadoop jar target/hadoop-sort-0.1.jar hadoop.HashSort input output

wget https://downloads.apache.org/hadoop/common/hadoop-3.3.6/hadoop-3.3.6.tar.gz
tar xzf hadoop-3.3.6.tar.gz
sudo mv hadoop-3.3.6 /usr/local/hadoop
echo 'export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")' | sudo tee -a /usr/local/hadoop/etc/hadoop/hadoop-env.sh


on namenode:
/usr/local/hadoop/bin/hdfs namenode -format
/usr/local/hadoop/bin/hdfs --daemon start namenode
/usr/local/hadoop/bin/hdfs --daemon start datanode
