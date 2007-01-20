/*
 * InputAttribute.java July 2006
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

import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;

/**
 * The <code>InputAttribute</code> is used to represent an attribute
 * within an element. Rather than representing an attribute as a
 * name value pair of strings, an attribute is instead represented
 * as an input state, in the same manner as an element. The reason
 * for representing an attribute in this way is such that a uniform
 * means of extracting and parsing values can be used for inputs.
 *
 * @author Niall Gallagher
 */ 
class InputAttribute implements InputNode {

   /**
    * Represents the name of this input attribute instance.
    */         
   private String name;

   /**
    * Represents the value for this input attribute instance.
    */ 
   private String value;
        
   /**
    * Constructor for the <code>InputAttribute</code> object. This
    * is used to wrap a an attribute as an input state object. The
    * attribute can then be used in a similar manner to elements.
    *
    * @param source this is the attribute that this will wrap
    */   
   public InputAttribute(Attribute source) {
      this(source.getName(), source.getValue());
   }        

   /**
    * Constructor for the <code>InputAttribute</code> object. This
    * is used to create an input attribute using the provided name
    * and value, all other values for this input node will be null.
    *
    * @param name this is the name for this attribute object
    * @param value this is the value for this attribute object
    */     
   public InputAttribute(QName name, String value) {
      this(name.getLocalPart(), value);           
   }

   /**
    * Constructor for the <code>InputAttribute</code> object. This
    * is used to create an input attribute using the provided name
    * and value, all other values for this input node will be null.
    *
    * @param name this is the name for this attribute object
    * @param value this is the value for this attribute object
    */     
   public InputAttribute(String name, String value) {
      this.value = value;
      this.name = name;           
   }

   /**
    * Returns the name of the node that this represents. This is
    * an immutable property and will not change for this node. 
    *  
    * @return returns the name of the node that this represents
    */   
   public String getName() {
      return name;
   }

   /**
    * Returns the value for the node that this represents. This 
    * is an immutable value for the node and cannot be changed.
    * 
    * @return the name of the value for this node instance
    */   
   public String getValue() {
      return value;
   }
   
   /**
    * Because the <code>InputAttribute</code> object represents an
    * attribute this method will return null. If nodes are added 
    * to the node map the values will not be available here.
    *
    * @return this always returns null for a requested attribute
    */ 
   public InputNode getAttribute(String name) {
      return null;
   }

   /**
    * Because the <code>InputAttribute</code> object represents an
    * attribute this method will return an empty map. If nodes are
    * added to the node map the values will not be maintained.
    *
    * @return this always returns an empty node map of attributes
    */
   public NodeMap getAttributes() {
      return new InputNodeMap(this);
   }
   
   /**
    * Because the <code>InputAttribute</code> object represents an
    * attribute this method will return null. An attribute is a
    * simple name value pair an so can not contain any child nodes.
    *
    * @return this always returns null for a requested child node
    */
   public InputNode getNext() {
      return null;           
   }
}
