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

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.stream.Format;

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
    * This is a list of all the signatures represented by the type.
    */
   private List<Signature> signatures;
   
   /**
    * This is used to acquire a parameter by the parameter name.
    */
   private ParameterMap registry;
   
   /**
    * This represents the default no argument constructor.
    */
   private Signature primary;
   
   /**
    * This is the format used to style the parameters extracted.
    */
   private Format format;
   
   /**
    * Constructor for the <code>ConstructorScanner</code> object. 
    * This is used to scan the specified class for constructors that
    * can be used to instantiate the class. Only constructors that
    * have all parameters annotated will be considered.
    * 
    * @param type this is the type that is to be scanned
    */
   public ConstructorScanner(Class type, Format format) throws Exception {
      this.signatures = new ArrayList<Signature>();
      this.registry = new ParameterMap();
      this.format = format;
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
      return primary;
   }
   
   /**
    * This returns the signatures for the type. All constructors are
    * represented as a signature and returned. More signatures than
    * constructors will be returned if a constructor is annotated 
    * with a union annotation.
    *
    * @return this returns the list of signatures for the type
    */
   public List<Signature> getSignatures() {
      return new ArrayList<Signature>(signatures);
   }
   
   /**
    * This returns a map of all parameters that exist. This is used
    * to validate all the parameters against the field and method
    * annotations that exist within the class. 
    * 
    * @return this returns a map of all parameters within the type
    */
   public ParameterMap getParameters() {
      return registry;
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
         if(!type.isPrimitive()) { 
            scan(factory);
         }
      } 
   }
   
   /**
    * This is used to scan the parameters within a constructor to 
    * determine the signature of the constructor. If the constructor
    * contains a union annotation multiple signatures will be used.
    * 
    * @param factory the constructor to scan for parameters
    */
   private void scan(Constructor factory) throws Exception {
      SignatureScanner scanner = new SignatureScanner(factory, registry, format);

      if(scanner.isValid()) {
         List<Signature> list = scanner.getSignatures();
            
         for(Signature signature : list) {
            int size = signature.size();
               
            if(size == 0) {
               primary = signature;
               }
            signatures.add(signature);
            }
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
