/*
 * Repeater.java July 2007
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

import org.simpleframework.xml.stream.InputNode;

/**
 * The <code>Repeater</code> interface is used to for converters that
 * can repeat a read on a given element. This is typically used for
 * inline lists and maps so that the elements can be mixed within the
 * containing element. This ensures a more liberal means of writing
 * the XML such that elements not grouped in a containing XML element
 * can be declared throughout the document.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.load.CompositeInlineMap
 */
interface Repeater extends Converter {
   
   /**
    * The <code>read</code> method reads an object to a specific type
    * from the provided node. If the node provided is an attribute
    * then the object must be a primitive such as a string, integer,
    * boolean, or any of the other Java primitive types.  
    * 
    * @param node contains the details used to deserialize the object
    * @param value this is the value to read the objects in to
    * 
    * @return a fully deserialized object will all its fields 
    * 
    * @throws Exception if a deserialized type cannot be instantiated
    */
   public Object read(InputNode node, Object value) throws Exception;

}
