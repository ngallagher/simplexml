package simple.xml.graph;

import simple.xml.stream.NodeMap;

final class WriteGraph {
   
   private ObjectGraph graph;
   
   private String label;
   
   private String key;
   
   private String mark;
   
   public WriteGraph(String label, String key, String mark) {
      this.graph = new ObjectGraph();
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
      String name = graph.get(value);
      
      if(name != null) {
         node.put(mark, name);
         return true;
      } 
      String unique = getKey();      
      
      node.put(key, unique);
      graph.put(value, unique);
      
      return false;     
   }
   
   private String getKey() {
      int size = graph.size();
      
      return String.valueOf(size);      
   }

}
