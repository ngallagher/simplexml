package org.simpleframework.xml.load;

// This object will clone an object according to its XML annotations. It requires
// that the objects must be of the EXACT same time, because this is the only way
// to ensure that all validation concerns are dealt with!!
//
// Also this simplifies the whole process in the lond run
public class Cloner {
   
   private final Source root;
   
   public Cloner(Source root) {
      this.root = root;
   }
   
   public void write(Object source, Object clone) throws Exception {
      Schema schema = root.getSchema(source);
      
      if(!isCompatible(source, clone)) {
         throw new PersistenceException("Objects must be of the EXACT SAME type");
      }
      write(source, clone, schema);
   }
   
   private void write(Object source, Object clone, Schema schema) throws Exception {
      writeAttributes(source, source, schema);
      writeElements(source, source, schema);
      writeText(source, source, schema);
   }
   
   private void writeAttributes(Object source, Object clone, Schema schema) throws Exception {
      LabelMap attributes = schema.getAttributes();            

      for(Label label : attributes) {
         Contact contact = label.getContact();         
         Object value = contact.get(source);
         
         contact.set(clone, value);
      }       
   }
   
   private void writeElements(Object source, Object clone, Schema schema) throws Exception {
      LabelMap attributes = schema.getElements();            

      for(Label label : attributes) {
         Contact contact = label.getContact();         
         Object value = contact.get(source);
         
         contact.set(clone, value);
      }       
   }   
   
   private void writeText(Object source, Object clone, Schema schema) throws Exception {
      Label label = schema.getText();
      Contact contact = label.getContact();
      Object value = contact.get(source);
      
      contact.set(clone, value);
   }
   
   // HERE we decide how to clone a value
   private Object clone(Object value) throws Exception {
      return value;
   }

   
   private boolean isCompatible(Object source, Object clone) {
      return false;
   }
}
