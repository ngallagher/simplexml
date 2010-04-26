#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class MethodScannerDefaultTest : TestCase {
      [Default(DefaultType.PROPERTY)]
      public static class NoAnnotations {
         private String[] array;
         private Map<String, String> map;
         private List<String> list;
         private Date date;
         private String customer;
         private String name;
         private int price;
         public Date Date {
            get {
               return date;
            }
            set {
               this.date = value;
            }
         }
         //public Date GetDate() {
         //   return date;
         //}
            this.array = array;
         }
         public String[] Array {
            get {
               return array;
            }
         }
         //public String[] GetArray() {
         //   return array;
         //}
            this.map = map;
         }
         public Map<String, String> getMap() {
            return map;
         }
         public List<String> List {
            set {
               this.list = value;
            }
         }
         //public void SetList(List<String> list) {
         //   this.list = list;
         //}
            return list;
         }
         //public void SetDate(Date date) {
         //   this.date = date;
         //}
            return customer;
         }
         public String Customer {
            set {
               this.customer = value;
            }
         }
         //public void SetCustomer(String customer) {
         //   this.customer = customer;
         //}
            return name;
         }
         public String Name {
            set {
               this.name = value;
            }
         }
         //public void SetName(String name) {
         //   this.name = name;
         //}
            return price;
         }
         public int Price {
            set {
               this.price = value;
            }
         }
         //public void SetPrice(int price) {
         //   this.price = price;
         //}
      [Default(DefaultType.PROPERTY)]
      public static class MixedAnnotations {
         private String[] array;
         private Map<String, String> map;
         private String name;
         private int value;
         [Attribute]
         public String Name {
            get {
               return name;
            }
            set {
               this.name = value;
            }
         }
         //public String GetName() {
         //   return name;
         //}
         //public void SetName(String name) {
         //   this.name = name;
         //}
         public int Value {
            get {
               return value;
            }
            set {
               this.value = _value;
            }
         }
         //public int GetValue() {
         //   return value;
         //}
         //public void SetValue(int value) {
         //   this.value = value;
         //}
            this.array = array;
         }
         public String[] Array {
            get {
               return array;
            }
         }
         //public String[] GetArray() {
         //   return array;
         //}
            this.map = map;
         }
         public Map<String, String> getMap() {
            return map;
         }
      }
      public static class ExtendedAnnotations : MixedAnnotations {
         [Element]
         public String[] Array {
            get {
               return super.Array;
            }
            set {
               super.Array = array;
            }
         }
         //public String[] GetArray() {
         //   return super.Array;
         //}
         //public void SetArray(String[] array) {
         //   super.Array = array;
         //}
      public void TestNoAnnotationsWithNoDefaults() {
         Map<String, Contact> map = getContacts(NoAnnotations.class, null);
         assertTrue(map.isEmpty());
      }
      public void TestMixedAnnotationsWithNoDefaults() {
         Map<String, Contact> map = getContacts(MixedAnnotations.class, null);
         AssertEquals(map.size(), 2);
         assertFalse(map.get("name").isReadOnly());
         assertFalse(map.get("value").isReadOnly());
         AssertEquals(int.class, map.get("value").getType());
         AssertEquals(String.class, map.get("name").getType());
         AssertEquals(Attribute.class, map.get("name").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("value").getAnnotation().annotationType());
         AssertEquals(Attribute.class, map.get("name").getAnnotation(Attribute.class).annotationType());
         AssertEquals(Element.class, map.get("value").getAnnotation(Element.class).annotationType());
         AssertNull(map.get("name").getAnnotation(Root.class));
         AssertNull(map.get("value").getAnnotation(Root.class));
      }
      public void TestExtendedAnnotations() {
         Map<String, Contact> map = getContacts(ExtendedAnnotations.class, DefaultType.PROPERTY);
         assertFalse(map.get("array").isReadOnly());
         assertFalse(map.get("map").isReadOnly());
         assertFalse(map.get("name").isReadOnly());
         assertFalse(map.get("value").isReadOnly());
         AssertEquals(String[].class, map.get("array").getType());
         AssertEquals(Map.class, map.get("map").getType());
         AssertEquals(int.class, map.get("value").getType());
         AssertEquals(String.class, map.get("name").getType());
         AssertEquals(Attribute.class, map.get("name").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("value").getAnnotation().annotationType());
         AssertEquals(ElementMap.class, map.get("map").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("array").getAnnotation().annotationType());
         AssertEquals(Attribute.class, map.get("name").getAnnotation(Attribute.class).annotationType());
         AssertEquals(Element.class, map.get("value").getAnnotation(Element.class).annotationType());
         AssertEquals(ElementMap.class, map.get("map").getAnnotation(ElementMap.class).annotationType());
         AssertEquals(Element.class, map.get("array").getAnnotation(Element.class).annotationType());
         AssertNull(map.get("name").getAnnotation(Root.class));
         AssertNull(map.get("value").getAnnotation(Root.class));
         AssertNull(map.get("map").getAnnotation(Root.class));
         AssertNull(map.get("array").getAnnotation(Root.class));
      }
      public void TestMixedAnnotations() {
         Map<String, Contact> map = getContacts(MixedAnnotations.class, DefaultType.PROPERTY);
         assertFalse(map.get("array").isReadOnly());
         assertFalse(map.get("map").isReadOnly());
         assertFalse(map.get("name").isReadOnly());
         assertFalse(map.get("value").isReadOnly());
         AssertEquals(String[].class, map.get("array").getType());
         AssertEquals(Map.class, map.get("map").getType());
         AssertEquals(int.class, map.get("value").getType());
         AssertEquals(String.class, map.get("name").getType());
         AssertEquals(Attribute.class, map.get("name").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("value").getAnnotation().annotationType());
         AssertEquals(ElementMap.class, map.get("map").getAnnotation().annotationType());
         AssertEquals(ElementArray.class, map.get("array").getAnnotation().annotationType());
         AssertEquals(Attribute.class, map.get("name").getAnnotation(Attribute.class).annotationType());
         AssertEquals(Element.class, map.get("value").getAnnotation(Element.class).annotationType());
         AssertEquals(ElementMap.class, map.get("map").getAnnotation(ElementMap.class).annotationType());
         AssertEquals(ElementArray.class, map.get("array").getAnnotation(ElementArray.class).annotationType());
         AssertNull(map.get("name").getAnnotation(Root.class));
         AssertNull(map.get("value").getAnnotation(Root.class));
         AssertNull(map.get("map").getAnnotation(Root.class));
         AssertNull(map.get("array").getAnnotation(Root.class));
      }
      public void TestNoAnnotations() {
         Map<String, Contact> map = getContacts(NoAnnotations.class, DefaultType.PROPERTY);
         assertFalse(map.get("date").isReadOnly());
         assertFalse(map.get("customer").isReadOnly());
         assertFalse(map.get("name").isReadOnly());
         assertFalse(map.get("price").isReadOnly());
         assertFalse(map.get("list").isReadOnly());
         assertFalse(map.get("map").isReadOnly());
         assertFalse(map.get("array").isReadOnly());
         AssertEquals(Date.class, map.get("date").getType());
         AssertEquals(String.class, map.get("customer").getType());
         AssertEquals(String.class, map.get("name").getType());
         AssertEquals(int.class, map.get("price").getType());
         AssertEquals(List.class, map.get("list").getType());
         AssertEquals(Map.class, map.get("map").getType());
         AssertEquals(String[].class, map.get("array").getType());
         AssertEquals(Element.class, map.get("date").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("customer").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("name").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("price").getAnnotation().annotationType());
         AssertEquals(ElementList.class, map.get("list").getAnnotation().annotationType());
         AssertEquals(ElementMap.class, map.get("map").getAnnotation().annotationType());
         AssertEquals(ElementArray.class, map.get("array").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("date").getAnnotation(Element.class).annotationType());
         AssertEquals(Element.class, map.get("customer").getAnnotation(Element.class).annotationType());
         AssertEquals(Element.class, map.get("name").getAnnotation(Element.class).annotationType());
         AssertEquals(Element.class, map.get("price").getAnnotation(Element.class).annotationType());
         AssertEquals(ElementList.class, map.get("list").getAnnotation(ElementList.class).annotationType());
         AssertEquals(ElementMap.class, map.get("map").getAnnotation(ElementMap.class).annotationType());
         AssertEquals(ElementArray.class, map.get("array").getAnnotation(ElementArray.class).annotationType());
         AssertNull(map.get("date").getAnnotation(Root.class));
         AssertNull(map.get("customer").getAnnotation(Root.class));
         AssertNull(map.get("name").getAnnotation(Root.class));
         AssertNull(map.get("price").getAnnotation(Root.class));
         AssertNull(map.get("list").getAnnotation(Root.class));
         AssertNull(map.get("map").getAnnotation(Root.class));
         AssertNull(map.get("array").getAnnotation(Root.class));
      }
      private static Map<String, Contact> getContacts(Class type, DefaultType defaultType) {
         MethodScanner scanner = new MethodScanner(type, defaultType);
         Map<String, Contact> map = new HashMap<String, Contact>();
         for(Contact contact : scanner) {
            map.put(contact.Name, contact);
         }
         return map;
      }
   }
}
