package org.simpleframework.http.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.simpleframework.util.buffer.Allocator;
import org.simpleframework.util.buffer.Buffer;
import org.simpleframework.util.buffer.FileAllocator;

public class Client {
   
   private ExecutorService executor;
   private Allocator allocator;
   private int timeout;
   
   public Client(int count, int timeout) throws IOException {
      this.executor = Executors.newFixedThreadPool(count);
      this.allocator = new FileAllocator();
      this.timeout = timeout;
   }
   
   public Buffer execute(InetSocketAddress address, byte[] request) throws Exception {
      Buffer response = allocator.allocate();
      String host = address.getHostName();
      int port = address.getPort();
      Socket socket = new Socket(host, port);
      SendTask sender = new SendTask(socket, request);
      InputStream in = socket.getInputStream();
      byte[] buffer = new byte[2048];
      int count = 0;
      
      socket.setSoTimeout(timeout);
      executor.execute(sender);
      
      while((count = in.read(buffer)) != -1){
         response.append(buffer, 0, count);
      }      
      socket.close();
      return response;
   }

   public void shutdown() throws Exception {
      executor.shutdown();
      executor.awaitTermination(5000L, TimeUnit.MILLISECONDS);      
   }

   
   private class SendTask implements Runnable {
      
      private final Socket socket;
      private final byte[] request;
      
      public SendTask(Socket socket, byte[] request) {
         this.socket = socket;
         this.request = request;  
      }     
      
      public void run() {
         try {
            OutputStream out = socket.getOutputStream();            
            out.write(request);                       
         } catch(IOException e) {
            e.printStackTrace();
         }
      }
   }
}
