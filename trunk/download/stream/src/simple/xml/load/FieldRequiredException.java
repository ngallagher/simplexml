/*
 * FieldRequiredException.java July 2006
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

/**
 * The <code>FieldRequiredException</code> is thrown when an attribute
 * or element is missing from the XML document. This is thrown only if
 * the attribute or element is required according to the annotation
 * for that field within the XML schema class.
 * 
 * @author Niall Gallagher
 */
public class FieldRequiredException extends PersistenceException {

   /**
    * Constructor for the <code>FieldRequiredException</code> object. 
    * This constructor takes a format string an a variable number of 
    * object arguments, which can be inserted into the format string. 
    * 
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string
    */
   public FieldRequiredException(String text, Object... list) {
      super(text, list);           
   }        

   /**
    * Constructor for the <code>FieldRequiredException</code> object. 
    * This constructor takes a format string an a variable number of 
    * object arguments, which can be inserted into the format string. 
    * 
    * @param cause the source exception this is used to represent
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string
    */  
   public FieldRequiredException(Throwable cause, String text, Object... list) {
      super(cause, text, list);           
   }
}
