package org.simpleframework.xml.stream;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

import javax.xml.stream.events.Attribute;

import org.xmlpull.v1.XmlPullParser;

class PullReader implements EventReader {

   private XmlPullParser parser;
   private NodeEvent peek;
   
   public PullReader(XmlPullParser parser) {
      this.parser = parser;
   }

   public NodeEvent peek() throws Exception {
      if(peek == null) {
         peek = next();
      }
      return peek;
   }
   
   public NodeEvent next() throws Exception {
      NodeEvent next = peek;

      if(next == null) {
         next = read();
      } else {
         peek = null;
      }
      return next;
   }
   
   private NodeEvent read() throws Exception {
      int event = parser.next();      
      
      if(event == START_TAG){
         return start();
      }
      if(event == TEXT) {
         return text();
      }
      if(event == END_TAG) {
         return end();
      }
      return read();
   }

   private Text text() throws Exception {
      return new Text(parser);
   }
   
   private Start start() throws Exception {
      Start event = new Start(parser);
      
      if(event.isEmpty()) {
         return populate(event);
      }
      return event;
   }
   
   private Start populate(Start event) throws Exception {
      int count = parser.getAttributeCount();
      
      for(int i = 0; i < count; i++) {
         Entry entry = attribute(i);

         if(!entry.isReserved()) {
            event.add(entry);
         }
      }
      return event;
   }
   
   private Entry attribute(int index) throws Exception {
      return new Entry(parser, index);
   }
   
   private End end() throws Exception {
      return new End();
   } 
   
   private class Entry extends NodeAttribute {
      
      private final String name;
      private final String value;
      
      public Entry(XmlPullParser node, int index) {
         this.value = node.getAttributeValue(index);
         this.name = node.getAttributeName(index);
      }
      
      public String getName() {
         return name;
      }
      
      public String getValue() {
         return value;
      }
      
      public boolean isReserved() {
         return false;
      }
   }
   
   private static class Start extends NodeStart {
      
      private final String reference;
      private final String prefix;
      private final String name;
      private final int line;
      
      public Start(XmlPullParser parser) {
         this.reference = parser.getNamespace();
         this.line = parser.getLineNumber();
         this.prefix = parser.getPrefix();
         this.name = parser.getName();
      }
      
      public int getLine() {
         return line;
      }
      
      public String getName() {
         return name;
      }
      
      public String getReference() {
         return reference;
      }
      
      public String getPrefix() {
         return prefix;
      }
   }
   
   private static class Text extends NodeValue {
      
      private final String text;
      
      public Text(XmlPullParser parser){
         this.text = parser.getText();   
      }
      
      public String getValue(){
         return text;
      }
      
      public boolean isText() {
         return true;
      }
   } 
   
   private static class End extends NodeValue {
      
      public boolean isEnd() {
         return true;
      }
   }
}
