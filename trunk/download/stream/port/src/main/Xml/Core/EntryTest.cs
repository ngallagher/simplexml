#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class EntryTest : TestCase {
      @Root
      private static class CompositeKey {
         @Attribute
         private String value;
         public String Value {
            get {
               return value;
            }
         }
         //public String GetValue() {
         //   return value;
         //}
      @ElementMap
      private Map<String, String> defaultMap;
      [ElementMap(KeyType=Integer.class, ValueType=Long.class)]
      private Dictionary annotatedMap;
      [ElementMap(Value="value")]
      private Map<String, String> bodyMap;
      [ElementMap(Value="value", Key="key", Attribute=true)]
      private Map<String, String> attributeMap;
      [ElementMap(Entry="entry")]
      private Map<Double, String> entryMap;
      @ElementMap
      private Map<CompositeKey, String> compositeMap;
      public void TestEntry() {
         Entry entry = GetEntry(EntryTest.class, "defaultMap");
         assertEquals(entry.getKeyType().getType(), String.class);
         assertEquals(entry.getValueType().getType(), String.class);
         assertEquals(entry.Value, null);
         assertEquals(entry.getKey(), null);
         assertEquals(entry.GetEntry(), "entry");
         entry = GetEntry(EntryTest.class, "annotatedMap");
         assertEquals(entry.getKeyType().getType(), Integer.class);
         assertEquals(entry.getValueType().getType(), Long.class);
         assertEquals(entry.Value, null);
         assertEquals(entry.getKey(), null);
         assertEquals(entry.GetEntry(), "entry");
         entry = GetEntry(EntryTest.class, "bodyMap");
         assertEquals(entry.getKeyType().getType(), String.class);
         assertEquals(entry.getValueType().getType(), String.class);
         assertEquals(entry.Value, "value");
         assertEquals(entry.getKey(), null);
         assertEquals(entry.GetEntry(), "entry");
         entry = GetEntry(EntryTest.class, "attributeMap");
         assertEquals(entry.getKeyType().getType(), String.class);
         assertEquals(entry.getValueType().getType(), String.class);
         assertEquals(entry.Value, "value");
         assertEquals(entry.getKey(), "key");
         assertEquals(entry.GetEntry(), "entry");
         entry = GetEntry(EntryTest.class, "entryMap");
         assertEquals(entry.getKeyType().getType(), Double.class);
         assertEquals(entry.getValueType().getType(), String.class);
         assertEquals(entry.Value, null);
         assertEquals(entry.getKey(), null);
         assertEquals(entry.GetEntry(), "entry");
         entry = GetEntry(EntryTest.class, "compositeMap");
         assertEquals(entry.getKeyType().getType(), CompositeKey.class);
         assertEquals(entry.getValueType().getType(), String.class);
         assertEquals(entry.Value, null);
         assertEquals(entry.getKey(), null);
         assertEquals(entry.GetEntry(), "entry");
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
