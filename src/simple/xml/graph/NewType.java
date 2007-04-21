package simple.xml.graph;

import java.util.Map;

import simple.xml.load.Type;

final class NewType implements Type {
   
   private Type type;
   
   private String key;
   
   private Map map;
   
   public NewType(Class field, Map map, String key) {
      this.type = new ClassType(field);
      this.map = map;
      this.key = key;
   }
   
   public Object getInstance() throws Exception {      
      Object value = type.getInstance();
      
      if(value != null) {
         map.put(key, value);
      }
      return value;
   }
   
   public Class getType() {
      return type.getType();
   }
   
   public boolean isReference() {
      return false;
   }

}
