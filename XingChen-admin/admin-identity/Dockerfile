FROM openjdk:17-jdk-slim

LABEL maintainer="admin"
LABEL description="统一身份管理服务"

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建应用目录
RUN mkdir -p /app

# 设置工作目录
WORKDIR /app

# 复制jar包
COPY target/admin-identity-*.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs

# 暴露端口
EXPOSE 8082

# 启动命令
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
