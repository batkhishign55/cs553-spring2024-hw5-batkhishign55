sudo adduser hduser

sudo lxc launch ubuntu:22.04 hadoop-small --vm -c limits.cpu=4 -c limits.memory=4GiB
sudo lxc shell hadoop-small
sudo apt-get update && sudo apt-get -y upgrade
sudo apt install openssh-server openssh-client -y
sudo apt install openjdk-11-jdk -y
sudo apt install maven

sudo apt-get install ssh
sudo apt-get install rsync

mvn package
java -cp target/blake3-hashing-1.0-SNAPSHOT.jar blake.Blake3Hashing
java -cp target/blake3-hashing-1.0-SNAPSHOT.jar blake.Blake3Hashing read
/usr/local/hadoop/bin/hadoop jar target/blake3-hashing-1.0-SNAPSHOT.jar blake.Blake3Hashing read
/usr/local/hadoop/bin/hadoop jar target/blake3-hashing-1.0-SNAPSHOT.jar blake.Blake3Hashing write input small
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
