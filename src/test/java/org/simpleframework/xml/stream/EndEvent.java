package org.simpleframework.xml.stream;

import java.util.Iterator;

class EndEvent implements NodeEvent {

   public String getName() {
      return null;
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
