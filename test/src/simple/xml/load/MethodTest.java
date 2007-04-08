package simple.xml.load;

import java.io.StringWriter;

import simple.xml.Element;
import simple.xml.Root;
import simple.xml.ValidationTestCase;

public class MethodTest extends ValidationTestCase {
        
   private static final String SOURCE =
   "<?xml version=\"1.0\"?>\n"+
   "<test>\n"+
   "   <boolean>true</boolean>\r\n"+
   "   <byte>16</byte>  \n\r"+
   "   <short>120</short>  \n\r"+
   "   <int>1234</int>\n"+
   "   <float>1234.56</float>  \n\r"+
   "   <long>1234567</long>\n"+
   "   <double>1234567.89</double>  \n\r"+
   "</test>";
   
   @Root(name="test")
   private static class MethodExample {

      private boolean booleanValue;            
      private byte byteValue;
      private short shortValue;
      private int intValue;   
      private float floatValue;
      private long longValue;         
      private double doubleValue;
      
      @Element(name="boolean")
      public boolean getBooleanValue() {
         return booleanValue;
      }
      
      @Element(name="boolean")
      public void setBooleanValue(boolean booleanValue) {
         this.booleanValue = booleanValue;
      }
      
      @Element(name="byte")
      public byte getByteValue() {
         return byteValue;
      }

      @Element(name="byte")
      public void setByteValue(byte byteValue) {
         this.byteValue = byteValue;
      }
      
      @Element(name="double")
      public double getDoubleValue() {
         return doubleValue;
      }
      
      @Element(name="double")
      public void setDoubleValue(double doubleValue) {
         this.doubleValue = doubleValue;
      }
      
      @Element(name="float")
      public float getFloatValue() {
         return floatValue;
      }
      
      @Element(name="float")
      public void setFloatValue(float floatValue) {
         this.floatValue = floatValue;
      }
      
      @Element(name="int")
      public int getIntValue() {
         return intValue;
      }
      
      @Element(name="int")
      public void setIntValue(int intValue) {
         this.intValue = intValue;
      }
      
      @Element(name="long")
      public long getLongValue() {
         return longValue;
      }
      
      @Element(name="long")
      public void setLongValue(long longValue) {
         this.longValue = longValue;
      }
      
      @Element(name="short")
      public short getShortValue() {
         return shortValue;
      }
      
      @Element(name="short")
      public void setShortValue(short shortValue) {
         this.shortValue = shortValue;
      }           
   }

   private Persister persister;

   public void setUp() throws Exception {
      persister = new Persister();
   }
	
   public void testPrimitive() throws Exception {    
      MethodExample entry = persister.read(MethodExample.class, SOURCE);

      assertEquals(entry.booleanValue, true);
      assertEquals(entry.byteValue, 16);
      assertEquals(entry.shortValue, 120);
      assertEquals(entry.intValue, 1234);
      assertEquals(entry.floatValue, 1234.56f);
      assertEquals(entry.longValue, 1234567l);
      assertEquals(entry.doubleValue, 1234567.89d);
     
      StringWriter buffer = new StringWriter();
      persister.write(entry, buffer);
      validate(entry, persister);

      entry = persister.read(MethodExample.class, buffer.toString());

      assertEquals(entry.booleanValue, true);
      assertEquals(entry.byteValue, 16);
      assertEquals(entry.shortValue, 120);
      assertEquals(entry.intValue, 1234);
      assertEquals(entry.floatValue, 1234.56f);
      assertEquals(entry.longValue, 1234567l);
      assertEquals(entry.doubleValue, 1234567.89d);
      
      validate(entry, persister);
   }
}
