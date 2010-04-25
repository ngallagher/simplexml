#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class InlineListWithDataTest : ValidationTestCase {
      @Root
      private static class ListWithDataExample {
         private @ElementList(inline=true, data=true) List<String> list;
         public ListWithDataExample(){
            this.list = new ArrayList<String>();
         }
         public void AddValue(String value) {
            list.add(value);
         }
         public List<String> List {
            get {
               return list;
            }
         }
         //public List<String> GetList() {
         //   return list;
         //}
      @Root
      private static class MapWithDataExample {
         private @ElementMap(inline=true, data=true, attribute=true) Map<String, String> map;
         public MapWithDataExample(){
            this.map = new LinkedHashMap<String, String>();
         }
         public void PutValue(String name, String value) {
            map.put(name, value);
         }
         public  Map<String, String> List {
            return map;
         }
      }
      public void TestListWithData() {
         Persister persister = new Persister();
         ListWithDataExample example = new ListWithDataExample();
         StringWriter writer = new StringWriter();
         example.AddValue("A");
         example.AddValue("B");
         example.AddValue("C");
         persister.write(example, writer);
         String text = writer.toString();
         System.out.println(text);
         assertElementHasCDATA(text, "/listWithDataExample/string[0]", "A");
         assertElementHasCDATA(text, "/listWithDataExample/string[1]", "B");
         assertElementHasCDATA(text, "/listWithDataExample/string[2]", "C");
         validate(example, persister);
      }
      public void TestMapWithData() {
         Persister persister = new Persister();
         MapWithDataExample example = new MapWithDataExample();
         StringWriter writer = new StringWriter();
         example.PutValue("A", "1");
         example.PutValue("B", "2");
         example.PutValue("C", "3");
         persister.write(example, writer);
         String text = writer.toString();
         System.out.println(text);
         assertElementHasCDATA(text, "/mapWithDataExample/entry[0]", "1");
         assertElementHasCDATA(text, "/mapWithDataExample/entry[1]", "2");
         assertElementHasCDATA(text, "/mapWithDataExample/entry[2]", "3");
         validate(example, persister);
      }
   }
}
