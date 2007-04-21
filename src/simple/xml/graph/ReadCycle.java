package simple.xml.graph;

import java.util.HashMap;
import java.util.Map;

import simple.xml.load.Type;
import simple.xml.stream.Node;
import simple.xml.stream.NodeMap;

public class ReadCycle {
   
   private Map map;
   
   private String identity;
   
   private String reference;
   
   private String label;
  
   public ReadCycle(String label, String identity, String reference) {
      this.map = new HashMap();
      this.reference = reference;
      this.identity = identity;
      this.label = label;
   }

   public Type getElement(Class field, NodeMap node) throws Exception {
      Node entry = node.remove(label);
         
      if(entry != null) {
         String name = entry.getValue();
         field = Class.forName(name);
      }         
      return getInstance(field, node); 
   }
   
   public Type getInstance(Class field, NodeMap node) throws Exception {
      Node entry = node.remove(identity);
      
      if(entry == null) {
         return getReference(field, node);
      }
      String key = entry.getValue();
      
      if(map.containsKey(key)) {
         throw new Exception("Value already exists");
      }
      return new NewType(field, map, key);
   }
   
   public Type getReference(Class field, NodeMap node) throws Exception {
      Node entry = node.remove(reference);
      
      if(entry == null) {
         return new ClassType(field);
      }
      String key = entry.getValue();
      Object value = map.get(key); 
         
      if(value == null) {        
         throw new Exception("Value does not exist");
      }
      return new ReferenceType(value);
   }
}
