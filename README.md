[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/oGmEBvoO)
### CS553 Cloud Computing Assignment 5 Repo
**Illinois Institute of Technology**

**Students**:  
* Batkhishig Dulamsurankhor (bdulamsurankhor@hawk.iit.edu) A20543498

## Repo structure
```bash
.
├── Blake                        # hadoop hashgen root dir
│   ├── data.txt
│   ├── dependency-reduced-pom.xml
│   ├── pom.xml
│   ├── src
│   │   └── main
│   │       └── java
│   │           └── blake
│   │               └── Blake3Hashing.java
│   └── target
├── HadoopSort                   # hadoop sort root dir
│   ├── dependency-reduced-pom.xml
│   ├── log.txt
│   ├── pom.xml
│   ├── src
│   │   └── main
│   │       └── java
│   │           └── hadoop
│   │               └── HashSort.java
│   └── target
├── README.md
├── hadoop-config           # hadoop config files
│   ├── core-site-datanode.xml
│   ├── core-site.xml
│   ├── hdfs-site-datanode.xml
│   ├── hdfs-site.xml
│   ├── mapred-site.xml
│   └── yarn-site.xml
├── hadoop-root-namenode-tiny.log   # hadoop namenode log (parts of mapreduce log)
├── hw5_report.pdf                  # report
├── prep.sh
├── setup-hd-datanode.sh            # hadoop datanode setup script
├── setup-hd-namenode.sh            # hadoop namenode setup script
├── test-results.txt
└── vault
```

## Setting up Hadoop
Start VMs with lxd.
```bash
sudo lxc launch ubuntu:22.04 tiny --vm -c limits.cpu=4 -c limits.memory=4GiB
sudo lxc launch ubuntu:22.04 small1 --vm -c limits.cpu=4 -c limits.memory=4GiB
sudo lxc launch ubuntu:22.04 small2 --vm -c limits.cpu=4 -c limits.memory=4GiB
sudo lxc launch ubuntu:22.04 small3 --vm -c limits.cpu=4 -c limits.memory=4GiB
sudo lxc launch ubuntu:22.04 small4 --vm -c limits.cpu=4 -c limits.memory=4GiB
sudo lxc launch ubuntu:22.04 small5 --vm -c limits.cpu=4 -c limits.memory=4GiB
sudo lxc launch ubuntu:22.04 small6 --vm -c limits.cpu=4 -c limits.memory=4GiB
sudo lxc launch ubuntu:22.04 large --vm -c limits.cpu=24 -c limits.memory=24GiB
```

On each vm, setup passwordless ssh connection to and from the tiny instance.

Run the following script for namenode.
```bash
./setup-hd-namenode.sh
```

Run the following script for datanode.
```bash
./setup-hd-datanode.sh
```

If you see the expected components running after jps, hadoop cluster is running.

## Generate hashes on hdfs

Install maven.
```bash
sudo apt install maven
```

Go to Blake directory.
```bash
cd Blake
```

Compile the source.
```bash
mvn package
```

Run the generated jar.
```bash
/usr/local/hadoop/bin/hadoop jar target/blake3-hashing-0.1.jar blake.Blake3Hashing write input small 0 1024
```

## Sort hashes on hdfs


Go to Blake directory.
```bash
cd HadoopSort
```

Compile the source.
```bash
mvn package
```

Delete the previous output in hdfs.
```bash
usr/local/hadoop/bin/hdfs dfs -rm -r output
```

Run the generated jar.
```bash
/usr/local/hadoop/bin/hadoop jar target/hadoop-sort-0.1.jar hadoop.HashSort input output
```

The result will be in output directory in hdfs.