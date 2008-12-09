package org.simpleframework.xml.core;

import java.lang.reflect.Method;
import java.util.Map;

class Function {
   
   private final Method method;
   private final boolean contextual;
   
   public Function(Method method) {
      this(method, false);
   }
   
   public Function(Method method, boolean contextual) {
      this.contextual = contextual;
      this.method = method;
   }
   
   public Object call(Context context, Object source) throws Exception {
      Session session = context.getSession();
      Map table = session.getMap();
      
      if(source == null) {
         return null;
      }
      if(contextual) {              
         return method.invoke(source, table);           
      }
      return method.invoke(source);
   }

}
