package org.simpleframework.xml.stream;

import java.util.ArrayList;

import org.w3c.dom.Text;

class TextEvent extends ArrayList<Attribute> implements NodeEvent {

   private final Text text;
   
   public TextEvent(Text text) {
      this.text = text;
   }
   
   public String getName() {
      return null;
   }
   
   public String getValue() {
      return text.getData();
   }
   
   public String getReference() {
      return null;
   }
   
   public String getPrefix() {
      return null;
   }
   
   public boolean isEnd() {
      return false;
   }

   public boolean isStart() {
      return false;
   }

   public boolean isText() {
      return true;
   }
}
