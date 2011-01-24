package com.rbsfm.plugin.build.record;
import java.io.File;
import org.simpleframework.xml.core.Persister;
public class ConversationMarshaller extends Thread {
  private final Conversation conversation;
  private final Persister persister;
  private final File result;
  public ConversationMarshaller(Conversation conversation, File result) throws Exception {
    this.persister = new Persister();
    this.conversation = conversation;
    this.result = result;
  }
  public void run() {
    try {
      while(true) {
        try {
          Thread.sleep(1000);          
          persister.write(conversation, result);
        } catch(Exception e) {
          e.printStackTrace();        
        }
      }
    }catch(Exception e) {
      e.printStackTrace();
    }
  }
}
