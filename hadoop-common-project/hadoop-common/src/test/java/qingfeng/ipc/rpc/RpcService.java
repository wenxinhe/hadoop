package qingfeng.ipc.rpc;

import qingfeng.ipc.rpc.protobuf.RpcServiceProtos;
import org.apache.hadoop.ipc.ProtocolInfo;

/**
 * Created by hewenxin on 17-8-11.
 */
@ProtocolInfo(protocolName = "org.apache.hadoop.ipc.TestRpcBase$TestRpcService",
    protocolVersion = 1)
public interface RpcService extends RpcServiceProtos.ProtobufRpcProto.BlockingInterface {
}
