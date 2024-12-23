# 使用 Maven 作為基底映像，包含 OpenJDK 8
FROM maven:3.8.6-openjdk-8 AS build

# 設定工作目錄
WORKDIR /app

# 複製 pom.xml 和 source 目錄
COPY pom.xml /app/
COPY src /app/src/

# 執行 Maven clean:clean 清理先前的構建
RUN mvn clean:clean
RUN mvn protobuf:compile
RUN mvn protobuf:compile-custom

# 暴露埠號，假設 gRPC 監聽在 5000 埠
EXPOSE 5000
