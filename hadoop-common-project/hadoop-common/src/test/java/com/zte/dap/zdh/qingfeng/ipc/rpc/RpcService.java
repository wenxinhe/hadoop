package com.zte.dap.zdh.qingfeng.ipc.rpc;

import com.zte.dap.zdh.qingfeng.ipc.rpc.protobuf.RpcServiceProtos;
import org.apache.hadoop.ipc.ProtocolInfo;

/**
 * Created by hewenxin on 17-8-11.
 */
@ProtocolInfo(protocolName = "org.apache.hadoop.ipc.TestRpcBase$TestRpcService",
    protocolVersion = 1)
public interface RpcService extends RpcServiceProtos.ProtobufRpcProto.BlockingInterface {
}
