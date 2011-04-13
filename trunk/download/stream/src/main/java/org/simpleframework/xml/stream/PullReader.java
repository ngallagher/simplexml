package org.simpleframework.xml.stream;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

import org.xmlpull.v1.XmlPullParser;

class PullReader implements EventReader {

   private XmlPullParser parser;
   private EventNode peek;
   
   public PullReader(XmlPullParser parser) {
      this.parser = parser;
   }

   public EventNode peek() throws Exception {
      if(peek == null) {
         peek = next();
      }
      return peek;
   }
   
   public EventNode next() throws Exception {
      EventNode next = peek;

      if(next == null) {
         next = read();
      } else {
         peek = null;
      }
      return next;
   }
   
   private EventNode read() throws Exception {
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
   
   private class Entry extends EventAttribute {
      
      private final XmlPullParser source;
      private final String reference;
      private final String prefix;
      private final String name;
      private final String value;
      
      public Entry(XmlPullParser source, int index) {
         this.reference = source.getAttributeNamespace(index);
         this.prefix = source.getAttributePrefix(index);
         this.value = source.getAttributeValue(index);
         this.name = source.getAttributeName(index);
         this.source = source;
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
      
      public String getReference() {
         return reference;
      }
      
      public String getPrefix() {
         return prefix;
      }
      
      public Object getSource() {
         return source;
      }
   }
   
   private static class Start extends EventElement {
      
      private final XmlPullParser source;
      private final String reference;
      private final String prefix;
      private final String name;
      private final int line;
      
      public Start(XmlPullParser source) {
         this.reference = source.getNamespace();
         this.line = source.getLineNumber();
         this.prefix = source.getPrefix();
         this.name = source.getName();
         this.source = source;     
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

      public Object getSource() {
         return source;
      }
   }
   
   private static class Text extends EventToken {
      
      private final XmlPullParser source;
      private final String text;
      
      public Text(XmlPullParser source){
         this.text = source.getText(); 
         this.source = source;
      }
      
      public String getValue(){
         return text;
      }
      
      public boolean isText() {
         return true;
      }
      
      public Object getSource() {
         return source;
      }
   } 
   
   private static class End extends EventToken {
      
      public boolean isEnd() {
         return true;
      }
   }
}
