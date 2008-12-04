/*
 * ScannerCache.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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

import org.simpleframework.xml.util.WeakCache;

/**
 * The <code>ScannerCache</code> is used to cache schema objects. It 
 * is used so the overhead of reflectively interrogating each class 
 * is not required each time an instance of that class is serialized 
 * or deserialized. This acts as a typedef for the generic type.
 * 
 * @author Niall Gallagher
 */
class ScannerCache extends WeakCache<Class, Scanner> {

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
