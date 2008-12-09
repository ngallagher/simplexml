/*
 * ClassScanner.java July 2008
 *
 * Copyright (C) 2008, Niall Gallagher <niallg@users.sf.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General 
 * Public License along with this library; if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
 * Boston, MA  02111-1307  USA
 */

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * The <code>ClassScanner</code> performs the reflective inspection
 * of a class and extracts all the class level annotations. This will
 * also extract the methods that are annotated. This ensures that the
 * callback methods can be invoked during the deserialization process.
 * Also, this will read the namespace annotations that are used.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Scanner
 */ 
class ClassScanner  {
   
   /**
    * This is the decorator associated with this scanner.
    */
   private NamespaceDecorator decorator;
   
   /**
    * This is the namespace associated with the scanned class.
    */
   private Namespace namespace;
   
   /**
    * This function acts as a pointer to the types commit process.
    */
   private Function commit;
   
   /**
    * This function acts as a pointer to the types validate process.
    */
   private Function validate;

   /**
    * This function acts as a pointer to the types persist process.
    */
   private Function persist;

   /**
    * This function acts as a pointer to the types complete process.
    */
   private Function complete;   
   
   /**
    * This function is used as a pointer to the replacement method.
    */
   private Function replace;
   
   /**
    * This function is used as a pointer to the resolution method.
    */
   private Function resolve;
   
   /**
    * This is the optional order annotation for the scanned class.
    */
   private Order order;

   /**
    * This is the optional root annotation for the scanned class.
    */
   private Root root;
   
   /**
    * Constructor for the <code>ClassScanner</code> object. This is 
    * used to scan the provided class for annotations that are used 
    * to build a schema for an XML file to follow. 
    * 
    * @param type this is the type that is scanned for a schema
    */
   public ClassScanner(Class type) throws Exception {  
      this.decorator = new NamespaceDecorator();
      this.scan(type);
   }      
   
   /**
    * This is used to acquire the <code>Decorator</code> for this.
    * A decorator is an object that adds various details to the
    * node without changing the overall structure of the node. For
    * example comments and namespaces can be added to the node with
    * a decorator as they do not affect the deserialization.
    * 
    * @return this returns the decorator associated with this
    */
   public Decorator getDecorator() {
      return decorator;
   }
   
   /**
    * This returns the order annotation used to determine the order
    * of serialization of attributes and elements. The order is a
    * class level annotation that can be used only once per class
    * XML schema. If none exists then this will return null.
    *  of the class processed by this scanner.
    * 
    * @return this returns the name of the object being scanned
    */
   public Order getOrder() {
      return order;
   }
   
   /**
    * This returns the root of the class processed by this scanner.
    * The root determines the type of deserialization that is to
    * be performed and also contains the name of the root element. 
    * 
    * @return this returns the name of the object being scanned
    */
   public Root getRoot() {
      return root;
   }

   /**
    * This method is used to retrieve the schema class commit method
    * during the deserialization process. The commit method must be
    * marked with the <code>Commit</code> annotation so that when the
    * object is deserialized the persister has a chance to invoke the
    * method so that the object can build further data structures.
    * 
    * @return this returns the commit method for the schema class
    */
   public Function getCommit() {
      return commit;           
   }

   /**
    * This method is used to retrieve the schema class validation
    * method during the deserialization process. The validation method
    * must be marked with the <code>Validate</code> annotation so that
    * when the object is deserialized the persister has a chance to 
    * invoke that method so that object can validate its field values.
    * 
    * @return this returns the validate method for the schema class
    */   
   public Function getValidate() {
      return validate;       
   }
   
   /**
    * This method is used to retrieve the schema class persistence
    * method. This is invoked during the serialization process to
    * get the object a chance to perform an necessary preparation
    * before the serialization of the object proceeds. The persist
    * method must be marked with the <code>Persist</code> annotation.
    * 
    * @return this returns the persist method for the schema class
    */
   public Function getPersist() {
      return persist;           
   }

   /**
    * This method is used to retrieve the schema class completion
    * method. This is invoked after the serialization process has
    * completed and gives the object a chance to restore its state
    * if the persist method required some alteration or locking.
    * This is marked with the <code>Complete</code> annotation.
    * 
    * @return returns the complete method for the schema class
    */   
   public Function getComplete() {
      return complete;           
   }
   
