package org.simpleframework.xml.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import junit.framework.TestCase;
import sun.reflect.ReflectionFactory;

public class ObjectStreamClassTest extends TestCase {
   
   public static class Tuple implements Serializable{
      private String name;
      private String value;
      public Tuple(String name, String value) {
         this.name = name;
         this.value = value;
      }
   }
   public static class Example {
      private String name;
      private String value;
      public Example(String name, String value) {
         this.name = name;
         this.value = value;
      }
   }
   public void testObjectStreamTest() throws Exception {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ObjectOutputStream oout = new ObjectOutputStream(out);
      Tuple tuple = new Tuple("a", "b");
      
      oout.writeObject(tuple);
      byte[] array = out.toByteArray();
      ByteArrayInputStream in = new ByteArrayInputStream(array);
      ObjectInputStream oin = new ObjectInputStream(in);
      
      tuple = (Tuple)oin.readObject();
   }
   
   public void testInstantiation() throws Exception {
      ObjectStreamClass factory = ObjectStreamClass.lookup(Tuple.class);
      Method method = ObjectStreamClass.class.getDeclaredMethod("newInstance");
      
      if(!method.isAccessible()) {
         method.setAccessible(true);
      }
      Tuple tuple = (Tuple)method.invoke(factory);
      
      assertEquals(tuple.name, null);
      assertEquals(tuple.value, null);
   }
   
   public void testInstantiationWithSerialization() throws Exception {
      Constructor constructor = getSerializableConstructor(Example.class);
      
      if(!constructor.isAccessible()) {
         constructor.setAccessible(true);
      }
      Example example = (Example)constructor.newInstance(null);
      
      assertEquals(example.name, null);
      assertEquals(example.value, null);
   }
   
   private static Constructor getSerializableConstructor(Class cl) throws Exception {
      Constructor constructors = ReflectionFactory.class.getDeclaredConstructor();
      
      if(!constructors.isAccessible()) {
         constructors.setAccessible(true);
      }
      ReflectionFactory reflFactory = (ReflectionFactory)constructors.newInstance();
      Class initCl = Object.class;

      try {
          Constructor cons = initCl.getDeclaredConstructor(new Class[0]);
          int mods = cons.getModifiers();
          if ((mods & Modifier.PRIVATE) != 0 ||
         ((mods & (Modifier.PUBLIC | Modifier.PROTECTED)) == 0 &&
          !packageEquals(cl, initCl)))
          {
         return null;
          }
          cons = reflFactory.newConstructorForSerialization(cl, cons);
          cons.setAccessible(true);
          return cons;
      } catch (NoSuchMethodException ex) {
          return null;
      }
   }
   
   private static boolean packageEquals(Class cl1, Class cl2) {
      return (cl1.getClassLoader() == cl2.getClassLoader() &&
         getPackageName(cl1).equals(getPackageName(cl2)));
       }

       /**
        * Returns package name of given class.
        */
       private static String getPackageName(Class cl) {
      String s = cl.getName();
      int i = s.lastIndexOf('[');
      if (i >= 0) {
          s = s.substring(i + 2);
      }
      i = s.lastIndexOf('.');
      return (i >= 0) ? s.substring(0, i) : "";
       }

}
