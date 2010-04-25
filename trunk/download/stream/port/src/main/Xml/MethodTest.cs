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
      @Root(name="test")
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
         @Element(name="bool")
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
      @Root
      private static class ImplicitMethodNameExample : ExplicitMethodNameExample {
         @Element
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
         ExplicitMethodNameExample entry = persister.read(ExplicitMethodNameExample.class, SOURCE_EXPLICIT);
         assertEquals(entry.boolValue, true);
         assertEquals(entry.byteValue, 16);
         assertEquals(entry.shortValue, 120);
         assertEquals(entry.intValue, 1234);
         assertEquals(entry.floatValue, 1234.56f);
         assertEquals(entry.longValue, 1234567l);
         assertEquals(entry.doubleValue, 1234567.89d);
         StringWriter buffer = new StringWriter();
         persister.write(entry, buffer);
         validate(entry, persister);
         entry = persister.read(ExplicitMethodNameExample.class, buffer.toString());
         assertEquals(entry.boolValue, true);
         assertEquals(entry.byteValue, 16);
         assertEquals(entry.shortValue, 120);
         assertEquals(entry.intValue, 1234);
         assertEquals(entry.floatValue, 1234.56f);
         assertEquals(entry.longValue, 1234567l);
         assertEquals(entry.doubleValue, 1234567.89d);
         validate(entry, persister);
      }
      public void TestImplicitMethodNameExample() {
         ImplicitMethodNameExample entry = persister.read(ImplicitMethodNameExample.class, SOURCE_IMPLICIT);
         assertEquals(entry.boolValue, true);
         assertEquals(entry.byteValue, 16);
         assertEquals(entry.shortValue, 120);
         assertEquals(entry.intValue, 1234);
         assertEquals(entry.floatValue, 1234.56f);
         assertEquals(entry.longValue, 1234567l);
         assertEquals(entry.doubleValue, 1234567.89d);
         StringWriter buffer = new StringWriter();
         persister.write(entry, buffer);
         validate(entry, persister);
         entry = persister.read(ImplicitMethodNameExample.class, buffer.toString());
         assertEquals(entry.boolValue, true);
         assertEquals(entry.byteValue, 16);
         assertEquals(entry.shortValue, 120);
         assertEquals(entry.intValue, 1234);
         assertEquals(entry.floatValue, 1234.56f);
         assertEquals(entry.longValue, 1234567l);
         assertEquals(entry.doubleValue, 1234567.89d);
         validate(entry, persister);
      }
   }
}
