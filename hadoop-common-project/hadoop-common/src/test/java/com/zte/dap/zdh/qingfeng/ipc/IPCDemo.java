package com.zte.dap.zdh.qingfeng.ipc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.ipc.Client;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;
import org.apache.hadoop.net.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by hewenxin on 17-8-10.
 */
public class IPCDemo {

  private static final Logger LOG = LoggerFactory.getLogger(IPCDemo.class);

  private static final String ADDRESS = "0.0.0.0";
  private static final int PORT = 0;
  private static final int HANDLER_COUNT = 10;
  private static final int RPC_TIMEOUT = 50;

  public static void main(String[] args) throws IOException {
    Configuration conf = new Configuration();
    try(
        // 创建Server
        DemoServer server = new DemoServer(
        ADDRESS, PORT, LongWritable.class, HANDLER_COUNT, conf)) {

      // 启动
      server.start();

      // 创建Client
      Client client = new Client(LongWritable.class, conf);

      // 根据Server绑定的IP创建ConnectionId
      Client.ConnectionId remoteId = getConnectionId(server, RPC_TIMEOUT, conf);

      // 根据ConnectionId发起调用
      Writable call = client.call(
          RPC.RpcKind.RPC_BUILTIN, new LongWritable(10), remoteId, null);

      LOG.warn("response: " + call);
    }
  }

  private static Client.ConnectionId getConnectionId(
      Server server, int rpcTimeout, Configuration conf) throws IOException {
    InetSocketAddress addr = NetUtils.getConnectAddress(server);
    return Client.ConnectionId.getConnectionId(
        addr, null, null, rpcTimeout, null, conf);
  }

  private static class DemoServer extends Server implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(DemoServer.class);

    protected DemoServer(String bindAddress, int port,
                         Class<? extends Writable> paramClass, int handlerCount,
                         Configuration conf) throws IOException {
      super(bindAddress, port, paramClass, handlerCount, conf);
    }

    @Override
    public Writable call(RPC.RpcKind rpcKind, String protocol, Writable param,
                         long receiveTime) throws Exception {
      LOG.warn("ipc called!");
      return new LongWritable(((LongWritable)param).get() + 1);
    }

    @Override
    public void close() throws IOException {
      stop();
    }
  }
}
