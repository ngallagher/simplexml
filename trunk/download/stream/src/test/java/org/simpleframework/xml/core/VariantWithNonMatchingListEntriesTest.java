package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.ValidationTestCase;
import org.simpleframework.xml.VariantList;

public class VariantWithNonMatchingListEntriesTest extends ValidationTestCase {
   
   @Root
   private static class Type{}
   private static class A extends Type{};
   private static class B extends Type{};
   private static class C extends Type{};
   private static class D extends Type{};
   private static class E extends Type{};

   @Root
   private static class NonMatchingVariantList{
      @VariantList({
         @ElementList(entry="a", inline=true, type=A.class),
         @ElementList(entry="b", inline=true, type=B.class),
         @ElementList(entry="c", inline=true, type=C.class),
         @ElementList(entry="d", inline=true, type=D.class)         
      })
      private List<Type> list;
      public NonMatchingVariantList(){
         this.list = new ArrayList<Type>();
      }
      public void addType(Type t) {
         list.add(t);
      }
   }
   
   public void testNonMatching() throws Exception {
      Persister persister = new Persister();
      NonMatchingVariantList list = new NonMatchingVariantList();
      boolean exception = false;
      list.addType(new A());
      list.addType(new B());
      list.addType(new C());
      list.addType(new D());
      list.addType(new E());
      list.addType(new A());
      try {
         persister.write(list, System.out);
      }catch(Exception e){
         e.printStackTrace();
         exception = true;
      }
      assertTrue("Should fail when no match found", exception);
   }
}
