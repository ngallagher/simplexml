package com.rbsfm.plugin.build.record;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.util.buffer.ArrayBuffer;
import org.simpleframework.util.buffer.Buffer;
public class ConversationResponder implements ClientResponder {
  private final ConversationConverter converter;
  private final Response response;
  private final Request request;
  public ConversationResponder(ConversationConverter converter, Request request, Response response) {
    this.converter = converter;
    this.request = request;
    this.response = response;
  }
  public void respond(HttpMethod method) {
    try {
      OutputStream out = response.getOutputStream();
      int code = method.getStatusCode();
      String text = method.getStatusText();
      Header[] reply = method.getResponseHeaders();
      for (Header header : reply) {
        String name = header.getName();
        String value = header.getValue();
        response.add(name, value);
      }
      response.setCode(code);
      response.setText(text);
      response.setMajor(1);
      response.setMinor(1);
      InputStream responseStream = method.getResponseBodyAsStream();
      Buffer responseBody = new ArrayBuffer(1048576);
      byte[] chunk = new byte[1024];
      int count = 0;
    
      if (responseStream != null) {
        while ((count = responseStream.read(chunk)) != -1) {
          responseBody.append(chunk, 0, count);
        }
      }
      InputStream in = responseBody.getInputStream();
      while ((count = in.read(chunk)) != -1) {
        out.write(chunk, 0, count);
      }
      converter.convert(request);
      converter.convert(response, responseBody);
      response.close();
    }catch(Exception e) {
      e.printStackTrace();
    }    
  }

}
