package com.example.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import server.ExampleServiceImpl;

import java.io.IOException;

@SpringBootApplication  // 標註為 Spring Boot 應用
public class grpcApplication {

  public static void main(String[] args) throws IOException, InterruptedException{
    // 啟動 Spring Boot 應用
    ServerBuilder<?> serverBuilder = ServerBuilder.forPort(5000);
    serverBuilder.addService(new ExampleServiceImpl());
    Server server = serverBuilder.build().start();
    server.awaitTermination();
  }
}
