package org.simpleframework.xml.load;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.ValidationTestCase;

public class MapNullTest extends ValidationTestCase {
   
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
      
      public MapEntry() {
         super();
      }
      
      public MapEntry(String name, String value) {
         this.name = name;
         this.value = value;
      }
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
      
      public ComplexMap() {
         this.map = new HashMap<CompositeKey, MapEntry>();
      }
      
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
      
      public PrimitiveMap() {
         this.map = new HashMap<String, BigDecimal>();         
      }
      
      public BigDecimal getValue(String name) {
         return map.get(name);
      }
   }
   
   public void testNullValue() throws Exception {
      Serializer serializer = new Persister();
      PrimitiveMap primitiveMap = new PrimitiveMap();
      
      primitiveMap.map.put("a", new BigDecimal(1));
      primitiveMap.map.put("b", new BigDecimal(2));
      primitiveMap.map.put("c", null);     
      primitiveMap.map.put(null, new BigDecimal(4));
      
      StringWriter out = new StringWriter();
      serializer.write(primitiveMap, out);
      
      primitiveMap = serializer.read(PrimitiveMap.class, out.toString());
      
      assertEquals(primitiveMap.map.get(null), new BigDecimal(4));
      assertEquals(primitiveMap.map.get("c"), null);
      assertEquals(primitiveMap.map.get("a"), new BigDecimal(1));
      assertEquals(primitiveMap.map.get("b"), new BigDecimal(2));
      
      validate(primitiveMap, serializer);           
      
      ComplexMap complexMap = new ComplexMap();
      
      complexMap.map.put(new CompositeKey("name.1", "address.1"), new MapEntry("1", "1"));
      complexMap.map.put(new CompositeKey("name.2", "address.2"), new MapEntry("2", "2"));
      complexMap.map.put(null, new MapEntry("3", "3"));
      complexMap.map.put(new CompositeKey("name.4", "address.4"), null);
      
      validate(complexMap, serializer);
   }
}
