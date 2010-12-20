package org.simpleframework.xml.core;

import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.ValidationTestCase;
import org.simpleframework.xml.strategy.CycleStrategy;
import org.simpleframework.xml.strategy.Strategy;

public class MapWithValueAttributeTest extends ValidationTestCase {
 
   private static final String PRIMITIVE_TEXT_VALUE =
   "<mapWithValueAttributeExample>\r\n"+
   "   <entry key='a'>0.0</entry>\r\n"+
   "</mapWithValueAttributeExample>\r\n";
   
   private static final String PRIMITIVE_ATTRIBUTE_VALUE =
   "<mapWithValueAttributeExample>\r\n"+
   "   <entry key='a' value='0.0'/>\r\n"+
   "</mapWithValueAttributeExample>\r\n";
   
   private static final String PRIMITIVE_ELEMENT_VALUE =
   "<mapWithValueAttributeExample>\r\n"+
   "   <entry>\r\n"+
   "      <key>a</key>\r\n"+
   "      <value>0.0</value>\r\n"+
   "   </entry>\r\n"+
   "</mapWithValueAttributeExample>\r\n";     
   
   private static final String COMPOSITE_VALUE =
   "<mapWithValueAttributeExample>\r\n"+
   "   <entry key='a'>\r\n"+
   "      <value>" +
   "         <name>A</name>\r\n"+
   "         <value>B</value>\r\n"+
   "      </value>\r\n"+
   "   </entry>\r\n" +
   "</mapWithValueAttributeExample>\r\n";   
   
   @Root
   public static class MapWithValueTextExample {
      @ElementMap(inline=true, attribute=true, entry="entry", key="key")
      private Map<String, Double> map;
   }
   
   @Root
   public static class MapWithValueElementExample {
      @ElementMap(inline=true, entry="entry", key="key", value="value")
      private Map<String, Double> map;
   }
   
   @Root
   public static class MapWithValueAttributeExample {
      @ElementMap(inline=true, attribute=true, entry="entry", key="key", value="value")
      private Map<String, Double> map;
   }
   
   @Root
   public static class MapWithCompositeValueExample {
      @ElementMap(inline=true, attribute=true, entry="entry", key="key", value="value")
      private Map<String, ValueExample> map;
   }
   
   @Root
   @SuppressWarnings("all")
   private static class ValueExample {
      @Element
      private String name;
      @Element
      private String value;
      public ValueExample(@Element(name="name") String name,
                           @Element(name="value") String value) {
         this.name = name;
         this.value = value;
      }
   }
   
   public void testPrimitiveTextMap() throws Exception {
      Strategy strategy = new CycleStrategy();
      Persister persister = new Persister(strategy);
      MapWithValueTextExample example = persister.read(MapWithValueTextExample.class, PRIMITIVE_TEXT_VALUE);
      assertEquals(example.map.get("a"), 0.0);
      persister.write(example, System.err);
      validate(example, persister);
   }
   
   public void testPrimitiveElementMap() throws Exception {
      Strategy strategy = new CycleStrategy();
      Persister persister = new Persister(strategy);
      MapWithValueElementExample example = persister.read(MapWithValueElementExample.class, PRIMITIVE_ELEMENT_VALUE);
      assertEquals(example.map.get("a"), 0.0);
      persister.write(example, System.err);
      validate(example, persister);
   }
   
   public void testPrimitiveAttributeMap() throws Exception {
      Strategy strategy = new CycleStrategy();
      Persister persister = new Persister(strategy);
      MapWithValueAttributeExample example = persister.read(MapWithValueAttributeExample.class, PRIMITIVE_ATTRIBUTE_VALUE);
      assertEquals(example.map.get("a"), 0.0);
      persister.write(example, System.err);
      validate(example, persister);
   }
   
   public void testCompositeValueMap() throws Exception {
      Strategy strategy = new CycleStrategy();
      Persister persister = new Persister(strategy);
      MapWithCompositeValueExample example = persister.read(MapWithCompositeValueExample.class, COMPOSITE_VALUE);
      assertEquals(example.map.get("a").name, "A");
      assertEquals(example.map.get("a").value, "B");
      persister.write(example, System.err);
      validate(example, persister);
   }

}
