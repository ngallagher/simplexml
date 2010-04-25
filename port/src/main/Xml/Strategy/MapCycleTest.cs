#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class MapCycleTest : ValidationTestCase {
      private const String ENTRY_MAP =
      "<entryMap>\n"+
      "   <map id='map'>\n"+
      "      <entry key='a'>" +
      "         <mapEntry id='a'>\n" +
      "            <name>a</name>\n"+
      "            <value>example 1</value>\n"+
      "         </mapEntry>" +
      "      </entry>" +
      "      <entry key='b'>" +
      "         <mapEntry id='b'>\n" +
      "            <name>b</name>\n"+
      "            <value>example 2</value>\n"+
      "         </mapEntry>" +
      "      </entry>" +
      "      <entry key='c'>" +
      "         <mapEntry reference='a'/>\n" +
      "      </entry>" +
      "      <entry key='d'>" +
      "         <mapEntry reference='a'/>\n" +
      "      </entry>\n" +
      "   </map>\n"+
      "</entryMap>";
      private const String COMPLEX_MAP =
      "<complexMap>\n"+
      "   <map>\n"+
      "      <entry>" +
      "         <compositeKey id='1'>\n" +
      "            <name>name 1</name>\n" +
      "            <address>address 1</address>\n" +
      "         </compositeKey>\n" +
      "         <mapEntry>\n" +
      "            <name>a</name>\n"+
      "            <value>example 1</value>\n"+
      "         </mapEntry>" +
      "      </entry>" +
      "      <entry>" +
      "         <compositeKey reference='1'/>\n" +
      "         <mapEntry id='2'>\n" +
      "            <name>b</name>\n"+
      "            <value>example 2</value>\n"+
      "         </mapEntry>" +
      "      </entry>" +
      "      <entry>" +
      "         <compositeKey>\n" +
      "            <name>name 3</name>\n" +
      "            <address>address 3</address>\n" +
      "         </compositeKey>\n" +
      "         <mapEntry reference='2'/>\n" +
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
      "   <map>\n"+
      "      <entry>\n" +
      "         <string>one</string>\n" + // one
      "         <double id='one'>1.0</double>\n" +
      "      </entry>\n"+
      "      <entry>" +
      "         <string>two</string>\n" + // two
      "         <double reference='one'/>\n" +
      "      </entry>\n"+
      "      <entry>" +
      "         <string id='three'>three</string>\n" + // three
      "         <double>3.0</double>\n" +
      "      </entry>\n"+
      "      <entry>" +
      "         <string reference='three'/>\n" + // three
      "         <double>4.0</double>\n" +
      "      </entry>\n"+
      "   </map>\n"+
      "</primitiveMap>";
      @Root
      private static class EntryMap {
         [ElementMap(Key="key", Attribute=true)]
         private Map<String, MapEntry> map;
         public String GetValue(String name) {
            return map.get(name).value;
         }
         public MapEntry GetEntry(String name) {
            return map.get(name);
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
         [ElementMap(Key="letter", Attribute=true)]
         private Map<String, String> map;
         public String GetValue(String name) {
            return map.get(name);
         }
      }
      @Root
      private static class ComplexMap {
         @ElementMap
         private Map<CompositeKey, MapEntry> map;
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
      private static class BadMap {
         @ElementMap
         private Map<CompositeKey, String> map;
         public BadMap() {
            this.map = new HashMap<CompositeKey, String>();
         }
         public String GetValue(CompositeKey key) {
            return map.get(key);
         }
         public CompositeKey key, String Value {
            set {
               map.put(key, _value);
            }
         }
         //public void SetValue(CompositeKey key, String value) {
         //   map.put(key, value);
         //}
      @Root
      private static class PrimitiveMap {
         @ElementMap
         private Map<String, Double> map;
         public double GetValue(String name) {
            return map.get(name);
         }
      }
      public void TestEntryMap() {
         Strategy strategy = new CycleStrategy();
         Serializer serializer = new Persister(strategy);
         EntryMap example = serializer.read(EntryMap.class, ENTRY_MAP);
         assertEquals("example 1", example.GetValue("a"));
         assertEquals("example 2", example.GetValue("b"));
         assertEquals("example 1", example.GetValue("c"));
         assertEquals("example 1", example.GetValue("d"));
         MapEntry a = example.GetEntry("a");
         MapEntry b = example.GetEntry("b");
         MapEntry c = example.GetEntry("c");
         MapEntry d = example.GetEntry("d");
         assertTrue(a == c);
         assertTrue(c == d);
         assertFalse(a == b);
         validate(example, serializer);
      }
      public void TestComplexMap() {
         Strategy strategy = new CycleStrategy();
         Serializer serializer = new Persister(strategy);
         ComplexMap example = serializer.read(ComplexMap.class, COMPLEX_MAP);
         assertEquals("example 2", example.GetValue(new CompositeKey("name 1", "address 1")));
         assertEquals("example 2", example.GetValue(new CompositeKey("name 3", "address 3")));
         assertEquals("example 4", example.GetValue(new CompositeKey("name 4", "address 4")));
         validate(example, serializer);
      }
      public void TestPrimitiveMap() {
         Strategy strategy = new CycleStrategy();
         Serializer serializer = new Persister(strategy);
         PrimitiveMap example = serializer.read(PrimitiveMap.class, PRIMITIVE_MAP);
         assertEquals(1.0, example.GetValue("one"));
         assertEquals(1.0, example.GetValue("two"));
         assertEquals(4.0, example.GetValue("three"));
         validate(example, serializer);
      }
   }
}
