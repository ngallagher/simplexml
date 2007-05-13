package simple.xml.load;

import simple.xml.Root;

final class Signature {
   
   private Contact contact;
   
   private Label label;
   
   private Class type;
   
   public Signature(Contact contact, Label label) {
      this.type = contact.getType();
      this.contact = contact;
      this.label = label;
   }
  
   public Class getType() {
      return type;
   }
   
   public Contact getContact() {
      return contact;
   }
   
   public String getName() {
      if(label.isInline()) {
         return getRoot();
      }
      return getDefault();
   }
   
   private String getDefault() {
      String name = label.getOverride();
      
      if(!isEmpty(name)) {
         return name;
      }
      return contact.getName();
   }
   
   private String getRoot() {
      Class type = label.getType();
      Root root = getRoot(type);
      
      if(root == null) {
         /* Exception */  
      }
      String name = root.name();
      
      if(isEmpty(name)) {
         /* Exception */
      }     
      return root.name();
   }
   
   private Root getRoot(Class<?> type) {
      return type.getAnnotation(Root.class);      
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
