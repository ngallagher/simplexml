package org.simpleframework.xml.stream;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

class ElementEvent extends ArrayList<Attribute> implements NodeEvent {
   
   private String name;
   private String value;
   
   public ElementEvent(String name, String value) {
      this.name = name;
      this.value = value;
   }
   
   public ElementEvent(Element element) {
      this.name = element.getLocalName();
      this.value = element.getNodeValue();
      this.build(element);
   }
   
   public void build(Element element){
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
      return name;
   }
   
   public String getValue() {
      return value;
   }
   
   public String getReference(){
      return null;
   }
   
   public String getPrefix(){
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
}
