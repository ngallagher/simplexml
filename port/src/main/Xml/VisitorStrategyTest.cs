#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class VisitorStrategyTest : ValidationTestCase {
      @Root
      @Default
      private static class VisitorExample {
         private List<String> items;
         private Map<String, String> map;
         public VisitorExample() {
            this.map = new HashMap<String, String>();
            this.items = new ArrayList<String>();
         }
         public void Put(String name, String value) {
            map.Put(name, value);
         }
         public void Add(String value) {
            items.Add(value);
         }
      }
      public void TestStrategy() {
         Visitor visitor = new ClassToNamespaceVisitor();
         Strategy strategy = new VisitorStrategy(visitor);
         Persister persister = new Persister(strategy);
         VisitorExample item = new VisitorExample();
         StringWriter writer = new StringWriter();
         item.Put("1", "ONE");
         item.Put("2", "TWO");
         item.Add("A");
         item.Add("B");
         persister.write(item, writer);
         String text = writer.toString();
         System.out.println(text);
         VisitorExample recover = persister.read(VisitorExample.class, text);
         assertTrue(recover.map.containsKey("1"));
         assertTrue(recover.map.containsKey("2"));
         assertTrue(recover.items.contains("A"));
         assertTrue(recover.items.contains("B"));
         validate(recover, persister);
      }
   }
}
