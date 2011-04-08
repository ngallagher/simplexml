/*
 * ConstructorScanner.java July 2009
 *
 * Copyright (C) 2009, Niall Gallagher <niallg@users.sf.net>
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
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Text;

/**
 * The <code>ConstructorScanner</code> object is used to scan all 
 * all constructors that have XML annotations for their parameters. 
 * parameters. Each constructor scanned is converted in to a 
 * <code>Initializer</code> object. In order to ensure consistency
 * amongst the annotated parameters each named parameter must have
 * the exact same type and annotation attributes across the 
 * constructors. This ensures a consistent XML representation.
 *
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Scanner
 */
class ConstructorScanner {

   /**
    * This contains a list of all the initializers for the class.
    */
   private List<Initializer> list;
   
   /**
    * This represents the default no argument constructor used.
    */
   private Initializer primary;
   
   /**
    * This is used to acquire a parameter by the parameter name.
    */
   private Signature signature;
   
   /**
    * This is the type that is scanner for annotated constructors.
    */
   private Class type;
   
   /**
    * Constructor for the <code>ConstructorScanner</code> object. 
    * This is used to scan the specified class for constructors that
    * can be used to instantiate the class. Only constructors that
    * have all parameters annotated will be considered.
    * 
    * @param type this is the type that is to be scanned
    */
   public ConstructorScanner(Class type) throws Exception {
      this.list = new ArrayList<Initializer>();
      this.signature = new Signature(type);
      this.type = type;
      this.scan(type);
   }
   
   /**
    * This is used to create the object instance. It does this by
    * either delegating to the default no argument constructor or by
    * using one of the annotated constructors for the object. This
    * allows deserialized values to be injected in to the created
    * object if that is required by the class schema.
    * 
    * @return this returns the creator for the class object
    */
   public Creator getCreator() {
      return new ClassCreator(list, signature, primary);
   }
   
   /**
    * This is used to scan the specified class for constructors that
    * can be used to instantiate the class. Only constructors that
    * have all parameters annotated will be considered.
    * 
    * @param type this is the type that is to be scanned
    */
   private void scan(Class type) throws Exception {
      Constructor[] array = type.getDeclaredConstructors();
      
      if(!isInstantiable(type)) {
         throw new ConstructorException("Can not construct inner %s", type);
      }
      for(Constructor factory: array){
         Signature index = new Signature(type);
         
         if(!type.isPrimitive()) { 
            scan(factory, index);
         }
      } 
   }
   
   /**
    * This is used to scan the specified constructor for annotations
    * that it contains. Each parameter annotation is evaluated and 
    * if it is an XML annotation it is considered to be a valid
    * parameter and is added to the parameter map.
    * 
    * @param factory this is the constructor that is to be scanned
    * @param map this is the parameter map that contains parameters
    */
   private void scan(Constructor factory, Signature map) throws Exception {
      Annotation[][] labels = factory.getParameterAnnotations();
      Class[] types = factory.getParameterTypes();

      for(int i = 0; i < types.length; i++) {         
         for(int j = 0; j < labels[i].length; j++) {
            Parameter value = process(factory, labels[i][j], i);
            
            if(value != null) {
               String name = value.getName();
               
               if(map.containsKey(name)) {
                  throw new PersistenceException("Parameter '%s' is a duplicate in %s", name, factory);
               }
               signature.put(name, value);
               map.put(name, value);
            }
         }
      }
      if(types.length == map.size()) {
         build(factory, map);
      }
   }
   
   /**
    * This is used to build the <code>Initializer</code> object that is
    * to be used to instantiate the object. The initializer contains 
    * the constructor at the parameters in the declaration order.
    * 
    * @param factory this is the constructor that is to be scanned
    * @param signature the parameter map that contains parameters
    */
   private void build(Constructor factory, Signature signature) throws Exception {
      Initializer initializer = new Initializer(factory, signature);
      
      if(initializer.isDefault()) {
         primary = initializer;
      }
      list.add(initializer);   
   }
   
   /**
    * This is used to create a <code>Parameter</code> object which is
    * used to represent a parameter to a constructor. Each parameter
    * contains an annotation an the index it appears in.
    * 
    * @param factory this is the constructor the parameter is in
    * @param label this is the annotation used for the parameter
    * @param ordinal this is the position the parameter appears at
    * 
    * @return this returns the parameter for the constructor
    */
   private Parameter process(Constructor factory, Annotation label, int ordinal) throws Exception{
      if(label instanceof Attribute) {
         return create(factory, label, ordinal);
      }
      if(label instanceof ElementList) {
         return create(factory, label, ordinal);
      }     
      if(label instanceof ElementArray) {
         return create(factory, label, ordinal);
      }
      if(label instanceof ElementMap) {
         return create(factory, label, ordinal);
      }
      if(label instanceof Element) {
         return create(factory, label, ordinal);
      }
      if(label instanceof Text) {
         return create(factory, label, ordinal);
      }
      return null;
   }
   
   /**
    * This is used to create a <code>Parameter</code> object which is
    * used to represent a parameter to a constructor. Each parameter
    * contains an annotation an the index it appears in.
    * 
    * @param factory this is the constructor the parameter is in
    * @param label this is the annotation used for the parameter
    * @param ordinal this is the position the parameter appears at
    * 
    * @return this returns the parameter for the constructor
    */
   private Parameter create(Constructor factory, Annotation label, int ordinal) throws Exception {
      Parameter value = ParameterFactory.getInstance(factory, label, ordinal);
      String name = value.getName(); 
      
      if(signature.containsKey(name)) {
         validate(value, name);
      }
      return value;
   }
   
   /**
    * This is used to validate the parameter against all the other
    * parameters for the class. Validating each of the parameters
    * ensures that the annotations for the parameters remain
    * consistent throughout the class.
    * 
    * @param parameter this is the parameter to be validated
    * @param name this is the name of the parameter to validate
    */
   private void validate(Parameter parameter, String name) throws Exception {
      Parameter other = signature.get(name);
      Annotation label = other.getAnnotation();
      
      if(!parameter.getAnnotation().equals(label)) {
         throw new MethodException("Annotations do not match for '%s' in %s", name, type);
      }
      Class expect = other.getType();
      
      if(expect != parameter.getType()) {
         throw new MethodException("Method types do not match for '%s' in %s", name, type);
      }
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
   private boolean isInstantiable(Class type) {
      int modifiers = type.getModifiers();
       
      if(Modifier.isStatic(modifiers)) {
         return true;
      }
      return !type.isMemberClass();       
   }
}
