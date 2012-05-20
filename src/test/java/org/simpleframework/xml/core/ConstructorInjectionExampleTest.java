package org.simpleframework.xml.core;

import junit.framework.TestCase;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Root;

@SuppressWarnings("all")
public class ConstructorInjectionExampleTest extends TestCase {
   
   private static final String SOURCE_A =
   "<showConstructorForEachUnionEntry>" +
   "  <a>text</a>"+
   "</showConstructorForEachUnionEntry>";
   
   private static final String SOURCE_B =
   "<showConstructorForEachUnionEntry>" +
   "  <b>1</b>"+
   "</showConstructorForEachUnionEntry>";
   
   private static final String SOURCE_C =
   "<showConstructorForEachUnionEntry>" +
   "  <c>12.9</c>"+
   "</showConstructorForEachUnionEntry>";
   
   @Root
   private static class ShowConstructorForEachUnionEntry{
      @ElementUnion({
         @Element(name="a", type=String.class),
         @Element(name="b", type=Integer.class),
         @Element(name="c", type=Double.class)
      })
      private Object value;
      public ShowConstructorForEachUnionEntry(@Element(name="a") String a){}
      public ShowConstructorForEachUnionEntry(@Element(name="b") Integer b){}
      public ShowConstructorForEachUnionEntry(@Element(name="c") Double c){}
   }
   
   
   @Root
   private static class A {}
   private static class B extends A{}
   private static class C extends B{}
   
   public void testUnionConstruction() throws Exception {
      Persister persister = new Persister();
      ShowConstructorForEachUnionEntry a = persister.read(ShowConstructorForEachUnionEntry.class, SOURCE_A);
      ShowConstructorForEachUnionEntry b = persister.read(ShowConstructorForEachUnionEntry.class, SOURCE_C);
      ShowConstructorForEachUnionEntry c = persister.read(ShowConstructorForEachUnionEntry.class, SOURCE_B);
   }

}
