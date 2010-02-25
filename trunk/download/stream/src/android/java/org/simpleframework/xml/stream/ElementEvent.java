package org.simpleframework.xml.stream;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

class ElementEvent extends ArrayList<Attribute> implements NodeEvent {
   
   private final Element element;
   
   public ElementEvent(Element element) {
      this.element = element;
      this.build();
   }
   
   public void build(){
      NamedNodeMap list = element.getAttributes();
      int length = list.getLength();
      for(int i = 0; i < length; i++) {
         String total = list.item(i).getNodeName();
         if(!total.startsWith("xmlns")) { // xml.* is reserved so ignore
            String name = list.item(i).getLocalName();
            String value = list.item(i).getNodeValue();
            Attribute attribute = new Attribute(name, value);
            add(attribute);
         }
      }
   }
   
   public String getName() {
      return element.getLocalName();
   }
   
   public String getValue() {
      return element.getNodeValue();
   }
   
   public String getReference(){
      return element.getNamespaceURI();
   }
   
   public String getPrefix(){
      return element.getPrefix();
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
}
