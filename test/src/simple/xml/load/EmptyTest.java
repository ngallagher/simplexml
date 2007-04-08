package simple.xml.load;

import java.io.StringWriter;

import junit.framework.TestCase;
import simple.xml.Element;
import simple.xml.Attribute;
import simple.xml.Root;

public class EmptyTest extends TestCase {
        
   private static final String EMPTY_ELEMENT =
   "<?xml version=\"1.0\"?>\n"+
   "<test>\n"+
   "  <empty></empty>\r\n"+
   "</test>\n";
   
   private static final String BLANK_ELEMENT =
   "<?xml version=\"1.0\"?>\n"+
   "<test>\n"+
   "  <empty/>\r\n"+
   "</test>";

   private static final String EMPTY_ATTRIBUTE =
   "<?xml version=\"1.0\"?>\n"+
   "<test attribute=''/>\n";

   @Root(name="test")
   private static class RequiredElement {

      @Element(name="empty")
      private String empty;            
   }  

   @Root(name="test")
   private static class OptionalElement {

      @Element(name="empty", required=false)
      private String empty;
   }    

   @Root(name="test")
   private static class RequiredAttribute {

      @Attribute(name="attribute")            
      private String attribute;
   }

   @Root(name="test")
   private static class OptionalAttribute {

      @Attribute(name="attribute", required=false)            
      private String attribute;
   }

   private Persister persister;

   public void setUp() throws Exception {
      persister = new Persister();
   }
	
   public void testRequiredEmpty() throws Exception {    
      boolean success = false;
      
      try {           
         persister.read(RequiredElement.class, EMPTY_ELEMENT);      
      } catch(ValueRequiredException e) {
         success = true;              
      }
      assertTrue(success);
   }

   public void testRequiredBlank() throws Exception {    
      boolean success = false;
      
      try {           
         persister.read(RequiredElement.class, BLANK_ELEMENT);     
      } catch(ValueRequiredException e) {
         success = true;              
      }
      assertTrue(success);           
   }   

   public void testOptionalEmpty() throws Exception {   
      boolean success = false;
      
      try {           
         persister.read(RequiredElement.class, EMPTY_ELEMENT);     
      } catch(ValueRequiredException e) {
         success = true;              
      }
      assertTrue(success);   
   }

   public void testOptionalBlank() throws Exception {    
      OptionalElement element = persister.read(OptionalElement.class, BLANK_ELEMENT);     

      assertNull(element.empty);
   }     

   public void testRequiredEmptyAttribute() throws Exception {
      RequiredAttribute entry = persister.read(RequiredAttribute.class, EMPTY_ATTRIBUTE);

      assertEquals(entry.attribute, "");      
   }

   public void testOptionalEmptyAttribute() throws Exception {
      OptionalAttribute entry = persister.read(OptionalAttribute.class, EMPTY_ATTRIBUTE);

      assertEquals(entry.attribute, "");      
   }   
}
