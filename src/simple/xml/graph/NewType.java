package simple.xml.graph;

import java.lang.reflect.Constructor;
import java.util.Map;

import simple.xml.load.Type;

final class NewType implements Type {
   
   private Class field;
   
   private String key;
   
   private Map map;
   
   public NewType(Class field, Map map, String key) {
      this.field = field;
      this.map = map;
      this.key = key;
   }
   
   public Object getInstance() throws Exception {      
      Constructor c = field.getDeclaredConstructor();
      
      if(!c.isAccessible()) {
         c.setAccessible(true);
      }
      Object o = c.newInstance();    
     
      map.put(key, o);
      return o;
   }
   
   public Class getType() {
      return field;
   }
   
   public boolean isReference() {
      return false;
   }

}
