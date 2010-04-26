#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ArrayTest : ValidationTestCase {
      private const String SOURCE =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <array length='5'>\n\r"+
      "      <entry value='entry one'/>  \n\r"+
      "      <entry value='entry two'/>  \n\r"+
      "      <entry value='entry three'/>  \n\r"+
      "      <entry value='entry four'/>  \n\r"+
      "      <entry value='entry five'/>  \n\r"+
      "   </array>\n\r"+
      "</root>";
      private const String PRIMITIVE =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <array length='5'>\n\r"+
      "      <text>entry one</text>  \n\r"+
      "      <text>entry two</text>  \n\r"+
      "      <text>entry three</text>  \n\r"+
      "      <text>entry four</text>  \n\r"+
      "      <text>entry five</text>  \n\r"+
      "   </array>\n\r"+
      "</root>";
      private const String PRIMITIVE_INT =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <array length='5'>\n\r"+
      "      <text>1</text>  \n\r"+
      "      <text>2</text>  \n\r"+
      "      <text>3</text>  \n\r"+
      "      <text>4</text>  \n\r"+
      "      <text>5</text>  \n\r"+
      "   </array>\n\r"+
      "</root>";
      private const String PRIMITIVE_MULTIDIMENSIONAL_INT =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <array length='5'>\n\r"+
      "      <text> 1,2,3, 4, 5, 6</text>  \n\r"+
      "      <text>2, 4, 6, 8, 10, 12</text>  \n\r"+
      "      <text>3, 6 ,9,12, 15, 18</text>  \n\r"+
      "      <text>4, 8, 12, 16, 20, 24</text>  \n\r"+
      "      <text>5, 10,15,20,25,30</text>  \n\r"+
      "   </array>\n\r"+
      "</root>";
      private const String DEFAULT_PRIMITIVE =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <array length='5'>\n\r"+
      "      <string>entry one</string>  \n\r"+
      "      <string>entry two</string>  \n\r"+
      "      <string>entry three</string>  \n\r"+
      "      <string>entry four</string>  \n\r"+
      "      <string>entry five</string>  \n\r"+
      "   </array>\n\r"+
      "</root>";
      private const String COMPOSITE =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <array length='5'>\n\r"+
      "      <text value='entry one'/>  \n\r"+
      "      <text value='entry two'/>  \n\r"+
      "      <text value='entry three'/>  \n\r"+
      "      <text value='entry four'/>  \n\r"+
      "      <text value='entry five'/>  \n\r"+
      "   </array>\n\r"+
      "</root>";
      private const String DEFAULT_COMPOSITE =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <array length='5'>\n\r"+
      "      <text value='entry one'/>  \n\r"+
      "      <text value='entry two'/>  \n\r"+
      "      <text value='entry three'/>  \n\r"+
      "      <text value='entry four'/>  \n\r"+
      "      <text value='entry five'/>  \n\r"+
      "   </array>\n\r"+
      "</root>";
      private const String PRIMITIVE_NULL =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <array length='5'>\n\r"+
      "      <text/>  \n\r"+
      "      <text>entry two</text>  \n\r"+
      "      <text>entry three</text>  \n\r"+
      "      <text/>  \n\r"+
      "      <text/>  \n\r"+
      "   </array>\n\r"+
      "</root>";
      private const String COMPOSITE_NULL =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <array length='5'>\n\r"+
      "      <entry/>\r\n"+
      "      <entry value='entry two'/>  \n\r"+
      "      <entry/>\r\n"+
      "      <entry/>\r\n"+
      "      <entry value='entry five'/>  \n\r"+
      "   </array>\n\r"+
      "</root>";
      private const String CHARACTER =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <array length='5'>\n\r"+
      "      <char>a</char>  \n\r"+
      "      <char>b</char>  \n\r"+
      "      <char>c</char>  \n\r"+
      "      <char>d</char>  \n\r"+
      "      <char>e</char>  \n\r"+
      "   </array>\n\r"+
      "</root>";
      [Root(Name="root")]
      private static class ArrayExample {
         [ElementArray(Name="array", Entry="entry")]
         public Text[] array;
      }
      [Root(Name="root")]
      private static class BadArrayExample {
         [ElementArray(Name="array", Entry="entry")]
         public Text array;
      }
      [Root(Name="text")]
      private static class Text {
         [Attribute(Name="value")]
         public String value;
         public Text() {
            super();
         }
         public Text(String value) {
            this.value = value;
         }
      }
      [Root(Name="text")]
      private static class ExtendedText  : Text {
         public ExtendedText() {
            super();
         }
         public ExtendedText(String value) {
            super(value);
         }
      }
      [Root(Name="root")]
      private static class PrimitiveArrayExample {
         [ElementArray(Name="array", Entry="text")]
         private String[] array;
      }
      [Root(Name="root")]
      private static class PrimitiveIntegerArrayExample {
         [ElementArray(Name="array", Entry="text")]
         private int[] array;
      }
      [Root(Name="root")]
      private static class PrimitiveMultidimensionalIntegerArrayExample {
         [ElementArray(Name="array", Entry="text")]
         private int[][] array;
      }
      [Root(Name="root")]
      private static class DefaultPrimitiveArrayExample {
         [ElementArray]
         private String[] array;
      }
      [Root(Name="root")]
      private static class ParentCompositeArrayExample {
         [ElementArray(Name="array", Entry="entry")]
         private Text[] array;
      }
      [Root(Name="root")]
      private static class DefaultCompositeArrayExample {
         [ElementArray]
         private Text[] array;
      }
      [Root(Name="root")]
      private static class CharacterArrayExample {
         [ElementArray(Name="array", Entry="char")]
         private char[] array;
      }
      [Root(Name="root")]
      private static class DifferentArrayExample {
         [ElementArray(Name="array", Entry="entry")]
         private Text[] array;
         public DifferentArrayExample() {
            this.array = new Text[] { new ExtendedText("one"), null, null, new ExtendedText("two"), null, new ExtendedText("three") };
         }
      }
      private Persister serializer;
      public void SetUp() {
         serializer = new Persister();
      }
      public void TestExample() {
         ArrayExample example = serializer.Read(ArrayExample.class, SOURCE);
         AssertEquals(example.array.length, 5);
         AssertEquals(example.array[0].value, "entry one");
         AssertEquals(example.array[1].value, "entry two");
         AssertEquals(example.array[2].value, "entry three");
         AssertEquals(example.array[3].value, "entry four");
         AssertEquals(example.array[4].value, "entry five");
      }
      public void TestBadExample() {
         bool success = false;
         try {
            BadArrayExample example = serializer.Read(BadArrayExample.class, SOURCE);
         } catch(InstantiationException e) {
            success = true;
         }
         assertTrue(success);
      }
      public void TestWriteArray() {
         ArrayExample example = new ArrayExample();
         example.array = new Text[100];
         for(int i = 0; i < example.array.length; i++) {
            example.array[i] = new Text(String.format("index %s", i));
         }
         Validate(example, serializer);
         StringWriter writer = new StringWriter();
         serializer.Write(example, writer);
         String content = writer.toString();
         assertXpathExists("/root/array[@length='100']", content);
         assertXpathExists("/root/array/entry[1][@value='index 0']", content);
         assertXpathExists("/root/array/entry[100][@value='index 99']", content);
         ArrayExample deserialized = serializer.Read(ArrayExample.class, content);
         AssertEquals(deserialized.array.length, example.array.length);
         // Ensure serialization maintains exact content
         for(int i = 0; i < deserialized.array.length; i++) {
            AssertEquals(deserialized.array[i].value, example.array[i].value);
         }
         for(int i = 0; i < example.array.length; i++) {
            if(i % 2 == 0) {
               example.array[i] = null;
            }
         }
         Validate(example, serializer);
         StringWriter oddOnly = new StringWriter();
         serializer.Write(example, oddOnly);
         content = oddOnly.toString();
         assertXpathExists("/root/array[@length='100']", content);
         assertXpathNotExists("/root/array/entry[1][@value='index 0']", content);
         assertXpathExists("/root/array/entry[2][@value='index 1']", content);
         assertXpathNotExists("/root/array/entry[3][@value='index 2']", content);
         assertXpathExists("/root/array/entry[100][@value='index 99']", content);
         deserialized = serializer.Read(ArrayExample.class, content);
         for(int i = 0, j = 0; i < example.array.length; i++) {
            if(i % 2 != 0) {
               AssertEquals(example.array[i].value, deserialized.array[i].value);
            } else {
               AssertNull(example.array[i]);
               AssertNull(deserialized.array[i]);
            }
         }
      }
      public void TestPrimitive() {
         PrimitiveArrayExample example = serializer.Read(PrimitiveArrayExample.class, PRIMITIVE);
         AssertEquals(example.array.length, 5);
         AssertEquals(example.array[0], "entry one");
         AssertEquals(example.array[1], "entry two");
         AssertEquals(example.array[2], "entry three");
         AssertEquals(example.array[3], "entry four");
         AssertEquals(example.array[4], "entry five");
         Validate(example, serializer);
      }
      public void TestPrimitiveInteger() {
         PrimitiveIntegerArrayExample example = serializer.Read(PrimitiveIntegerArrayExample.class, PRIMITIVE_INT);
         AssertEquals(example.array.length, 5);
         AssertEquals(example.array[0], 1);
         AssertEquals(example.array[1], 2);
         AssertEquals(example.array[2], 3);
         AssertEquals(example.array[3], 4);
         AssertEquals(example.array[4], 5);
         Validate(example, serializer);
      }
      public void TestPrimitiveMultidimensionalInteger() {
         PrimitiveMultidimensionalIntegerArrayExample example = serializer.Read(PrimitiveMultidimensionalIntegerArrayExample.class, PRIMITIVE_MULTIDIMENSIONAL_INT);
         AssertEquals(example.array.length, 5);
         AssertEquals(example.array[0][0], 1);
         AssertEquals(example.array[0][1], 2);
         AssertEquals(example.array[0][2], 3);
         AssertEquals(example.array[0][3], 4);
         AssertEquals(example.array[0][4], 5);
         AssertEquals(example.array[0][5], 6);
         AssertEquals(example.array[1][0], 2);
         AssertEquals(example.array[1][1], 4);
         AssertEquals(example.array[1][2], 6);
         AssertEquals(example.array[1][3], 8);
         AssertEquals(example.array[1][4], 10);
         AssertEquals(example.array[1][5], 12);
         AssertEquals(example.array[2][0], 3);
         AssertEquals(example.array[2][1], 6);
         AssertEquals(example.array[2][2], 9);
         AssertEquals(example.array[2][3], 12);
         AssertEquals(example.array[2][4], 15);
         AssertEquals(example.array[2][5], 18);
         AssertEquals(example.array[3][0], 4);
         AssertEquals(example.array[3][1], 8);
         AssertEquals(example.array[3][2], 12);
         AssertEquals(example.array[3][3], 16);
         AssertEquals(example.array[3][4], 20);
         AssertEquals(example.array[3][5], 24);
         AssertEquals(example.array[4][0], 5);
         AssertEquals(example.array[4][1], 10);
         AssertEquals(example.array[4][2], 15);
         AssertEquals(example.array[4][3], 20);
         AssertEquals(example.array[4][4], 25);
         AssertEquals(example.array[4][5], 30);
         Validate(example, serializer);
      }
      public void TestDefaultPrimitive() {
         DefaultPrimitiveArrayExample example = serializer.Read(DefaultPrimitiveArrayExample.class, DEFAULT_PRIMITIVE);
         AssertEquals(example.array.length, 5);
         AssertEquals(example.array[0], "entry one");
         AssertEquals(example.array[1], "entry two");
         AssertEquals(example.array[2], "entry three");
         AssertEquals(example.array[3], "entry four");
         AssertEquals(example.array[4], "entry five");
         Validate(example, serializer);
      }
      public void TestPrimitiveNull() {
         PrimitiveArrayExample example = serializer.Read(PrimitiveArrayExample.class, PRIMITIVE_NULL);
         AssertEquals(example.array.length, 5);
         AssertEquals(example.array[0], null);
         AssertEquals(example.array[1], "entry two");
         AssertEquals(example.array[2], "entry three");
         AssertEquals(example.array[3], null);
         AssertEquals(example.array[4], null);
         Validate(example, serializer);
      }
      public void TestParentComposite() {
         ParentCompositeArrayExample example = serializer.Read(ParentCompositeArrayExample.class, COMPOSITE);
         AssertEquals(example.array.length, 5);
         AssertEquals(example.array[0].value, "entry one");
         AssertEquals(example.array[1].value, "entry two");
         AssertEquals(example.array[2].value, "entry three");
         AssertEquals(example.array[3].value, "entry four");
         AssertEquals(example.array[4].value, "entry five");
         Validate(example, serializer);
      }
      public void TestDefaultComposite() {
         DefaultCompositeArrayExample example = serializer.Read(DefaultCompositeArrayExample.class, DEFAULT_COMPOSITE);
         AssertEquals(example.array.length, 5);
         AssertEquals(example.array[0].value, "entry one");
         AssertEquals(example.array[1].value, "entry two");
         AssertEquals(example.array[2].value, "entry three");
         AssertEquals(example.array[3].value, "entry four");
         AssertEquals(example.array[4].value, "entry five");
         Validate(example, serializer);
      }
      public void TestParentCompositeNull() {
         ParentCompositeArrayExample example = serializer.Read(ParentCompositeArrayExample.class, COMPOSITE_NULL);
         AssertEquals(example.array.length, 5);
         AssertEquals(example.array[0], null);
         AssertEquals(example.array[1].value, "entry two");
         AssertEquals(example.array[2], null);
         AssertEquals(example.array[3], null);
         AssertEquals(example.array[4].value, "entry five");
         Validate(example, serializer);
      }
      public void TestCharacter() {
         CharacterArrayExample example = serializer.Read(CharacterArrayExample.class, CHARACTER);
         AssertEquals(example.array.length, 5);
         AssertEquals(example.array[0], 'a');
         AssertEquals(example.array[1], 'b');
         AssertEquals(example.array[2], 'c');
         AssertEquals(example.array[3], 'd');
         AssertEquals(example.array[4], 'e');
         Validate(example, serializer);
      }
      public void TestDifferentArray() {
         DifferentArrayExample example = new DifferentArrayExample();
         Validate(example, serializer);
      }
   }
}
