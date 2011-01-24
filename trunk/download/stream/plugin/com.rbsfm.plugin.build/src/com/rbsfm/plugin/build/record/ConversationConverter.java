package com.rbsfm.plugin.build.record;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.util.buffer.Buffer;
import com.rbsfm.plugin.build.record.Message.Header;
public class ConversationConverter {
  private Conversation conversation;
  public ConversationConverter(Conversation conversation){
    this.conversation = conversation;
  }
  public synchronized void convert(Request request) throws Exception {
    String command = String.format("%s %s HTTP/1.1", request.getMethod(), request.getPath());
    String body = request.getContent();
    List<Header> headers = new ArrayList<Header>();
    List<String> names = request.getNames();
    for(String name : names) {
      String value = request.getValue(name);
      headers.add(new Header(name, value));    
    }
    conversation.getMessages().add(new Message(command, headers, body));
  }
  public synchronized void convert(Response response, Buffer buffer) throws Exception {
    String command = String.format("HTTP/1.1 %s %s", response.getCode(), response.getText());
    String body = buffer.encode();
    List<Header> headers = new ArrayList<Header>();
    List<String> names = response.getNames();
    for(String name : names) {
      String value = response.getValue(name);
      headers.add(new Header(name, value));    
    }
    conversation.getMessages().add(new Message(command, headers, body));
  }  
}
