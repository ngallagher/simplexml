package org.simpleframework.xml.benchmark.asm.accessor;

import java.io.File;

public class ASMClassLoader extends ClassLoader {
   
   private final byte[] classData;
   private final String name;
   
   public ASMClassLoader(String name, byte[] data) {
      this.name = name;
      this.classData = data;
   }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
       if(name.equals(this.name)) {
          return defineClass(name, classData, 0, classData.length);
       }
        Class<?> clazz = findLoadedClass(name);
        if (clazz == null) {
            clazz = findSystemClass(name);
            if (clazz == null) {
                String fileName = name.replace('.', File.separatorChar) + ".class";
                clazz = defineClass(name, classData, 0, classData.length);
            }
        }
        return clazz;
    }
}
