# 使用官方的Java运行时作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 将jar包复制到容器中
COPY target/huanyou-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

# 容器启动时运行jar包
ENTRYPOINT ["java", "-jar", "app.jar"]
