# 使用包含 OpenJDK 17 的基底映像
FROM openjdk:17-jdk-slim AS build

# 設定工作目錄
WORKDIR /app

# 複製整個 target 目錄
COPY target /app/target

CMD ["java", "-jar", "target/gRPC-0.0.1-SNAPSHOT.jar"]