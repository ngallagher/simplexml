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
      [Root]
      private static class Value {
         [Element(Name="bool", Required=false)]
         private Boolean boolValue;
         [Element(Name="byte", Required=false)]
         private Byte byteValue;
         [Element(Name="double", Required=false)]
         private Double doubleValue;
         [Element(Name="float", Required=false)]
         private Float floatValue;
         [Element(Name="int", Required=false)]
         private Integer intValue;
         [Element(Name="long", Required=false)]
         private Long longValue;
         [Element(Name="short", Required=false)]
         private Short shortValue;
         [Element(Name="dateTime", Required=false)]
         private Date dateTime;
         [Element(Name="string", Required=false)]
         private String string;
         [Transient]
         private Object value;
         [Validate]
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
      [Root]
      private static class Properties {
         [ElementMap(Key="key", Value="value", Inline=true)]
         private Map<String, Value> map;
         public Object Get(String name) {
            return map.Get(name).Get();
         }
      }
      public void TestProperties() {
         Persister persister = new Persister();
         Properties properties = persister.read(Properties.class, SOURCE);
         AssertEquals(true, properties.Get("bool-value"));
         AssertEquals("hello world", properties.Get("string-value"));
         AssertEquals(42, properties.Get("int-value"));
         validate(persister, properties);
      }
   }
}
