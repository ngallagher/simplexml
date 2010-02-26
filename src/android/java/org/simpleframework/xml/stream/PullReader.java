package org.simpleframework.xml.stream;

import org.xmlpull.v1.XmlPullParser;

class PullReader implements EventReader {

   private XmlPullParser parser;
   private NodeEvent peek;
   
   public PullReader(XmlPullParser parser) {
      this.parser = parser;
   }
   
   public NodeEvent next() throws Exception {
      NodeEvent next = peek;

      if(next == null) {
         int event = parser.next();      
      
         if(event == XmlPullParser.START_TAG) {
            String name = parser.getName();
            ElementEvent node = new ElementEvent(name, null);
            int count = parser.getAttributeCount();
            
            for(int i = 0; i < count; i++) {
               String att = parser.getAttributeName(i);
               String val= parser.getAttributeValue(i);
               node.add(new Attribute(att, val));
            }
            next = node;
         }else if(event == XmlPullParser.TEXT) {
            String text = parser.getText();
            next = new TextEvent(text);
         }else if(event == XmlPullParser.END_TAG) {
            next = new EndEvent();           
         }else {
            throw new IllegalStateException("Unknown node type");
         }         
      } else {
         peek = null;
      }
      return next;
   }   

   public NodeEvent peek() throws Exception {
      if(peek == null) {
         peek = next();
      }
      return peek;
   }

}
