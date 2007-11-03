package org.simpleframework.xml.load;

import java.math.BigDecimal;
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.ValidationTestCase;

public class MapTest extends ValidationTestCase {
   
   private static final String ENTRY_MAP =     
   "<entryMap>\n"+
   "   <map>\n"+   
   "      <entry key='a'>" +
   "         <mapEntry>\n" +  
   "            <name>a</name>\n"+
   "            <value>example 1</value>\n"+
   "         </mapEntry>" + 
   "      </entry>" +
   "      <entry key='b'>" +
   "         <mapEntry>\n" +  
   "            <name>b</name>\n"+
   "            <value>example 2</value>\n"+
   "         </mapEntry>" + 
   "      </entry>" +
   "      <entry key='c'>" +
   "         <mapEntry>\n" +  
   "            <name>c</name>\n"+
   "            <value>example 3</value>\n"+
   "         </mapEntry>" + 
   "      </entry>" +
   "      <entry key='d'>" +
   "         <mapEntry>\n" +  
   "            <name>d</name>\n"+
   "            <value>example 4</value>\n"+
   "         </mapEntry>" + 
   "      </entry>" +
   "   </map>\n"+
   "</entryMap>";  
   
   private static final String STRING_MAP =     
   "<stringMap>\n"+
   "   <map>\n"+   
   "      <entry letter='a'>example 1</entry>\n" +
   "      <entry letter='b'>example 2</entry>\n" +
   "      <entry letter='c'>example 3</entry>\n" +
   "      <entry letter='d'>example 4</entry>\n" +   
   "   </map>\n"+
   "</stringMap>";
   
   private static final String COMPLEX_MAP =     
   "<complexMap>\n"+
   "   <map>\n"+   
   "      <entry>" +
   "         <compositeKey>\n" +
   "            <name>name 1</name>\n" +
   "            <address>address 1</address>\n" +
   "         </compositeKey>\n" +
   "         <mapEntry>\n" +  
   "            <name>a</name>\n"+
   "            <value>example 1</value>\n"+
   "         </mapEntry>" + 
   "      </entry>" +
   "      <entry>" +
   "         <compositeKey>\n" +
   "            <name>name 2</name>\n" +
   "            <address>address 2</address>\n" +
   "         </compositeKey>\n" +
   "         <mapEntry>\n" +  
   "            <name>b</name>\n"+
   "            <value>example 2</value>\n"+
   "         </mapEntry>" + 
   "      </entry>" +
   "      <entry>" +
   "         <compositeKey>\n" +
   "            <name>name 3</name>\n" +
   "            <address>address 3</address>\n" +
   "         </compositeKey>\n" +
   "         <mapEntry>\n" +  
   "            <name>c</name>\n"+
   "            <value>example 3</value>\n"+
   "         </mapEntry>" + 
   "      </entry>" +
   "      <entry>" +
   "         <compositeKey>\n" +
   "            <name>name 4</name>\n" +
   "            <address>address 4</address>\n" +
   "         </compositeKey>\n" +
   "         <mapEntry>\n" +  
   "            <name>d</name>\n"+
   "            <value>example 4</value>\n"+
   "         </mapEntry>" + 
   "      </entry>" +
   "   </map>\n"+
   "</complexMap>"; 
   
   private static final String PRIMITIVE_MAP =      
   "<primitiveMap>\n"+
   "   <table>\n"+   
   "      <entry>\n" +
   "         <string>one</string>\n" +
   "         <bigDecimal>1.0</bigDecimal>\n" +
   "      </entry>\n"+
   "      <entry>" +
   "         <string>two</string>\n" +
   "         <bigDecimal>2.0</bigDecimal>\n" +
   "      </entry>\n"+
   "      <entry>" +
   "         <string>three</string>\n" +
   "         <bigDecimal>3.0</bigDecimal>\n" +
   "      </entry>\n"+
   "      <entry>" +
   "         <string>four</string>\n" +
   "         <bigDecimal>4.0</bigDecimal>\n" +
   "      </entry>\n"+
   "   </table>\n"+
   "</primitiveMap>";  
   
