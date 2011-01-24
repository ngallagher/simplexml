package com.rbsfm.plugin.build.record;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
@Root
public class Conversation {  
  private @ElementList List<Message> messages;
  public Conversation() {
    this.messages = new ArrayList<Message>();
  }
  public List<Message> getMessages() {
    return messages;
  }
}
