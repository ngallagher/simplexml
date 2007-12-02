package org.simpleframework.xml.load;

import java.io.StringWriter;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.load.InstantiationException;
import org.simpleframework.xml.load.Persister;

import org.simpleframework.xml.ValidationTestCase;

public class ArrayTest extends ValidationTestCase {

   private static final String SOURCE =
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
   
   private static final String PRIMITIVE =
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
   
   private static final String DEFAULT_PRIMITIVE =
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
   
   private static final String COMPOSITE =
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
   
   private static final String DEFAULT_COMPOSITE =
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
   
   private static final String PRIMITIVE_NULL = 
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
   
   private static final String COMPOSITE_NULL = 
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
   
   private static final String CHARACTER =
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

   @Root(name="root")
   private static class ArrayExample {

      @ElementArray(name="array", entry="entry")           
      public Text[] array;
   }

   @Root(name="root")
   private static class BadArrayExample {
 
      @ElementArray(name="array", entry="entry")
      public Text array;
   }   

   @Root(name="text") 
   private static class Text {

      @Attribute(name="value")
      public String value;

      public Text() {
         super();              
      }

      public Text(String value) {
         this.value = value;              
      }
   }
   
   @Root(name="text")
   private static class ExtendedText  extends Text {
      
      public ExtendedText() {
         super();
      }
      
      public ExtendedText(String value) {
         super(value);
      }
      
   }
   
   @Root(name="root")
   private static class PrimitiveArrayExample {
      
      @ElementArray(name="array", entry="text")
      private String[] array;
   }
   
   @Root(name="root")
   private static class DefaultPrimitiveArrayExample {
      
      @ElementArray
      private String[] array;
   }
   
   @Root(name="root")
   private static class ParentCompositeArrayExample {
      
      @ElementArray(name="array", entry="entry")
      private Text[] array;
   }
   
   @Root(name="root")
   private static class DefaultCompositeArrayExample {
      
      @ElementArray
      private Text[] array;
   }
   
   @Root(name="root")
   private static class CharacterArrayExample {
      
      @ElementArray(name="array", entry="char")
      private char[] array;
   }
   
   @Root(name="root")
   private static class DifferentArrayExample {
      
      @ElementArray(name="array", entry="entry")
      private Text[] array;
      
      public DifferentArrayExample() {
         this.array = new Text[] { new ExtendedText("one"), null, null, new ExtendedText("two"), null, new ExtendedText("three") };
      }            
   }
   
   private Persister serializer;

   public void setUp() {
      serializer = new Persister();
   }
	
   public void testExample() throws Exception {    
      ArrayExample example = serializer.read(ArrayExample.class, SOURCE);
      
      assertEquals(example.array.length, 5);
      assertEquals(example.array[0].value, "entry one");
      assertEquals(example.array[1].value, "entry two");
      assertEquals(example.array[2].value, "entry three");
      assertEquals(example.array[3].value, "entry four");
      assertEquals(example.array[4].value, "entry five");
   }

   public void testBadExample() throws Exception {    
      boolean success = false;

      try {
         BadArrayExample example = serializer.read(BadArrayExample.class, SOURCE);
      } catch(InstantiationException e) {
         success = true;
      }
      assertTrue(success);
   }

   public void testWriteArray() throws Exception {
      ArrayExample example = new ArrayExample();
      example.array = new Text[100];
              
      for(int i = 0; i < example.array.length; i++) {
         example.array[i] = new Text(String.format("index %s", i));
      }      
      validate(example, serializer);

      StringWriter writer = new StringWriter();
      serializer.write(example, writer);
      String content = writer.toString();
      
      assertXpathExists("/root/array[@length='100']", content);
      assertXpathExists("/root/array/entry[1][@value='index 0']", content);
      assertXpathExists("/root/array/entry[100][@value='index 99']", content);      
      
      ArrayExample deserialized = serializer.read(ArrayExample.class, content);

      assertEquals(deserialized.array.length, example.array.length);
     
      // Ensure serialization maintains exact content 
      for(int i = 0; i < deserialized.array.length; i++) {
         assertEquals(deserialized.array[i].value, example.array[i].value);                    
      }
      for(int i = 0; i < example.array.length; i++) {
         if(i % 2 == 0) {              
            example.array[i] = null;              
         }            
      }
      validate(example, serializer);

      StringWriter oddOnly = new StringWriter();
      serializer.write(example, oddOnly);
      content = oddOnly.toString();
      
      assertXpathExists("/root/array[@length='100']", content);
      assertXpathNotExists("/root/array/entry[1][@value='index 0']", content);
      assertXpathExists("/root/array/entry[2][@value='index 1']", content);
      assertXpathNotExists("/root/array/entry[3][@value='index 2']", content);
      assertXpathExists("/root/array/entry[100][@value='index 99']", content);
      
      deserialized = serializer.read(ArrayExample.class, content);
      
      for(int i = 0, j = 0; i < example.array.length; i++) {
         if(i % 2 != 0) {
            assertEquals(example.array[i].value, deserialized.array[i].value);                 
         } else {
            assertNull(example.array[i]);
            assertNull(deserialized.array[i]);
         }
         
      }
   }
   
