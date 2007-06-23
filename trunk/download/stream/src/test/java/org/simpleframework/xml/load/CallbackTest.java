package simple.xml.load;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.load.Persister;

import junit.framework.TestCase;

public class CallbackTest extends TestCase {
        
   private static final String SOURCE =
   "<?xml version=\"1.0\"?>\n"+
   "<root number='1234' flag='true'>\n"+
   "   <value>complete</value>  \n\r"+
   "</root>";
   
   @Root(name="root")
   private static class Entry {

      @Attribute(name="number", required=false)
      private int number = 9999;     

      @Attribute(name="flag")
      private boolean bool;
      
      @Element(name="value", required=false)
      private String value = "default";

      private boolean validated;

      private boolean committed;

      private boolean persisted;

      private boolean completed;

      @Validate
      public void validate() {
         validated = true;                       
      }

      @Commit
      public void commit() {
         if(validated) {              
            committed = true;              
         }            
      }

      @Persist
      public void persist() {
         persisted = true;              
      }              

      @Complete
      public void complete() {
         if(persisted) {              
            completed = true;               
         }            
      }

      public boolean isCommitted() {
         return committed;              
      }

      public boolean isValidated() {
         return validated;              
      }

      public boolean isPersisted() {
         return persisted;              
      }

      public boolean isCompleted() {
         return completed;              
      }

      public int getNumber() {
         return number;              
      }         

      public boolean getFlag() {
         return bool;              
      }

      public String getValue() {
         return value;              
      }
   }

   private static class ExtendedEntry extends Entry {

      public boolean completed;           

      public boolean committed;

      public boolean validated;

      public boolean persisted;
     
      @Validate
      public void extendedValidate() {
         validated = true;                       
      }

      @Commit
      public void extendedCommit() {
         if(validated) {              
            committed = true;              
         }            
      }

      @Persist
      public void extendedPersist() {
         persisted = true;              
      }              

      @Complete
      public void extendedComplete() {
         if(persisted) {              
            completed = true;               
         }            
      }

      public boolean isExtendedCommitted() {
         return committed;              
      }

      public boolean isExtendedValidated() {
         return validated;              
      }

      public boolean isExtendedPersisted() {
         return persisted;              
      }

      public boolean isExtendedCompleted() {
         return completed;              
      }
   }

   private static class OverrideEntry extends Entry {

      public boolean validated;

      @Override
      public void validate() {
         validated = true;              
      }      

      public boolean isOverrideValidated() {
         return validated;              
      }
   }
        
   private Persister persister;

   public void setUp() {
      persister = new Persister();
   }
	
   public void testReadCallbacks() throws Exception {    
      Entry entry = persister.read(Entry.class, SOURCE);

      assertEquals("complete", entry.getValue());
      assertEquals(1234, entry.getNumber());
      assertEquals(true, entry.getFlag());      
      assertTrue(entry.isValidated());
      assertTrue(entry.isCommitted());
   }

   public void testWriteCallbacks() throws Exception {
      Entry entry = new Entry();

      assertFalse(entry.isCompleted());
      assertFalse(entry.isPersisted());
      
      persister.write(entry, System.out);

      assertEquals("default", entry.getValue());
      assertEquals(9999, entry.getNumber());
      assertTrue(entry.isPersisted());
      assertTrue(entry.isCompleted());
   }

   public void testExtendedReadCallbacks() throws Exception {    
      ExtendedEntry entry = persister.read(ExtendedEntry.class, SOURCE);

      assertEquals("complete", entry.getValue());
      assertEquals(1234, entry.getNumber());
      assertEquals(true, entry.getFlag());      
      assertFalse(entry.isValidated());
      assertFalse(entry.isCommitted());
      assertTrue(entry.isExtendedValidated());
      assertTrue(entry.isExtendedCommitted());
   }
   
   public void testExtendedWriteCallbacks() throws Exception {
      ExtendedEntry entry = new ExtendedEntry();

      assertFalse(entry.isCompleted());
      assertFalse(entry.isPersisted());
      assertFalse(entry.isExtendedCompleted());
      assertFalse(entry.isExtendedPersisted());
      
      persister.write(entry, System.out);

      assertEquals("default", entry.getValue());
      assertEquals(9999, entry.getNumber());
      assertFalse(entry.isPersisted());
      assertFalse(entry.isCompleted());
      assertTrue(entry.isExtendedCompleted());
      assertTrue(entry.isExtendedPersisted());
   }

   public void testOverrideReadCallbacks() throws Exception {    
      OverrideEntry entry = persister.read(OverrideEntry.class, SOURCE);

      assertEquals("complete", entry.getValue());
      assertEquals(1234, entry.getNumber());
      assertEquals(true, entry.getFlag());      
      assertFalse(entry.isValidated());
      assertTrue(entry.isOverrideValidated());
   }
   
}
