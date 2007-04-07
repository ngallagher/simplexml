package simple.xml.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.ElementArray;
import simple.xml.ElementList;
import simple.xml.Text;

public class XScanner {
   
   private Map attributes;
   
   private Map elements;
   
   private Class type;
   
   public XScanner(Class type) {
      
   }
   
   public void scan() throws Exception {
     // scan(type);
      field(type);
      method(type);
      //validate();
   }
   
   public void field(Class type) throws Exception {
      ContactList list = getFields(type);
      
      for(Contact contact : list) {
         scan(contact, contact.getAnnotation());
      }
   }
   
   
   public void method(Class type) throws Exception {
      ContactList list = getMethods(type);
      
      for(Contact contact : list) {           
         scan(contact, contact.getAnnotation());
      }
   }
   
   private void scan(Contact field, Annotation label) throws Exception {
      if(label instanceof Attribute) {
         process(field, label, attributes);
      }
      if(label instanceof ElementList) {
         process(field, label, elements);
      }
      if(label instanceof ElementArray) {
         process(field, label, elements);
      }
      if(label instanceof Element) {
         process(field, label, elements);
      }             
      if(label instanceof Text) {
      //   process(field, label);
      }
   }
   
   private void process(Contact field, Annotation type, Map map) throws Exception {
      //Label label = LabelFactory.getInstance(field, type);
      //String name = label.getName().toLowerCase();
      
      //if(map.containsKey(name)) {
      //   throws new Exception("Annotation of name %s declared twice", name);
      //}
      //map.put(name, label);      
   }
   
   public ContactList getFields(Class type) throws Exception {
      return new FieldScanner(type).getContacts();
   }
   
   public ContactList getMethods(Class type) throws Exception {
      return new MethodScanner(type).getContacts();
   }

}
