package org.simpleframework.xml.stream;

import java.util.Iterator;

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

class StartEvent implements NodeEvent {
   
   private StartElement element;
   
   public StartEvent(XMLEvent event) {
      this.element = event.asStartElement();
   }

   public String getName() {
      return element.getName().getLocalPart();           
   }

   public String getPrefix() {
      return element.getName().getPrefix();
   }
   
   public String getReference() {
      return element.getName().getNamespaceURI();
   }
   
   public String getValue() {
      return null;
   }

   public boolean isEnd() {
      return false;
   }

   public boolean isStart() {
      return true;
   }

   public boolean isText() {
      return false;
   }

   public Iterator<Attribute> iterator() {
      return null;
   }
}
