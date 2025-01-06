package client;

import Interceptor.gRPCInterceptor;
import com.example.grpc.ExampleProto;
import com.example.grpc.ExampleServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class client06 {

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

            StreamObserver<ExampleProto.ExampleRequest01> request01StreamObserver = exampleServiceStub.clients2Servers(new StreamObserver<ExampleProto.ExampleResponse01>() {
                @Override
                public void onNext(ExampleProto.ExampleResponse01 exampleResponse01) {
                    System.out.println("Server 回傳的訊息: " + exampleResponse01.getMessage());
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {
                    System.out.println("Server 回傳結束");
                }
            });

            for(int i = 0; i < 10 ; i++){
                ExampleProto.ExampleRequest01.Builder builder = ExampleProto.ExampleRequest01.newBuilder();
                builder.setName("This is the " + i + " th message.");
                builder.setYear(1990+i+".");
                ExampleProto.ExampleRequest01 exampleRequest01 = builder.build();

                request01StreamObserver.onNext(exampleRequest01);
                Thread.sleep(1000);
            }

            request01StreamObserver.onCompleted();

            managedChannel.awaitTermination(15, TimeUnit.SECONDS);
            managedChannel.shutdown();
        } else {
            System.out.println("Channel creation failed.");
        }
    }
}
