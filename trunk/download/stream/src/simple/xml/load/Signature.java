package simple.xml.load;

import java.beans.Introspector;
import simple.xml.Root;

final class Signature {
   
   private Contact contact;
   
   private Label label;
   
   private String name;
   
   public Signature(Contact contact, Label label) {      
      this.contact = contact;
      this.label = label;
   }
   
   public Contact getContact() {
      return contact;
   }
   
   public Class getDependant() throws Exception {
      return label.getDependant();
   }
   
   public String getName() throws Exception {
	  if(name != null) {
		  return name;
	  }
      if(label.isInline()) {
         name = getRoot();         
      } else {
         name = getDefault();
      }
      return name;
   }
   
   private String getRoot() throws Exception {
	  String name = label.getOverride();
	  
	  if(!isEmpty(name)) {
		  throw new ElementException("Inline element %s can not have name", label);
	  }
      Class type = getDependant();
      String root = getRoot(type);     
      
      if(root == null) {
         throw new RootException("Root required for %s in %s", type, label);        
      }   
      return root;
   }
   
   private String getRoot(Class type) { 
	   Class real = type;
	      
	   while(type != null) {
	      String name = getRoot(real, type);
	      
	      if(name != null) {
	    	  return name.intern();
	      }
	   }
	   return null;     
   }
   
   private String getRoot(Class<?> real, Class<?> type) {
	   String name = type.getSimpleName();
	   
	   if(type.isAnnotationPresent(Root.class)) {
          Root root = type.getAnnotation(Root.class);
          String text = root.name();
          
          if(!isEmpty(text)) {
        	  return text;
          }
          return Introspector.decapitalize(name);
	   }
	   return null;
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
