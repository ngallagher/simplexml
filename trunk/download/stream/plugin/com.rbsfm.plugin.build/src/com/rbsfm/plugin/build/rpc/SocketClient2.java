package com.rbsfm.plugin.build.rpc;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class SocketClient2 {
   private final ResponseListener listener;
   private final RequestBuilder builder;
   public SocketClient2(RequestBuilder builder,ResponseListener listener){
      this.listener=listener;
      this.builder=builder;
   }
   public String send(Method method) throws Exception{     
      Map<String,String> header = new HashMap<String,String>();
      StringBuilder address = new StringBuilder();
      StringWriter body =new StringWriter();
      builder.build(address, header, body);
      URI target=new URI(address.toString());     
      Socket socket=new Socket(target.getHost(),target.getPort());
      String content=body.toString();
      byte[] request=build(target.getPath(),header,content,method);
      write(socket,request);
      return read(socket);
   }
   private String read(Socket socket) throws Exception{
      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
      InputStream stream=socket.getInputStream();
      while(true){
         int octet=stream.read();
         if(octet==-1)
            break;
         buffer.write(octet);
      }
      return buffer.toString("ISO-8859-1");
   }
   private void write(Socket socket,byte[] request) throws Exception{
      OutputStream stream=socket.getOutputStream();
      stream.write(request);
   }
   private byte[] build(String target,Map<String,String> header,String body,Method method) throws Exception{
      StringBuilder builder=new StringBuilder();
      builder.append(String.format("%s %s HTTP/1.0\r\n",method.name(),target));
      for(String name:header.keySet()){
         if(!isIgnore(name)) {
            builder.append(header);
            builder.append("\r\n");
         }
      }
      builder.append("Content-Length:");
      builder.append(body.length());
      builder.append("\r\n\r\n");
      builder.append(body);
      return builder.toString().getBytes("ISO-8859-1");
   }
   private boolean isIgnore(String name){
      return name.equalsIgnoreCase("Content-Length") ||
              name.equalsIgnoreCase("Transfer-Encoding") ||
              name.equalsIgnoreCase("Connection");
   }
}
