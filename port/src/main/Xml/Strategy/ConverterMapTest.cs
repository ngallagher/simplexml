#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml.Util;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class ConverterMapTest : ValidationTestCase {
      private static class MapConverter : Converter<java.util.Map> {
         public Dictionary Read(InputNode node) {
            java.util.Map map = new HashMap();
            while(true) {
               InputNode next = node.getNext("entry");
               if(next == null) {
                  break;
               }
               Entry entry = ReadEntry(next);
               map.Put(entry.name, entry.value);
            }
            return map;
         }
         public void Write(OutputNode node, Dictionary map) {
            Set keys = map.keySet();
            for(Object key : keys) {
               OutputNode next = node.getChild("entry");
               next.setAttribute("key", key.toString());
               OutputNode value = next.getChild("value");
               value.setValue(map.get(key).toString());
            }
         }
         public Entry ReadEntry(InputNode node) {
            InputNode key = node.getAttribute("key");
            InputNode value = node.getNext("value");
            return new Entry(key.getValue(), value.getValue());
         }
         private static class Entry {
            private String name;
            private String value;
            public Entry(String name, String value){
               this.name = name;
               this.value = value;
            }
         }
      }
      [Root]
      [Default]
      private static class MapHolder {
         [Element]
         [ElementMap]
         [Convert(MapConverter.class)]
         private Map<String, String> map = new HashMap<String, String>();
         public void Put(String name, String value) {
            map.Put(name, value);
         }
      }
      public void TestMap() {
         Strategy strategy = new AnnotationStrategy();
         Serializer serializer = new Persister(strategy);
         MapHolder holder = new MapHolder();
         holder.Put("a", "A");
         holder.Put("b", "B");
         holder.Put("c", "C");
         holder.Put("d", "D");
         holder.Put("e", "E");
         holder.Put("f", "F");
         serializer.Write(holder, System.out);
         validate(holder, serializer);
      }
   }
}
