#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ErasureHandlingTest : ValidationTestCase {
      @Root
      @Default
      private static class ErasureExample<T> {
         private Map<String, T> list = new LinkedHashMap<String, T>();
         private Map<T, T> doubleGeneric = new LinkedHashMap<T, T>();
         public void AddItem(String key, T item) {
            list.put(key, item);
         }
         public T GetItem(String key) {
            return list.get(key);
         }
         public void AddDoubleGeneric(T key, T item) {
            doubleGeneric.put(key, item);
         }
      }
      @Root
      @Default
      private static class ErasureItem {
         private readonly String name;
         private readonly String value;
         public ErasureItem(@Element(name="name", required=false) String name, @Element(name="value", required=false) String value) {
            this.name = name;
            this.value = value;
         }
      }
      @Root
      private static class ErasureWithMapAttributeIllegalExample<T> {
         @ElementMap(attribute=true)
         private Map<T, String> erasedToString = new HashMap<T, String>();
         public void AddItem(T key, String value) {
            erasedToString.put(key, value);
         }
      }
      @Root
      private static class ErasureWithMapInlineValueIsIgnoredExample<T> {
         @ElementMap(attribute=true, inline=true, value="value", key="key")
         private Map<String, T> erasedToString = new LinkedHashMap<String, T>();
         public void AddItem(String key, T value) {
            erasedToString.put(key, value);
         }
         public T GetItem(String key) {
            return erasedToString.get(key);
         }
      }
      private static enum ErasureEnum {
         A, B, C, D
      }
      public void TestErasure() {
         Visitor visitor = new ClassToNamespaceVisitor();
         Strategy strategy = new VisitorStrategy(visitor);
         Persister persister = new Persister(strategy);
         ErasureExample<ErasureItem> example = new ErasureExample<ErasureItem>();
         StringWriter writer = new StringWriter();
         example.AddItem("a", new ErasureItem("A", "1"));
         example.AddItem("b", new ErasureItem("B", "2"));
         example.AddItem("c", new ErasureItem("C", "3"));
         example.AddDoubleGeneric(new ErasureItem("1", "1"), new ErasureItem("A", "1"));
         example.AddDoubleGeneric(new ErasureItem("2", "2"), new ErasureItem("B", "2"));
         example.AddDoubleGeneric(new ErasureItem("3", "3"), new ErasureItem("C", "3"));
         persister.write(example, writer);
         String text = writer.toString();
         System.out.println(text);
         ErasureExample<ErasureItem> exampleCopy = persister.read(ErasureExample.class, text);
         assertEquals(exampleCopy.GetItem("a").name, "A");
         assertEquals(exampleCopy.GetItem("b").name, "B");
         assertEquals(exampleCopy.GetItem("c").name, "C");
         validate(example, persister);
      }
      public void TestPrimitiveErasure() {
         Visitor visitor = new ClassToNamespaceVisitor();
         Strategy strategy = new VisitorStrategy(visitor);
         Persister persister = new Persister(strategy);
         ErasureExample<Double> example = new ErasureExample<Double>();
         example.AddItem("a", 2.0);
         example.AddItem("b", 1.2);
         example.AddItem("c", 5.4);
         example.AddDoubleGeneric(7.8, 8.7);
         example.AddDoubleGeneric(1.2, 2.1);
         example.AddDoubleGeneric(3.1, 1.3);
         persister.write(example, System.out);
         validate(example, persister);
      }
      public void TestEnumErasure() {
         Visitor visitor = new ClassToNamespaceVisitor();
         Strategy strategy = new VisitorStrategy(visitor);
         Persister persister = new Persister(strategy);
         ErasureExample<ErasureEnum> example = new ErasureExample<ErasureEnum>();
         example.AddItem("a", ErasureEnum.A);
         example.AddItem("b", ErasureEnum.B);
         example.AddItem("c", ErasureEnum.C);
         example.AddDoubleGeneric(ErasureEnum.A, ErasureEnum.B);
         example.AddDoubleGeneric(ErasureEnum.B, ErasureEnum.C);
         example.AddDoubleGeneric(ErasureEnum.C, ErasureEnum.D);
         persister.write(example, System.out);
         validate(example, persister);
      }
      public void TestErasureWithMapAttributeIllegalExample() {
         Visitor visitor = new ClassToNamespaceVisitor();
         Strategy strategy = new VisitorStrategy(visitor);
         Persister persister = new Persister(strategy);
         ErasureWithMapAttributeIllegalExample<ErasureEnum> example = new ErasureWithMapAttributeIllegalExample<ErasureEnum>();
         bool failure = false;
         example.AddItem(ErasureEnum.A, "a");
         example.AddItem(ErasureEnum.B, "b");
         example.AddItem(ErasureEnum.C, "c");
         try {
            persister.write(example, System.out);
         }catch(Exception e) {
            e.printStackTrace();
            failure = true;
         }
         assertTrue("Attribute should not be possible with an erased key", failure);
      }
      public void TestErasureWithMapInlineValueIsIgnoredExample() {
         Persister persister = new Persister();
         ErasureWithMapInlineValueIsIgnoredExample<ErasureItem> example = new ErasureWithMapInlineValueIsIgnoredExample<ErasureItem>();
         StringWriter writer = new StringWriter();
         example.AddItem("a", new ErasureItem("A", "1"));
         example.AddItem("b", new ErasureItem("B", "2"));
         example.AddItem("c", new ErasureItem("C", "3"));
         persister.write(example, writer);
         String text = writer.toString();
         System.out.println(text);
         assertElementHasAttribute(text, "/erasureWithMapInlineValueIsIgnoredExample/entry[0]", "key", "a");
         assertElementHasAttribute(text, "/erasureWithMapInlineValueIsIgnoredExample/entry[1]", "key", "b");
         assertElementHasAttribute(text, "/erasureWithMapInlineValueIsIgnoredExample/entry[2]", "key", "c");
         assertElementHasAttribute(text, "/erasureWithMapInlineValueIsIgnoredExample/entry[0]/value", "class", ErasureItem.class.getName());
         assertElementHasAttribute(text, "/erasureWithMapInlineValueIsIgnoredExample/entry[1]/value", "class", ErasureItem.class.getName());
         assertElementHasAttribute(text, "/erasureWithMapInlineValueIsIgnoredExample/entry[2]/value", "class", ErasureItem.class.getName());
         assertElementHasValue(text, "/erasureWithMapInlineValueIsIgnoredExample/entry[0]/value/name", "A");
         assertElementHasValue(text, "/erasureWithMapInlineValueIsIgnoredExample/entry[1]/value/name", "B");
         assertElementHasValue(text, "/erasureWithMapInlineValueIsIgnoredExample/entry[2]/value/name", "C");
         assertElementHasValue(text, "/erasureWithMapInlineValueIsIgnoredExample/entry[0]/value/value", "1");
         assertElementHasValue(text, "/erasureWithMapInlineValueIsIgnoredExample/entry[1]/value/value", "2");
         assertElementHasValue(text, "/erasureWithMapInlineValueIsIgnoredExample/entry[2]/value/value", "3");
         System.out.println(text);
         ErasureWithMapInlineValueIsIgnoredExample<ErasureItem> exampleCopy = persister.read(ErasureWithMapInlineValueIsIgnoredExample.class, text);
         assertEquals(exampleCopy.GetItem("a").name, "A");
         assertEquals(exampleCopy.GetItem("b").name, "B");
         assertEquals(exampleCopy.GetItem("c").name, "C");
         assertEquals(exampleCopy.GetItem("a").value, "1");
         assertEquals(exampleCopy.GetItem("b").value, "2");
         assertEquals(exampleCopy.GetItem("c").value, "3");
         validate(exampleCopy, persister);
      }
   }
}
