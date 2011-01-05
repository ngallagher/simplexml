package com.rbsfm.plugin.build.rpc;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
public class RequestTest{
   @Test
   public void process() throws Exception{
      TestContainer container = new TestContainer();
      Connection connection = new SocketConnection(container);
      InetSocketAddress address = new InetSocketAddress(9999);
      TestBuilder builder = new TestBuilder();
      TestListener listener = new TestListener();
      Request request = new Request(builder, listener, true);
      connection.connect(address);
      request.execute(Method.POST);
      listener.join();
      container.validate();
      listener.validate();
      connection.close();
   }
   private class TestListener implements ResponseListener{
      private CountDownLatch latch;
      private Throwable error;
      private String status;
      public TestListener(){
         this.latch = new CountDownLatch(1);
      }
      public void join() throws InterruptedException{
         latch.await();
      }
      public void exception(Throwable cause){
         latch.countDown();
         error = cause;
      }
      public void success(String message){        
         latch.countDown();
         status = message;
      }
      public void validate(){
         assertEquals(status, 200);
         assertNull(error);
      }
   }
   private class TestContainer implements Container{
      private String path;
      private String body;
      public void handle(org.simpleframework.http.Request request, Response response){
         try{
            PrintStream stream = response.getPrintStream();
            response.set("Content-Type", "text/plain");
            body = request.getContent();
            path = request.getPath().getPath();
            stream.println("SUCCESS");
            stream.close();
         }catch(Exception e){
            e.printStackTrace();
         }
      }
      public void validate(){
         assertEquals(body, "STATUS");
         assertEquals(path, "/index.html");
      }
   }
   private class TestBuilder implements RequestBuilder{
      public void address(StringBuilder builder){
         builder.append("http://localhost:9999/index.html");
      }
      public void body(StringBuilder builder){
         builder.append("STATUS");
      }
      public void header(Map<String,String> header){
         header.put("Host", "localhost:9999");
         header.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.9.1.8) Gecko/20100202 Firefox/3.5.8");
         header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,");
         header.put("Accept-Language", "en-gb,en;q=0.5");
         header.put("Accept-Encoding", "gzip,deflate");
         header.put("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
         header.put("Content-Type", "text/x-gwt-rpc; charset=utf-8");
         header.put("Referer", "http://localhost:9999/ProjectAssemblyServer/C35FAD9739E7C3D9CDB7FE7B7BD68DCD.cache.html");
         header.put("Pragma", "no-cache");
         header.put("Cache-Control", "no-cache");
      }
   }
}