   private static final String PRIMITIVE_VALUE_OVERRIDE_MAP =      
   "<primitiveValueOverrideMap>\n"+
   "   <map>\n"+   
   "      <entry>\n" +
   "         <string>one</string>\n" +
   "         <decimal>1.0</decimal>\n" +
   "      </entry>\n"+
   "      <entry>" +
   "         <string>two</string>\n" +
   "         <decimal>2.0</decimal>\n" +
   "      </entry>\n"+
   "      <entry>" +
   "         <string>three</string>\n" +
   "         <decimal>3.0</decimal>\n" +
   "      </entry>\n"+
   "      <entry>" +
   "         <string>four</string>\n" +
   "         <decimal>4.0</decimal>\n" +
   "      </entry>\n"+
   "   </map>\n"+
   "</primitiveValueOverrideMap>";
   
   private static final String PRIMITIVE_VALUE_KEY_OVERRIDE_MAP =      
   "<primitiveValueKeyOverrideMap>\n"+
   "   <map>\n"+   
   "      <item>\n" +
   "         <text>one</text>\n" +
   "         <decimal>1.0</decimal>\n" +
   "      </item>\n"+
   "      <item>" +
   "         <text>two</text>\n" +
   "         <decimal>2.0</decimal>\n" +
   "      </item>\n"+
   "      <item>" +
   "         <text>three</text>\n" +
   "         <decimal>3.0</decimal>\n" +
   "      </item>\n"+
   "      <item>" +
   "         <text>four</text>\n" +
   "         <decimal>4.0</decimal>\n" +
   "      </item>\n"+
   "   </map>\n"+
   "</primitiveValueKeyOverrideMap>"; 
   
   private static final String PRIMITIVE_INLINE_MAP =            
   "<primitiveInlineMap>\n"+ 
   "   <entity>\n" +
   "      <string>one</string>\n" +
   "      <bigDecimal>1.0</bigDecimal>\n" +
   "   </entity>\n"+
   "   <entity>" +
   "      <string>two</string>\n" +
   "      <bigDecimal>2.0</bigDecimal>\n" +
   "   </entity>\n"+
   "   <entity>" +
   "      <string>three</string>\n" +
   "      <bigDecimal>3.0</bigDecimal>\n" +
   "   </entity>\n"+
   "   <entity>" +
   "      <string>four</string>\n" +
   "      <bigDecimal>4.0</bigDecimal>\n" +
   "   </entity>\n"+
   "</primitiveInlineMap>";   
   
   
   @Root
   private static class EntryMap {      
      
      @ElementMap(key="key", attribute=true)
      private Map<String, MapEntry> map;
      
      public String getValue(String name) {
         return map.get(name).value;
      }
   }
   
   @Root
   private static class MapEntry {
      
      @Element
      private String name;
      
      @Element
      private String value;
   }
   
   @Root
   private static class StringMap {
      
      @ElementMap(key="letter", attribute=true, data=true)
      private Map<String, String> map;
      
      public String getValue(String name) {
         return map.get(name);
      }
   }
   
   @Root
   private static class ComplexMap {      
      
      @ElementMap
      private Map<CompositeKey, MapEntry> map;
      
      public String getValue(CompositeKey key) {
         return map.get(key).value;
      }
   }
   
   @Root
   private static class CompositeKey {
      
      @Element
      private String name;
      
      @Element
      private String address;
      
      public CompositeKey() {
         super();
      }
      
      public CompositeKey(String name, String address) {
         this.name = name;
         this.address = address;
      }
      
      public int hashCode() {
         return name.hashCode() + address.hashCode();
      }
      
      public boolean equals(Object item) {
         if(item instanceof CompositeKey) {
            CompositeKey other = (CompositeKey)item;
            
            return other.name.equals(name) && other.address.equals(address);
         }
         return false;
      }
   }
   
   @Root
   private static class PrimitiveMap {
      
      @ElementMap(name="table")
      private Map<String, BigDecimal> map;
      
      public BigDecimal getValue(String name) {
         return map.get(name);
      }
   }
   
   @Root
   private static class PrimitiveValueOverrideMap {
      
      @ElementMap(value="decimal")
      private Map<String, BigDecimal> map;
      
      public BigDecimal getValue(String name) {
         return map.get(name);
      }
   }
   
   @Root
   private static class PrimitiveValueKeyOverrideMap {
      
