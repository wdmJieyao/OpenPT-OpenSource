#!/usr/bin/env bash
#定义应用的组名
group_name='hzaw'
#定义应用名称
app_name='tracker-service-app'
#定义应用版本
app_version='1.0.0'
#容器端口
app_in_port='7758'
#宿主端口
app_out_port='7758'
echo '----stop container----'
docker stop ${app_name}
echo '----rm container----'
docker rm ${app_name}
echo '----rm image----'
docker rmi docker.io/${app_name}:${app_version}
echo '----build image----'
docker build -t docker.io/${app_name}:${app_version} .
echo '----start container----'
docker run -d --restart=always --network=host \
-e TZ="Asia/Shanghai" \
-p ${app_out_port}:${app_in_port} \
-v /data/logs/${group_name}:/logs \
--name ${app_name} docker.io/${app_name}:${app_version}
