FROM openjdk:17-jdk-slim

LABEL maintainer="admin@example.com"
LABEL description="Spring Cloud Admin System Service"

WORKDIR /app

# 复制 Maven 构建的 jar 文件
COPY target/*.jar app.jar

# 暴露端口
EXPOSE 8083

# 设置 JVM 参数
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
