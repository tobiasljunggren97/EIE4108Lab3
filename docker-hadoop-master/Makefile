DOCKER_NETWORK = docker-hadoop-net
ENV_FILE = hadoop.env
current_version := 2.0.0-hadoop3.2.1-java8
build:
	docker build -t bde2020/hadoop-base:$(current_version) ./base
	docker build -t bde2020/hadoop-namenode:$(current_version) ./namenode
	docker build -t bde2020/hadoop-datanode:$(current_version) ./datanode
	docker build -t bde2020/hadoop-resourcemanager:$(current_version) ./resourcemanager
	docker build -t bde2020/hadoop-nodemanager:$(current_version) ./nodemanager
	docker build -t bde2020/hadoop-historyserver:$(current_version) ./historyserver
	docker build -t bde2020/hadoop-submit:$(current_version) ./submit

wordcount:
	docker build -t hadoop-wordcount ./submit-wordcount
	docker run --network ${DOCKER_NETWORK} --env-file ${ENV_FILE} bde2020/hadoop-base:$(current_version) hdfs dfs -mkdir -p /input/
	docker run --network ${DOCKER_NETWORK} --env-file ${ENV_FILE} hadoop-wordcount
	docker run --network ${DOCKER_NETWORK} --env-file ${ENV_FILE} bde2020/hadoop-base:$(current_version) hdfs dfs -cat /output/*
	# docker run --network ${DOCKER_NETWORK} --env-file ${ENV_FILE} bde2020/hadoop-base:$(current_version) hdfs dfs -rm -r /output
	docker run --network ${DOCKER_NETWORK} --env-file ${ENV_FILE} bde2020/hadoop-base:$(current_version) hdfs dfs -rm -r /input

salestatistics:
	docker build -t hadoop-statistics ./submit-statistics
	docker run --network ${DOCKER_NETWORK} --env-file ${ENV_FILE} bde2020/hadoop-base:$(current_version) hdfs dfs -mkdir -p /input/
	docker run --network ${DOCKER_NETWORK} --env-file ${ENV_FILE} hadoop-statistics
	docker run --network ${DOCKER_NETWORK} --env-file ${ENV_FILE} bde2020/hadoop-base:$(current_version) hdfs dfs -cat /output/*
	# docker run --network ${DOCKER_NETWORK} --env-file ${ENV_FILE} bde2020/hadoop-base:$(current_version) hdfs dfs -rm -r /output
	docker run --network ${DOCKER_NETWORK} --env-file ${ENV_FILE} bde2020/hadoop-base:$(current_version) hdfs dfs -rm -r /input

clean:
	docker run --network ${DOCKER_NETWORK} --env-file ${ENV_FILE} bde2020/hadoop-base:$(current_version) hdfs dfs -rm -r /output
