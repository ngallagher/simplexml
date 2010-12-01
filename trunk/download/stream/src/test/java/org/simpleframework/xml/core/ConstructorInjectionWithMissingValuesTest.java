package org.simpleframework.xml.core;

import junit.framework.TestCase;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

public class ConstructorInjectionWithMissingValuesTest extends TestCase{

   private static final String SOURCE =
   "<test>\n"+
   "   <a>Value for A</a>\n"+
   "</test>\n";
   
   @Root(name="test")
   private static class ConstructorWithMissingValues{
      @Element(required=false)
      private final String a;
      @Element(required=false)
      private final String b;
      @Element(required=false)
      private final String c;
      public ConstructorWithMissingValues(
         @Element(name="a", required=false) String a,
         @Element(name="b", required=false) String b,
         @Element(name="c", required=false) String c)
      {
         this.a = a;
         this.b = b;
         this.c = c;
      }
      public String getA() {
         return a;
      }
      public String getB() {
         return b;
      }
      public String getC() {
         return c;
      }
   }
   
   @Root(name="test")
   private static class TwoConstructorsWithMissingValues{
      @Element(required=false)
      private final String a;
      @Element(required=false)
      private final String b;
      @Element(required=false)
      private final String c;
      public TwoConstructorsWithMissingValues(
         @Element(name="a", required=false) String a,
         @Element(name="b", required=false) String b)
      {
         this.a = a;
         this.b = b;
         this.c = "Default C";
      }
      public TwoConstructorsWithMissingValues(
         @Element(name="a", required=false) String a,
         @Element(name="b", required=false) String b,
         @Element(name="c", required=false) String c)
      {
         this.a = a;
         this.b = b;
         this.c = c;
      }
      public String getA() {
         return a;
      }
      public String getB() {
         return b;
      }
      public String getC() {
         return c;
      }
   }
   
   public void testConstructor() throws Exception {
      Persister persister = new Persister();
      ConstructorWithMissingValues example = persister.read(ConstructorWithMissingValues.class, SOURCE);
      assertEquals(example.getA(), "Value for A");
      assertEquals(example.getB(), null);
      assertEquals(example.getC(), null);
   }
   
   public void testTwoConstructors() throws Exception {
      Persister persister = new Persister();
      TwoConstructorsWithMissingValues example = persister.read(TwoConstructorsWithMissingValues.class, SOURCE);
      assertEquals(example.getA(), "Value for A");
      assertEquals(example.getB(), null);
      assertEquals(example.getC(), "Default C");
   }
}
