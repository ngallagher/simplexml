#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class AnnotationHandlerTest : TestCase {
      @ElementArray
      @ElementList
      @ElementMap
      @Element
      public void TestHandler() {
         AnnotationHandler elementHandler = new AnnotationHandler(Element.class);
         Element element = getClass().getDeclaredMethod("testHandler").getAnnotation(Element.class);
         System.err.println(elementHandler);
         System.err.println(element);
         AnnotationHandler elementListHandler = new AnnotationHandler(ElementList.class);
         ElementList elementList = getClass().getDeclaredMethod("testHandler").getAnnotation(ElementList.class);
         System.err.println(elementListHandler);
         System.err.println(elementList);
         AnnotationHandler elementMapHandler = new AnnotationHandler(ElementMap.class);
         ElementMap elementMap = getClass().getDeclaredMethod("testHandler").getAnnotation(ElementMap.class);
         System.err.println(elementMapHandler);
         System.err.println(elementMap);
         AnnotationHandler elementArrayHandler = new AnnotationHandler(ElementArray.class);
         ElementArray elementArray = getClass().getDeclaredMethod("testHandler").getAnnotation(ElementArray.class);
         System.err.println(elementArrayHandler);
         System.err.println(elementArray);
      }
   }
}
