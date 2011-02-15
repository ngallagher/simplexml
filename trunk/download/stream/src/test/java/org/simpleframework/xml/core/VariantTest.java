package org.simpleframework.xml.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import junit.framework.TestCase;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

public class VariantTest extends TestCase {
   private static final String SOURCE = 
      "<variantExample>" +
      "  <a/>" +
      "  <b/>" +
      "  <c/>" +
      "</variantExample>";
   
   @Retention(RetentionPolicy.RUNTIME)
   public static @interface Variant {
      public Class[] value();
   }
   @Root(name="a")
   public static class A implements DynamicEntry {
      public String foo(){
         return "a";
      }
   }
   @Root(name="b")
   public static class B implements DynamicEntry {
      public String foo() {
         return "b";
      }
   }
   @Root(name="c")
   public static class C implements DynamicEntry {
      public String foo() {
         return "c";
      }
   }
   @Variant({A.class, B.class, C.class})
   public static interface DynamicEntry {
      public String foo();
   }
   @Default
   public static class VariantExample {
      @ElementList(inline=true)
      private List<DynamicEntry> list;
   }
   public void testVariant() throws Exception {
      Persister persister = new Persister();
      VariantExample example = persister.read(VariantExample.class, SOURCE);
      assertEquals(example.list.get(0).getClass(), A.class);
      assertEquals(example.list.get(1).getClass(), B.class);
      assertEquals(example.list.get(2).getClass(), C.class);
   }

}
