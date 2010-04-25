#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class MethodPartFactoryTest : TestCase {
      private static interface Bean {
         public abstract int Integer {
            get;
         }
         //public int GetInteger();
         public abstract List<String> List {
            get;
         }
         //public List<String> GetList();
         public Map<String, String> getMap();
         public abstract Map<String, String> Map {
            set;
         }
         //public void SetMap(Map<String, String> map);
         public abstract String[] Array {
            set;
         }
         //public void SetArray(String[] array);
      public void TestMethodPart() {
         assertTrue(Element.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("getInteger")).getAnnotation().getClass()));
         assertTrue(Element.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("setInteger", int.class)).getAnnotation().getClass()));
         assertTrue(ElementMap.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("getMap")).getAnnotation().getClass()));
         assertTrue(ElementMap.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("setMap", Map.class)).getAnnotation().getClass()));
         assertTrue(ElementList.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("getList")).getAnnotation().getClass()));
         assertTrue(ElementList.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("setList", List.class)).getAnnotation().getClass()));
         assertTrue(ElementArray.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("getArray")).getAnnotation().getClass()));
         assertTrue(ElementArray.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("setArray", String[].class)).getAnnotation().getClass()));
      }
   }
}
