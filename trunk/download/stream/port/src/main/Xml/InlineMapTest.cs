#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class InlineMapTest : ValidationTestCase {
      @Root
      private static class PrimitiveInlineMap {
         @ElementMap(entry="entity", inline=true)
         private Map<String, BigDecimal> map;
         public PrimitiveInlineMap() {
            this.map = new HashMap<String, BigDecimal>();
         }
         public BigDecimal GetValue(String name) {
            return map.get(name);
         }
      }
      @Root
      private static class PrimitiveInlineAttributeMap {
         @ElementMap(entry="entity", attribute=true, inline=true)
         private Map<String, BigDecimal> map;
         public PrimitiveInlineAttributeMap() {
            this.map = new HashMap<String, BigDecimal>();
         }
         public BigDecimal GetValue(String name) {
            return map.get(name);
         }
      }
      @Root
      private static class PrimitiveInlineAttributeValueMap {
         @ElementMap(entry="entity", value="value", attribute=true, inline=true)
         private Map<String, BigDecimal> map;
         public PrimitiveInlineAttributeValueMap() {
            this.map = new HashMap<String, BigDecimal>();
         }
         public BigDecimal GetValue(String name) {
            return map.get(name);
         }
      }
      public void TestPrimitiveMap() {
         PrimitiveInlineMap map = new PrimitiveInlineMap();
         Serializer serializer = new Persister();
         map.map.put("a", new BigDecimal(1.1));
         map.map.put("b", new BigDecimal(2.2));
         validate(map, serializer);
      }
      public void TestPrimitiveAttributeMap() {
         PrimitiveInlineAttributeMap map = new PrimitiveInlineAttributeMap();
         Serializer serializer = new Persister();
         map.map.put("a", new BigDecimal(1.1));
         map.map.put("b", null);
         map.map.put("c", new BigDecimal(2.2));
         map.map.put("d", null);
         validate(map, serializer);
      }
      public void TestPrimitiveAttributeValueMap() {
         PrimitiveInlineAttributeValueMap map = new PrimitiveInlineAttributeValueMap();
         Serializer serializer = new Persister();
         map.map.put("a", new BigDecimal(1.1));
         map.map.put("b", new BigDecimal(2.2));
         map.map.put(null, new BigDecimal(3.3));
         map.map.put("d", null);
         validate(map, serializer);
      }
   }
}
