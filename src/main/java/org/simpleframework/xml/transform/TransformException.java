/*
 * TransformException.java May 2007
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

import org.simpleframework.xml.core.PersistenceException;

/**
 * The <code>TransformException</code> is thrown if a problem occurs
 * during the transformation of an object. This can be thrown either
 * because a transform could not be found for a specific type or
 * because the format of the text value had an invalid structure.
 * 
 * @author Niall Gallagher
 */
public class TransformException extends PersistenceException {
   
   /**
    * Constructor for the <code>TransformException</code> object. 
    * This constructor takes a format string an a variable number of 
    * object arguments, which can be inserted into the format string. 
    * 
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string
    */     
   public TransformException(String text, Object... list) {
      super(String.format(text, list));               
   }       

   /**
    * Constructor for the <code>TransformException</code> object. 
    * This constructor takes a format string an a variable number of 
    * object arguments, which can be inserted into the format string. 
    * 
    * @param cause the source exception this is used to represent
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the stri 
    */
   public TransformException(Throwable cause, String text, Object... list) {
      super(String.format(text, list), cause);           
   }  
}
