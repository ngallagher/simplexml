package org.simpleframework.xml.stream;

import java.util.Iterator;

class EndEvent implements NodeEvent {
   
   private final String name;
   
   public EndEvent(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public String getPrefix() {
      return null;
   }

   public String getReference() {
      return null;
   }

   public String getValue() {
      return null;
   }

   public boolean isEnd() {
      return true;
   }

   public boolean isStart() {
      return false;
   }

   public boolean isText() {
      return false;
   }

   public Iterator<Attribute> iterator() {
      return null;
   }

}
