package com.rbsfm.plugin.build.record;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.simpleframework.http.Address;
import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.parse.AddressParser;
public class ProxyClient implements Client{
  public static final String GET = "GET";
  public static final String POST = "POST";
  public static final String HOST = "Host";
  public static final String IF_MODIFIED_SINCE = "If-Modified-Since";  
  private Address address;
  private String proxy;
  public ProxyClient(String proxy) {
    this.address = new AddressParser(proxy);
    this.proxy = proxy;
  }
  public void handle(ClientResponder responder, Request request) throws Exception{
    String method = request.getMethod();    
    if(method.equalsIgnoreCase(GET)) {
      forwardGet(responder, request);
    } else if(method.equalsIgnoreCase(POST)) {
      forwardPost(responder, request);
    } else {
      forwardGet(responder, request);
    }
  }
  private void forwardPost(ClientResponder responder, Request request) throws Exception {
    HttpClient client = new HttpClient();
    PostMethod method = new PostMethod(proxy);
    List<String> names = request.getNames();
    Path path = request.getPath();    
    method.setPath(path.getPath());
    
    for(String name : names) {
       method.addRequestHeader(name, request.getValue(name));
    }            
    try {
       InputStream body = request.getInputStream();
       method.setRequestEntity(new InputStreamRequestEntity(body)); 
       client.executeMethod(method);
       method.getResponseBody();
    }finally {
       method.releaseConnection();
    }
    responder.respond(method);
  }
  private void forwardGet(ClientResponder responder, Request request) throws Exception {
    HttpClient client = new HttpClient();
    GetMethod method = new GetMethod(proxy);
    List<String> names = request.getNames();
    Path path = request.getPath();
    method.setPath(path.getPath());
    
    for(String name : names) {
       if(!name.equalsIgnoreCase(IF_MODIFIED_SINCE)) {
         if(!name.equalsIgnoreCase(HOST)) {
            method.addRequestHeader(name, request.getValue(name));
         }else {
            method.addRequestHeader(name, address.getDomain() + ":" +address.getPort());
         }
       }
    }            
    try {
       client.executeMethod(method);
       method.getResponseBody();
    }finally {
       method.releaseConnection();
    }
    responder.respond(method);
  }
}
