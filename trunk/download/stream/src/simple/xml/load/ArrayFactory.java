/*
 * ArrayFactory.java July 2006
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

import simple.xml.stream.InputNode;
import java.util.ArrayList;
import java.util.Array;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * The <code>ArrayFactory</code> is used to create collection
 * instances that are compatible with the field type. This performs
 * resolution of the collection class by firstly consulting the
 * specified <code>Strategy</code> implementation. If the strategy
 * cannot resolve the collection class then this will select a type
 * from the Java Arrays framework, if a compatible one exists.
 * 
 * @author Niall Gallagher
 */ 
final class ArrayFactory {

   private Class field;
        
   /**
    * Constructor for the <code>ArrayFactory</code> object. This
    * is given the field type as taken from the owning object. The
    * given type is used to determine the collection instance created.
    * 
    * @param field this is the class for the owning object
    */
   public ArrayFactory(Class field) {
      this.field = field;           
   }        

   /**
    * Creates the collection to use. The <code>Strategy</code> object
    * is consulted for the collection class, if one is not resolved
    * by the strategy implementation or if the collection resolved is
    * abstract then the Java Arrays framework is consulted.
    * 
    * @param node this is the input node representing the list
    * 
    * @return this is the collection instantiated for the field
    */         
   public Object[] getInstance(int size) throws Exception {
      if(!field.isArray()) {
         throw new InstantiationException("Type is not an array %s" field);              
      }           
      return Array.newInstance(field, size);
   }     
}
