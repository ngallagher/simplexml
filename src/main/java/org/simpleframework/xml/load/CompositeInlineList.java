/*
 * CompositeInlineList.java July 2006
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
import org.simpleframework.xml.stream.OutputNode;
import java.util.Collection;

/**
 * The <code>CompositeInlineList</code> object is used to convert an 
 * group of elements in to a collection of element entries. This is
 * used when a containing element for a list is not required. It 
 * extracts the elements by matching elements to name of the type
 * that the annotated field or method requires. This enables these
 * element entries to exist as siblings to other objects within the
 * object.  One restriction is that the <code>Root</code> annotation
 * for each of the types within the list must be the same.
 * <pre> 
 *    
 *    &lt;entry attribute="one"&gt;
 *       &lt;text&gt;example text value&lt;/text&gt;
 *    &lt;/entry&gt;
 *    &lt;entry attribute="two"&gt;
 *       &lt;text&gt;some other example&lt;/text&gt;
 *    &lt;/entry&gt;  
 *    &lt;entry attribute="three"&gt;
 *       &lt;text&gt;yet another example&lt;/text&gt;
 *    &lt;/entry&gt;      
 * 
 * </pre>
 * For the above XML element list the element <code>entry</code> is
 * contained within the list. Each entry element is thus deserialized
 * as a root element and then inserted into the list. This enables
 * lists to be composed from XML documents. For serialization the
 * reverse is done, each element taken from the collection is written
 * as a root element to the owning element to create the list. 
 * Entry objects do not need to be of the same type.
 * 
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.load.Traverser
 * @see org.simpleframework.xml.ElementList
 */ 
class CompositeInlineList implements Converter {

   /**
    * This factory is used to create a suitable collection list.
    */         
   private final CollectionFactory factory;

   /**
    * This performs the traversal used for object serialization.
    */ 
   private final Traverser root;
      
   /**
    * This is the entry type for elements within the list.
    */   
   private final Class entry;

   /**
    * Constructor for the <code>CompositeInlineList</code> object. 
    * This is given the list type and entry type to be used. The list
    * type is the <code>Collection</code> implementation that is used 
    * to collect the deserialized entry objects from the XML source. 
    *
    * @param root this is the source object used for serialization
    * @param type this is the collection type for the list used
    * @param entry the entry type to be stored within the list
    */    
   public CompositeInlineList(Source root, Class type, Class entry) {
      this.factory = new CollectionFactory(root, type); 
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
    * @return this returns the item to attach to the object contact
    */ 
   public Object read(InputNode node) throws Exception{     
      Object value = factory.getInstance();
      Collection list = (Collection) value;
      
      if(list != null) {
         return read(node, list);
      }
      return null;
   }
   
   /**
    * This <code>read</code> method wll read the XML element list from
    * the provided node and deserialize its children as entry types.
    * This will each entry type is deserialized as a root type, that 
    * is, its <code>Root</code> annotation must be present and the
    * name of the entry element must match that root element name.
    * 
    * @param node this is the XML element that is to be deserialized
    * @param list this is the collection that is to be populated
    * 
    * @return this returns the item to attach to the object contact
    */ 
   private Object read(InputNode node, Collection list) throws Exception {              
      InputNode from = node.getParent();
      String name = node.getName();
      
      while(node != null) {
         Object item = read(node, entry);
         
         if(item != null) {
            list.add(item);
         }      
         node = from.getNext(name);
      }
      return list;
   }     
   
   /**
    * This <code>read</code> method wll read the XML element from the     
    * provided node. This checks to ensure that the deserialized type
    * is the same as the entry type provided. If the types are not 
    * the same then an exception is thrown. This is done to ensure
    * each node in the collection contain the same root annotaiton. 
    * 
    * @param node this is the XML element that is to be deserialized
    * @param expect this is the type expected of the deserialized type
    *      
    * @return this returns the item to attach to the object contact
    */ 
   private Object read(InputNode node, Class expect) throws Exception {
      Object item = root.read(node, expect);
      Class type = item.getClass();
      
      if(!isCompatible(type, entry)) {
         throw new RootException("Root name for %s must match %s", type, expect);          
      }
      return item;      
   }
   
   /**
    * This <code>write</code> method will write the specified object
    * to the given XML element as as list entries. Each entry within
    * the given collection must be assignable from the annotated 
    * type specified within the <code>ElementList</code> annotation.
    * Each entry is serialized as a root element, that is, its
    * <code>Root</code> annotation is used to extract the name. 
    * 
    * @param source this is the source collection to be serialized 
    * @param node this is the XML element container to be populated
    */ 
   public void write(OutputNode node, Object source) throws Exception {
      Collection list = (Collection) source;                
      OutputNode parent = node.getParent();      
      
      if(!node.isCommitted()) {
         node.remove();
      }
      write(parent, list);
   }
   
   /**
    * This <code>write</code> method will write the specified object
    * to the given XML element as as list entries. Each entry within
    * the given collection must be assignable from the annotated 
    * type specified within the <code>ElementList</code> annotation.
    * Each entry is serialized as a root element, that is, its
    * <code>Root</code> annotation is used to extract the name. 
    * 
    * @param list this is the source collection to be serialized 
    * @param node this is the XML element container to be populated
    */ 
   public void write(OutputNode node, Collection list) throws Exception {  
      for(Object item : list) {
         if(item != null) {
            Class type = item.getClass();

            if(!isCompatible(type, entry)) {
               throw new RootException("Root name for %s must match %s", type, entry);                     
            }
            root.write(node, item, entry);
         }
      }
   }
   
   /**
    * This is used to determine if the two types specified have the
    * same <code>Root</code> annotation name. Also, this requires
    * that the types be assignable to each other, that is, that the
    * expected type can reference a class of the specified type.
    * 
    * @param type this is the actual type of the deserialized item
    * @param expect this is the expected type from the annotation
    * 
    * @return this returns true if the types are compatible 
    */
   private boolean isCompatible(Class type, Class expect) throws Exception {
     String require = root.getName(expect);
     String real = root.getName(type);
      
      if(!entry.isAssignableFrom(type)) {
         throw new PersistenceException("Entry %s does not match %s", type, entry);
      }
      return require == real;
   }
}
