/*
 * InvalidFormatException.java May 2007
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

package org.simpleframework.xml.transform;

/**
 * The <code>InvalidFormatException</code> is thrown when there is 
 * a format exception. This exception this will be thrown from the
 * <code>Transformer</code> should serialization or deserialization
 * of an object fail. Error messages provided to this exception are
 * formatted similar to the <code>PrintStream.printf</code> method.
 * 
 * @author Niall Gallagher
 */
public class InvalidFormatException extends TransformException {
   
   /**
    * Constructor for the <code>InvalidFormatException</code> object. 
    * This constructor takes a format string an a variable number of 
    * object arguments, which can be inserted into the format string. 
    * 
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string
    */   
   public InvalidFormatException(String text, Object... list) {
      super(String.format(text, list));               
   }       

   /**
    * Constructor for the <code>InvalidFormatException</code> object. 
    * This constructor takes a format string an a variable number of 
    * object arguments, which can be inserted into the format string. 
    * 
    * @param cause the source exception this is used to represent
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the stri 
    */
   public InvalidFormatException(Throwable cause, String text, Object... list) {
      super(String.format(text, list), cause);           
   }  
}