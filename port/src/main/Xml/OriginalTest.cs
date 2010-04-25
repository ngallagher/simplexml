#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class OriginalTest : ValidationTestCase {
      private const String SOURCE =
      "<exampleWithOriginals>\n"+
      "   <list>\n"+
      "      <string>a</string>\n"+
      "      <string>b</string>\n"+
      "      <string>c</string>\n"+
      "      <string>d</string>\n"+
      "   </list>\n"+
      "   <map>\n"+
      "      <entry>\n"+
      "         <string>a</string>\n"+
      "         <double>1.0</double>\n"+
      "      </entry>\n"+
      "      <entry>\n"+
      "         <string>b</string>\n"+
      "         <double>2.0</double>\n"+
      "      </entry>\n"+
      "      <entry>\n"+
      "         <string>c</string>\n"+
      "         <double>3.0</double>\n"+
      "      </entry>\n"+
      "   </map>\n"+
      "   <listEntry name='a' value='1'/>\n"+
      "   <listEntry name='b' value='2'/>\n"+
      "   <listEntry name='c' value='3'/>\n"+
      "   <listEntry name='d' value='4'/>\n"+
      "   <mapEntry>\n"+
      "      <double>1.0</double>\n"+
      "      <entry name='a' value='1'/>\n"+
      "   </mapEntry>\n"+
      "   <mapEntry>\n"+
      "      <double>2.0</double>\n"+
      "      <entry name='b' value='2'/>\n"+
      "   </mapEntry>\n"+
      "   <mapEntry>\n"+
      "      <double>3.0</double>\n"+
      "      <entry name='c' value='3'/>\n"+
      "   </mapEntry>\n"+
      "</exampleWithOriginals>\n";
      [Root]
      [Namespace(Prefix="entry", Reference="http://domain/entry")]
      private static class Entry {
         [Attribute]
         private String name;
         [Attribute]
         private String value;
         public Entry() {
            super();
         }
         public Entry(String name, String value) {
            this.name = name;
            this.value = value;
         }
         public bool Equals(Object entry) {
            if(entry instanceof Entry) {
               Entry other = (Entry) entry;
               return other.name.Equals(name) && other.value.Equals(value);
            }
            return false;
         }
         public int HashCode() {
            return name.HashCode() ^ value.HashCode();
         }
      }
      /*
      [Root]
      [NamespaceList({@Namespace(prefix="root", Reference="http://domain/entry")})]
      private static class ExampleWithOriginals {
         [ElementList]
         private Collection<String> list = new CopyOnWriteArrayList<String>();
         [ElementMap]
         private Map<String, Double> map = new ConcurrentHashMap<String, Double>();
         [ElementList(Inline=true, Entry="listEntry")]
         private Collection<Entry> inlineList = new CopyOnWriteArrayList<Entry>();
         [ElementMap(Inline=true, Entry="mapEntry")]
         private Map<Double, Entry> inlineMap = new ConcurrentHashMap<Double, Entry>();
         public ExampleWithOriginals() {
            this.list.add("original from constructor");
            this.map.put("original key", 1.0);
            this.inlineList.add(new Entry("original name", "original value"));
            this.inlineMap.put(7.0, new Entry("an original name", "an original value"));
         }
      }
      public void TestOriginals() {
         Persister persister = new Persister();
         ExampleWithOriginals original = persister.read(ExampleWithOriginals.class, SOURCE);
         persister.write(original, System.out);
         assertTrue(original.list.contains("original from constructor"));
         assertTrue(original.list.contains("a"));
         assertTrue(original.list.contains("b"));
         assertTrue(original.list.contains("c"));
         assertTrue(original.list.contains("d"));
         assertEquals(original.map.get("original key"), 1.0);
         assertEquals(original.map.get("a"), 1.0);
         assertEquals(original.map.get("b"), 2.0);
         assertEquals(original.map.get("c"), 3.0);
         assertTrue(original.inlineList.contains(new Entry("original name", "original value")));
         assertTrue(original.inlineList.contains(new Entry("a", "1")));
         assertTrue(original.inlineList.contains(new Entry("b", "2")));
         assertTrue(original.inlineList.contains(new Entry("c", "3")));
         assertTrue(original.inlineList.contains(new Entry("d", "4")));
         assertEquals(original.inlineMap.get(7.0), new Entry("an original name", "an original value"));
         assertEquals(original.inlineMap.get(1.0), (new Entry("a", "1")));
         assertEquals(original.inlineMap.get(2.0), (new Entry("b", "2")));
         assertEquals(original.inlineMap.get(3.0), (new Entry("c", "3")));
         validate(persister, original);
      }*/
      public void TestA() {
   }
}
