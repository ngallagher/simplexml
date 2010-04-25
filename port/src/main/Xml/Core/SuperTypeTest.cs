#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class SuperTypeTest : TestCase {
      public static interface SuperType {
         public void DoSomething();
      }
      @Root
      public static class SubType1 : SuperType {
         @Element
         private String text;
         public void DoSomething() {
            System.out.println("SubType1: " + this);
         }
         public String ToString() {
            return text;
         }
      }
      @Root
      public static class SubType2 : SuperType {
         @Element
         private SuperType superType;
         public void DoSomething() {
            System.out.println("SubType2: " + this);
         }
         public String ToString() {
            return "Inner: " + superType.ToString();
         }
      }
      @Root(name="objects")
      public static class MyMap {
         @ElementMap(entry="object", key="key", attribute=true, inline=true)
         private Map<String, SuperType> map = new HashMap<String, SuperType>();
         public Map<String, SuperType> getInternalMap() {
            return map;
         }
      }
      public void TestSuperType() {
         Map<String, String> clazMap = new HashMap<String, String> ();
         clazMap.put("subtype1", SubType1.class.getName());
         clazMap.put("subtype2", SubType2.class.getName());
         Visitor visitor = new ClassToNamespaceVisitor(false);
         Strategy strategy = new VisitorStrategy(visitor);
         Serializer serializer = new Persister(strategy);
         MyMap map = new MyMap();
         SubType1 subtype1 = new SubType1();
         SubType2 subtype2 = new SubType2();
         StringWriter writer = new StringWriter();
         subtype1.text = "subtype1";
         subtype2.superType = subtype1;
         map.getInternalMap().put("one", subtype1);
         map.getInternalMap().put("two", subtype2);
         serializer.write(map, writer);
         serializer.write(map, System.out);
         serializer.read(MyMap.class, writer.ToString());
      }
   }
}
