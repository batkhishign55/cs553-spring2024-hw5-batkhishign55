sudo apt install maven

cd Blake
mvn package
$HADOOP_HOME/bin/hadoop jar target/blake3-hashing-0.1.jar blake.Blake3Hashing write input small