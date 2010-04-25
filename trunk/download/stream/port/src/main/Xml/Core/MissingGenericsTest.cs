#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class MissingGenericsTest : TestCase {
       @Root
       private static class MissingGenerics {
           [SuppressWarnings("unchecked")]
           [ElementMap(KeyType=String.class, ValueType=String.class)]
           private Dictionary map = new HashMap();
           [SuppressWarnings("unchecked")]
           [ElementList(Type=String.class)]
           private List list = new ArrayList();
           [SuppressWarnings("unchecked")]
           public Dictionary Map {
              get {
                  return map;
              }
           }
           //public Dictionary GetMap() {
           //    return map;
           //}
           public List List {
              get {
                  return list;
              }
           }
           //public List GetList() {
           //    return list;
           //}
       [SuppressWarnings("unchecked")]
       public void TestMissingGenerics() {
           MissingGenerics example = new MissingGenerics();
           Persister persister = new Persister();
           Dictionary map = example.Map;
           map.put("a", "A");
           map.put("b", "B");
           map.put("c", "C");
           map.put("d", "D");
           map.put("e", "E");
           List list = example.List;
           list.add("1");
           list.add("2");
           list.add("3");
           list.add("4");
           list.add("5");
           StringWriter out = new StringWriter();
           persister.write(example, out);
           String text = out.toString();
           MissingGenerics recovered = persister.read(MissingGenerics.class, text);
           assertEquals(recovered.Map.size(), 5);
           assertEquals(recovered.Map.get("a"), "A");
           assertEquals(recovered.Map.get("b"), "B");
           assertEquals(recovered.Map.get("c"), "C");
           assertEquals(recovered.Map.get("d"), "D");
           assertEquals(recovered.Map.get("e"), "E");
           assertTrue(recovered.List.contains("1"));
           assertTrue(recovered.List.contains("2"));
           assertTrue(recovered.List.contains("3"));
           assertTrue(recovered.List.contains("4"));
           assertTrue(recovered.List.contains("5"));
        }
   }
}
