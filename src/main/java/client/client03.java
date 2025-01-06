package client;

import Interceptor.gRPCInterceptor;
import com.example.grpc.ExampleProto;
import com.example.grpc.ExampleServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;

public class client03 {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port).
                intercept(new gRPCInterceptor()).
                usePlaintext().build();

        if(managedChannel != null){
            System.out.println("Channel created successfully.");

            //通過通道連線(此處為同步)
            ExampleServiceGrpc.ExampleServiceBlockingStub exampleServiceBlockingStub = ExampleServiceGrpc.newBlockingStub(managedChannel);

            ExampleProto.ExampleRequest01.Builder builder = ExampleProto.ExampleRequest01.newBuilder();
            builder.setName("John");
            builder.setYear("1990");
            ExampleProto.ExampleRequest01 exampleRequest01 = builder.build();

            Iterator<ExampleProto.ExampleResponse01> exampleResponse01Iterator = exampleServiceBlockingStub.client2Servers(exampleRequest01);
            while(exampleResponse01Iterator.hasNext()){
                ExampleProto.ExampleResponse01 exampleResponse01 = exampleResponse01Iterator.next();
                String message = exampleResponse01.getMessage();
                System.out.println("message: " + message);
            }
            managedChannel.shutdown();
        }else{
            System.out.println("Channel creation failed.");
        }
    }
}
