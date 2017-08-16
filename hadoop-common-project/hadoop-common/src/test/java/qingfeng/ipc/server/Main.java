package qingfeng.ipc.server;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.ipc.Client;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.util.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by hewenxin on 17-8-10.
 */
public class Main implements Tool {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  private final String ADDRESS = "0.0.0.0";
  private final int PORT = 0;
  private final int HANDLER_COUNT = 10;
  private final int RPC_TIMEOUT = 50;
  private Configuration conf;

  public static void main(String[] args) throws IOException {
    Main main = new Main();
    main.setConf(new Configuration());
    main.run(args);
  }

  private static Client.ConnectionId getConnectionId(
      Server server, int rpcTimeout, Configuration conf) throws IOException {
    InetSocketAddress addr = NetUtils.getConnectAddress(server);
    return Client.ConnectionId.getConnectionId(
        addr, null, null, rpcTimeout, null, conf);
  }

  @Override
  public int run(String[] args) throws IOException {
    try (
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
    return 0;
  }

  @Override
  public void setConf(Configuration conf) {
    this.conf = conf;
  }

  @Override
  public Configuration getConf() {
    return conf;
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
      return new LongWritable(((LongWritable) param).get() + 1);
    }

    @Override
    public void close() throws IOException {
      stop();
    }
  }
}
