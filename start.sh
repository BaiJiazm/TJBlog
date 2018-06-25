#!/bin/sh

#指定JDK目录&AppName
APP_NAME=TJBlog.jar
echo $JAVA_HOME
echo $APP_NAME

#nohup命令后台启动jar包并写入日志
nohup java -jar $APP_NAME >start.log 2>startError.log &

#sleep等待10秒后，判断包含AppName的线程是否存在
sleep 10

if test $(pgrep -f $APP_NAME|wc -l) -eq 0
then
   echo "Start Failed"
else
   echo "Start Successed"
fi
