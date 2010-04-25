#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class CollectionEntryTest : ValidationTestCase {
      private const String LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<exampleCollection>\n"+
      "   <list>\n"+
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
      "</exampleCollection>";
      private const String INLINE_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<exampleInlineCollection>\n"+
      "   <substitute id='1'>\n"+
      "      <text>one</text>  \n\r"+
      "   </substitute>\n\r"+
      "   <substitute id='2'>\n"+
      "      <text>two</text>  \n\r"+
      "   </substitute>\n"+
      "   <substitute id='3'>\n"+
      "      <text>three</text>  \n\r"+
      "   </substitute>\n"+
      "</exampleInlineCollection>";
      private const String INLINE_PRIMITIVE_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<examplePrimitiveCollection>\n"+
      "   <substitute>a</substitute>\n"+
      "   <substitute>b</substitute>\n"+
      "   <substitute>c</substitute>\n"+
      "   <substitute>d</substitute>\n"+
      "</examplePrimitiveCollection>";
      private const String PRIMITIVE_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<examplePrimitiveCollection>\n"+
      "  <list>\r\n" +
      "     <substitute>a</substitute>\n"+
      "     <substitute>b</substitute>\n"+
      "     <substitute>c</substitute>\n"+
      "     <substitute>d</substitute>\n"+
      "  </list>\r\n" +
      "</examplePrimitiveCollection>";
      @Root
      private static class Entry {
         @Attribute
         private int id;
         @Element
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
      @Root
      private static class ExampleCollection {
         [ElementList(Name="list", Entry="substitute")]
         private List<Entry> list;
         public List<Entry> List {
            get {
               return list;
            }
         }
         //public List<Entry> GetList() {
         //   return list;
         //}
      @Root
      private static class ExampleInlineCollection {
         [ElementList(Name="list", Entry="substitute", Inline=true)]
         private List<Entry> list;
         public List<Entry> List {
            get {
               return list;
            }
         }
         //public List<Entry> GetList() {
         //   return list;
         //}
      @Root
      private static class ExamplePrimitiveCollection {
         [ElementList(Name="list", Entry="substitute")]
         private List<Character> list;
         public List<Character> List {
            get {
               return list;
            }
         }
         //public List<Character> GetList() {
         //   return list;
         //}
      @Root
      private static class ExamplePrimitiveInlineCollection {
         [ElementList(Name="list", Entry="substitute", Inline=true)]
         private List<String> list;
         public List<String> List {
            get {
               return list;
            }
         }
         //public List<String> GetList() {
         //   return list;
         //}
      public void TestExampleCollection() {
         Serializer serializer = new Persister();
         ExampleCollection list = serializer.read(ExampleCollection.class, LIST);
         validate(list, serializer);
      }
      public void TestExampleInlineCollection() {
         Serializer serializer = new Persister();
         ExampleInlineCollection list = serializer.read(ExampleInlineCollection.class, INLINE_LIST);
         validate(list, serializer);
      }
      public void TestExamplePrimitiveInlineCollection() {
         Serializer serializer = new Persister();
         ExamplePrimitiveInlineCollection list = serializer.read(ExamplePrimitiveInlineCollection.class, INLINE_PRIMITIVE_LIST);
         validate(list, serializer);
      }
      public void TestExamplePrimitiveCollection() {
         Serializer serializer = new Persister();
         ExamplePrimitiveCollection list = serializer.read(ExamplePrimitiveCollection.class, PRIMITIVE_LIST);
         validate(list, serializer);
      }
   }
}
