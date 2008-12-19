package org.simpleframework.http.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.simpleframework.util.buffer.Buffer;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


/**
 * <test repeat='10' timeout='10000'>
 *    <connection host='localhost' port='8080'>
 *       <request method='GET' target='/path.html'>
 *          <header name='Content-Type'>text/plain</header>
 *          <header name='Content-Length'>10</header>
 *          <body>name=value&a=b</body>
 *       </request>        
 *    </connection>
 * </test>
 * 
 */
@Root
public class Test {
   
   @Element
   private ConnectionTask connection;
   
   @Attribute
   private int repeat;
   
   @Attribute
   private int timeout;
   
   public List<Buffer> execute() throws Exception {
      ExecutorService executor = Executors.newFixedThreadPool(repeat);
      Client client = new Client(repeat, timeout);

      try {
        List<Buffer> list = new ArrayList<Buffer>();
        CountDownLatch start = new CountDownLatch(repeat);
        CountDownLatch finish = new CountDownLatch(repeat);
       
        for(int i = 0; i < repeat; i++) {
          connection.execute(client);
        }
        finish.await();      
        return list;
      }finally{
        client.shutdown();              
        executor.shutdown();     
        executor.awaitTermination(5000L, TimeUnit.MILLISECONDS);
      }
   }
   
   private class ExecutionTask implements Runnable {
      
      private final List<Buffer> list;
      private final CountDownLatch start;      
      private final CountDownLatch finish;      
      private final Client client;      
      
      public ExecutionTask(CountDownLatch start, CountDownLatch finish, Client client, List<Buffer> list) {
         this.start = start;
         this.finish = finish;
         this.client = client;
         this.list = list;
      }
      
      public void run() {
         try {
            start.countDown();
            start.await();
            execute();
            finish.countDown();           
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
      
      private void execute() throws Exception {
         Buffer result = connection.execute(client);
         list.add(result);
      }
   }
   

}
