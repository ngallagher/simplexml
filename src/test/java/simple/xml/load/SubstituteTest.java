package simple.xml.load;

import java.io.StringWriter;

import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.Root;
import simple.xml.Text;
import simple.xml.ValidationTestCase;

public class SubstituteTest extends ValidationTestCase {

   private static final String SOURCE =
   "<?xml version=\"1.0\"?>\n"+
   "<substituteExample>\n"+
   "   <substitute class='simple.xml.load.SubstituteTest$SimpleSubstitute'>some example text</substitute>  \n\r"+
   "</substituteExample>";


   @Root
   private static class SubstituteExample {

      @Element   
      public Substitute substitute;                   
   }

   @Root
   private static class Substitute {

      @Text
      public String text;
   }

   private static class SimpleSubstitute extends Substitute {
      
      @Replace
      public Substitute replace() {
         return new OtherSubstitute("this is the other substitute", text);
      }
      
      @Persist
      public void persist() {
         throw new IllegalStateException("Simple substitute should never be written");
      }
   }
   
   private static class OtherSubstitute extends Substitute {
      
      @Attribute
      public String name;
      
      public OtherSubstitute() {
         super();
      }
      
      public OtherSubstitute(String name, String text) {
         this.text = text;
         this.name = name;
      }
      
      @Persist
      public void persist() {
         System.out.println("persist");
      }
   }
   
   private static class YetAnotherSubstitute extends Substitute {
   
      public YetAnotherSubstitute() {
         super();
      }
      
      @Resolve
      public Substitute resolve() {
         return new LargeSubstitute("John Doe", "Sesame Street", "Metropilis");
      }
   }
   
   private static class LargeSubstitute extends Substitute {
      
      @Element
      private String name;
      
      @Element 
      private String street;
      
      @Element
      private String city;     
      
      public LargeSubstitute(String name, String street, String city) {
         this.city = city;
         this.street = street;
         this.city = city;
      }
   }
        
   private Persister serializer;

   public void setUp() {
      serializer = new Persister();
   }
   
   public void testFirst() throws Exception {    
      SubstituteExample example = serializer.read(SubstituteExample.class, SOURCE);
      
      assertEquals(example.substitute.getClass(), SimpleSubstitute.class);
      assertEquals(example.substitute.text, "some example text");
      
      validate(example, serializer);
      
      StringWriter out = new StringWriter();
      serializer.write(example, out);
      String text = out.toString();      
      
      example = serializer.read(SubstituteExample.class, text);
      
      assertEquals(example.substitute.getClass(), OtherSubstitute.class);
      assertEquals(example.substitute.text, "some example text");
      
      validate(example, serializer);
   }
}
