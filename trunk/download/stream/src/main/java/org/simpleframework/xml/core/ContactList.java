/*
 * ContactList.java April 2007
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

package org.simpleframework.xml.core;

import java.util.ArrayList;

/**
 * The <code>ContactList</code> object is used to represent a list
 * that contains contacts for an object. This is used to collect
 * the methods and fields within an object that are to be used in
 * the serialization and deserialization process.
 * 
 * @author Niall Gallagher
 */ 
abstract class ContactList extends ArrayList<Contact>  {

   /**
    * Constructor for the <code>ContactList</code> object. This
    * must be subclassed by a scanning class which will fill the
    * list with the contacts from a specified class.
    */      
   protected ContactList() {
      super();           
   }        
}
