package org.simpleframework.xml;

import java.lang.reflect.Method;

import junit.framework.TestCase;

public class TestScanner {
   public void execute(TestCase test) throws Exception {
      Class type = test.getClass();
      Method[] methods = type.getDeclaredMethods();
      boolean setUpExists = true;
      boolean tearDownExists = true;
      for(Method method : methods) {
         Class[] types = method.getParameterTypes();
         if(types.length == 0) {
            String name = method.getName();
            if(name.startsWith("test")) {
               if(setUpExists) {
                  try {
                     Method setUp = type.getDeclaredMethod("setUp");
                     setUp.setAccessible(true);
                     setUp.invoke(test);
                  } catch(Exception e) {
                     setUpExists = false;
                  }
               }
               if(tearDownExists) {
                  try {            
                     Method tearDown = type.getDeclaredMethod("tearDown");
                     tearDown.setAccessible(true);
                     tearDown.invoke(test);
                  }catch(Throwable e) {
                     tearDownExists = false;
                  }
               }
               method.setAccessible(true);
               method.invoke(test);
               System.gc();
               Thread.sleep(10);
            }
         }
      }
   }
}
