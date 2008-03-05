/*
 * Hierarchy.java April 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
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

package org.simpleframework.xml.load;

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
   }       
}