      @ElementMap(value="decimal", key="text", entry="item")
      private Map<String, BigDecimal> map;
      
      public BigDecimal getValue(String name) {
         return map.get(name);
      }
   }
   
   @Root
   private static class PrimitiveInlineMap {
      
      @ElementMap(entry="entity", inline=true)
      private Map<String, BigDecimal> map;
      
      public BigDecimal getValue(String name) {
         return map.get(name);
      }
   }
   
   public void testEntryMap() throws Exception {
      Serializer serializer = new Persister();
      EntryMap example = serializer.read(EntryMap.class, ENTRY_MAP);
      
      assertEquals("example 1", example.getValue("a"));
      assertEquals("example 2", example.getValue("b"));
      assertEquals("example 3", example.getValue("c"));
      assertEquals("example 4", example.getValue("d"));
      
      validate(example, serializer);
   }
   
   public void testStringMap() throws Exception {
      Serializer serializer = new Persister();
      StringMap example = serializer.read(StringMap.class, STRING_MAP);
      
      assertEquals("example 1", example.getValue("a"));
      assertEquals("example 2", example.getValue("b"));
      assertEquals("example 3", example.getValue("c"));
      assertEquals("example 4", example.getValue("d"));
      
      validate(example, serializer);
   }
   
   public void testComplexMap() throws Exception {
      Serializer serializer = new Persister();
      ComplexMap example = serializer.read(ComplexMap.class, COMPLEX_MAP);
      
      assertEquals("example 1", example.getValue(new CompositeKey("name 1", "address 1")));
      assertEquals("example 2", example.getValue(new CompositeKey("name 2", "address 2")));
      assertEquals("example 3", example.getValue(new CompositeKey("name 3", "address 3")));
      assertEquals("example 4", example.getValue(new CompositeKey("name 4", "address 4")));
      
      validate(example, serializer);
   }
   
   public void testPrimitiveMap() throws Exception {
      Serializer serializer = new Persister();
      PrimitiveMap example = serializer.read(PrimitiveMap.class, PRIMITIVE_MAP);
      
      assertEquals(new BigDecimal("1.0"), example.getValue("one"));
      assertEquals(new BigDecimal("2.0"), example.getValue("two"));
      assertEquals(new BigDecimal("3.0"), example.getValue("three"));
      assertEquals(new BigDecimal("4.0"), example.getValue("four"));
      
      validate(example, serializer);
   }
   
   public void testPrimitiveValueOverrideMap() throws Exception {
      Serializer serializer = new Persister();
      PrimitiveValueOverrideMap example = serializer.read(PrimitiveValueOverrideMap.class, PRIMITIVE_VALUE_OVERRIDE_MAP);
      
      assertEquals(new BigDecimal("1.0"), example.getValue("one"));
      assertEquals(new BigDecimal("2.0"), example.getValue("two"));
      assertEquals(new BigDecimal("3.0"), example.getValue("three"));
      assertEquals(new BigDecimal("4.0"), example.getValue("four"));
      
      validate(example, serializer);
   }
   
   public void testPrimitiveValueKeyOverrideMap() throws Exception {
      Serializer serializer = new Persister();
      PrimitiveValueKeyOverrideMap example = serializer.read(PrimitiveValueKeyOverrideMap.class, PRIMITIVE_VALUE_KEY_OVERRIDE_MAP);
      
      assertEquals(new BigDecimal("1.0"), example.getValue("one"));
      assertEquals(new BigDecimal("2.0"), example.getValue("two"));
      assertEquals(new BigDecimal("3.0"), example.getValue("three"));
      assertEquals(new BigDecimal("4.0"), example.getValue("four"));
      
      validate(example, serializer);
   }
   
   public void testPrimitiveInlineMap() throws Exception {
      Serializer serializer = new Persister();
      PrimitiveInlineMap example = serializer.read(PrimitiveInlineMap.class, PRIMITIVE_INLINE_MAP);
      
      assertEquals(new BigDecimal("1.0"), example.getValue("one"));
      assertEquals(new BigDecimal("2.0"), example.getValue("two"));
      assertEquals(new BigDecimal("3.0"), example.getValue("three"));
      assertEquals(new BigDecimal("4.0"), example.getValue("four"));
      
      validate(example, serializer);
   }
}
