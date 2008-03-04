/*
 * CompositeValue.java July 2007
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

package org.simpleframework.xml.load;

import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Position;

/**
 * The <code>CompositeValue</code> object is used to convert an object
 * to an from an XML element. This accepts only composite objects and
 * will maintain all references within the object using the cycle
 * strategy if required. This also ensures that should the value to
 * be written to the XML element be null that nothing is written. 
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.ElementMap
 */
class CompositeValue implements Converter {
   
   /**
    * This is the traverser used to read and write the value with.
    */
   private final Traverser root;
   
   /**
    * This is the entry object used to provide configuration details.
    */   
   private final Entry entry;
  
   /**
    * This represents the type of object the value is written as.
    */
   private final Class type;
   
   /**
    * Constructor for the <code>CompositeValue</code> object. This 
    * will create an object capable of reading an writing composite 
    * values from an XML element. This also allows a parent element 
    * to be created to wrap the key object if desired.
    * 
    * @param root this is the root context for the serialization
    * @param entry this is the entry object used for configuration
    * @param type this is the type of object the value represents
    */
   public CompositeValue(Source root, Entry entry, Class type) throws Exception {
      this.root = new Traverser(root);
      this.entry = entry;
      this.type = type;
   }
   
   /**
    * This method is used to read the value object from the node. The 
    * value read from the node is resolved using the template filter.
    * If the value data can not be found according to the annotation 
    * attributes then an exception is thrown.
    * 
    * @param node this is the node to read the value object from
    * 
    * @return this returns the value deserialized from the node
    */ 
   public Object read(InputNode node) throws Exception { 
      InputNode next = node.getNext();
      
      if(next == null) {
         return null;
      }
      return root.read(next, type);
   }
   
   /**
    * This method is used to read the value object from the node. The 
    * value read from the node is resolved using the template filter.
    * If the value data can not be found according to the annotation 
    * attributes then an exception is thrown.
    * 
    * @param node this is the node to read the value object from
    * 
    * @return this returns true if this represents a valid value
    */ 
   public boolean validate(InputNode node) throws Exception { 
      Position line = node.getPosition();
      String name = entry.getValue();
      
      if(name == null) {
         name = Factory.getName(type);
      }
      return validate(node, name);
   }  
   
   /**
    * This method is used to read the value object from the node. The 
    * value read from the node is resolved using the template filter.
    * If the value data can not be found according to the annotation 
    * attributes then an exception is thrown.
    * 
    * @param node this is the node to read the value object from
    * @param name this is the name of the value element
    * 
    * @return this returns true if this represents a valid value
    */    
   private boolean validate(InputNode node, String name) throws Exception {      
      InputNode next = node.getNext(name);
      Position line = node.getPosition();
      
      if(next == null) {
         throw new ElementException("Element '%s' missing at line %s", name, line);
      }
      if(next.isEmpty()) {
         return true;
      }
      return root.validate(next, type);
   }
   
   /**
    * This method is used to write the value to the specified node.
    * The value written to the node must be a composite object and if
    * the object provided to this is null then nothing is written.
    * 
    * @param node this is the node that the value is written to
    * @param item this is the item that is to be written
    */
   public void write(OutputNode node, Object item) throws Exception {
      String name = entry.getValue();
      
      if(name == null) {
         name = Factory.getName(type);
      }
      root.write(node, item, type, name);      
   }
}