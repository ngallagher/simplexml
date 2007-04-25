package simple.xml.graph;

import java.util.HashMap;

import simple.xml.load.Type;
import simple.xml.stream.Node;
import simple.xml.stream.NodeMap;

final class ReadGraph extends HashMap {
      
   private String label;
   
   private String key;
   
   private String mark;
   
   public ReadGraph(String label, String key, String mark) {     
      this.label = label;
      this.key = key;
      this.mark = mark;
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
      Node entry = node.remove(key);
      
      if(entry == null) {
         return getReference(field, node);
      }
      String key = entry.getValue();
      
      if(containsKey(key)) {
         throw new Exception("Value already exists");
      }
      return new NewType(field, this, key);
   }
   
   public Type getReference(Class field, NodeMap node) throws Exception {
      Node entry = node.remove(mark);
      
      if(entry == null) {
         return new ClassType(field);
      }
      String key = entry.getValue();
      Object value = get(key); 
         
      if(value == null) {        
         throw new Exception("Value does not exist");
      }
      return new ReferenceType(value);
   }
}