   /**
    * This method is used to retrieve the schema class replacement
    * method. The replacement method is used to substitute an object
    * that has been deserialized with another object. This allows
    * a seamless delegation mechanism to be implemented. This is
    * marked with the <code>Replace</code> annotation. 
    * 
    * @return returns the replace method for the schema class
    */
   public Function getReplace() {
      return replace;
   }
   
   /**
    * This method is used to retrieve the schema class replacement
    * method. The replacement method is used to substitute an object
    * that has been deserialized with another object. This allows
    * a seamless delegation mechanism to be implemented. This is
    * marked with the <code>Replace</code> annotation. 
    * 
    * @return returns the replace method for the schema class
    */
   public Function getResolve() {
      return resolve;
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
      if(root != null) {
         return root.strict();              
      }              
      return true;
   }
  
   /**
    * Scan the fields and methods such that the given class is scanned 
    * first then all super classes up to the root <code>Object</code>. 
    * All fields and methods from the most specialized classes override 
    * fields and methods from higher up the inheritance heirarchy. This
    * means that annotated details can be overridden and so may not 
    * have a value assigned to them during deserialization.
    * 
    * @param type the class to extract fields and methods from
    */   
   private void scan(Class type) throws Exception {
      Class real = type;
      
      while(type != null) {
         if(namespace == null) {
            namespace(type);
         }
         if(root == null) {              
            root(type);
         }  
         if(order == null) {
            order(type);
         }
         scope(type);
         scan(real, type);
         type = type.getSuperclass();
      }      
      process(real); 
   }

   /**
    * This is used to scan the specified class for methods so that
    * the persister callback annotations can be collected. These
    * annotations help object implementations to validate the data
    * that is injected into the instance during deserialization.
    * 
    * @param real this is the actual type of the scanned class 
    * @param type this is a type from within the class hierarchy
    * 
    * @throws Exception thrown if the class schema is invalid
    */
   private void scan(Class real, Class type) throws Exception {
      Method[] method = type.getDeclaredMethods();

      for(int i = 0; i < method.length; i++) {
         Method next = method[i];
         
         if(!next.isAccessible()) {
            next.setAccessible(true);
         }
         scan(next);              
      }     
   }

   /**
    * This is used to acquire the optional <code>Root</code> from the
    * specified class. The root annotation provides information as
    * to how the object is to be parsed as well as other information
    * such as the name of the object if it is to be serialized.
    *
    * @param type this is the type of the class to be inspected
    */    
   private void root(Class<?> type) {
      if(type.isAnnotationPresent(Root.class)) {
         root = type.getAnnotation(Root.class);
      }
   }
   
   /**
    * This is used to acquire the optional order annotation to provide
    * order to the elements and attributes for the generated XML. This
    * acts as an override to the order provided by the declaration of
    * the types within the object.  
    * 
    * @param type this is the type to be scanned for the order
    */
   private void order(Class<?> type) {
      if(type.isAnnotationPresent(Order.class)) {
         order = type.getAnnotation(Order.class);
      }
   }
   
   /**
    * This is use to scan for <code>Namespace</code> annotations on
    * the class. Once a namespace has been located then it is used
    * to populate the internal namespace decorator. This can then be
    * used to decorate any output node that requires it.
    * 
    * @param type this is the XML schema class to scan for namespaces
    */
   private void namespace(Class<?> type) {
      if(type.isAnnotationPresent(Namespace.class)) {
         namespace = type.getAnnotation(Namespace.class);
         
         if(namespace != null) {
            decorator.add(namespace);
         }
      }
   }
   
   /**
    * This is use to scan for <code>NamespaceList</code> annotations 
    * on the class. Once a namespace list has been located then it is 
    * used to populate the internal namespace decorator. This can then 
    * be used to decorate any output node that requires it.
    * 
    * @param type this is the XML class to scan for namespace lists
    */
   private void scope(Class<?> type) {
      if(type.isAnnotationPresent(NamespaceList.class)) {
         NamespaceList scope = type.getAnnotation(NamespaceList.class);
         Namespace[] list = scope.value();
         
         for(Namespace name : list) {
            decorator.add(name);
         }
      }
   }
   
