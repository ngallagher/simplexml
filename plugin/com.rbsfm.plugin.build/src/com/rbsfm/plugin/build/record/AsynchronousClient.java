package com.rbsfm.plugin.build.record;
import java.util.concurrent.Executor;
import org.simpleframework.http.Request;
import org.simpleframework.util.thread.PoolExecutor;
/**
 * The <code>AsynchronousClient</code> is used to execute requests in 
 * separate threads. This ensures that if there are slow connections
 * the server does not get overloaded. The thread pool used by this 
 * can shrink and grow according to demand.
 * 
 * @author Niall Gallagher
 */
public class AsynchronousClient implements Client {
  private final Executor executor;
  private final Client client;  
  public AsynchronousClient(Client client) {
    this.executor = new PoolExecutor(ClientTask.class, 1, 10);
    this.client = client;
  }
  public void handle(ClientResponder responder, Request request) throws Exception{
    ClientTask task = new ClientTask(responder, request);
    executor.execute(task);
  }
  private class ClientTask extends Thread {
    private final ClientResponder responder;
    private final Request request;
    public ClientTask(ClientResponder responder, Request request){
      this.responder = responder;
      this.request = request;
    }
    public void run() {
      try {
        client.handle(responder, request);
      }catch(Exception e){
        e.printStackTrace();
      }
    }
  }
}
