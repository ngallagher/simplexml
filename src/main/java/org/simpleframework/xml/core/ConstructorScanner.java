/*
 * ConstructorScanner.java July 2009
 *
 * Copyright (C) 2009, Niall Gallagher <niallg@users.sf.net>
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
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;

/**
 * The <code>ConstructorScanner</code> object is used to scan all 
 * all constructors that have XML annotations for their parameters. 
 * parameters. Each constructor scanned is converted in to a 
 * <code>Builder</code> object. In order to ensure consistency
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
    * This contains a list of all the builders for the class.
    */
   private List<Builder> done;
   
   /**
    * This is used to acquire a parameter by the parameter name.
    */
   private ParameterMap all;
   
   /**
    * This represents the default no argument constructor used.
    */
   private Builder primary;
   
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
      this.done = new ArrayList<Builder>();
      this.all = new ParameterMap();
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
      return new ClassCreator(done, all, primary);
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
      
      for(Constructor factory: array){
         ParameterMap map = new ParameterMap();
         
         if(!factory.isAccessible()) {
            factory.setAccessible(true);
         }
         scan(factory, map);
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
   private void scan(Constructor factory, ParameterMap map) throws Exception {
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
               all.put(name, value);
               map.put(name, value);
            }
         }
      }
      if(types.length == map.size()) {
         build(factory, map);
      }
   }
   
   /**
    * This is used to build the <code>Builder</code> object that is
    * to be used to instantiate the object. The builder contains 
    * the constructor at the parameters in the declaration order.
    * 
    * @param factory this is the constructor that is to be scanned
    * @param map this is the parameter map that contains parameters
    */
   private void build(Constructor factory, ParameterMap map) throws Exception {
      Builder builder = new Builder(factory, map);
      
      if(builder.isDefault()) {
         primary = builder;
      }
      done.add(builder);   
   }
   
   /**
    * This is used to create a <code>Parameter</code> object which is
    * used to represent a parameter to a constructor. Each parameter
    * contains an annotation an the index it appears in.
    * 
    * @param factory this is the constructor the parameter is in
    * @param label this is the annotation used for the parameter
    * @param index this is the index the parameter appears in
    * 
    * @return this returns the parameter for the constructor
    */
   private Parameter process(Constructor factory, Annotation label, int index) throws Exception{
      if(label instanceof Attribute) {
         return create(factory, label, index);
      }
      if(label instanceof ElementList) {
         return create(factory, label, index);
      }     
      if(label instanceof ElementArray) {
         return create(factory, label, index);
      }
      if(label instanceof ElementMap) {
         return create(factory, label, index);
      }
      if(label instanceof Element) {
         return create(factory, label, index);
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
    * @param index this is the index the parameter appears in
    * 
    * @return this returns the parameter for the constructor
    */
   private Parameter create(Constructor factory, Annotation label, int index) throws Exception {
      Parameter value = ParameterFactory.getInstance(factory, label, index);
      String name = value.getName(); 
      
      if(all.containsKey(name)) {
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
      Parameter other = all.get(name);
      Annotation label = other.getAnnotation();
      
      if(!parameter.getAnnotation().equals(label)) {
         throw new MethodException("Annotations do not match for '%s' in %s", name, type);
      }
      Class expect = other.getType();
      
      if(expect != parameter.getType()) {
         throw new MethodException("Method types do not match for '%s' in %s", name, type);
      }
   }
}