#!/bin/bash
nohup java -Dlogback.configurationFile=/home/eco1056-namns/Workspace/job/parameterdownload/src/main/resources/log4j2.xml -Dfile.ending=UTF8 -Dspring.config.location=/home/eco1056-namns/Workspace/job/parameterdownload/src/main/resources/application.yml -jar /home/eco1056-namns/Workspace/job/parameterdownload/target/parameterdownload-0.0.1-SNAPSHOT.jar  &
tail -f /home/eco1056-namns/Desktop/file/logs/parameterDownload.log
