package org.simpleframework.xml.core;

import junit.framework.TestCase;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

public class DefaultWithParametersInGetterTest extends TestCase {

   @Root
   @Default(DefaultType.PROPERTY)
   static class DefaultTestClass {
      private int foo;
      public int getFoo() {
         return foo;
      }
      public void setFoo(int foo) {
         this.foo = foo;
      }
      public String getWithParams(int foo) {
         return "foo";
      }
   }
   
   public void testDefaultWithParameters() throws Exception{
      Persister persister = new Persister();
      DefaultTestClass type = new DefaultTestClass();
      type.foo = 100;
      persister.write(type, System.out);
   }

}
