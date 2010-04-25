#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class NullArrayEntryTest : ValidationTestCase {
      private const String LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<exampleArray>\n"+
      "   <list length='3'>\n"+
      "      <substitute id='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </substitute>\n\r"+
      "      <substitute/>\n"+
      "      <substitute/>\n"+
      "   </list>\n"+
      "</exampleArray>";
      private const String PRIMITIVE_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<examplePrimitiveArray>\n"+
      "  <list length='4'>\r\n" +
      "     <substitute>a</substitute>\n"+
      "     <substitute>b</substitute>\n"+
      "     <substitute/>\r\n"+
      "     <substitute/>\r\n" +
      "  </list>\r\n" +
      "</examplePrimitiveArray>";
      [Root]
      private static class Entry {
         [Attribute]
         private int id;
         [Element]
         private String text;
         public Entry() {
            super();
         }
         public Entry(String text, int id) {
            this.id = id;
            this.text = text;
         }
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
         private String[] list;
         public String[] Array {
            get {
               return list;
            }
         }
         //public String[] GetArray() {
         //   return list;
         //}
      public void TestExampleArray() {
         Serializer serializer = new Persister();
         ExampleArray list = serializer.read(ExampleArray.class, LIST);
         list.list = new Entry[] { new Entry("a", 1), new Entry("b", 2), null, null, new Entry("e", 5) };
         serializer.write(list, System.out);
         validate(list, serializer);
      }
      public void TestExamplePrimitiveArray() {
         Serializer serializer = new Persister();
         ExamplePrimitiveArray list = serializer.read(ExamplePrimitiveArray.class, PRIMITIVE_LIST);
         list.list = new String[] { "a", "b", null, null, "e", null, "f" };
         serializer.write(list, System.out);
         validate(list, serializer);
      }
   }
}
