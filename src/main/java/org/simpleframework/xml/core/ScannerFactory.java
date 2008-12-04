/*
 * ScannerFactory.java July 2006
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

/**
 * The <code>ScannerFactory</code> is used to create scanner objects
 * that will scan a class for its XML class schema. Caching is done
 * by this factory so that repeat retrievals of a <code>Scanner</code>
 * will not require repeat scanning of the class for its XML schema.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.XSource
 */
final class ScannerFactory {

   /**
    * This is used to cache all schemas built to represent a class.
    * 
    * @see org.simpleframework.xml.core.Scanner
    */
   private static ScannerCache cache;

   static {
      cache = new ScannerCache();           
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
   public static Scanner getInstance(Class type) throws Exception {
      Scanner schema = cache.fetch(type);
      
      if(schema == null) {
         schema = new Scanner(type);             
         cache.cache(type, schema);
      }
      return schema;
   }
}
