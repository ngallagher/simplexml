/*
 * InputState.java July 2006
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

package simple.xml.stream;

/**
 * The <code>InputState</code> object represents an iterator for the
 * elements within an element. This allows the input state object to
 * become a self contained iterator for an element and its children.
 * Each child taken from the input state object, is itself an input
 * state, can can be used to exlpore its sub elements without having
 * any affect on its outer elements.
 *
 * @author Niall Gallagher
 */ 
public interface InputNode extends Node {

   /**
    * Provides an attribute from the element represented. If an
    * attribute for the specified name does not exist within the
    * element represented then this method will return null.
    *
    * @param name this is the name of the attribute to retrieve
    *
    * @return this returns the value for the named attribute
    */ 
   public InputNode getAttribute(String name);

   /**
    * This returns a map of the attributes contained within the
    * element. If no elements exist within the element then this
    * returns an empty map. 
    * 
    * @return this returns a map of attributes for the element
    */  
   public NodeMap getAttributes();
   
   /**
    * This returns the next child element within this element if
    * one exists. If all children have been read, or if there are
    * no child elements for this element then this returns null.
    *
    * @return this returns an input state for the next child
    *
    * @exception Exception thrown if there was a parse error
    */ 
   public InputNode getNext() throws Exception;   
}
