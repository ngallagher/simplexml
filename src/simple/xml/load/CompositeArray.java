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

package simple.xml.load;

import simple.xml.stream.OutputNode;
import simple.xml.stream.InputNode;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>CompositeArray</code> object is used to convert a list of
 * elements to an array of object entries. This in effect performs a 
 * root serialization and deserialization of entry elements for the
 * array object. On serialization each objects type must be checked 
 * against the array component type so that it is serialized in a form 
 * that can be deserialized dynamically. 
 * <pre>
 *
 *    &lt;array&gt;
 *       &lt;entry attribute="value"&gt;
 *          &lt;text&gt;example text value&lt;/text&gt;
 *       &lt;/entry&gt;
 *       &lt;entry attribute="demo"&gt;
 *          &lt;text&gt;some other example&lt;/text/&gt;
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
 * @see simple.xml.load.Traverser
 * @see simple.xml.ElementArray
 */ 
final class CompositeArray implements Converter {

   /**
    * This factory is used to create an array for the field.
    */
   private ArrayFactory factory;

   /**
    * This performs the traversal used for object serialization.
    */ 
   private Traverser root;
      
   /**
    * This is the entry type for elements within the array.
    */   
   private Class entry;

   /**
    * Constructor for the <code>CompositeArray</code> object. This is
    * given the array type for the field that is to be converted. An
    * array of the specified type is used to hold the deserialized
    * elements and will be the same length as the number of elements.
    *
    * @param root this is the source object used for serialization
    * @param type this is the object array type that is to be used
    * @param entry the entry type to be stored within the array
    */    
   public CompositeArray(Source root, Class type, Class entry) {
      this.factory = new ArrayFactory(type);           
      this.root = new Traverser(root);      
      this.entry = entry;
   }

   /**
    * This <code>read</code> method wll read the XML element list from
    * the provided node and deserialize its children as entry types.
    * This will each entry type is deserialized as a root type, that 
    * is, its <code>Root</code> annotation must be present and the
    * name of the entry element must match that root element name.
    * 
    * @param node this is the XML element that is to be deserialized
    * 
    * @return this returns the item to attach to the object field
    */ 
   public Object read(InputNode node) throws Exception{
      List list = new ArrayList();
      
      for(int i = 0; true; i++) {
         InputNode next = node.getNext();
        
         if(next == null) {
            return read(list, i);
         }
         list.add(root.read(next, entry));
      } 
   }    

   /**
    * This <code>read</code> method is used to convert the list of
    * objects specified into an array. The array created will be the
    * same size as the number of objects within the list. This means
    * that no objects within the array will contain a null value.
    *
    * @param list this is the list of objects to convert to an array
    * @param size this is the size of the array to be created
    *
    * @return an array object containing the deserialized elements
    */
   private Object read(List list, int size) throws Exception {
      Object[] array = factory.getInstance(size);

      for(int i = 0; i < size; i++) {
         array[i] = list.get(i);
      }
      return array;
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
      Object[] list = (Object[])source;                
      
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
    * Each array entry is serialized as a root element, that is, its
    * <code>Root</code> annotation is used to extract the name. 
    * 
    * @param source this is the source object array to be serialized 
    * @param node this is the XML element container to be populated
    * @param entry this is the type of the object that is expected
    */ 
   private void write(OutputNode node, Object item, Class entry) throws Exception {   
      Class type = item.getClass();

      if(!entry.isAssignableFrom(type)) {
         throw new PersistenceException("Entry %s does not match %s", type, entry);                     
      } 
      root.write(node, item, entry);                       
   }
}