   public void testPrimitive() throws Exception {    
      PrimitiveArrayExample example = serializer.read(PrimitiveArrayExample.class, PRIMITIVE);
      
      assertEquals(example.array.length, 5);
      assertEquals(example.array[0], "entry one");
      assertEquals(example.array[1], "entry two");
      assertEquals(example.array[2], "entry three");
      assertEquals(example.array[3], "entry four");
      assertEquals(example.array[4], "entry five");
      
      validate(example, serializer);
   }
   
   public void testDefaultPrimitive() throws Exception {    
      DefaultPrimitiveArrayExample example = serializer.read(DefaultPrimitiveArrayExample.class, DEFAULT_PRIMITIVE);
      
      assertEquals(example.array.length, 5);
      assertEquals(example.array[0], "entry one");
      assertEquals(example.array[1], "entry two");
      assertEquals(example.array[2], "entry three");
      assertEquals(example.array[3], "entry four");
      assertEquals(example.array[4], "entry five");
      
      validate(example, serializer);
   }
   
   public void testPrimitiveNull() throws Exception {    
      PrimitiveArrayExample example = serializer.read(PrimitiveArrayExample.class, PRIMITIVE_NULL);
      
      assertEquals(example.array.length, 5);
      assertEquals(example.array[0], null);
      assertEquals(example.array[1], "entry two");
      assertEquals(example.array[2], "entry three");
      assertEquals(example.array[3], null);
      assertEquals(example.array[4], null);
      
      validate(example, serializer);
   }
   
   public void testParentComposite() throws Exception {    
      ParentCompositeArrayExample example = serializer.read(ParentCompositeArrayExample.class, COMPOSITE);
      
      assertEquals(example.array.length, 5);
      assertEquals(example.array[0].value, "entry one");
      assertEquals(example.array[1].value, "entry two");
      assertEquals(example.array[2].value, "entry three");
      assertEquals(example.array[3].value, "entry four");
      assertEquals(example.array[4].value, "entry five");
      
      validate(example, serializer);
   }
   
   public void testDefaultComposite() throws Exception {    
      DefaultCompositeArrayExample example = serializer.read(DefaultCompositeArrayExample.class, DEFAULT_COMPOSITE);
      
      assertEquals(example.array.length, 5);
      assertEquals(example.array[0].value, "entry one");
      assertEquals(example.array[1].value, "entry two");
      assertEquals(example.array[2].value, "entry three");
      assertEquals(example.array[3].value, "entry four");
      assertEquals(example.array[4].value, "entry five");
      
      validate(example, serializer);
   }
  
   public void testParentCompositeNull() throws Exception {    
      ParentCompositeArrayExample example = serializer.read(ParentCompositeArrayExample.class, COMPOSITE_NULL);
      
      assertEquals(example.array.length, 5);
      assertEquals(example.array[0], null);
      assertEquals(example.array[1].value, "entry two");
      assertEquals(example.array[2], null);
      assertEquals(example.array[3], null);
      assertEquals(example.array[4].value, "entry five");
      
      validate(example, serializer);
   }
   
   public void testCharacter() throws Exception {    
      CharacterArrayExample example = serializer.read(CharacterArrayExample.class, CHARACTER);
      
      assertEquals(example.array.length, 5);
      assertEquals(example.array[0], 'a');
      assertEquals(example.array[1], 'b');
      assertEquals(example.array[2], 'c');
      assertEquals(example.array[3], 'd');
      assertEquals(example.array[4], 'e');
      
      validate(example, serializer);
   }
   
   public void testDifferentArray() throws Exception {    
      DifferentArrayExample example = new DifferentArrayExample();
      
      validate(example, serializer);
   }
}
