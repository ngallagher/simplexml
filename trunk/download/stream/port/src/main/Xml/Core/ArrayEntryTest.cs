#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ArrayEntryTest : ValidationTestCase {
      private const String LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<exampleArray>\n"+
      "   <list length='3'>\n"+
      "      <substitute id='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </substitute>\n\r"+
      "      <substitute id='2'>\n"+
      "         <text>two</text>  \n\r"+
      "      </substitute>\n"+
      "      <substitute id='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </substitute>\n"+
      "   </list>\n"+
      "</exampleArray>";
      private const String PRIMITIVE_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<examplePrimitiveArray>\n"+
      "  <list length='4'>\r\n" +
      "     <substitute>a</substitute>\n"+
      "     <substitute>b</substitute>\n"+
      "     <substitute>c</substitute>\n"+
      "     <substitute>d</substitute>\n"+
      "  </list>\r\n" +
      "</examplePrimitiveArray>";
      [Root]
      private static class Entry {
         [Attribute]
         private int id;
         [Element]
         private String text;
         public String Text {
            get {
               return text;
            }
         }
         //public String GetText() {
         //   return text;
         //}
            return id;
         }
      }
      [Root]
      private static class ExampleArray {
         [ElementArray(Name="list", Entry="substitute")]
         private Entry[] list;
         public Entry[] Array {
            get {
               return list;
            }
         }
         //public Entry[] GetArray() {
         //   return list;
         //}
      [Root]
      private static class ExamplePrimitiveArray {
         [ElementArray(Name="list", Entry="substitute")]
         private Character[] list;
         public Character[] Array {
            get {
               return list;
            }
         }
         //public Character[] GetArray() {
         //   return list;
         //}
      public void TestExampleArray() {
         Serializer serializer = new Persister();
         ExampleArray list = serializer.read(ExampleArray.class, LIST);
         validate(list, serializer);
      }
      public void TestExamplePrimitiveArray() {
         Serializer serializer = new Persister();
         ExamplePrimitiveArray list = serializer.read(ExamplePrimitiveArray.class, PRIMITIVE_LIST);
         validate(list, serializer);
      }
   }
}
