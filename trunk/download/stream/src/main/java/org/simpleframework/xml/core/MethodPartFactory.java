/*
 * MethodPartFactory.java April 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
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

/**
 * The <code>MethodPartFactory</code> is used to create method parts
 * based on the method signature and the XML annotation. This is 
 * effectively where a method is classified as either a getter or a
 * setter method within an object. In order to determine the type of
 * method the method name is checked to see if it is prefixed with
 * either the "get", "is", or "set" tokens.
 * <p>
 * Once the method is determined to be a Java Bean method according 
 * to conventions the method signature is validated. If the method
 * signature does not follow a return type with no arguments for the
 * get method, and a single argument for the set method then this
 * will throw an exception.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.MethodScanner
 */
final class MethodPartFactory {   

   /**
    * This is used to acquire a <code>MethodPart</code> for the name
    * and annotation of the provided method. This will determine the
    * method type by examining its signature. If the method follows
    * Java Bean conventions then either a setter method part or a
    * getter method part is returned. If the method does not comply
    * with the conventions an exception is thrown.
    * 
    * @param method this is the method to acquire the part for
    * @param label this is the annotation associated with the method
    * 
    * @return this is the method part object for the method
    * 
    * @throws Exception if Java Bean conventions are not followed
    */
   public static MethodPart getInstance(Method method, Annotation label) throws Exception {
      MethodName name = getName(method, label);
      MethodType type = name.getType();
      
      if(type == MethodType.SET) {
         return new SetPart(name, label);
      }
      return new GetPart(name, label);
   }
   
   /**
    * This is used to acquire a <code>MethodName</code> for the name
    * and annotation of the provided method. This will determine the
    * method type by examining its signature. If the method follows
    * Java Bean conventions then either a setter method name or a
    * getter method name is returned. If the method does not comply
    * with the conventions an exception is thrown.
    * 
    * @param method this is the method to acquire the name for
    * @param label this is the annotation associated with the method
    * 
    * @return this is the method name object for the method
    * 
    * @throws Exception if Java Bean conventions are not followed
    */
   private static MethodName getName(Method method, Annotation label) throws Exception {
      MethodType type = getPrefixType(method);
      
      if(type == MethodType.GET) {
         return getRead(method, type);
      }
      if(type == MethodType.IS) {
         return getRead(method, type);         
      }
      if(type == MethodType.SET) {
         return getWrite(method, type);
      }
      throw new MethodException("Annotation %s must mark a set or get method", label);      
   }    

   /**
    * This is used to acquire a <code>MethodType</code> for the name
    * of the method provided. This will determine the method type by 
    * examining its prefix. If the name follows Java Bean conventions 
    * then either a setter method type is returned. If the name does
    * not comply with the naming conventions then null is returned.
    * 
    * @param method this is the method to acquire the type for
    * 
    * @return this is the method name object for the method    
    */
   private static MethodType getPrefixType(Method method) {
      String name = method.getName();      
      
      if(name.startsWith("get")) {
         return MethodType.GET;
      }
      if(name.startsWith("is")) {
         return MethodType.IS;         
      }
      if(name.startsWith("set")) {
         return MethodType.SET;
      }
      return null;
   }
   
   /**
    * This is used to acquire a <code>MethodName</code> for the name
    * and annotation of the provided method. This must be a getter
    * method, and so must have a return type that is not void and 
    * have not arguments. If the method has arguments an exception 
    * is thrown, if not the Java Bean method name is provided.
    *
    * @param method this is the method to acquire the name for
    * @param type this is the method type to acquire the name for    
    * 
    * @return this is the method name object for the method
    * 
    * @throws Exception if Java Bean conventions are not followed
    */
   private static MethodName getRead(Method method, MethodType type) throws Exception {
      Class[] list = method.getParameterTypes();
      String real = method.getName();
         
      if(list.length != 0) {
         throw new MethodException("Get method %s in %s contains parameters", real, type);
      }
      String name = getTypeName(real, type);
      
      return new MethodName(method, type, name);
   }

   /**
    * This is used to acquire a <code>MethodName</code> for the name
    * and annotation of the provided method. This must be a setter
    * method, and so must accept a single argument, if it contains 
    * more or less than one argument an exception is thrown.
    * return type that is not void and
    *
    * @param method this is the method to acquire the name for
    * @param type this is the method type to acquire the name for    
    * 
    * @return this is the method name object for the method
    * 
    * @throws Exception if Java Bean conventions are not followed
    */
   private static MethodName getWrite(Method method, MethodType type) throws Exception {
      Class[] list = method.getParameterTypes();
      String real = method.getName();
      
      if(list.length != 1) {
         throw new MethodException("Set method %s has invalid signature in %s", real, type);         
      }
      String name = getTypeName(real, type);
      
      return new MethodName(method, type, name);
   }
   
   /**
    * This is used to acquire the name of the method in a Java Bean
    * property style. Thus any "get", "is", or "set" prefix is 
    * removed from the name and the following character is changed
    * to lower case if it does not represent an acroynm.
    * 
    * @param name this is the name of the method to be converted
    * @param type this is the type of method the name represents
    * 
    * @return this returns the Java Bean name for the method
    */
   private static String getTypeName(String name, MethodType type) {
      int prefix = type.getPrefix();
      int size = name.length();
      
      if(size > prefix) {
         name = name.substring(prefix, size);
      }
      return Reflector.getName(name);          
   }
}