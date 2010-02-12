package org.simpleframework.xml.stream;

import java.util.Iterator;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

class CharacterEvent implements NodeEvent {

   private Characters text;
   
   public CharacterEvent(XMLEvent event) {
      this.text = event.asCharacters();
   }

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
      return text.getData();
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

   public Iterator<Attribute> iterator() {
      return null;
   }
}
