@echo off
set DOCKER_NETWORK=docker-hadoop-net
set ENV_FILE=hadoop.env
set current_version=2.0.0-hadoop3.2.1-java8

if "%~1" == "build" (goto build)
if "%~1" == "wordcount" (goto wordcount)
if "%~1" == "salestatistics" (goto salestatistics)
if "%~1" == "clean" (goto clean)
goto usage

:build
	docker build -t bde2020/hadoop-base:%current_version% ./base
	docker build -t bde2020/hadoop-namenode:%current_version% ./namenode
	docker build -t bde2020/hadoop-datanode:%current_version% ./datanode
	docker build -t bde2020/hadoop-resourcemanager:%current_version% ./resourcemanager
	docker build -t bde2020/hadoop-nodemanager:%current_version% ./nodemanager
	docker build -t bde2020/hadoop-historyserver:%current_version% ./historyserver
	docker build -t bde2020/hadoop-submit:%current_version% ./submit
	exit /b 0

:wordcount
	docker build -t hadoop-wordcount ./submit-wordcount
	docker run --network %DOCKER_NETWORK% --env-file %ENV_FILE% bde2020/hadoop-base:%current_version% hdfs dfs -mkdir -p /input/
	docker run --network %DOCKER_NETWORK% --env-file %ENV_FILE% hadoop-wordcount
	docker run --network %DOCKER_NETWORK% --env-file %ENV_FILE% bde2020/hadoop-base:%current_version% hdfs dfs -cat /output/*
	:: docker run --network %DOCKER_NETWORK% --env-file %ENV_FILE% bde2020/hadoop-base:%current_version% hdfs dfs -rm -r /output
	docker run --network %DOCKER_NETWORK% --env-file %ENV_FILE% bde2020/hadoop-base:%current_version% hdfs dfs -rm -r /input
	exit /b 0

:salestatistics
	docker build -t hadoop-statistics ./submit-statistics
	docker run --network %DOCKER_NETWORK% --env-file %ENV_FILE% bde2020/hadoop-base:%current_version% hdfs dfs -mkdir -p /input/
	docker run --network %DOCKER_NETWORK% --env-file %ENV_FILE% hadoop-statistics
	docker run --network %DOCKER_NETWORK% --env-file %ENV_FILE% bde2020/hadoop-base:%current_version% hdfs dfs -cat /output/*
	:: docker run --network %DOCKER_NETWORK% --env-file %ENV_FILE% bde2020/hadoop-base:%current_version% hdfs dfs -rm -r /output
	docker run --network %DOCKER_NETWORK% --env-file %ENV_FILE% bde2020/hadoop-base:%current_version% hdfs dfs -rm -r /input
	exit /b 0

:clean
	docker run --network %DOCKER_NETWORK% --env-file %ENV_FILE% bde2020/hadoop-base:%current_version% hdfs dfs -rm -r /output
	exit /b 0

:usage
    echo This script required exactly one argument
    if not "%~1" == "help" (
        if not [%1] == [] (
            echo `%1` is not a valid action
        )
    )
    echo usage: %~n0%~x0 ^[ build ^| wordcount ^| salestatistics ^| clean ^]
    exit /b 0
