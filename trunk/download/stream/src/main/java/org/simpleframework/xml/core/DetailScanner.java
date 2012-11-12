package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

class DetailScanner implements Detail {
   
   private NamespaceList declaration;
   private Namespace namespace;
   private Annotation[] annotations;
   private Method[] methods;
   private Field[] fields;
   private DefaultType access;
   private Order order;
   private Root root;
   private Class type;
   private String name;
   private boolean required;
   private boolean strict;
   
   public DetailScanner(Class type) throws Exception {
      this.annotations = type.getDeclaredAnnotations();
      this.methods = type.getDeclaredMethods();
      this.fields = type.getDeclaredFields();
      this.strict = true;
      this.type = type;
      this.scan(type);
   }

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
   
   public boolean isEmpty() {
      return root == null;
   }
   
   public boolean isPrimitive() {
      return type.isPrimitive();
   }
   
   /**
    * This is used to determine if the class is an inner class. If
    * the class is a inner class and not static then this returns
    * false. Only static inner classes can be instantiated using
    * reflection as they do not require a "this" argument.
    * 
    * @param type this is the class that is to be evaluated
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
   
   public Method[] getMethods() {
      return methods;
   }
   
   public Annotation[] getAnnotations() {
      return annotations;
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
      for(Annotation label : annotations) {
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
