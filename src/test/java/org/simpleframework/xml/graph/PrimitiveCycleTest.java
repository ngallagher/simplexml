package org.simpleframework.xml.graph;

import java.io.StringWriter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.graph.CycleStrategy;
import org.simpleframework.xml.load.Persister;

import org.simpleframework.xml.ValidationTestCase;

public class PrimitiveCycleTest extends ValidationTestCase {
           
   private static final String SOURCE =
   "<?xml version=\"1.0\"?>\n"+
   "<test>\n"+
   "   <primitive>\n"+
   "     <boolean>true</boolean>\r\n"+
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

   @Root(name="test")
   private static class PrimitiveCycleEntry {

      @Element(name="primitive")
      private PrimitiveEntry primitive;

      @Element(name="object")
      private ObjectEntry object;
   }

   private static class PrimitiveEntry {

      @Element(name="boolean")
      private boolean booleanValue;            

      @Element(name="byte")
      private byte byteValue;

      @Element(name="short")
      private short shortValue;

      @Element(name="int")
      private int intValue;   

      @Element(name="float")
      private float floatValue;

      @Element(name="long")
      private long longValue;         

      @Element(name="double")
      private double doubleValue;
   }

   private static class ObjectEntry {

      @Element(name="Boolean")
      private Boolean booleanValue;              

      @Element(name="Byte")
      private Byte byteValue;

      @Element(name="Short")
      private Short shortValue;

      @Element(name="Integer")
      private Integer intValue;   

      @Element(name="Float")
      private Float floatValue;

      @Element(name="Long")
      private Long longValue;         

      @Element(name="Double")
      private Double doubleValue;

      @Element(name="String")
      private String stringValue;

      @Element(name="Enum")
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

   public void setUp() throws Exception {
      persister = new Persister(new CycleStrategy());
   }
	
   public void testPrimitive() throws Exception {    
      PrimitiveCycleEntry entry = persister.read(PrimitiveCycleEntry.class, SOURCE);

      assertEquals(entry.primitive.booleanValue, true);
      assertEquals(entry.primitive.byteValue, 16);
      assertEquals(entry.primitive.shortValue, 120);
      assertEquals(entry.primitive.intValue, 1234);
      assertEquals(entry.primitive.floatValue, 1234.56f);
      assertEquals(entry.primitive.longValue, 1234567l);
      assertEquals(entry.primitive.doubleValue, 1234567.89d);

      assertEquals(entry.object.booleanValue, Boolean.TRUE);
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

      assertXpathEvaluatesTo("true", "/test/primitive/boolean", text);   
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
   
   public void testPrimitiveReference() throws Exception {
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
