package client;

import Interceptor.gRPCInterceptor;
import com.example.grpc.ExampleProto;
import com.example.grpc.ExampleServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class client04 {

    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 5000;

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port).
                intercept(new gRPCInterceptor()).
                usePlaintext().build();

        if(managedChannel != null){
            System.out.println("Channel created successfully.");

            //通過通道連線(此處為非同步)
            ExampleServiceGrpc.ExampleServiceStub exampleServiceStub = ExampleServiceGrpc.newStub(managedChannel);

            ExampleProto.ExampleRequest01.Builder builder = ExampleProto.ExampleRequest01.newBuilder();
            builder.setName("John");
            builder.setYear("1990");
            ExampleProto.ExampleRequest01 exampleRequest01 = builder.build();

            exampleServiceStub.client2Servers(exampleRequest01, new StreamObserver<ExampleProto.ExampleResponse01>() {
                @Override
                public void onNext(ExampleProto.ExampleResponse01 exampleResponse01) {
                    System.out.println("server回傳的訊息: " + exampleResponse01.getMessage());
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("傳遞訊息錯誤");
                }

                @Override
                public void onCompleted() {
                    System.out.println("server回傳結束，並等待後續處理");
                }
            });
            managedChannel.awaitTermination(10, TimeUnit.SECONDS);
            managedChannel.shutdown();
        }else{
            System.out.println("Channel creation failed.");
        }
    }
}
