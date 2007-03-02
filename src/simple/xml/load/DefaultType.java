
package simple.xml.load;

import java.lang.reflect.Constructor;

final class DefaultType implements Type {

   private Class type;

   public DefaultType(Class type) {
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
}
