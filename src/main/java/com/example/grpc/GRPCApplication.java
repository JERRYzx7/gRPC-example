package com.example.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.grpc", "server"})  // 掃描 gRPC 服務實現類所在的包
public class GRPCApplication {
  public static void main(String[] args) {

    SpringApplication.run(GRPCApplication.class, args);  // 啟動 Spring Boot 應用
  }

}
