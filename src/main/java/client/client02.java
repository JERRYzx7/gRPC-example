package client;

import Interceptor.gRPCInterceptor;
import com.example.grpc.ExampleProto;
import com.example.grpc.ExampleServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class client02 {
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

            ExampleProto.ExampleRequest02.Builder builder = ExampleProto.ExampleRequest02.newBuilder();
            builder.addName("John");
            builder.addName("Tom");
            builder.addName("Jerry");
            builder.addName("Mickey");
            ExampleProto.ExampleRequest02 exampleRequest02 = builder.build();

            ExampleProto.ExampleResponse02 exampleResponse02 = exampleServiceBlockingStub.example02(exampleRequest02);
            String message = exampleResponse02.getMessage();
            System.out.println("message: " + message);

            managedChannel.shutdown();

        }else{
            System.out.println("Channel creation failed.");
        }


    }

}
