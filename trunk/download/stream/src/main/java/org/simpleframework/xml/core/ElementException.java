/*
 * ElementException.java July 2006
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
 * The <code>ElementException</code> is used to represent conditions 
 * when an XML element is in an invalid state. Typically this is used
 * when and element cannot be serialized or deserialized. Such a
 * condition can arise when an element exists within the source XML 
 * that does not match the schema class. For serialization this 
 * exception is thrown if a required field is null.
 * 
 * @author Niall Gallagher
 */
public class ElementException extends PersistenceException {

   /**
    * Constructor for the <code>ElementException</code> object. This
    * constructor takes a format string an a variable number of object
    * arguments, which can be inserted into the format string. 
    * 
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string
    */
   public ElementException(String text, Object... list) {
      super(text, list);           
   }        

   /**
    * Constructor for the <code>ElementException</code> object. This
    * constructor takes a format string an a variable number of object
    * arguments, which can be inserted into the format string. 
    * 
    * @param cause the source exception this is used to represent
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string
    */
   public ElementException(Throwable cause, String text, Object... list) {
      super(cause, text, list);           
   }
}
