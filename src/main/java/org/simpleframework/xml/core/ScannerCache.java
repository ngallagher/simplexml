/*
 * ScannerCache.java July 2006
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

import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>ScannerCache</code> is used to cache schema objects. It 
 * is used so the overhead of reflectively interrogating each class 
 * is not required each time an instance of that class is serialized 
 * or deserialized. This acts as a typedef for the generic type.
 * 
 * @author Niall Gallagher
 */
class ScannerCache extends ConcurrentHashMap<Class, Scanner> {

   /**
    * Constructor for the <code>ScannerCache</code> object. This is
    * a concurrent hash map that maps class types to the XML schema
    * objects they represent. To enable reloading of classes by the
    * system this will drop the scanner if the class in unloaded.
    */
   public ScannerCache() {
      super();
   }
}
