package Interceptor;

import io.grpc.*;

public class gRPCInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {

        long start = System.currentTimeMillis();

        // 呼叫下一層的 ClientCall
        ClientCall<ReqT, RespT> listener = channel.newCall(methodDescriptor, callOptions);

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(listener) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onMessage(RespT message) {
                        long end = System.currentTimeMillis();
                        System.out.println("Time taken: " + (end - start) + "ms");
                        super.onMessage(message);
                    }
                }, headers);
            }
        };
    }
}
