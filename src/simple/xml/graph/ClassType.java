package simple.xml.graph;

import java.lang.reflect.Constructor;

import simple.xml.load.Type;

final class ClassType implements Type {

   private Class type;
   
   public ClassType(Class type) {
      this.type = type;
   }
   
   public Object getInstance() throws Exception {     
      return getInstance(type);
   }
   
   private Object getInstance(Class type) throws Exception {
      Constructor method = type.getDeclaredConstructor();      

      if(!method.isAccessible()) {
         method.setAccessible(true);              
      }
      return method.newInstance();
   }

   public Class getType() {      
      return type;
   }

   public boolean isReference() {      
      return false;
   }

}
