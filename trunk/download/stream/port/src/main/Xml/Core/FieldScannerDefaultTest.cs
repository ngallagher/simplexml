#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class FieldScannerDefaultTest : TestCase {
      [Default(DefaultType.FIELD)]
      private static class NoAnnotations {
         private String name;
         private int value;
         private Date date;
         private Locale locale;
         private int[] array;
         private List<String> list;
         private Map<String, String> map;
      }
      [Default(DefaultType.FIELD)]
      private static class MixedAnnotations {
         private String name;
         private @Attribute int value;
         private @Transient Date date;
         private List<String> list;
      }
      public void TestMixedAnnotations() {
         Map<String, Contact> map = getContacts(MixedAnnotations.class, DefaultType.FIELD);
         AssertEquals(map.size(), 3);
         assertFalse(map.get("name").isReadOnly());
         assertFalse(map.get("value").isReadOnly());
         assertFalse(map.get("list").isReadOnly());
         AssertEquals(String.class, map.get("name").getType());
         AssertEquals(int.class, map.get("value").getType());
         AssertEquals(List.class, map.get("list").getType());
         AssertEquals(Element.class, map.get("name").getAnnotation().annotationType());
         AssertEquals(Attribute.class, map.get("value").getAnnotation().annotationType());
         AssertEquals(ElementList.class, map.get("list").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("name").getAnnotation(Element.class).annotationType());
         AssertEquals(Attribute.class, map.get("value").getAnnotation(Attribute.class).annotationType());
         AssertEquals(ElementList.class, map.get("list").getAnnotation(ElementList.class).annotationType());
         AssertNull(map.get("name").getAnnotation(Root.class));
         AssertNull(map.get("value").getAnnotation(Root.class));
         AssertNull(map.get("list").getAnnotation(Root.class));
      }
      public void TestNoAnnotations() {
         Map<String, Contact> map = getContacts(NoAnnotations.class, DefaultType.FIELD);
         assertFalse(map.get("name").isReadOnly());
         assertFalse(map.get("value").isReadOnly());
         assertFalse(map.get("date").isReadOnly());
         assertFalse(map.get("locale").isReadOnly());
         assertFalse(map.get("array").isReadOnly());
         assertFalse(map.get("list").isReadOnly());
         assertFalse(map.get("map").isReadOnly());
         AssertEquals(String.class, map.get("name").getType());
         AssertEquals(int.class, map.get("value").getType());
         AssertEquals(Date.class, map.get("date").getType());
         AssertEquals(Locale.class, map.get("locale").getType());
         AssertEquals(int[].class, map.get("array").getType());
         AssertEquals(List.class, map.get("list").getType());
         AssertEquals(Map.class, map.get("map").getType());
         AssertEquals(Element.class, map.get("name").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("value").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("date").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("locale").getAnnotation().annotationType());
         AssertEquals(ElementArray.class, map.get("array").getAnnotation().annotationType());
         AssertEquals(ElementList.class, map.get("list").getAnnotation().annotationType());
         AssertEquals(ElementMap.class, map.get("map").getAnnotation().annotationType());
         AssertEquals(Element.class, map.get("name").getAnnotation(Element.class).annotationType());
         AssertEquals(Element.class, map.get("value").getAnnotation(Element.class).annotationType());
         AssertEquals(Element.class, map.get("date").getAnnotation(Element.class).annotationType());
         AssertEquals(Element.class, map.get("locale").getAnnotation(Element.class).annotationType());
         AssertEquals(ElementArray.class, map.get("array").getAnnotation(ElementArray.class).annotationType());
         AssertEquals(ElementList.class, map.get("list").getAnnotation(ElementList.class).annotationType());
         AssertEquals(ElementMap.class, map.get("map").getAnnotation(ElementMap.class).annotationType());
         AssertNull(map.get("name").getAnnotation(Root.class));
         AssertNull(map.get("value").getAnnotation(Root.class));
         AssertNull(map.get("date").getAnnotation(Root.class));
         AssertNull(map.get("locale").getAnnotation(Root.class));
         AssertNull(map.get("array").getAnnotation(Root.class));
         AssertNull(map.get("list").getAnnotation(Root.class));
         AssertNull(map.get("map").getAnnotation(Root.class));
      }
      private static Map<String, Contact> getContacts(Class type, DefaultType defaultType) {
         FieldScanner scanner = new FieldScanner(type, defaultType);
         Map<String, Contact> map = new HashMap<String, Contact>();
         for(Contact contact : scanner) {
            map.put(contact.getName(), contact);
         }
         return map;
      }
   }
}
