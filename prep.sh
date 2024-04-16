sudo lxc launch ubuntu:22.04 hadoop-small --vm -c limits.cpu=4 -c limits.memory=4GiB
sudo lxc shell hadoop-small
sudo apt-get update && sudo apt-get -y upgrade
sudo apt install openssh-server openssh-client -y
sudo apt install openjdk-11-jdk -y
sudo apt install maven

mvn package
java -cp target/blake3-hashing-1.0-SNAPSHOT.jar blake.Blake3Hashing
java -cp target/blake3-hashing-1.0-SNAPSHOT.jar blake.Blake3Hashing read
hadoop jar target/blake3-hashing-1.0-SNAPSHOT.jar blake.Blake3Hashing read

wget https://downloads.apache.org/hadoop/common/hadoop-3.3.6/hadoop-3.3.6.tar.gz
tar xzf hadoop-3.3.6.tar.gz
sudo mv hadoop-3.3.6 /usr/local/hadoop
echo 'export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")' | sudo tee -a /usr/local/hadoop/etc/hadoop/hadoop-env.sh
/usr/local/hadoop/bin/hdfs namenode -format