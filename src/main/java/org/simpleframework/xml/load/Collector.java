package org.simpleframework.xml.load;

import java.util.Collection;
import java.util.HashMap;

class Collector extends HashMap<String, Pointer> {

   private Source root;
   
   public Collector(Source root) {
      this.root = root;
   }
   
   public void put(Label label, Object value) throws Exception {
      String name = label.getName();
      
      put(name, new Pointer(label, value));
   }
   
   public void commit(Object source) throws Exception {
      Collection<Pointer> set = values();
      
      for(Pointer entry : set) { 
         Contact contact = entry.getContact();
         Object value = entry.getValue();
                
         contact.set(source, value);
      }
   }   
}

