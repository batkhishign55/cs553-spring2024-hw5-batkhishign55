sudo apt install maven

cd Blake
mvn package

/usr/local/hadoop/bin/hadoop jar target/blake3-hashing-0.1.jar blake.Blake3Hashing write input small 0 128