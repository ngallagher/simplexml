/*
 * InstantiationException.java July 2006
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

package org.simpleframework.xml.load;

/**
 * The <code>InstantiationException</code> is thrown when an object
 * cannot be instantiated either because it is an abstract class or an
 * interface. Such a situation can arise if a serializable field is an 
 * abstract type and a suitable concrete class cannot be found. Also,
 * if an override type is not assignable to the field type this is
 * thrown, for example if an XML element list is not a collection.
 *  
 * @author Niall Gallagher
 */         
public class InstantiationException extends PersistenceException {

   /**
    * Constructor for the <code>InstantiationException</code> object. 
    * This constructor takes a format string an a variable number of 
    * object arguments, which can be inserted into the format string. 
    * 
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string
    */
   public InstantiationException(String text, Object... list) {
      super(text, list);           
   }        
   
   /**
    * Constructor for the <code>InstantiationException</code> object. 
    * This constructor takes a format string an a variable number of 
    * object arguments, which can be inserted into the format string. 
    * 
    * @param cause the source exception this is used to represent
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string 
    */
   public InstantiationException(Throwable cause, String text, Object... list) {
      super(cause, text, list);           
   }
}
