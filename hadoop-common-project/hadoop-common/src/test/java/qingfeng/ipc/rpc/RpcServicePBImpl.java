package qingfeng.ipc.rpc;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import qingfeng.ipc.rpc.protobuf.Protos;

/**
 * Created by hewenxin on 17-8-11.
 */
public class RpcServicePBImpl implements RpcService {
  @Override
  public Protos.EmptyResponseProto ping(RpcController controller, Protos.EmptyRequestProto request) throws ServiceException {
    return Protos.EmptyResponseProto.newBuilder().build();
  }

  @Override
  public Protos.EchoResponseProto echo(RpcController controller, Protos.EchoRequestProto request) throws ServiceException {
    return Protos.EchoResponseProto.newBuilder().setMessage(
        request.getMessage())
        .build();
  }
}
