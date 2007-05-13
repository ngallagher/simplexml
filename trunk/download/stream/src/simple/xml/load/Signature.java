package simple.xml.load;

import simple.xml.Root;

final class Signature {
   
   private Contact contact;
   
   private Label label;
   
   public Signature(Contact contact, Label label) {      
      this.contact = contact;
      this.label = label;
   }
  
   public Class getDependant() {
      return label.getDependant();
   }
   
   public Contact getContact() {
      return contact;
   }
   
   public String getName() throws Exception {
      if(label.isInline()) {
         return getRoot();         
      }
      return getDefault();
   }
   
   private String getRoot() throws Exception {
      Class type = getDependant();
      Root root = getRoot(type);     
      
      if(root == null) {
         throw new PersistenceException("Root required for %s in %s", type, label);        
      }
      String name = root.name();
      
      if(isEmpty(name)) {
         throw new PersistenceException("Root requires name for %s in %s", type, label);
      }     
      return root.name();
   }
   
   private Root getRoot(Class<?> type) {
      return type.getAnnotation(Root.class);      
   }
   
   private String getDefault() {
      String name = label.getOverride();
      
      if(!isEmpty(name)) {
         return name;
      }
      return contact.getName();
   }
   
   /**
    * This method is used to determine if a root annotation value is
    * an empty value. Rather than determining if a string is empty
    * be comparing it to an empty string this method allows for the
    * value an empty string represents to be changed in future.
    * 
    * @param value this is the value to determine if it is empty
    * 
    * @return true if the string value specified is an empty value
    */
   private boolean isEmpty(String value) {
      return value.length() == 0;
   }
}
