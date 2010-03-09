package com.rbsfm.plugin.build.rpc;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
public class Request{
   private final ResponseListener listener;
   private final RequestBuilder builder;
   public Request(RequestBuilder builder,ResponseListener listener){
      this.listener=listener;
      this.builder=builder;
   }
   public void execute(Method method) throws Exception{
      Client client=new Client(method);
      client.start();
   }
   private void post(Entity entity) throws Exception{
      HttpClient client=new HttpClient();
      PostMethod method=new PostMethod();
      populate(method,entity);
      method.setRequestEntity(entity.getBody());
      try{
         client.executeMethod(method);
      }finally{
         method.releaseConnection();
      }
      respond(method);
   }
   private void get(Entity entity) throws Exception{
      HttpClient client=new HttpClient();
      GetMethod method=new GetMethod();
      populate(method,entity);
      try{
         client.executeMethod(method);
      }finally{
         method.releaseConnection();
      }
      respond(method);
   }
   private void populate(HttpMethod method,Entity entity) throws Exception{
      Map<String,String> header=entity.getHeader();
      method.setURI(entity.getURI());
      method.setPath(entity.getPath());
      for(String name:header.keySet()){
         method.setRequestHeader(name,header.get(name));
      }
   }
   private void respond(HttpMethod method) throws Exception{
      int status=method.getStatusCode();
      if(listener!=null){
         listener.success(status);
      }
   }
   private Entity build(Method method) throws Exception{
      Map<String,String> header=new HashMap<String,String>();
      StringBuilder address=new StringBuilder();
      StringBuilder body=new StringBuilder();
      if(builder!=null){
         builder.address(address);
         builder.header(header);
         builder.body(body);
      }
      String target=address.toString();
      String content=body.toString();
      return new Entity(method,header,target,content);
   }
   private class Client extends Thread{
      private final Entity entity;
      public Client(Method method) throws Exception{
         this.entity=build(method);
      }
      public void run(){
         try{
            Method method=entity.getMethod();
            switch(method){
            case POST:
               post(entity);
               break;
            case GET:
               get(entity);
               break;
            }
         }catch(Exception e){
            listener.exception(e);
         }
      }
   }
   private class Entity{
      private final Method method;
      private final Map<String,String> header;
      private final RequestEntity body;
      private final URI address;
      public Entity(Method method,Map<String,String> header,String address,String body) throws Exception{
         this.body=new StringRequestEntity(body,null,null);
         this.address=new URI(address,true);
         this.method=method;
         this.header=header;
      }
      public URI getURI(){
         return address;
      }
      public Method getMethod(){
         return method;
      }
      public String getPath(){
         return address.getEscapedPath();
      }
      public long getContentLength(){
         return body.getContentLength();
      }
      public Map<String,String> getHeader(){
         return header;
      }
      public RequestEntity getBody(){
         return body;
      }
   }
}
