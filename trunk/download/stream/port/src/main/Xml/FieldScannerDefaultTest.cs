#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class FieldScannerDefaultTest : TestCase {
      @Default(DefaultType.FIELD)
      private static class NoAnnotations {
         private String name;
         private int value;
         private Date date;
         private Locale locale;
         private int[] array;
         private List<String> list;
         private Map<String, String> map;
      }
      @Default(DefaultType.FIELD)
      private static class MixedAnnotations {
         private String name;
         private @Attribute int value;
         private @Transient Date date;
         private List<String> list;
      }
      public void TestMixedAnnotations() {
         Map<String, Contact> map = getContacts(MixedAnnotations.class, DefaultType.FIELD);
         assertEquals(map.size(), 3);
         assertFalse(map.get("name").isReadOnly());
         assertFalse(map.get("value").isReadOnly());
         assertFalse(map.get("list").isReadOnly());
         assertEquals(String.class, map.get("name").getType());
         assertEquals(int.class, map.get("value").getType());
         assertEquals(List.class, map.get("list").getType());
         assertEquals(Element.class, map.get("name").getAnnotation().annotationType());
         assertEquals(Attribute.class, map.get("value").getAnnotation().annotationType());
         assertEquals(ElementList.class, map.get("list").getAnnotation().annotationType());
         assertEquals(Element.class, map.get("name").getAnnotation(Element.class).annotationType());
         assertEquals(Attribute.class, map.get("value").getAnnotation(Attribute.class).annotationType());
         assertEquals(ElementList.class, map.get("list").getAnnotation(ElementList.class).annotationType());
         assertNull(map.get("name").getAnnotation(Root.class));
         assertNull(map.get("value").getAnnotation(Root.class));
         assertNull(map.get("list").getAnnotation(Root.class));
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
         assertEquals(String.class, map.get("name").getType());
         assertEquals(int.class, map.get("value").getType());
         assertEquals(Date.class, map.get("date").getType());
         assertEquals(Locale.class, map.get("locale").getType());
         assertEquals(int[].class, map.get("array").getType());
         assertEquals(List.class, map.get("list").getType());
         assertEquals(Map.class, map.get("map").getType());
         assertEquals(Element.class, map.get("name").getAnnotation().annotationType());
         assertEquals(Element.class, map.get("value").getAnnotation().annotationType());
         assertEquals(Element.class, map.get("date").getAnnotation().annotationType());
         assertEquals(Element.class, map.get("locale").getAnnotation().annotationType());
         assertEquals(ElementArray.class, map.get("array").getAnnotation().annotationType());
         assertEquals(ElementList.class, map.get("list").getAnnotation().annotationType());
         assertEquals(ElementMap.class, map.get("map").getAnnotation().annotationType());
         assertEquals(Element.class, map.get("name").getAnnotation(Element.class).annotationType());
         assertEquals(Element.class, map.get("value").getAnnotation(Element.class).annotationType());
         assertEquals(Element.class, map.get("date").getAnnotation(Element.class).annotationType());
         assertEquals(Element.class, map.get("locale").getAnnotation(Element.class).annotationType());
         assertEquals(ElementArray.class, map.get("array").getAnnotation(ElementArray.class).annotationType());
         assertEquals(ElementList.class, map.get("list").getAnnotation(ElementList.class).annotationType());
         assertEquals(ElementMap.class, map.get("map").getAnnotation(ElementMap.class).annotationType());
         assertNull(map.get("name").getAnnotation(Root.class));
         assertNull(map.get("value").getAnnotation(Root.class));
         assertNull(map.get("date").getAnnotation(Root.class));
         assertNull(map.get("locale").getAnnotation(Root.class));
         assertNull(map.get("array").getAnnotation(Root.class));
         assertNull(map.get("list").getAnnotation(Root.class));
         assertNull(map.get("map").getAnnotation(Root.class));
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
