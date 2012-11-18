/*
 * ClassScanner.java July 2008
 *
 * Copyright (C) 2008, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.stream.Format;

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
    * This is the namespace decorator associated with this scanner.
    */
   private NamespaceDecorator decorator;
   
   /**
    * This is the scanner that is used to acquire the constructors.
    */
   private ConstructorScanner scanner;
   
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
    * This is used to determine if there is a default type for this.
    */
   private Default access;
   
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
    * @param format this is the format used to style the XML
    */
   public ClassScanner(Class type, Format format) throws Exception { 
      this.scanner = new ConstructorScanner(type, format);
      this.decorator = new NamespaceDecorator();
      this.scan(type);
   }      

   /**
    * This is used to acquire the default signature for the class. 
    * The default signature is the signature for the no argument
    * constructor for the type. If there is no default constructor
    * for the type then this will return null.
    * 
    * @return this returns the default signature if it exists
    */
   public Signature getSignature() {
      return scanner.getSignature();
   }
   
   /**
    * This returns the signatures for the type. All constructors are
    * represented as a signature and returned. More signatures than
    * constructors will be returned if a constructor is annotated 
    * with a union annotation.
    *
    * @return this returns the list of signatures for the type
    */
   public List<Signature> getSignatures(){
      return scanner.getSignatures();
   }
   
   /**
    * This returns a map of all parameters that exist. This is used
    * to validate all the parameters against the field and method
    * annotations that exist within the class. 
    * 
    * @return this returns a map of all parameters within the type
    */
   public ParameterMap getParameters() {
      return scanner.getParameters();
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
    * This is used to provide the <code>Default</code> annotation
    * that holds the default access type to use for the class. This
    * is important in determining how the serializer will deal with
    * methods or fields in the class that do not have annotations.
    * 
    * @return this returns the default annotation for access type
    */
   public Default getDefault() {
      return access;
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
    * fields and methods from higher up the inheritance hierarchy. This
    * means that annotated details can be overridden.
    * 
    * @param type the class to extract method and class annotations
    */   
   private void scan(Class type) throws Exception {
      Class real = type;
      
      while(type != null) {
         global(type);
         scan(real, type);
         type = type.getSuperclass();
      }      
      process(real); 
   }
   
   /**
    * This is used to acquire the annotations that apply globally to 
    * the scanned class. Global annotations are annotations that
    * are applied to the class, such annotations will be used to
    * determine characteristics for the fields and methods of the
    * class, which the serializer uses in the serialization process.  
    * 
    * @param type this is the type to extract the annotations from
    */
   private void global(Class type) throws Exception {
      Annotation[] list = type.getDeclaredAnnotations();
      
      for(Annotation label : list) {
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
         scan(method[i]);              
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
      if(root == null) {
         root = (Root)label;
      }
   }
   
   /**
    * This is used to set the optional <code>Order</code> annotation for
    * the class. The order can only be set once, so if a supertype also
    * has a order annotation define it must be ignored. 
    * 
    * @param label this is the label used to define the order
    */
   private void order(Annotation label) {
      if(order == null) {
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
      if(access == null) {
         access = (Default)label;
      }
   }
   
   /**
    * This is use to scan for <code>Namespace</code> annotations on
    * the class. Once a namespace has been located then it is used
    * to populate the internal namespace decorator. This can then be
    * used to decorate any output node that requires it.
    * 
    * @param label this XML annotation to scan for namespaces
    */
   private void namespace(Annotation label) {
      if(label != null) {
         namespace = (Namespace)label;
         
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
    * @param label the XML annotation to scan for namespace lists
    */
   private void scope(Annotation label) {
      if(label != null) {
         NamespaceList scope = (NamespaceList)label;
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
   private void process(Class type) {
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
      Annotation[] list = method.getDeclaredAnnotations();
      
      for(Annotation label : list) {
         if(label instanceof Commit) {           
            commit(method);
         }
         if(label instanceof Validate) {      
            validate(method);
         }
         if(label instanceof Persist) {      
            persist(method);
         }
         if(label instanceof Complete) {      
            complete(method);
         }    
         if(label instanceof Replace) {
            replace(method);              
         }   
         if(label instanceof Resolve) {
            resolve(method);              
         }  
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
      if(replace == null) {
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
      if(resolve == null) {
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
      if(commit == null) {
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
      if(validate == null) {
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
      if(persist == null) {
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
      if(complete == null) {
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
      
      if(!method.isAccessible()) {
         method.setAccessible(true);
      }
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
