package com.rbsfm.plugin.build.rpc;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class SocketClient{
   private final String host;
   private final int port;
   public SocketClient(String host,int port) throws Exception{
      this.host=host;
      this.port=port;
   }
   public String get(String target,String body) throws Exception{
      return send(target,body,Method.GET);
   }
   public String post(String target,String body) throws Exception{
      return send(target,body,Method.POST);
   }
   private String send(String target,String body,Method method) throws Exception{
      Socket socket=new Socket(host,port);
      List<Header> header=new ArrayList<Header>();
      String length=String.valueOf(body.length());
      header.add(new Header("Connection","close"));
      header.add(new Header("Host","localhost:9999"));
      header.add(new Header("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.9.1.8) Gecko/20100202 Firefox/3.5.8"));
      header.add(new Header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,"));
      header.add(new Header("Accept-Language","en-gb,en;q=0.5"));
      header.add(new Header("Accept-Encoding","gzip,deflate"));
      header.add(new Header("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7"));
      header.add(new Header("Content-Type","text/x-gwt-rpc; charset=utf-8"));
      header.add(new Header("Referer","http://localhost:9999/ProjectAssemblyServer/C35FAD9739E7C3D9CDB7FE7B7BD68DCD.cache.html"));
      header.add(new Header("Pragma","no-cache"));
      header.add(new Header("Cache-Control","no-cache"));
      header.add(new Header("Content-Length",length));
      byte[] request=build(target,header,body,method);
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
   private byte[] build(String target,List<Header> list,String body,Method method) throws Exception{
      StringBuilder builder=new StringBuilder();
      builder.append(String.format("%s %s HTTP/1.0\r\n",method.name(),target));
      for(Header header:list){
         builder.append(header);
         builder.append("\r\n");
      }
      builder.append("\r\n");
      builder.append(body);
      return builder.toString().getBytes("ISO-8859-1");
   }
   private static class Header{
      private final String name;
      private final String value;
      public Header(String name,String value){
         this.name=name;
         this.value=value;
      }
      public String toString(){
         return String.format("%s: %s",name,value);
      }
   }
   private static enum Method {
      POST, GET
   }
}