   /**
    * This is used to scan the specified object to extract the fields
    * and methods that are to be used in the serialization process.
    * This will acquire all fields and getter setter pairs that have
    * been annotated with the XML annotations.
    *
    * @param type this is the object type that is to be scanned
    */  
   private void process(Class type) throws Exception {
      if(namespace != null) {
         decorator.set(namespace);
      }
   }
   
   /**
    * Scans the provided method for a persister callback method. If 
    * the method contains an method annotated as a callback that 
    * method is stored so that it can be invoked by the persister
    * during the serialization and deserialization process.
    * 
    * @param method this is the method to scan for callback methods
    */
   private void scan(Method method) {
      if(commit == null) {           
         commit(method);
      }
      if(validate == null) {      
         validate(method);
      }
      if(persist == null) {      
         persist(method);
      }
      if(complete == null) {      
         complete(method);
      }    
      if(replace == null) {
         replace(method);              
      }   
      if(resolve == null) {
         resolve(method);              
      }  
   }
   
   /**
    * This method is used to check the provided method to determine
    * if it contains the <code>Replace</code> annotation. If the
    * method contains the required annotation it is stored so that
    * it can be invoked during the deserialization process.
    *
    * @param method this is the method checked for the annotation
    */ 
   private void replace(Method method) {
      Annotation mark = method.getAnnotation(Replace.class);

      if(mark != null) {
         replace = getFunction(method);                    
      }      
   }
   
   /**
    * This method is used to check the provided method to determine
    * if it contains the <code>Resolve</code> annotation. If the
    * method contains the required annotation it is stored so that
    * it can be invoked during the deserialization process.
    *
    * @param method this is the method checked for the annotation
    */ 
   private void resolve(Method method) {
      Annotation mark = method.getAnnotation(Resolve.class);

      if(mark != null) {
         resolve = getFunction(method);                    
      }      
   }
   
   /**
    * This method is used to check the provided method to determine
    * if it contains the <code>Commit</code> annotation. If the
    * method contains the required annotation it is stored so that
    * it can be invoked during the deserialization process.
    *
    * @param method this is the method checked for the annotation
    */ 
   private void commit(Method method) {
      Annotation mark = method.getAnnotation(Commit.class);

      if(mark != null) {
         commit = getFunction(method);                    
      }    
   }
   
   /**
    * This method is used to check the provided method to determine
    * if it contains the <code>Validate</code> annotation. If the
    * method contains the required annotation it is stored so that
    * it can be invoked during the deserialization process.
    *
    * @param method this is the method checked for the annotation
    */ 
   private void validate(Method method) {
      Annotation mark = method.getAnnotation(Validate.class);

      if(mark != null) {
         validate = getFunction(method);                    
      }         
   }
   
   /**
    * This method is used to check the provided method to determine
    * if it contains the <code>Persist</code> annotation. If the
    * method contains the required annotation it is stored so that
    * it can be invoked during the deserialization process.
    *
    * @param method this is the method checked for the annotation
    */    
   private void persist(Method method) {
      Annotation mark = method.getAnnotation(Persist.class);

      if(mark != null) {
         persist = getFunction(method);                    
      }      
   }

   /**
    * This method is used to check the provided method to determine
    * if it contains the <code>Complete</code> annotation. If the
    * method contains the required annotation it is stored so that
    * it can be invoked during the deserialization process.
    *
    * @param method this is the method checked for the annotation
    */ 
   private void complete(Method method) {
      Annotation mark = method.getAnnotation(Complete.class);

      if(mark != null) {
         complete = getFunction(method);                    
      }      
   } 
   
   /**
    * This is used to acquire a <code>Function</code> object for the
    * method provided. The function returned will allow the callback
    * method to be invoked when given the context and target object.
    * 
    * @param method this is the method that is to be invoked
    * 
    * @return this returns the function that is to be invoked
    */
   private Function getFunction(Method method) {
      boolean contextual = isContextual(method);
      
      return new Function(method, contextual);
   }
   
   /**
    * This is used to determine whether the annotated method takes a
    * contextual object. If the method takes a <code>Map</code> then
    * this returns true, otherwise it returns false.
    *
    * @param method this is the method to check the parameters of
    *
    * @return this returns true if the method takes a map object
    */ 
   private boolean isContextual(Method method)  {
      Class[] list = method.getParameterTypes();

      if(list.length == 1) {
         return Map.class.equals(list[0]);                 
      }      
      return false;
   }
}
