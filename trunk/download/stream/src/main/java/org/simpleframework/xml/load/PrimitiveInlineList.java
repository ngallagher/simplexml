/*
 * PrimitiveInlineList.java July 2006
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
import org.simpleframework.xml.stream.Mode;
import org.simpleframework.xml.stream.OutputNode;
import java.util.Collection;

/**
 * The <code>PrimitiveInlineList</code> object is used to convert a
 * group of elements in to a collection of element entries. This is
 * used when a containing element for a list is not required. It 
 * extracts the elements by matching elements to name of the type
 * that the annotated field or method requires. This enables these
 * element entries to exist as siblings to other objects within the
 * object.  One restriction is that the <code>Root</code> annotation
 * for each of the types within the list must be the same.
 * <pre> 
 *    
 *    &lt;entry&gt;example one&lt;/entry&gt;
 *    &lt;entry&gt;example two&lt;/entry&gt;
 *    &lt;entry&gt;example three&lt;/entry&gt;
 *    &lt;entry&gt;example four&lt;/entry&gt;      
 * 
 * </pre>
 * For the above XML element list the element <code>entry</code> is
 * used to wrap the primitive string value. This wrapping XML element 
 * is configurable and defaults to the lower case string for the name
 * of the class it represents. So, for example, if the primitive type
 * is an <code>int</code> the enclosing element will be called int.
 * 
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.load.Primitive
 * @see org.simpleframework.xml.ElementList
 */ 
class PrimitiveInlineList implements Converter {

   /**
    * This factory is used to create a suitable collection list.
    */         
   private final CollectionFactory factory;

   /**
    * This performs the traversal used for object serialization.
    */ 
   private final Primitive root;
   
   /**
    * This is the name that each list element is wrapped with.
    */
   private final String parent;
   
   /**
    * This is the type of object that will be held in the list.
    */
   private final Class entry;
   
   /**
    * Constructor for the <code>PrimitiveInlineList</code> object. 
    * This is given the list type and entry type to be used. The list
    * type is the <code>Collection</code> implementation that is used 
    * to collect the deserialized entry objects from the XML source. 
    *
    * @param root this is the source object used for serialization
    * @param type this is the collection type for the list used
    * @param entry the entry type to be stored within the list
    * @param parent this is the name to wrap the list element with 
    */    
   public PrimitiveInlineList(Source root, Class type, Class entry, String parent) {
      this.factory = new CollectionFactory(root, type); 
      this.root = new Primitive(root, entry, null);          
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
    * This will deserialize each entry type as a primitive value. In
    * order to do this the parent string provided forms the element.
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
         Object item = root.read(node);
         
         if(item != null) {
            list.add(item);
         }      
         node = from.getNext(name);
      }
      return list;
   }     
   
   /**
    * This <code>write</code> method will write the specified object
    * to the given XML element as as list entries. Each entry within
    * the given list must be assignable to the given primitive type.
    * This will deserialize each entry type as a primitive value. In
    * order to do this the parent string provided forms the element.
    * 
    * @param source this is the source collection to be serialized 
    * @param node this is the XML element container to be populated
    */ 
   public void write(OutputNode node, Object source) throws Exception {               
      OutputNode parent = node.getParent();      
      Mode mode = node.getMode();
      
      if(!node.isCommitted()) {
         node.remove();
      }      
      write(parent, source, mode);
   }
   
   /**
    * This <code>write</code> method will write the specified object
    * to the given XML element as as list entries. Each entry within
    * the given list must be assignable to the given primitive type.
    * This will deserialize each entry type as a primitive value. In
    * order to do this the parent string provided forms the element.
    * 
    * @param node this is the parent output node to write values to
    * @param source this is the source collection to be serialized 
    * @param mode this is used to determine whether to output CDATA    
    */ 
   public void write(OutputNode node, Object source, Mode mode) throws Exception {
      Collection list = (Collection) source;
      
      for(Object item : list) {
         OutputNode child = node.getChild(parent);
         
         if(!isOverridden(child, item, entry)) { 
            child.setMode(mode);
            root.write(child, item);
         }
      }
   } 
   
   /**
    * This is used to determine whether the specified value has been
    * overrideen by the strategy. If the item has been overridden
    * then no more serialization is require for that value, this is
    * effectivly telling the serialization process to stop writing.
    * 
    * @param node the node that a potential override is written to
    * @param value this is the object instance to be serialized
    * @param type this is the type of the object to be serialized
    * 
    * @return returns true if the strategy overrides the object
    */
   private boolean isOverridden(OutputNode node, Object value, Class type) throws Exception{
      return factory.setOverride(type, value, node);
   }
}
