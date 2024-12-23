# Client
```java
public class client01 {

    public static void main(String[] args) {
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
                builder.setYear("1990");
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

```
# Server
```java
public class ExampleServiceImpl extends ExampleServiceGrpc.ExampleServiceImplBase{

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
```

v`ExampleServiceGrpc.ExampleServiceStub exampleServiceStub = ExampleServiceGrpc.newStub(managedChannel);
`
- onNext(): 用來回傳資料
- onError(): 用來回傳錯誤
- onCompleted(): 用來回傳完成


`.proto`


# Output

























