#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class MultiElementMapTest : ValidationTestCase {
      private const String SOURCE =
      "<properties>\n"+
      "    <entry>\n"+
      "        <key>bool-value</key>\n"+
      "        <value>\n"+
      "            <bool>true</bool>\n"+
      "        </value>\n"+
      "    </entry>\n"+
      "    <entry>\n"+
      "        <key>string-value</key>\n"+
      "        <value>\n"+
      "            <string>hello world</string>\n"+
      "        </value>\n"+
      "    </entry>\n"+
      "    <entry>\n"+
      "        <key>int-value</key>\n"+
      "        <value>\n"+
      "            <int>42</int>\n"+
      "        </value>\n"+
      "    </entry>\n"+
      "</properties>";
      @Root
      private static class Value {
         @Element(name="bool", required=false)
         private Boolean boolValue;
         @Element(name="byte", required=false)
         private Byte byteValue;
         @Element(name="double", required=false)
         private Double doubleValue;
         @Element(name="float", required=false)
         private Float floatValue;
         @Element(name="int", required=false)
         private Integer intValue;
         @Element(name="long", required=false)
         private Long longValue;
         @Element(name="short", required=false)
         private Short shortValue;
         @Element(name="dateTime", required=false)
         private Date dateTime;
         @Element(name="string", required=false)
         private String string;
         @Transient
         private Object value;
         @Validate
         public void Commit() {
            if(boolValue != null) {
               value = boolValue;
            }
            if(byteValue != null) {
               value = byteValue;
            }
            if(doubleValue != null) {
               value = doubleValue;
            }
            if(floatValue != null) {
               value = floatValue;
            }
            if(intValue != null) {
               value = intValue;
            }
            if(longValue != null) {
               value = longValue;
            }
            if(shortValue != null) {
               value = shortValue;
            }
            if(dateTime != null) {
               value = dateTime;
            }
            if(string != null) {
               value = string;
            }
         }
         public Object Get() {
            return value;
         }
         public void Set(Object value) {
            this.value = value;
         }
      }
      @Root
      private static class Properties {
         @ElementMap(key="key", value="value", inline=true)
         private Map<String, Value> map;
         public Object Get(String name) {
            return map.Get(name).Get();
         }
      }
      public void TestProperties() {
         Persister persister = new Persister();
         Properties properties = persister.read(Properties.class, SOURCE);
         assertEquals(true, properties.Get("bool-value"));
         assertEquals("hello world", properties.Get("string-value"));
         assertEquals(42, properties.Get("int-value"));
         validate(persister, properties);
      }
   }
}
