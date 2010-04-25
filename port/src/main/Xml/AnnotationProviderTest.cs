#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class AnnotationProviderTest : TestCase {
      public void TestProvider() {
         assertTrue(ElementMap.class.isAssignableFrom(new AnnotationFactory().getInstance(Map.class).getClass()));
         assertTrue(ElementMap.class.isAssignableFrom(new AnnotationFactory().getInstance(HashMap.class).getClass()));
         assertTrue(ElementMap.class.isAssignableFrom(new AnnotationFactory().getInstance(ConcurrentHashMap.class).getClass()));
         assertTrue(ElementMap.class.isAssignableFrom(new AnnotationFactory().getInstance(LinkedHashMap.class).getClass()));
         assertTrue(ElementMap.class.isAssignableFrom(new AnnotationFactory().getInstance(Map.class).getClass()));
         assertTrue(ElementList.class.isAssignableFrom(new AnnotationFactory().getInstance(Set.class).getClass()));
         assertTrue(ElementList.class.isAssignableFrom(new AnnotationFactory().getInstance(Collection.class).getClass()));
         assertTrue(ElementList.class.isAssignableFrom(new AnnotationFactory().getInstance(List.class).getClass()));
         assertTrue(ElementList.class.isAssignableFrom(new AnnotationFactory().getInstance(TreeSet.class).getClass()));
         assertTrue(ElementList.class.isAssignableFrom(new AnnotationFactory().getInstance(HashSet.class).getClass()));
         assertTrue(ElementList.class.isAssignableFrom(new AnnotationFactory().getInstance(ArrayList.class).getClass()));
      }
   }
}
