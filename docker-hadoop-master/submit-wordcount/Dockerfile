FROM bde2020/hadoop-base:2.0.0-hadoop3.2.1-java8

COPY WordCount.jar /opt/hadoop/applications/WordCount.jar

COPY input.txt /data/

ENV JAR_FILEPATH="/opt/hadoop/applications/WordCount.jar"
ENV CLASS_TO_RUN="eie4108.WordCount"
ENV PARAMS="/input /output"
ENV DATA_FILE="/data/input.txt"

ADD run.sh /run.sh
RUN chmod a+x /run.sh

CMD ["/run.sh"]
