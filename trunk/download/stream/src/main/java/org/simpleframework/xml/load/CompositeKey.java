/*
 * CompositeKey.java July 2007
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
 * The <code>CompositeKey</code> object is used to convert an object
 * to an from an XML element. This accepts only composite objects and
 * will throw an exception if the <code>ElementMap</code> annotation
 * is configured to have an attribute key. If a key name is given for
 * the annotation then this will act as a parent element to the 
 * resulting XML element for the composite object. 
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.ElementMap
 */
class CompositeKey implements Converter {
   
   /**
    * This is the traverser used to read and write the composite key.
    */
   private final Traverser root;
   
   /**
    * This is the entry object used to provide configuration details.
    */
   private final Entry entry;
   
   /**
    * This represents the type of object the key is written as.
    */
   private final Class type;
      
   /**
    * Constructor for the <code>CompositeKey</code> object. This will
    * create an object capable of reading an writing composite keys
    * from an XML element. This also allows a parent element to be
    * created to wrap the key object if desired.
    * 
    * @param root this is the root context for the serialization
    * @param entry this is the entry object used for configuration
    * @param type this is the type of object the key represents
    */
   public CompositeKey(Source root, Entry entry, Class type) throws Exception {
      this.root = new Traverser(root);
      this.entry = entry;
      this.type = type;
   }
   
   /**
    * This method is used to read the key value from the node. The 
    * value read from the node is resolved using the template filter.
    * If the key value can not be found according to the annotation
    * attributes then an exception is thrown.
    * 
    * @param node this is the node to read the key value from
    * 
    * @return this returns the value deserialized from the node
    */ 
   public Object read(InputNode node) throws Exception { 
      Position line = node.getPosition();
      String name = entry.getKey();
      
      if(entry.isAttribute()) {
         throw new ElementException("Can not have %s as an attribute at %s", type, line);
      }
      return read(node, name);
   }
   
   /**
    * This method is used to read the key value from the node. The 
    * value read from the node is resolved using the template filter.
    * If the key value can not be found according to the annotation
    * attributes then an exception is thrown.
    * 
    * @param node this is the node to read the key value from
    * @param name this is the name of the key wrapper XML element
    * 
    * @return this returns the value deserialized from the node
    */ 
   private Object read(InputNode node, String name) throws Exception {
      Position line = node.getPosition();
      
      if(name != null) {
         node = node.getNext(name);
      }    
      if(node == null) {
         throw new ElementException("Element '%s' does not exist at %s", name, line);
      }   
      return read(node, type);
   }
   
   /**
    * This method is used to read the key value from the node. The 
    * value read from the node is resolved using the template filter.
    * If the key value can not be found according to the annotation
    * attributes then an exception is thrown.
    * 
    * @param node this is the node to read the key value from
    * @param type this is the type that the key is to be read as
    * 
    * @return this returns the value deserialized from the node
    */ 
   private Object read(InputNode node, Class type) throws Exception {
      Position line = node.getPosition();      
      InputNode next = node.getNext(); 
      
      if(next == null) {
         throw new ElementException("Element does not exist at %s for %s", line, type);
      }
      return root.read(next, type);
   }
   
   /**
    * This method is used to read the key value from the node. The 
    * value read from the node is resolved using the template filter.
    * If the key value can not be found according to the annotation
    * attributes then an exception is thrown.
    * 
    * @param node this is the node to read the key value from
    * 
    * @return this returns the value deserialized from the node
    */ 
   public boolean validate(InputNode node) throws Exception { 
      Position line = node.getPosition();
      String name = entry.getKey();
      
      if(entry.isAttribute()) {
         throw new ElementException("Can not have %s as an attribute at %s", type, line);
      }
      return validate(node, name);
   }
   
   /**
    * This method is used to read the key value from the node. The 
    * value read from the node is resolved using the template filter.
    * If the key value can not be found according to the annotation
    * attributes then an exception is thrown.
    * 
    * @param node this is the node to read the key value from
    * @param name this is the name of the key wrapper XML element
    * 
    * @return this returns the value deserialized from the node
    */ 
   private boolean validate(InputNode node, String name) throws Exception {
      Position line = node.getPosition();
      
      if(name != null) {
         node = node.getNext(name);
      }    
      if(node == null) {
         throw new ElementException("Element '%s' does not exist at %s", name, line);
      }   
      return validate(node, type);
   }
   
   /**
    * This method is used to read the key value from the node. The 
    * value read from the node is resolved using the template filter.
    * If the key value can not be found according to the annotation
    * attributes then an exception is thrown.
    * 
    * @param node this is the node to read the key value from
    * @param type this is the type that the key is to be read as
    * 
    * @return this returns the value deserialized from the node
    */ 
   private boolean validate(InputNode node, Class type) throws Exception {
      Position line = node.getPosition();      
      InputNode next = node.getNext(); 
      
      if(next == null) {
         throw new ElementException("Element does not exist at %s for %s", line, type);
      }
      return root.validate(next, type);
   }

   /**
    * This method is used to write the value to the specified node.
    * The value written to the node must be a composite object and if
    * the element map annotation is configured to have a key attribute
    * then this method will throw an exception.
    * 
    * @param node this is the node that the value is written to
    * @param item this is the item that is to be written
    */
   public void write(OutputNode node, Object item) throws Exception {
      String name = entry.getKey();
      
      if(entry.isAttribute()) {
         throw new ElementException("Can not have %s as an attribute", type);
      }
      if(name != null) {
         node = node.getChild(name);
      }
      root.write(node, item, type);      
   }
}
