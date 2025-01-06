package client;

import Interceptor.gRPCInterceptor;
import com.example.grpc.ExampleProto;
import com.example.grpc.ExampleServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class client05 {

    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 5000;

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port)
                .intercept(new gRPCInterceptor())
                .usePlaintext()
                .build();

        if (managedChannel != null) {
            System.out.println("Channel created successfully.");

            // 通過通道連線(此處為同步)
            ExampleServiceGrpc.ExampleServiceStub exampleServiceStub = ExampleServiceGrpc.newStub(managedChannel);

            // 建立雙向串流的請求
            StreamObserver<ExampleProto.ExampleRequest01> request01StreamObserver = exampleServiceStub.clients2Server(new StreamObserver<ExampleProto.ExampleResponse01>() {
                @Override
                public void onNext(ExampleProto.ExampleResponse01 exampleResponse01) {
                    System.out.println("Server 回傳的訊息: " + exampleResponse01.getMessage());
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("傳遞訊息錯誤: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("Server 回傳結束，並等待後續處理");
                }
            });

            // 發送 10 個請求
            for (int i = 0; i < 10; i++) {
                ExampleProto.ExampleRequest01.Builder builder = ExampleProto.ExampleRequest01.newBuilder();
                builder.setName("This is the " + i + " th message.");
                builder.setYear("1990");
                ExampleProto.ExampleRequest01 exampleRequest01 = builder.build();

                // 發送請求
//                System.out.println("發送請求: " + exampleRequest01.getName());
                request01StreamObserver.onNext(exampleRequest01);

                // 模擬延遲
                Thread.sleep(1000);
            }

            // 發送完成通知
            request01StreamObserver.onCompleted();

            // 等待足夠時間以接收回應
            managedChannel.awaitTermination(20, TimeUnit.SECONDS);

        } else {
            System.out.println("Channel creation failed.");
        }
    }
}
