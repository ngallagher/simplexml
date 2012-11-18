/*
 * Hierarchy.java April 2007
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

import java.util.LinkedList;

/**
 * The <code>Hierarchy</code> object is used to acquire the hierarchy
 * of a specified class. This ensures that the iteration order of the
 * hierarchy is from the base class to the most specialized class.
 * It is used during scanning to ensure that the order of methods and
 * fields written as XML is in declaration order from the most
 * basic to the most specialized.
 * 
 * @author Niall Gallagher
 */
class Hierarchy extends LinkedList<Class> {
   
   /**
    * Constructor for the <code>Hierarchy</code> object. This is used 
    * to create the hierarchy of the specified class. It enables the
    * scanning process to evaluate methods and fields in the order of
    * most basic to most specialized.
    * 
    * @param type this is the type that is to be scanned
    */
   public Hierarchy(Class type) {
      scan(type);
   }
   
   /**
    * This is used to scan the specified <code>Class</code> in such a
    * way that the most basic type is at the head of the list and the
    * most specialized is at the last, ensuring correct iteration.
    * 
    * @param type this is the type that is to be scanned
    */
   private void scan(Class type) {
      while(type != null) {  
         addFirst(type);         
         type = type.getSuperclass();
      }
      remove(Object.class);
   }       
}