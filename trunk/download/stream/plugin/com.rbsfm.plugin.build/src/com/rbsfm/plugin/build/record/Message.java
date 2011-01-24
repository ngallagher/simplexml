package com.rbsfm.plugin.build.record;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
@Root
public class Message {  
  private final @ElementList List<Header> headers;  
  private final @Element(required=false) String content;
  private final @Attribute String command; 
  public Message(@Attribute(name="command") String command, @ElementList(name="headers") List<Header> headers, @Element(name="content", required=false) String content) {
    this.headers = headers;
    this.content = content;
    this.command = command;
  }
  public String getCommand() {
    return command;
  }
  public List<Header> getHeaders() {
    return headers;
  }
  public String getContent(){
    return content;
  }
  @Root
  public static class Header {
    private final @Attribute String name;
    private final @Element(required=false) String value;
    public Header(@Attribute(name="name") String name, @Element(name="value", required=false) String value) {
      this.name = name;
      this.value = value;
    }
    public String getName(){
      return name;
    }
    public String getValue(){
      return value;
    }
  }
}
