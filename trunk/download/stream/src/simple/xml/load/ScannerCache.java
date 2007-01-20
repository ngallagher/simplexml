/*
 * SchemaCache.java July 2006
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

package simple.xml.load;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>SchemaCache</code> is used to cache schema objects. It 
 * is used so the overhead of reflectively interrogating each class 
 * is not required each time an instance of that class is serialized 
 * or deserialized. This acts as a typedef for the generic type.
 * 
 * @author Niall Gallagher
 */
final class ScannerCache extends ConcurrentHashMap<Class, Scanner> {

   /**
    * Constructor for the <code>SchemaCache</code> object. This is
    * a concurrent hash map that maps class types to the XML schema
    * objects they represent. To ensure the cache can be used by
    * multiple threads this extends the concurrent hash map.
    */
   public ScannerCache() {
      super();
   }

   /**
    * This method will cache the provided scanner object using the
    * provided class object. Once cached the scanner object can be
    * used to create <code>Schema</code> objects that are required
    * for the serialization and deserialization process.
    *
    * @param type this is the class the scanner is mapped to
    * @param schema this is the scanner object that is cached
    *
    * @return this is the scanner instance that has been cached
    */ 
   public Scanner cache(Class type, Scanner schema) {
      return put(type, schema);
   }
}
