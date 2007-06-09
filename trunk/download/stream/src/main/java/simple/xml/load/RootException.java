/*
 * RootException.java July 2006
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
 * The <code>RootException</code> is thrown if the <code>Root</code>
 * annotation is missing from a root object that is to be serialized
 * or deserialized. Not all objects require a root annotation, only
 * those objects that are to be deserialized to or serialized from
 * an <code>ElementList</code> field and root objects that are to be
 * deserialized or serialized directly from the persister.
 * 
 * @author Niall Gallagher
 */
public class RootException extends PersistenceException {
   
   /**
    * Constructor for the <code>RootException</code> exception. This
    * constructor takes a format string an a variable number of object
    * arguments, which can be inserted into the format string. 
    * 
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string
    */
   public RootException(String text, Object... list) {
      super(text, list);           
   }        

   /**
    * Constructor for the <code>RootException</code> exception. This
    * constructor takes a format string an a variable number of object
    * arguments, which can be inserted into the format string. 
    * 
    * @param cause the source exception this is used to represent
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string 
    */
   public RootException(Throwable cause, String text, Object... list) {
      super(cause, text, list);           
   }
}
