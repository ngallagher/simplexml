package simple.xml.graph;

import java.util.IdentityHashMap;

import simple.xml.stream.NodeMap;

public class WriteCycle {
   
   private IdentityHashMap<Object, String> graph;
   
   private String identity;
   
   private String reference;
   
   private String label;
  
   public WriteCycle(String label, String identity, String reference) {
      this.graph = new IdentityHashMap<Object, String>();
      this.reference = reference;
      this.identity = identity;
      this.label = label;
   }

   public boolean setElement(Class field, Object value, NodeMap node){
      Class type = value.getClass();
      Class real = type;
      
      if(type.isArray()) {
         real = type.getComponentType();
      }
      if(type != field) {
         node.put(label, real.getName());
      }       
      return setReference(value, node);
   }
   
   private boolean setReference(Object value, NodeMap node) {
      String name = graph.get(value);
      
      if(name != null) {
         node.put(reference, name); // put existing address
         return true;
      } 
      name = String.valueOf(graph.size());
      
      node.put(identity, name);
      graph.put(value, name);
      
      return false;     
   }

}
