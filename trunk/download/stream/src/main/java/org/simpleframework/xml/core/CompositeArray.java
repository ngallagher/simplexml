/*
 * CompositeArray.java July 2006
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

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import java.lang.reflect.Array;

/**
 * The <code>CompositeArray</code> object is used to convert a list of
 * elements to an array of object entries. This in effect performs a 
 * root serialization and deserialization of entry elements for the
 * array object. On serialization each objects type must be checked 
 * against the array component type so that it is serialized in a form 
 * that can be deserialized dynamically. 
 * <pre>
 *
 *    &lt;array length="2"&gt;
 *       &lt;entry&gt;
 *          &lt;text&gt;example text value&lt;/text&gt;
 *       &lt;/entry&gt;
 *       &lt;entry&gt;
 *          &lt;text&gt;some other example&lt;/text&gt;
 *       &lt;/entry&gt;
 *    &lt;/array&gt;
 * 
 * </pre>
 * For the above XML element list the element <code>entry</code> is
 * contained within the array. Each entry element is deserialized as 
 * a root element and then inserted into the array. For serialization 
 * the reverse is done, each element taken from the array is written
 * as a root element to the parent element to create the list. Entry
 * objects do not need to be of the same type.
 * 
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.core.Traverser
 * @see org.simpleframework.xml.ElementArray
 */ 
class CompositeArray implements Converter {

   /**
    * This factory is used to create an array for the contact.
    */
   private final ArrayFactory factory;

   /**
    * This performs the traversal used for object serialization.
    */ 
   private final Traverser root;

   /**
    * This is the name to wrap each entry that is represented.
    */
   private final String parent;
   
   /**
    * This is the entry type for elements within the array.
    */   
   private final Class entry;
   
   /**
    * Constructor for the <code>CompositeArray</code> object. This is
    * given the array type for the contact that is to be converted. An
    * array of the specified type is used to hold the deserialized
    * elements and will be the same length as the number of elements.
    *
    * @param context this is the context object used for serialization
    * @param field this is the field type for the array being used
    * @param entry this is the entry type for the array elements
    * @param parent this is the name to wrap the array element with
    */    
   public CompositeArray(Context context, Class field, Class entry, String parent) {
      this.factory = new ArrayFactory(context, field);           
      this.root = new Traverser(context);     
      this.parent = parent;
      this.entry = entry;
   }

   /**
    * This <code>read</code> method wll read the XML element list from
    * the provided node and deserialize its children as entry types.
    * This ensures each entry type is deserialized as a root type, that 
    * is, its <code>Root</code> annotation must be present and the
    * name of the entry element must match that root element name.
    * 
    * @param node this is the XML element that is to be deserialized
    * 
    * @return this returns the item to attach to the object contact
    */ 
   public Object read(InputNode node) throws Exception{
      Type type = factory.getInstance(node);
      Object list = type.getInstance();
      
      if(!type.isReference()) {
         return read(node, list);         
      }
      return list;
   }

   /**
    * This <code>read</code> method will read the XML element list from
    * the provided node and deserialize its children as entry types.
    * This ensures each entry type is deserialized as a root type, that 
    * is, its <code>Root</code> annotation must be present and the
    * name of the entry element must match that root element name.
    * 
    * @param node this is the XML element that is to be deserialized
    * @param list this is the array that is to be deserialized
    * 
    * @return this returns the item to attach to the object contact
    */  
   public Object read(InputNode node, Object list) throws Exception{
      for(int i = 0; true; i++) {
         InputNode next = node.getNext();
        
         if(next == null) {
            return list;
         }
         read(next, list, i);
      } 
   }    
   
   /**
    * This is used to read the specified node from in to the list. If
    * the node is null then this represents a null element value in
    * the array. The node can be null only if there is a parent and
    * that parent contains no child XML elements.
    * 
    * @param node this is the node to read the array value from 
    * @param list this is the list to add the array value in to
    * @param index this is the offset to set the value in the array
    */
   private void read(InputNode node, Object list, int index) throws Exception {
      Object value = null;     
      
      if(!node.isEmpty()) {
         value = root.read(node, entry);
      }
      Array.set(list, index, value);      
   }

   /**
    * This <code>validate</code> method wll validate the XML element 
    * list against the provided node and validate its children as entry 
    * types. This ensures each entry type is validated as a root type, 
    * that is, its <code>Root</code> annotation must be present and the
    * name of the entry element must match that root element name.
    * 
    * @param node this is the XML element that is to be validated
    * 
    * @return true if the element matches the XML schema class given 
    */ 
   public boolean validate(InputNode node) throws Exception{
      Type type = factory.getInstance(node);
      
      if(!type.isReference()) {
         Object real = type.getInstance(type);
         Class expect = type.getType();
            
         return validate(node, expect);
      }
      return true; 
   }

   /**
    * This <code>validate</code> method wll validate the XML element 
    * list against the provided node and validate its children as entry 
    * types. This ensures each entry type is validated as a root type, 
    * that is, its <code>Root</code> annotation must be present and the
    * name of the entry element must match that root element name.
    * 
    * @param node this is the XML element that is to be validated
    * @param type this is the array type used to create the array
    * 
    * @return true if the element matches the XML schema class given 
    */  
   private boolean validate(InputNode node, Class type) throws Exception{
      for(int i = 0; true; i++) {
         InputNode next = node.getNext();
        
         if(next == null) {
            return true;
         }
         if(!next.isEmpty()) {
            root.validate(next, type);
         }
      }
   }    
   
   /**
    * This <code>write</code> method will write the specified object
    * to the given XML element as as array entries. Each entry within
    * the given array must be assignable to the array component type.
    * Each array entry is serialized as a root element, that is, its
    * <code>Root</code> annotation is used to extract the name. 
    * 
    * @param source this is the source object array to be serialized 
    * @param node this is the XML element container to be populated
    */ 
   public void write(OutputNode node, Object source) throws Exception {
      int size = Array.getLength(source);                
      
      for(int i = 0; i < size; i++) {   
         Object item = Array.get(source, i);      
      
         root.write(node, item, entry, parent);   
      }
      node.commit();
   }
}
