sudo apt install maven

cd HadoopSort
mvn package
$HADOOP_HOME/bin/hdfs dfs -rm -r output
$HADOOP_HOME/bin/hadoop jar target/hadoop-sort-0.1.jar hadoop.HashSort input output