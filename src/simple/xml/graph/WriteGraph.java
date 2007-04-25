package simple.xml.graph;

import java.util.IdentityHashMap;
import simple.xml.stream.NodeMap;

final class WriteGraph extends IdentityHashMap<Object, String> {
   
   private String label;
   
   private String key;
   
   private String mark;
   
   public WriteGraph(String label, String key, String mark) {
      this.label = label;
      this.key = key;
      this.mark = mark;
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
      String name = get(value);
      
      if(name != null) {
         put(mark, name);
         return true;
      } 
      String unique = getKey();      
      
      node.put(key, unique);
      put(value, unique);
      
      return false;     
   }
   
   private String getKey() {      
      return String.valueOf(size());      
   }

}
