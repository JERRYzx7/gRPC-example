package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class ServerLauncher {

    private Server server;

    @PostConstruct
    public void startGrpcServer() throws Exception {
        server = ServerBuilder.forPort(8000)
                .addService(new ExampleServiceImpl())
                .build();

        server.start();
        System.out.println("gRPC server started, listening on 8000");
    }

    @PreDestroy
    public void stopGrpcServer() {
        if (server != null) {
            server.shutdown();
            System.out.println("gRPC server stopped.");
        }
    }

    // 可以根據需求提供一個方法來啟動server
    public void start() throws Exception {
        if (server != null) {
            server.start();
        }
    }

    // 也可以提供停止的方法
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }
}
