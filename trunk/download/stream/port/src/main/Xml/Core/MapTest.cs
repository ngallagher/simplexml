#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class MapTest : ValidationTestCase {
      private const String ENTRY_MAP =
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
      private const String STRING_MAP =
      "<stringMap>\n"+
      "   <map>\n"+
      "      <entry letter='a'>example 1</entry>\n" +
      "      <entry letter='b'>example 2</entry>\n" +
      "      <entry letter='c'>example 3</entry>\n" +
      "      <entry letter='d'>example 4</entry>\n" +
      "   </map>\n"+
      "</stringMap>";
      private const String COMPLEX_VALUE_KEY_OVERRIDE_MAP =
      "<complexMap>\n"+
      "   <map>\n"+
      "      <item>" +
      "         <key>\n" +
      "            <name>name 1</name>\n" +
      "            <address>address 1</address>\n" +
      "         </key>\n" +
      "         <value>\n" +
      "            <name>a</name>\n"+
      "            <value>example 1</value>\n"+
      "         </value>" +
      "      </item>" +
      "      <item>" +
      "         <key>\n" +
      "            <name>name 2</name>\n" +
      "            <address>address 2</address>\n" +
      "         </key>\n" +
      "         <value>\n" +
      "            <name>b</name>\n"+
      "            <value>example 2</value>\n"+
      "         </value>" +
      "      </item>" +
      "      <item>" +
      "         <key>\n" +
      "            <name>name 3</name>\n" +
      "            <address>address 3</address>\n" +
      "         </key>\n" +
      "         <value>\n" +
      "            <name>c</name>\n"+
      "            <value>example 3</value>\n"+
      "         </value>" +
      "      </item>" +
      "      <item>" +
      "         <key>\n" +
      "            <name>name 4</name>\n" +
      "            <address>address 4</address>\n" +
      "         </key>\n" +
      "         <value>\n" +
      "            <name>d</name>\n"+
      "            <value>example 4</value>\n"+
      "         </value>" +
      "      </item>" +
      "   </map>\n"+
      "</complexMap>";
      private const String COMPLEX_MAP =
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
      private const String PRIMITIVE_MAP =
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
      private const String PRIMITIVE_VALUE_OVERRIDE_MAP =
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
      private const String PRIMITIVE_VALUE_KEY_OVERRIDE_MAP =
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
      private const String PRIMITIVE_INLINE_MAP =
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
      private const String INDEX_EXAMPLE =
      "<?xml version='1.0' encoding='ISO-8859-1'?>\r\n" +
      "<index id='users'>\r\n" +
      "     <database>xyz</database>\r\n" +
      "     <query>\r\n" +
      "         <columns>foo,bar</columns>\r\n" +
      "         <tables>a,b,c</tables>\r\n" +
      "     </query>\r\n" +
      "     <fields>\r\n" +
      "         <field id='foo'>\r\n" +
      "             <lucene>\r\n" +
      "                 <index>TOKENIZED</index>\r\n" +
      "                 <store>false</store>\r\n" +
      "                 <default>true</default>\r\n" +
      "             </lucene>\r\n" +
      "         </field>\r\n" +
      "         <field id='bar'>\r\n" +
      "             <lucene>\r\n" +
      "                 <index>TOKENIZED</index>\r\n" +
      "                 <store>false</store>\r\n" +
      "                 <default>true</default>\r\n" +
      "             </lucene>\r\n" +
      "         </field>\r\n" +
      "     </fields>\r\n" +
      "</index>\r\n";
      @Root(name="index", strict=false)
      public static class IndexConfig {
         @Attribute
         private String id;
         @Element
         private String database;
         @Element
         private Query query;
         @ElementMap(name="fields", entry="field", key="id", attribute=true, keyType=String.class, valueType=Lucene.class)
         private HashMap<String, Lucene> fields = new HashMap<String, Lucene>();
      }
      @Root
      public static class Field {
         @Attribute
         private String id;
         @Element
         private Lucene lucene;
      }
      @Root(strict=false)
      public static class Lucene {
         @Element
         private String index;
         @Element
         private bool store;
         @Element(name="default")
         private bool flag;
      }
      @Root
      private static class Query {
         @Element
         private String[] columns;
         @Element
         private String[] tables;
      }
      @Root
      private static class EntryMap {
         @ElementMap(key="key", attribute=true)
         private Map<String, MapEntry> map;
         public String GetValue(String name) {
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
         public String GetValue(String name) {
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
         public String GetValue(CompositeKey key) {
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
         public int HashCode() {
            return name.HashCode() + address.HashCode();
         }
         public bool Equals(Object item) {
            if(item instanceof CompositeKey) {
               CompositeKey other = (CompositeKey)item;
               return other.name.Equals(name) && other.address.Equals(address);
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
         public BigDecimal GetValue(String name) {
            return map.get(name);
         }
      }
      @Root
      private static class PrimitiveValueOverrideMap {
         @ElementMap(value="decimal")
         private Map<String, BigDecimal> map;
         public BigDecimal GetValue(String name) {
            return map.get(name);
         }
      }
      @Root
      private static class PrimitiveValueKeyOverrideMap {
         @ElementMap(value="decimal", key="text", entry="item")
         private Map<String, BigDecimal> map;
         public BigDecimal GetValue(String name) {
            return map.get(name);
         }
      }
      @Root
      private static class ComplexValueKeyOverrideMap {
         @ElementMap(key="key", value="value", entry="item")
         private Map<CompositeKey, MapEntry> map;
         public ComplexValueKeyOverrideMap() {
            this.map = new HashMap<CompositeKey, MapEntry>();
         }
         public String GetValue(CompositeKey key) {
            return map.get(key).value;
         }
      }
      @Root
      private static class PrimitiveInlineMap {
         @ElementMap(entry="entity", inline=true)
         private Map<String, BigDecimal> map;
         public BigDecimal GetValue(String name) {
            return map.get(name);
         }
      }
      public void TestEntryMap() {
         Serializer serializer = new Persister();
         EntryMap example = serializer.read(EntryMap.class, ENTRY_MAP);
         assertEquals("example 1", example.GetValue("a"));
         assertEquals("example 2", example.GetValue("b"));
         assertEquals("example 3", example.GetValue("c"));
         assertEquals("example 4", example.GetValue("d"));
         validate(example, serializer);
      }
      public void TestStringMap() {
         Serializer serializer = new Persister();
         StringMap example = serializer.read(StringMap.class, STRING_MAP);
         assertEquals("example 1", example.GetValue("a"));
         assertEquals("example 2", example.GetValue("b"));
         assertEquals("example 3", example.GetValue("c"));
         assertEquals("example 4", example.GetValue("d"));
         validate(example, serializer);
      }
      public void TestComplexMap() {
         Serializer serializer = new Persister();
         ComplexMap example = serializer.read(ComplexMap.class, COMPLEX_MAP);
         assertEquals("example 1", example.GetValue(new CompositeKey("name 1", "address 1")));
         assertEquals("example 2", example.GetValue(new CompositeKey("name 2", "address 2")));
         assertEquals("example 3", example.GetValue(new CompositeKey("name 3", "address 3")));
         assertEquals("example 4", example.GetValue(new CompositeKey("name 4", "address 4")));
         validate(example, serializer);
      }
      public void TestPrimitiveMap() {
         Serializer serializer = new Persister();
         PrimitiveMap example = serializer.read(PrimitiveMap.class, PRIMITIVE_MAP);
         assertEquals(new BigDecimal("1.0"), example.GetValue("one"));
         assertEquals(new BigDecimal("2.0"), example.GetValue("two"));
         assertEquals(new BigDecimal("3.0"), example.GetValue("three"));
         assertEquals(new BigDecimal("4.0"), example.GetValue("four"));
         validate(example, serializer);
      }
      public void TestPrimitiveValueOverrideMap() {
         Serializer serializer = new Persister();
         PrimitiveValueOverrideMap example = serializer.read(PrimitiveValueOverrideMap.class, PRIMITIVE_VALUE_OVERRIDE_MAP);
         assertEquals(new BigDecimal("1.0"), example.GetValue("one"));
         assertEquals(new BigDecimal("2.0"), example.GetValue("two"));
         assertEquals(new BigDecimal("3.0"), example.GetValue("three"));
         assertEquals(new BigDecimal("4.0"), example.GetValue("four"));
         validate(example, serializer);
      }
      public void TestPrimitiveValueKeyOverrideMap() {
         Serializer serializer = new Persister();
         PrimitiveValueKeyOverrideMap example = serializer.read(PrimitiveValueKeyOverrideMap.class, PRIMITIVE_VALUE_KEY_OVERRIDE_MAP);
         assertEquals(new BigDecimal("1.0"), example.GetValue("one"));
         assertEquals(new BigDecimal("2.0"), example.GetValue("two"));
         assertEquals(new BigDecimal("3.0"), example.GetValue("three"));
         assertEquals(new BigDecimal("4.0"), example.GetValue("four"));
         validate(example, serializer);
      }
      public void TestComplexValueKeyOverrideMap() {
         Serializer serializer = new Persister();
         ComplexValueKeyOverrideMap example = serializer.read(ComplexValueKeyOverrideMap.class, COMPLEX_VALUE_KEY_OVERRIDE_MAP);
         assertEquals("example 1", example.GetValue(new CompositeKey("name 1", "address 1")));
         assertEquals("example 2", example.GetValue(new CompositeKey("name 2", "address 2")));
         assertEquals("example 3", example.GetValue(new CompositeKey("name 3", "address 3")));
         assertEquals("example 4", example.GetValue(new CompositeKey("name 4", "address 4")));
         validate(example, serializer);
      }
      public void TestPrimitiveInlineMap() {
         Serializer serializer = new Persister();
         PrimitiveInlineMap example = serializer.read(PrimitiveInlineMap.class, PRIMITIVE_INLINE_MAP);
         assertEquals(new BigDecimal("1.0"), example.GetValue("one"));
         assertEquals(new BigDecimal("2.0"), example.GetValue("two"));
         assertEquals(new BigDecimal("3.0"), example.GetValue("three"));
         assertEquals(new BigDecimal("4.0"), example.GetValue("four"));
         validate(example, serializer);
      }
      public void TestNullValue() {
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
      public void TestIndexExample() {
         Serializer serializer = new Persister();
         IndexConfig config = serializer.read(IndexConfig.class, INDEX_EXAMPLE);
         validate(config, serializer);
      }
   }
}
