#依赖的父镜像
FROM java:8
#作者
MAINTAINER icatw
#jar包添加到镜像中
# ADD wallpaper-server-1.0.jar wallpaper.jar
#容器暴露的端口 即jar程序在容器中运行的端口
EXPOSE 9888
#容器启动之后要执行的命令
ENTRYPOINT ["java","-Xmx256m","-Xms256m","-jar","/data/wallpaper.jar"]
