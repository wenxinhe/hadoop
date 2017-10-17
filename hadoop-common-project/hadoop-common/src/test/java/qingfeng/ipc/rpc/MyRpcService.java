package qingfeng.ipc.rpc;

import qingfeng.ipc.rpc.protobuf.MyRpcServiceProtos;
import org.apache.hadoop.ipc.ProtocolInfo;

/**
 * Created by hewenxin on 17-8-11.
 */
@ProtocolInfo(protocolName = "MyRpcServiceProtos",
    protocolVersion = 1)
public interface MyRpcService extends MyRpcServiceProtos.MyProtobufRpcProto.BlockingInterface {
}
