package simple.xml.temp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.ElementArray;
import simple.xml.ElementList;
import simple.xml.Text;

public class FieldScanner {
   
   private ContactList list;
   
   private Class type;
   
   public FieldScanner(Class type) {
      this.list = new ContactList();
      this.type = type;
   }
   
   public ContactList getContacts() {
      return list;
   }
   
   public void run() throws Exception {
      scan(type);
   }
   
   private void scan(Class type) throws Exception {
      Class real = type;
      
      do {           
         scan(real, type);
         type = type.getSuperclass();
      }
      while(type != null);    
   }
   
   private void scan(Class real, Class type) {
      Field[] field = type.getDeclaredFields();
      
      for(int i = 0; i < field.length; i++) {                       
         scan(field[i]);                      
      }   
   }
   
   public void scan(Field field) {
      Annotation[] list = field.getDeclaredAnnotations();
      
      for(int i = 0; i < list.length; i++) {
         scan(field, list[i]);                       
      }  
   }
   
   public void scan(Field field, Annotation label) {
      if(label instanceof Attribute) {
         process(field, label);
      }
      if(label instanceof ElementList) {
         process(field, label);
      }
      if(label instanceof ElementArray) {
         process(field, label);
      }
      if(label instanceof Element) {
         process(field, label);
      }             
      if(label instanceof Text) {
         process(field, label);
      }
   }
   
   public void process(Field field, Annotation label) {
      list.add(new FieldContact(field, label));
   }
}