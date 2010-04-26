#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class EntryTest : TestCase {
      [Root]
      private static class CompositeKey {
         [Attribute]
         private String value;
         public String Value {
            get {
               return value;
            }
         }
         //public String GetValue() {
         //   return value;
         //}
      [ElementMap]
      private Map<String, String> defaultMap;
      [ElementMap(KeyType=Integer.class, ValueType=Long.class)]
      private Dictionary annotatedMap;
      [ElementMap(Value="value")]
      private Map<String, String> bodyMap;
      [ElementMap(Value="value", Key="key", Attribute=true)]
      private Map<String, String> attributeMap;
      [ElementMap(Entry="entry")]
      private Map<Double, String> entryMap;
      [ElementMap]
      private Map<CompositeKey, String> compositeMap;
      public void TestEntry() {
         Entry entry = GetEntry(EntryTest.class, "defaultMap");
         AssertEquals(entry.getKeyType().getType(), String.class);
         AssertEquals(entry.getValueType().getType(), String.class);
         AssertEquals(entry.Value, null);
         AssertEquals(entry.getKey(), null);
         AssertEquals(entry.GetEntry(), "entry");
         entry = GetEntry(EntryTest.class, "annotatedMap");
         AssertEquals(entry.getKeyType().getType(), Integer.class);
         AssertEquals(entry.getValueType().getType(), Long.class);
         AssertEquals(entry.Value, null);
         AssertEquals(entry.getKey(), null);
         AssertEquals(entry.GetEntry(), "entry");
         entry = GetEntry(EntryTest.class, "bodyMap");
         AssertEquals(entry.getKeyType().getType(), String.class);
         AssertEquals(entry.getValueType().getType(), String.class);
         AssertEquals(entry.Value, "value");
         AssertEquals(entry.getKey(), null);
         AssertEquals(entry.GetEntry(), "entry");
         entry = GetEntry(EntryTest.class, "attributeMap");
         AssertEquals(entry.getKeyType().getType(), String.class);
         AssertEquals(entry.getValueType().getType(), String.class);
         AssertEquals(entry.Value, "value");
         AssertEquals(entry.getKey(), "key");
         AssertEquals(entry.GetEntry(), "entry");
         entry = GetEntry(EntryTest.class, "entryMap");
         AssertEquals(entry.getKeyType().getType(), Double.class);
         AssertEquals(entry.getValueType().getType(), String.class);
         AssertEquals(entry.Value, null);
         AssertEquals(entry.getKey(), null);
         AssertEquals(entry.GetEntry(), "entry");
         entry = GetEntry(EntryTest.class, "compositeMap");
         AssertEquals(entry.getKeyType().getType(), CompositeKey.class);
         AssertEquals(entry.getValueType().getType(), String.class);
         AssertEquals(entry.Value, null);
         AssertEquals(entry.getKey(), null);
         AssertEquals(entry.GetEntry(), "entry");
      }
      public Entry GetEntry(Class type, String name) {
         Contact contact = GetContact(EntryTest.class, name);
         ElementMap label = GetField(EntryTest.class, name).GetAnnotation(ElementMap.class);
         Entry entry = new Entry(contact, label);
         return entry;
      }
      public Contact GetContact(Class type, String name) {
         Field field = GetField(type, name);
         Annotation label = field.GetAnnotation(ElementMap.class);
         return new FieldContact(field, label);
      }
      public Annotation GetAnnotation(Field field) {
         Annotation[] list = field.getDeclaredAnnotations();
         for(Annotation label : list) {
            if(label instanceof ElementMap) {
               return label;
            }
         }
         return null;
      }
      public Field GetField(Class type, String name) {
         return type.getDeclaredField(name);
      }
   }
}
