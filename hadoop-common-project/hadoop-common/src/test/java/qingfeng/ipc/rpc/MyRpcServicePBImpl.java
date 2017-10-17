package qingfeng.ipc.rpc;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import qingfeng.ipc.rpc.protobuf.MyProtos;

/**
 * Created by hewenxin on 17-8-11.
 */
public class MyRpcServicePBImpl implements MyRpcService {
  @Override
  public MyProtos.EmptyResponseProto ping(RpcController controller, MyProtos.EmptyRequestProto request) throws ServiceException {
    return MyProtos.EmptyResponseProto.newBuilder().build();
  }

  @Override
  public MyProtos.EchoResponseProto echo(RpcController controller, MyProtos.EchoRequestProto request) throws ServiceException {
    return MyProtos.EchoResponseProto.newBuilder().setMessage(
        request.getMessage())
        .build();
  }
}
