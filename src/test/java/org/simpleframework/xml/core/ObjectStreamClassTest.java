package org.simpleframework.xml.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Method;

import junit.framework.TestCase;

public class ObjectStreamClassTest extends TestCase {
   
   public static class Tuple implements Serializable{
      private String name;
      private String value;
      public Tuple(String name, String value) {
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

}
