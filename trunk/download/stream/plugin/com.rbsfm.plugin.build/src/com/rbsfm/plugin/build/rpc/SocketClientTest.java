package com.rbsfm.plugin.build.rpc;
import org.junit.Test;
public class SocketClientTest{
   @Test
   public void get() throws Exception{
      SocketClient client =new SocketClient("www.google.com",80);
      client.get("/","");
   }
}
