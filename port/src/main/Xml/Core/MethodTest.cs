#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class MethodTest : ValidationTestCase {
      private const String SOURCE_EXPLICIT =
      "<?xml version=\"1.0\"?>\n"+
      "<test>\n"+
      "   <bool>true</bool>\r\n"+
      "   <byte>16</byte>  \n\r"+
      "   <short>120</short>  \n\r"+
      "   <int>1234</int>\n"+
      "   <float>1234.56</float>  \n\r"+
      "   <long>1234567</long>\n"+
      "   <double>1234567.89</double>  \n\r"+
      "</test>";
      private const String SOURCE_IMPLICIT =
      "<?xml version=\"1.0\"?>\n"+
      "<implicitMethodNameExample>\n"+
      "   <boolValue>true</boolValue>\r\n"+
      "   <byteValue>16</byteValue>  \n\r"+
      "   <shortValue>120</shortValue>  \n\r"+
      "   <intValue>1234</intValue>\n"+
      "   <floatValue>1234.56</floatValue>  \n\r"+
      "   <longValue>1234567</longValue>\n"+
      "   <doubleValue>1234567.89</doubleValue>  \n\r"+
      "</implicitMethodNameExample>";
      [Root(Name="test")]
      private static class ExplicitMethodNameExample {
         protected bool boolValue;
         protected byte byteValue;
         protected short shortValue;
         protected int intValue;
         protected float floatValue;
         protected long longValue;
         protected double doubleValue;
         public ExplicitMethodNameExample() {
            super();
         }
         [Element(Name="bool")]
         public bool BooleanValue {
            get {
               return boolValue;
            }
            set {
               this.boolValue = value;
            }
         }
         //public bool GetBooleanValue() {
         //   return boolValue;
         //}
         //public void SetBooleanValue(bool boolValue) {
         //   this.boolValue = boolValue;
         //}
         public byte ByteValue {
            get {
               return byteValue;
            }
            set {
               this.byteValue = value;
            }
         }
         //public byte GetByteValue() {
         //   return byteValue;
         //}
         //public void SetByteValue(byte byteValue) {
         //   this.byteValue = byteValue;
         //}
         public double DoubleValue {
            get {
               return doubleValue;
            }
            set {
               this.doubleValue = value;
            }
         }
         //public double GetDoubleValue() {
         //   return doubleValue;
         //}
         //public void SetDoubleValue(double doubleValue) {
         //   this.doubleValue = doubleValue;
         //}
         public float FloatValue {
            get {
               return floatValue;
            }
            set {
               this.floatValue = value;
            }
         }
         //public float GetFloatValue() {
         //   return floatValue;
         //}
         //public void SetFloatValue(float floatValue) {
         //   this.floatValue = floatValue;
         //}
         public int IntValue {
            get {
               return intValue;
            }
            set {
               this.intValue = value;
            }
         }
         //public int GetIntValue() {
         //   return intValue;
         //}
         //public void SetIntValue(int intValue) {
         //   this.intValue = intValue;
         //}
         public long LongValue {
            get {
               return longValue;
            }
            set {
               this.longValue = value;
            }
         }
         //public long GetLongValue() {
         //   return longValue;
         //}
         //public void SetLongValue(long longValue) {
         //   this.longValue = longValue;
         //}
         public short ShortValue {
            get {
               return shortValue;
            }
            set {
               this.shortValue = value;
            }
         }
         //public short GetShortValue() {
         //   return shortValue;
         //}
         //public void SetShortValue(short shortValue) {
         //   this.shortValue = shortValue;
         //}
      [Root]
      private static class ImplicitMethodNameExample : ExplicitMethodNameExample {
         [Element]
         public bool BooleanValue {
            get {
               return boolValue;
            }
            set {
               this.boolValue = value;
            }
         }
         //public bool GetBooleanValue() {
         //   return boolValue;
         //}
         //public void SetBooleanValue(bool boolValue) {
         //   this.boolValue = boolValue;
         //}
         public byte ByteValue {
            get {
               return byteValue;
            }
            set {
               this.byteValue = value;
            }
         }
         //public byte GetByteValue() {
         //   return byteValue;
         //}
         //public void SetByteValue(byte byteValue) {
         //   this.byteValue = byteValue;
         //}
         public double DoubleValue {
            get {
               return doubleValue;
            }
            set {
               this.doubleValue = value;
            }
         }
         //public double GetDoubleValue() {
         //   return doubleValue;
         //}
         //public void SetDoubleValue(double doubleValue) {
         //   this.doubleValue = doubleValue;
         //}
         public float FloatValue {
            get {
               return floatValue;
            }
            set {
               this.floatValue = value;
            }
         }
         //public float GetFloatValue() {
         //   return floatValue;
         //}
         //public void SetFloatValue(float floatValue) {
         //   this.floatValue = floatValue;
         //}
         public int IntValue {
            get {
               return intValue;
            }
            set {
               this.intValue = value;
            }
         }
         //public int GetIntValue() {
         //   return intValue;
         //}
         //public void SetIntValue(int intValue) {
         //   this.intValue = intValue;
         //}
         public long LongValue {
            get {
               return longValue;
            }
            set {
               this.longValue = value;
            }
         }
         //public long GetLongValue() {
         //   return longValue;
         //}
         //public void SetLongValue(long longValue) {
         //   this.longValue = longValue;
         //}
         public short ShortValue {
            get {
               return shortValue;
            }
            set {
               this.shortValue = value;
            }
         }
         //public short GetShortValue() {
         //   return shortValue;
         //}
         //public void SetShortValue(short shortValue) {
         //   this.shortValue = shortValue;
         //}
      private Persister persister;
      public void SetUp() {
         persister = new Persister();
      }
      public void TestExplicitMethodNameExample() {
         ExplicitMethodNameExample entry = persister.Read(ExplicitMethodNameExample.class, SOURCE_EXPLICIT);
         AssertEquals(entry.boolValue, true);
         AssertEquals(entry.byteValue, 16);
         AssertEquals(entry.shortValue, 120);
         AssertEquals(entry.intValue, 1234);
         AssertEquals(entry.floatValue, 1234.56f);
         AssertEquals(entry.longValue, 1234567l);
         AssertEquals(entry.doubleValue, 1234567.89d);
         StringWriter buffer = new StringWriter();
         persister.Write(entry, buffer);
         Validate(entry, persister);
         entry = persister.Read(ExplicitMethodNameExample.class, buffer.toString());
         AssertEquals(entry.boolValue, true);
         AssertEquals(entry.byteValue, 16);
         AssertEquals(entry.shortValue, 120);
         AssertEquals(entry.intValue, 1234);
         AssertEquals(entry.floatValue, 1234.56f);
         AssertEquals(entry.longValue, 1234567l);
         AssertEquals(entry.doubleValue, 1234567.89d);
         Validate(entry, persister);
      }
      public void TestImplicitMethodNameExample() {
         ImplicitMethodNameExample entry = persister.Read(ImplicitMethodNameExample.class, SOURCE_IMPLICIT);
         AssertEquals(entry.boolValue, true);
         AssertEquals(entry.byteValue, 16);
         AssertEquals(entry.shortValue, 120);
         AssertEquals(entry.intValue, 1234);
         AssertEquals(entry.floatValue, 1234.56f);
         AssertEquals(entry.longValue, 1234567l);
         AssertEquals(entry.doubleValue, 1234567.89d);
         StringWriter buffer = new StringWriter();
         persister.Write(entry, buffer);
         Validate(entry, persister);
         entry = persister.Read(ImplicitMethodNameExample.class, buffer.toString());
         AssertEquals(entry.boolValue, true);
         AssertEquals(entry.byteValue, 16);
         AssertEquals(entry.shortValue, 120);
         AssertEquals(entry.intValue, 1234);
         AssertEquals(entry.floatValue, 1234.56f);
         AssertEquals(entry.longValue, 1234567l);
         AssertEquals(entry.doubleValue, 1234567.89d);
         Validate(entry, persister);
      }
   }
}
