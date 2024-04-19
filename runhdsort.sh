sudo apt install maven

cd HadoopSort
mvn package
/usr/local/hadoop/bin/hdfs dfs -rm -r output
/usr/local/hadoop/bin/hadoop jar target/hadoop-sort-0.1.jar hadoop.HashSort input output