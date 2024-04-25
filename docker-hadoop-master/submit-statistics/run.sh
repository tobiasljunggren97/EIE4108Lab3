#!/bin/bash
hdfs dfs -copyFromLocal -f $DATA_FILE /input/
$HADOOP_HOME/bin/hadoop jar $JAR_FILEPATH $CLASS_TO_RUN $PARAMS
