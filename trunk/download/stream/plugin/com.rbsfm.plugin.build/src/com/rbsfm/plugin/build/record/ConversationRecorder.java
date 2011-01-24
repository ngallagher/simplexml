package com.rbsfm.plugin.build.record;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
/**
 * This is a recording proxy server used to record the GWT messages that
 * pass between a browser and a GWT enabled web site. Recording messages
 * is required to ensure that a message replay can be performed if
 * required for various messages.
 * 
 * @author Niall Gallagher
 */
public class ConversationRecorder implements Container {
  private final ConversationConverter converter;
  private final Client client;
  public ConversationRecorder(Conversation conversation, Client client) {
    this.converter = new ConversationConverter(conversation);
    this.client = new AsynchronousClient(client);
  }
  public void handle(Request request, Response response) {
    try {
      ClientResponder responder = new ConversationResponder(converter, request, response);
      client.handle(responder, request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public static void main(String[] list) throws Exception {
    if(list.length != 3) {
      throw new IllegalArgumentException("Usage: <proxy-address> <output-file> <listen-port>");
    }
    String proxy = list[0];
    File result = new File(list[1]);
    Integer port = Integer.parseInt(list[2]);
    Conversation conversation = new Conversation();
    ConversationMarshaller marshaller = new ConversationMarshaller(conversation, result);
    Client client = new ProxyClient(proxy);
    Container container = new ConversationRecorder(conversation, client);
    ContainerServer server = new ContainerServer(container, 2);
    Connection connection = new SocketConnection(server);
    SocketAddress address = new InetSocketAddress(port);
    
    connection.connect(address);
    marshaller.start();
  }
}
