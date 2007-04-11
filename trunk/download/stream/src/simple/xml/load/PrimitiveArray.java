/*
 * PrimitiveArray.java July 2006
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

import simple.xml.stream.OutputNode;
import simple.xml.stream.InputNode;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>PrimitiveArray</code> object is used to convert a list of
 * elements to an array of object entries. This in effect performs a 
 * serialization and deserialization of primitive elements for the
 * array object. On serialization each primitive type must be checked 
 * against the array component type so that it is serialized in a form 
 * that can be deserialized dynamically. 
 * <pre>
 *
 *    &lt;array&gt;
 *       &lt;entry&gt;example text one&lt;/entry&gt;
 *       &lt;entry&gt;example text two&lt;/entry&gt;
 *       &lt;entry&gt;example text three&lt;/entry&gt;
 *    &lt;/array&gt;
 * 
 * </pre>
 * For the above XML element list the element <code>entry</code> is
 * contained within the array. Each entry element is deserialized as 
 * a from a parent XML element, which is specified in the annotation.
 * For serialization the reverse is done, each element taken from the 
 * array is written into an element created from the parent element.
 * 
 * @author Niall Gallagher
 *
 * @see simple.xml.load.Traverser
 * @see simple.xml.ElementArray
 */ 
final class PrimitiveArray implements Converter {

   /**
    * This factory is used to create an array for the contact.
    */
   private ArrayFactory factory;

   /**
    * This performs the serialization of the primitive element.
    */ 
   private Primitive root;
      
   /**
    * This is the entry type for elements within the array.
    */   
   private Class entry;

   /**
    * This is the name that each array element is wrapped with.
    */
   private String parent;

   /**
    * Constructor for the <code>PrimitiveArray</code> object. This is
    * given the array type for the contact that is to be converted. An
    * array of the specified type is used to hold the deserialized
    * elements and will be the same length as the number of elements.
    *
    * @param root this is the source object used for serialization
    * @param entry the entry type to be stored within the array
    * @param parent this is the name to wrap the array element with
    */    
   public PrimitiveArray(Source root, Class entry, String parent) {
      this.root = new Primitive(root, entry);
      this.factory = new ArrayFactory(entry);           
      this.parent = parent;
      this.entry = entry;
   }

   /**
    * This <code>read</code> method wll read the XML element list from
    * the provided node and deserialize its children as entry types.
    * This will deserialize each entry type as a primitive value. In
    * order to do this the parent string provided forms the element.
    * 
    * @param node this is the XML element that is to be deserialized
    * 
    * @return this returns the item to attach to the object contact
    */ 
   public Object read(InputNode node) throws Exception{
      List list = new ArrayList();
      
      while(true) {
         InputNode next = node.getNext();
    
         if(next == null) {
            return factory.getArray(list);            
         }
         list.add(root.read(next));
      } 
   }    

   /**
    * This <code>write</code> method will write the specified object
    * to the given XML element as as array entries. Each entry within
    * the given array must be assignable to the array component type.
    * This will deserialize each entry type as a primitive value. In
    * order to do this the parent string provided forms the element.
    * 
    * @param source this is the source object array to be serialized 
    * @param node this is the XML element container to be populated
    */ 
   public void write(OutputNode node, Object source) throws Exception {
      List list = factory.getList(source);
      
      for(Object item : list) {
         if(item != null) {  
            write(node, item, entry);
         }
      }
   }
   
   /**
    * This <code>write</code> method will write the specified object
    * to the given XML element as as array entries. Each entry within
    * the given array must be assignable to the array component type.
    * This will serialize each entry type as a primitive value. In
    * order to do this the parent string provided forms the element.
    *  
    * @param source this is the source object array to be serialized 
    * @param node this is the XML element container to be populated
    * @param entry this is the type of the object that is expected
    */ 
   private void write(OutputNode node, Object item, Class entry) throws Exception {   
      OutputNode child = node.getChild(parent);  
      Class type = item.getClass();

      root.write(child, item);                       
   }
}
