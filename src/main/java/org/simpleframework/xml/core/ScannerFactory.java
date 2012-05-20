/*
 * ScannerFactory.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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

import org.simpleframework.xml.stream.Format;


/**
 * The <code>ScannerFactory</code> is used to create scanner objects
 * that will scan a class for its XML class schema. Caching is done
 * by this factory so that repeat retrievals of a <code>Scanner</code>
 * will not require repeat scanning of the class for its XML schema.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Context
 */
class ScannerFactory {

   /**
    * This is used to cache all schemas built to represent a class.
    */
   private final ScannerCache cache;
   
   /**
    * This is the context that is used to create the scanner object.
    */
   private final Format format;
   
   /**
    * Constructor for the <code>ScannerFactory</code> object. This is
    * used to create a factory that will create and cache scanned 
    * data for a given class. Scanning the class is required to find
    * the fields and methods that have been annotated.
    * 
    * @param format this is the format used for the serialization
    */
   public ScannerFactory(Format format) {
      this.cache = new ScannerCache();
      this.format = format;
   }
   
   /**
    * This creates a <code>Scanner</code> object that can be used to
    * examine the fields within the XML class schema. The scanner
    * maintains information when a field from within the scanner is
    * visited, this allows the serialization and deserialization
    * process to determine if all required XML annotations are used.
    * 
    * @param type the schema class the scanner is created for
    * 
    * @return a scanner that can maintains information on the type
    * 
    * @throws Exception if the class contains an illegal schema 
    */ 
   public Scanner getInstance(Class type) throws Exception {
      Scanner schema = cache.get(type);
      
      if(schema == null) {
         schema = new Scanner(type, format);             
         cache.put(type, schema);
      }
      return schema;
   }
}
