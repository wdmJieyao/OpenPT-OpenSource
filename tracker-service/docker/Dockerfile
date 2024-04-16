# 该项目镜像需要依赖的基础镜像
FROM openjdk:17
# 将当前目录下的jar包复制到docker容器的根目录下
ADD tracker-service-app.jar /tracker-service-app.jar
# 指定docker容器启动时运行jar包，在容器启动的时候运行命令，来启动我们的项目
ENTRYPOINT ["java","-jar","/tracker-service-app.jar","-Xms128m","-Xmx128m","-c"]
# 指定维护者的名字
MAINTAINER tracker
