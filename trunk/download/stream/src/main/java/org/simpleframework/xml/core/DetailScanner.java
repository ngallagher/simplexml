package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

class DetailScanner implements Detail {
   
   private List<MethodDetail> methods;
   private List<FieldDetail> fields;
   private NamespaceList declaration;
   private Namespace namespace;
   private Annotation[] labels;
   private DefaultType access;
   private Order order;
   private Root root;
   private Class type;
   private String name;
   private boolean required;
   private boolean strict;
   
   public DetailScanner(Class type) throws Exception {
      this.methods = new LinkedList<MethodDetail>();
      this.fields = new LinkedList<FieldDetail>();
      this.labels = type.getDeclaredAnnotations();
      this.strict = true;
      this.type = type;
      this.scan(type);
   }

   /**
    * This is used to determine if the generated annotations are
    * required or not. By default generated parameters are required.
    * Setting this to false means that null values are accepted
    * by all defaulted fields or methods depending on the type.
    * 
    * @return this is used to determine if defaults are required
    */
   public boolean isRequired() {
      return required;
   }
     
   /**
    * This method is used to determine whether strict mappings are
    * required. Strict mapping means that all labels in the class
    * schema must match the XML elements and attributes in the
    * source XML document. When strict mapping is disabled, then
    * XML elements and attributes that do not exist in the schema
    * class will be ignored without breaking the parser.
    *
    * @return true if strict parsing is enabled, false otherwise
    */ 
   public boolean isStrict() {
      return strict;
   }
   
   /**
    * This is used to determine whether this detail represents a
    * primitive type. A primitive type is any type that does not
    * extend <code>Object</code>, examples are int, long and double.
    * 
    * @return this returns true if no XML annotations were found
    */
   public boolean isPrimitive() {
      return type.isPrimitive();
   }
   
   /**
    * This is used to determine if the class is an inner class. If
    * the class is a inner class and not static then this returns
    * false. Only static inner classes can be instantiated using
    * reflection as they do not require a "this" argument.
    * 
    * @return this returns true if the class is a static inner
    */
   public boolean isInstantiable() {
      int modifiers = type.getModifiers();
       
      if(Modifier.isStatic(modifiers)) {
         return true;
      }
      return !type.isMemberClass();       
   }
   
   public Root getRoot() {
      return root;
   }
   
   /**
    * This returns the name of the class processed by this scanner.
    * The name is either the name as specified in the last found
    * <code>Root</code> annotation, or if a name was not specified
    * within the discovered root then the Java Bean class name of
    * the last class annotated with a root annotation.
    * 
    * @return this returns the name of the object being scanned
    */
   public String getName() {
      return name;
   }
   
   public Class getType() {
      return type;
   }
   
   public Order getOrder() {
      return order;
   }
   
   public DefaultType getAccess() {
      return access;
   }
   
   public Namespace getNamespace() {
      return namespace;
   }
   
   public NamespaceList getNamespaceList() {
      return declaration;
   }
   
   public List<MethodDetail> getMethods() {
      return methods;
   }
   
   public List<FieldDetail> getFields() {
      return fields;
   }
   
   public Annotation[] getAnnotations() {
      return labels;
   }

   public Constructor[] getConstructors() {
      return type.getDeclaredConstructors();
   }
   
   public Class getSuper() {
      Class base = type.getSuperclass();
      
      if(base == Object.class) {
         return null;
      }
      return base;
   }
   
   private void scan(Class type) throws Exception {
      methods(type);
      fields(type);
      extract(type);
   }
   
   private void extract(Class type) throws Exception {
      for(Annotation label : labels) {
         if(label instanceof Namespace) {
            namespace(label);
         }
         if(label instanceof NamespaceList) {
            scope(label);
         }
         if(label instanceof Root) {
            root(label);
         }
         if(label instanceof Order) {
            order(label);
         }
         if(label instanceof Default) {
            access(label);
         }
      }
   }
   
   private void methods(Class type) throws Exception {
      Method[] list = type.getDeclaredMethods();
      
      for(Method method : list) {
         MethodDetail detail = new MethodDetail(method);
         methods.add(detail);
      }
   }
   
   private void fields(Class type) throws Exception {
      Field[] list = type.getDeclaredFields();
      
      for(Field field : list) {
         FieldDetail detail = new FieldDetail(field);
         fields.add(detail);
      }
   }
   

   /**
    * This is used to set the optional <code>Root</code> annotation for
    * the class. The root can only be set once, so if a supertype also
    * has a root annotation define it must be ignored. 
    *
    * @param label this is the label used to define the root
    */    
   private void root(Annotation label) {
      if(label != null) {
         Root value = (Root)label;
         String real = type.getSimpleName();
         String text = real;

         if(value != null) {
            text = value.name();

            if(isEmpty(text)) {
               text = Reflector.getName(real);
            }      
            name = text.intern();  
            strict = value.strict();
            root = value;
         }
      }
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
   
   /**
    * This is used to set the optional <code>Order</code> annotation for
    * the class. The order can only be set once, so if a supertype also
    * has a order annotation define it must be ignored. 
    * 
    * @param label this is the label used to define the order
    */
   private void order(Annotation label) {
      if(label != null) {
         order = (Order)label;
      }
   }
   
   /**
    * This is used to set the optional <code>Default</code> annotation for
    * the class. The default can only be set once, so if a supertype also
    * has a default annotation define it must be ignored. 
    * 
    * @param label this is the label used to define the defaults
    */
   private void access(Annotation label) {
      if(label != null) {
         Default value = (Default)label;
         
         required = value.required();
         access = value.value();
      }
   }
   private void namespace(Annotation label) {
      if(label != null) {
         namespace = (Namespace)label;
      }
   }
   
   /**
    * This is use to scan for <code>NamespaceList</code> annotations 
    * on the class. Once a namespace list has been located then it is 
    * used to populate the internal namespace decorator. This can then 
    * be used to decorate any output node that requires it.
    * 
    * @param label the XML annotation to scan for namespace lists
    */
   private void scope(Annotation label) {
      if(label != null) {
         declaration = (NamespaceList)label;
      }
   }
   
   public String toString() {
      return type.toString();
   }
}
