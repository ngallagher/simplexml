#region Using directives
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class CycleStrategyTest : TestCase {
      private const String ARRAY =
      "<array id='1' length='12' class='java.lang.String'/>";
      private const String OBJECT =
      "<array id='1' class='java.lang.String'/>";
      private const String REFERENCE =
      "<array reference='1' class='java.lang.String'/>";
      private static class Entry : Type {
         private readonly Class type;
         public Entry(Class type) {
            this.type = type;
         }
         public <T : Annotation> T getAnnotation(Class<T> type) {
            return null;
         }
         public Class Type {
            get {
               return type;
            }
         }
         //public Class GetType() {
         //   return type;
         //}
      public void TestArray() {
         Dictionary map = new HashMap();
         StringReader reader = new StringReader(ARRAY);
         CycleStrategy strategy = new CycleStrategy();
         InputNode event = NodeBuilder.read(reader);
         NodeMap attributes = event.getAttributes();
         Value value = strategy.read(new Entry(String[].class), attributes, map);
         AssertEquals(12, value.getLength());
         AssertEquals(null, value.getValue());
         AssertEquals(String.class, value.Type);
         AssertEquals(false, value.isReference());
      }
      public void TestObject() {
         Dictionary map = new HashMap();
         StringReader reader = new StringReader(OBJECT);
         CycleStrategy strategy = new CycleStrategy();
         InputNode event = NodeBuilder.read(reader);
         NodeMap attributes = event.getAttributes();
         Value value = strategy.read(new Entry(String.class), attributes, map);
         AssertEquals(0, value.getLength());
         AssertEquals(null, value.getValue());
         AssertEquals(String.class, value.Type);
         AssertEquals(false, value.isReference());
      }
      public void TestReference() {
         StringReader reader = new StringReader(REFERENCE);
         Contract contract = new Contract("id", "reference", "class", "length");
         ReadGraph graph = new ReadGraph(contract, new Loader());
         InputNode event = NodeBuilder.read(reader);
         NodeMap attributes = event.getAttributes();
         graph.put("1", "first text");
         graph.put("2", "second text");
         graph.put("3", "third text");
         Value value = graph.read(new Entry(String.class), attributes);
         AssertEquals(0, value.getLength());
         AssertEquals("first text", value.getValue());
         AssertEquals(String.class, value.Type);
         AssertEquals(true, value.isReference());
      }
   }
}
