package server;


import com.example.grpc.ExampleProto;
import com.example.grpc.ExampleServiceGrpc;
import com.google.protobuf.ProtocolStringList;
import io.grpc.stub.StreamObserver;

public class ExampleServiceImpl extends ExampleServiceGrpc.ExampleServiceImplBase{

    @Override
    public void example01(ExampleProto.ExampleRequest01 request, StreamObserver<ExampleProto.ExampleResponse01> responseObserver) {
        String name = request.getName();
        String year = request.getYear();
        String message = "Hello " + name + ", you were born in " + year + ".";

        ExampleProto.ExampleResponse01.Builder builder = ExampleProto.ExampleResponse01.newBuilder();
        builder.setMessage(message);
        builder.setYear(year);
        ExampleProto.ExampleResponse01 exampleResponse01 = builder.build();

        System.out.println("Name: " + name + ", Year: " + year);
        System.out.println("This is server. Got a request from client.");
        responseObserver.onNext(exampleResponse01);
        responseObserver.onCompleted();
    }
    //--------------------------~-------------------------------------------------------------------------------

    @Override
    public void example02(ExampleProto.ExampleRequest02 request, StreamObserver<ExampleProto.ExampleResponse02> responseObserver) {
        ProtocolStringList names = request.getNameList();
        for(String name : names){
            System.out.println("Name: " + name);
        }
        System.out.println("This is server. Got a request from client.");

        ExampleProto.ExampleResponse02.Builder builder = ExampleProto.ExampleResponse02.newBuilder();
        builder.setMessage("This is server. I got your request.");
        ExampleProto.ExampleResponse02 exampleResponse02 = builder.build();

        responseObserver.onNext(exampleResponse02);
        responseObserver.onCompleted();
    }
    //---------------------------------------------------------------------------------------------------------

    @Override
    public void client2Servers(ExampleProto.ExampleRequest01 request, StreamObserver<ExampleProto.ExampleResponse01> responseObserver) {

        for(int i=0; i< 10 ; i++){
            ExampleProto.ExampleResponse01.Builder builder = ExampleProto.ExampleResponse01.newBuilder();
            builder.setMessage("收到client的訊息，現在是第" + i + "秒");
            ExampleProto.ExampleResponse01 exampleResponse01 = builder.build();

            responseObserver.onNext(exampleResponse01);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }
    //---------------------------------------------------------------------------------------------------------


    @Override
    public StreamObserver<ExampleProto.ExampleRequest01> clients2Server(StreamObserver<ExampleProto.ExampleResponse01> responseObserver) {
        return new StreamObserver<ExampleProto.ExampleRequest01>() {
            @Override
            public void onNext(ExampleProto.ExampleRequest01 exampleRequest01) {
                String name = exampleRequest01.getName();
                String year = exampleRequest01.getYear();
                System.out.println("Name: " + name + ", Year: " + year);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

                ExampleProto.ExampleResponse01.Builder builder = ExampleProto.ExampleResponse01.newBuilder();
                builder.setMessage("This is server. I got all your request.");
                ExampleProto.ExampleResponse01 exampleResponse01 = builder.build();

                responseObserver.onNext(exampleResponse01);
                responseObserver.onCompleted();
            }
        };
    }
    //---------------------------------------------------------------------------------------------------------
    @Override
    public StreamObserver<ExampleProto.ExampleRequest01> clients2Servers(StreamObserver<ExampleProto.ExampleResponse01> responseObserver) {
        return new StreamObserver<ExampleProto.ExampleRequest01>() {
            @Override
            public void onNext(ExampleProto.ExampleRequest01 exampleRequest01) {
                ExampleProto.ExampleResponse01.Builder builder = ExampleProto.ExampleResponse01.newBuilder();
                builder.setMessage("This is server. You are "+ exampleRequest01.getName() + " and you were born in " + exampleRequest01.getYear());
                builder.setYear(exampleRequest01.getYear());
                ExampleProto.ExampleResponse01 exampleResponse01 = builder.build();

                responseObserver.onNext(exampleResponse01);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("接收完所有Client的request");
                responseObserver.onCompleted();
            }
        };
    }


}
