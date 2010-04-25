#region Using directives
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ConstructorInjectionTest : ValidationTestCase {
      private const String SOURCE =
      "<example number='32'>"+
      "  <integer>12</integer>"+
      "  <string>text</string>"+
      "</example>";
      private const String PARTIAL =
      "<example>"+
      "  <integer>12</integer>"+
      "  <string>text</string>"+
      "</example>";
      private const String BARE =
      "<example>"+
      "  <integer>12</integer>"+
      "</example>";
      private const String ARRAY =
      "<ExampleArray>"+
      "   <Array length='5'>\n\r"+
      "      <String>entry one</String>  \n\r"+
      "      <String>entry two</String>  \n\r"+
      "      <String>entry three</String>  \n\r"+
      "      <String>entry four</String>  \n\r"+
      "      <String>entry five</String>  \n\r"+
      "   </Array>\n\r"+
      "</ExampleArray>";
      @Root
      private static class Example {
         @Element
         private int integer;
         @Element(required=false)
         private String string;
         @Attribute(name="number", required=false)
         private long number;
         public Example(@Element(name="integer") int integer){
            this.integer = integer;
         }
         public Example(@Element(name="integer") int integer, @Element(name="string", required=false) String string, @Attribute(name="number", required=false) long number){
            this.integer = integer;
            this.string = string;
            this.number = number;
         }
         public Example(@Element(name="integer") int integer, @Element(name="string", required=false) String string){
            this.integer = integer;
            this.string = string;
         }
      }
      @Root
      private static class ArrayExample {
         @ElementArray(name="array")
         private readonly String[] array;
         public ArrayExample(@ElementArray(name="array") String[] array) {
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
      public void TestConstructor() {
         Persister persister = new Persister();
         Example example = persister.read(Example.class, SOURCE);
         assertEquals(example.integer, 12);
         assertEquals(example.number, 32);
         assertEquals(example.string, "text");
         validate(persister, example);
      }
      public void TestPartialConstructor() {
         Persister persister = new Persister();
         Example example = persister.read(Example.class, PARTIAL);
         assertEquals(example.integer, 12);
         assertEquals(example.number, 0);
         assertEquals(example.string, "text");
         validate(persister, example);
      }
      public void TestBareConstructor() {
         Persister persister = new Persister();
         Example example = persister.read(Example.class, BARE);
         assertEquals(example.integer, 12);
         assertEquals(example.number, 0);
         assertEquals(example.string, null);
         validate(persister, example);
      }
      public void TestArrayExample() {
         Style style = new CamelCaseStyle();
         Format format = new Format(style);
         Persister persister = new Persister(format);
         ArrayExample example = persister.read(ArrayExample.class, ARRAY);
         assertEquals(example.Array.length, 5);
         assertEquals(example.Array[0], "entry one");
         assertEquals(example.Array[1], "entry two");
         assertEquals(example.Array[2], "entry three");
         assertEquals(example.Array[3], "entry four");
         assertEquals(example.Array[4], "entry five");
         validate(persister, example);
      }
   }
}
