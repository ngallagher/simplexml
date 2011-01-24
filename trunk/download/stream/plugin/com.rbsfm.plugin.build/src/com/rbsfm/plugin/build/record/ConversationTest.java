package com.rbsfm.plugin.build.record;
import static org.junit.Assert.assertEquals;
import java.io.StringReader;
import java.util.List;
import org.junit.Test;
import org.simpleframework.xml.core.Persister;
public class ConversationTest {
  private static final String SOURCE = 
  "<conversation>\n" +
  " <messages>\n"+
  "   <message command='GET / HTTP/1.1'>\n" +
  "      <headers>\n"+
  "         <header name='Content-Length'>\n"+
  "            <value>1024</value>\n"+
  "         </header>\n"+
  "         <header name='Content-Type'>\n"+
  "            <value>text/plain</value>\n"+
  "         </header>\n"+
  "         <header name='Connection'>\n"+
  "            <value>keep-alive</value>\n"+
  "         </header>\n"+  
  "      </headers>\n" +  
  "   </message>\n" +  
  " </messages>"+
  "</conversation>";
  @Test
  public void parse() throws Exception{
     StringReader reader = new StringReader(SOURCE);
     Persister persister = new Persister();
     Conversation conversation = persister.read(Conversation.class, reader);
     List<Message> message = conversation.getMessages();
     assertEquals(message.get(0).getCommand(), "GET / HTTP/1.1");
     assertEquals(message.get(0).getContent(), null);
     assertEquals(message.get(0).getHeaders().get(0).getName(), "Content-Length");
     assertEquals(message.get(0).getHeaders().get(0).getValue(), "1024");
     assertEquals(message.get(0).getHeaders().get(1).getName(), "Content-Type");
     assertEquals(message.get(0).getHeaders().get(1).getValue(), "text/plain");
     assertEquals(message.get(0).getHeaders().get(2).getName(), "Connection");
     assertEquals(message.get(0).getHeaders().get(2).getValue(), "keep-alive");
  }
}
