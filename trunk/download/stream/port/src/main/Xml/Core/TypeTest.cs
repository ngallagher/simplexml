#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class TypeTest : ValidationTestCase {
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
      private static class Entry {
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
      private Persister persister;
      public void SetUp() {
         persister = new Persister();
      }
      public void TestPrimitive() {
         Entry entry = persister.Read(Entry.class, SOURCE);
         AssertEquals(entry.primitive.boolValue, true);
         AssertEquals(entry.primitive.byteValue, 16);
         AssertEquals(entry.primitive.shortValue, 120);
         AssertEquals(entry.primitive.intValue, 1234);
         AssertEquals(entry.primitive.floatValue, 1234.56f);
         AssertEquals(entry.primitive.longValue, 1234567l);
         AssertEquals(entry.primitive.doubleValue, 1234567.89d);
         AssertEquals(entry.object.boolValue, Boolean.TRUE);
         AssertEquals(entry.object.byteValue, new Byte("16"));
         AssertEquals(entry.object.shortValue, new Short("120"));
         AssertEquals(entry.object.intValue, new Integer(1234));
         AssertEquals(entry.object.floatValue, new Float(1234.56));
         AssertEquals(entry.object.longValue, new Long(1234567));
         AssertEquals(entry.object.doubleValue, new Double(1234567.89));
         AssertEquals(entry.object.stringValue, "text value");
         AssertEquals(entry.object.enumValue, TestEnum.TWO);
         StringWriter buffer = new StringWriter();
         persister.Write(entry, buffer);
         Validate(entry, persister);
         entry = persister.Read(Entry.class, buffer.toString());
         AssertEquals(entry.primitive.boolValue, true);
         AssertEquals(entry.primitive.byteValue, 16);
         AssertEquals(entry.primitive.shortValue, 120);
         AssertEquals(entry.primitive.intValue, 1234);
         AssertEquals(entry.primitive.floatValue, 1234.56f);
         AssertEquals(entry.primitive.longValue, 1234567l);
         AssertEquals(entry.primitive.doubleValue, 1234567.89d);
         AssertEquals(entry.object.boolValue, Boolean.TRUE);
         AssertEquals(entry.object.byteValue, new Byte("16"));
         AssertEquals(entry.object.shortValue, new Short("120"));
         AssertEquals(entry.object.intValue, new Integer(1234));
         AssertEquals(entry.object.floatValue, new Float(1234.56));
         AssertEquals(entry.object.longValue, new Long(1234567));
         AssertEquals(entry.object.doubleValue, new Double(1234567.89));
         AssertEquals(entry.object.stringValue, "text value");
         AssertEquals(entry.object.enumValue, TestEnum.TWO);
         Validate(entry, persister);
      }
   }
}
