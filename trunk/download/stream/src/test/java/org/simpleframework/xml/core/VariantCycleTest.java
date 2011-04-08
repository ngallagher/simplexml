package org.simpleframework.xml.core;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.ValidationTestCase;
import org.simpleframework.xml.Variant;
import org.simpleframework.xml.strategy.CycleStrategy;
import org.simpleframework.xml.strategy.Strategy;

public class VariantCycleTest extends ValidationTestCase {
   
   private static final String SOURCE =
   "<shapeExample>" +
   "  <circle>" +
   "    <type>CIRCLE</type>" +
   "  </circle>" +
   "</shapeExample>";
   
   @Root
   public static class Square implements Shape {
      @Element
      private String type;
      
      public String type() {
         return type;
      }
   }
   @Root
   public static class Circle implements Shape {
      @Element
      private String type;
      
      public String type() {
         return type;
      }
   }
   public static interface Shape<T> {
      public String type();
   }
   @Root
   public static class ShapeExample {
      
      @Variant({
         @Element(name="circle", type=Circle.class),
         @Element(name="square", type=Square.class)
      })
      private Shape shape;
   }
      
   public void testVariantCycle() throws Exception {
      Strategy strategy = new CycleStrategy();
      Persister persister = new Persister(strategy);
      ShapeExample example = persister.read(ShapeExample.class, SOURCE);
      validate(persister, example);
   }
}
