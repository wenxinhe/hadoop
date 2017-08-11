package com.zte.dap.zdh.qingfeng.ipc.rpc;

import com.google.protobuf.BlockingService;
import com.google.protobuf.ServiceException;
import com.zte.dap.zdh.qingfeng.ipc.rpc.protobuf.Protos;
import com.zte.dap.zdh.qingfeng.ipc.rpc.protobuf.RpcServiceProtos;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtobufRpcEngine;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.net.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by hewenxin on 17-8-11.
 */
public class Main {

  private final static Logger LOG = LoggerFactory.getLogger(Main.class);

  private final static String ADDRESS = "0.0.0.0";
  private final static int PORT = 0;
  private static RPC.Server server;

  public static void main(String[] args) throws IOException, ServiceException {
    Configuration conf = new Configuration();
    // Set RPC engine to protobuf RPC engine
    RPC.setProtocolEngine(conf, RpcService.class, ProtobufRpcEngine.class);

    try {
      // Create server side implementation
      BlockingService service = RpcServiceProtos.ProtobufRpcProto
          .newReflectiveBlockingService(new RpcServicePBImpl());

      // Get RPC server for server side implementation
      RPC.Builder builder = new RPC.Builder(conf)
          .setProtocol(RpcService.class)
          .setInstance(service).setBindAddress(ADDRESS).setPort(PORT);

      // Setup Server
      server = builder.build();
      server.start();

      InetSocketAddress serverAddr = NetUtils.getConnectAddress(server);

      // Get Client
      RpcService client = RPC.getProxy(RpcService.class, 0, serverAddr, conf);

      // Call
      Protos.EchoRequestProto echoRequest = Protos.EchoRequestProto.newBuilder()
          .setMessage("hello").build();
      Protos.EchoResponseProto echoResponse = client.echo(null, echoRequest);
      LOG.warn(echoResponse.getMessage());
    } finally {
      server.stop();
    }
  }
}
