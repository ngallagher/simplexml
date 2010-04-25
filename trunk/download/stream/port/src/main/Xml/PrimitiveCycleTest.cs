#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class PrimitiveCycleTest : ValidationTestCase {
      private const String SOURCE =
      "<?xml version=\"1.0\"?>\n"+
      "<test>\n"+
      "   <primitive>\n"+
      "     <bool>true</bool>\r\n"+
      "     <byte>16</byte>  \n\r"+
      "     <short>120</short>  \n\r"+
      "     <int>1234</int>\n"+
      "     <float>1234.56</float>  \n\r"+
      "     <long>1234567</long>\n"+
      "     <double>1234567.89</double>  \n\r"+
      "   </primitive>\n"+
      "   <object>\n"+
      "     <Boolean>true</Boolean>\r\n"+
      "     <Byte>16</Byte>  \n\r"+
      "     <Short>120</Short>  \n\r"+
      "     <Integer>1234</Integer>\n"+
      "     <Float>1234.56</Float>  \n\r"+
      "     <Long>1234567</Long>\n"+
      "     <Double>1234567.89</Double>  \n\r"+
      "     <String>text value</String>\n\r"+
      "     <Enum>TWO</Enum>\n"+
      "   </object>\n\r"+
      "</test>";
      [Root(Name="test")]
      private static class PrimitiveCycleEntry {
         [Element(Name="primitive")]
         private PrimitiveEntry primitive;
         [Element(Name="object")]
         private ObjectEntry object;
      }
      private static class PrimitiveEntry {
         [Element(Name="bool")]
         private bool boolValue;
         [Element(Name="byte")]
         private byte byteValue;
         [Element(Name="short")]
         private short shortValue;
         [Element(Name="int")]
         private int intValue;
         [Element(Name="float")]
         private float floatValue;
         [Element(Name="long")]
         private long longValue;
         [Element(Name="double")]
         private double doubleValue;
      }
      private static class ObjectEntry {
         [Element(Name="Boolean")]
         private Boolean boolValue;
         [Element(Name="Byte")]
         private Byte byteValue;
         [Element(Name="Short")]
         private Short shortValue;
         [Element(Name="Integer")]
         private Integer intValue;
         [Element(Name="Float")]
         private Float floatValue;
         [Element(Name="Long")]
         private Long longValue;
         [Element(Name="Double")]
         private Double doubleValue;
         [Element(Name="String")]
         private String stringValue;
         [Element(Name="Enum")]
         private TestEnum enumValue;
      }
      private static enum TestEnum {
         ONE,
         TWO,
         THREE
      }
      @Root
      private static class StringReferenceExample {
         @Element
         private String a;
         @Element
         private String b;
         @Element
         private String c;
         public StringReferenceExample() {
            super();
         }
         public StringReferenceExample(String a, String b, String c) {
            this.a = a;
            this.b = b;
            this.c = c;
         }
      }
      @Root
      private static class IntegerReferenceExample {
         @Element
         private Integer a;
         @Element
         private Integer b;
         @Element
         private Integer c;
         public IntegerReferenceExample() {
            super();
         }
         public IntegerReferenceExample(Integer a, Integer b, Integer c) {
            this.a = a;
            this.b = b;
            this.c = c;
         }
      }
      private Persister persister;
      public void SetUp() {
         persister = new Persister(new CycleStrategy());
      }
      public void TestPrimitive() {
         PrimitiveCycleEntry entry = persister.read(PrimitiveCycleEntry.class, SOURCE);
         assertEquals(entry.primitive.boolValue, true);
         assertEquals(entry.primitive.byteValue, 16);
         assertEquals(entry.primitive.shortValue, 120);
         assertEquals(entry.primitive.intValue, 1234);
         assertEquals(entry.primitive.floatValue, 1234.56f);
         assertEquals(entry.primitive.longValue, 1234567l);
         assertEquals(entry.primitive.doubleValue, 1234567.89d);
         assertEquals(entry.object.boolValue, Boolean.TRUE);
         assertEquals(entry.object.byteValue, new Byte("16"));
         assertEquals(entry.object.shortValue, new Short("120"));
         assertEquals(entry.object.intValue, new Integer(1234));
         assertEquals(entry.object.floatValue, new Float(1234.56));
         assertEquals(entry.object.longValue, new Long(1234567));
         assertEquals(entry.object.doubleValue, new Double(1234567.89));
         assertEquals(entry.object.stringValue, "text value");
         assertEquals(entry.object.enumValue, TestEnum.TWO);
         StringWriter out = new StringWriter();
         persister.write(entry, out);
         String text = out.toString();
         assertXpathExists("/test[@id='0']", text);
         assertXpathExists("/test/primitive[@id='1']", text);
         assertXpathExists("/test/object[@id='2']", text);
         assertXpathEvaluatesTo("true", "/test/primitive/bool", text);
         assertXpathEvaluatesTo("16", "/test/primitive/byte", text);
         assertXpathEvaluatesTo("120", "/test/primitive/short", text);
         assertXpathEvaluatesTo("1234", "/test/primitive/int", text);
         assertXpathEvaluatesTo("1234.56", "/test/primitive/float", text);
         assertXpathEvaluatesTo("1234567", "/test/primitive/long", text);
         assertXpathEvaluatesTo("1234567.89", "/test/primitive/double", text);
         assertXpathEvaluatesTo("true", "/test/object/Boolean", text);
         assertXpathEvaluatesTo("16", "/test/object/Byte", text);
         assertXpathEvaluatesTo("120", "/test/object/Short", text);
         assertXpathEvaluatesTo("1234", "/test/object/Integer", text);
         assertXpathEvaluatesTo("1234.56", "/test/object/Float", text);
         assertXpathEvaluatesTo("1234567", "/test/object/Long", text);
         assertXpathEvaluatesTo("1234567.89", "/test/object/Double", text);
         assertXpathEvaluatesTo("text value", "/test/object/String", text);
         assertXpathEvaluatesTo("TWO", "/test/object/Enum", text);
         validate(entry, persister);
      }
      public void TestPrimitiveReference() {
         StringReferenceExample example = new StringReferenceExample("a", "a", "a");
         StringWriter out = new StringWriter();
         persister.write(example, out);
         String text = out.toString();
         assertXpathExists("/stringReferenceExample[@id='0']", text);
         assertXpathExists("/stringReferenceExample/a[@id='1']", text);
         assertXpathExists("/stringReferenceExample/b[@reference='1']", text);
         assertXpathExists("/stringReferenceExample/c[@reference='1']", text);
         validate(example, persister);
         example = new StringReferenceExample("a", "b", "a");
         out = new StringWriter();
         persister.write(example, out);
         text = out.toString();
         assertXpathExists("/stringReferenceExample[@id='0']", text);
         assertXpathExists("/stringReferenceExample/a[@id='1']", text);
         assertXpathExists("/stringReferenceExample/b[@id='2']", text);
         assertXpathExists("/stringReferenceExample/c[@reference='1']", text);
         validate(example, persister);
         Integer one = new Integer(1);
         Integer two = new Integer(1);
         Integer three = new Integer(1);
         IntegerReferenceExample integers = new IntegerReferenceExample(one, two, three);
         out = new StringWriter();
         persister.write(integers, out);
         text = out.toString();
         assertXpathExists("/integerReferenceExample[@id='0']", text);
         assertXpathExists("/integerReferenceExample/a[@id='1']", text);
         assertXpathExists("/integerReferenceExample/b[@id='2']", text);
         assertXpathExists("/integerReferenceExample/c[@id='3']", text);
         validate(integers, persister);
         integers = new IntegerReferenceExample(one, one, two);
         out = new StringWriter();
         persister.write(integers, out);
         text = out.toString();
         assertXpathExists("/integerReferenceExample[@id='0']", text);
         assertXpathExists("/integerReferenceExample/a[@id='1']", text);
         assertXpathExists("/integerReferenceExample/b[@reference='1']", text);
         assertXpathExists("/integerReferenceExample/c[@id='2']", text);
         validate(integers, persister);
      }
   }
}
