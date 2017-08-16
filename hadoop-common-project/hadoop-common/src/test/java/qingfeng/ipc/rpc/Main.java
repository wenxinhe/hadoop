package qingfeng.ipc.rpc;

import com.google.protobuf.BlockingService;
import com.google.protobuf.ServiceException;
import org.apache.hadoop.util.Tool;
import qingfeng.ipc.rpc.protobuf.Protos;
import qingfeng.ipc.rpc.protobuf.RpcServiceProtos;
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
public class Main implements Tool {

  private final static Logger LOG = LoggerFactory.getLogger(Main.class);

  private final String ADDRESS = "0.0.0.0";
  private final int PORT = 0;
  private RPC.Server server;
  private Configuration conf;

  public static void main(String[] args) throws IOException, ServiceException {
    Main main = new Main();
    main.setConf(new Configuration());
    main.run(args);
  }

  @Override
  public int run(String[] args) throws IOException, ServiceException {
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
}
