/*
 * CompositeList.java July 2006
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
import java.util.Collection;

/**
 * The <code>CompositeList</code> object is used to convert an element
 * list to a collection of element entries. This in effect performs a 
 * root serialization and deserialization of entry elements for the
 * collection object. On serialization each objects type must be 
 * checked against the XML annotation entry so that it is serialized
 * in a form that can be deserialized. 
 * <pre>
 *
 *    &lt;list&gt;
 *       &lt;entry attribute="value"&gt;
 *          &lt;text&gt;example text value&lt;/text&gt;
 *       &lt;/entry&gt;
 *       &lt;entry attribute="demo"&gt;
 *          &lt;text&gt;some other example&lt;/text&gt;
 *       &lt;/entry&gt;
 *    &lt;/list&gt;
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
 * @see org.simpleframework.xml.core.Traverser
 * @see org.simpleframework.xml.ElementList
 */ 
class CompositeList implements Converter {

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
    * This represents the name of the entry elements to write.
    */
   private final String name;

   /**
    * Constructor for the <code>CompositeList</code> object. This is
    * given the list type and entry type to be used. The list type is
    * the <code>Collection</code> implementation that deserialized
    * entry objects are inserted into. 
    *
    * @param context this is the context object used for serialization
    * @param type this is the collection type for the list used
    * @param entry the entry type to be stored within the list
    */    
   public CompositeList(Context context, Class type, Class entry, String name) {
      this.factory = new CollectionFactory(context, type); 
      this.root = new Traverser(context);      
      this.entry = entry;
      this.name = name;
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
      Type type = factory.getInstance(node);
      Object list = type.getInstance();
      
      if(!type.isReference()) {
         return populate(node, list);
      }
      return list;      
   }
   
   /**
    * This <code>read</code> method will read the XML element map from
    * the provided node and deserialize its children as entry types.
    * Each entry type must contain a key and value so that the entry 
    * can be inserted in to the map as a pair. If either the key or 
    * value is composite it is read as a root object, which means its
    * <code>Root</code> annotation must be present and the name of the
    * object element must match that root element name.
    * 
    * @param node this is the XML element that is to be deserialized
    * @param result this is the map object that is to be populated
    * 
    * @return this returns the item to attach to the object contact
    */
   public Object read(InputNode node, Object result) throws Exception {
      Type type = factory.getInstance(node);
      
      if(type.isReference()) {
         return type.getInstance();
      }
      Object list = type.getInstance(result);
      
      if(list != null) {
         return populate(node, list);
      }
      return list;
   }
   
   /**
    * This <code>populate</code> method wll read the XML element list 
    * from the provided node and deserialize its children as entry types.
    * This will each entry type is deserialized as a root type, that 
    * is, its <code>Root</code> annotation must be present and the
    * name of the entry element must match that root element name.
    * 
    * @param node this is the XML element that is to be deserialized
    * @param result this is the collection that is to be populated
    * 
    * @return this returns the item to attach to the object contact
    */ 
   private Object populate(InputNode node, Object result) throws Exception {
      Collection list = (Collection) result;                 
      
      while(true) {
         InputNode next = node.getNext();
        
         if(next == null) {
            return list;
         }
         list.add(root.read(next, entry));
      }
   }      
   
   /**
    * This <code>validate</code> method wll validate the XML element 
    * list from the provided node and deserialize its children as entry 
    * types. This takes each entry type and validates it as a root type, 
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
    * This <code>validate</code> method will validate the XML element 
    * list from the provided node and deserialize its children as entry 
    * types. This takes each entry type and validates it as a root type, 
    * that is, its <code>Root</code> annotation must be present and the
    * name of the entry element must match that root element name.
    * 
    * @param node this is the XML element that is to be validated
    * @param type this is the type to validate against the input node
    * 
    * @return true if the element matches the XML schema class given 
    */ 
   private boolean validate(InputNode node, Class type) throws Exception {
      while(true) {
         InputNode next = node.getNext();
        
         if(next == null) {
            return true;
         }
         root.validate(next, entry);
      }
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
      
      for(Object item : list) {
         if(item != null) {
            Class type = item.getClass();

            if(!entry.isAssignableFrom(type)) {
               throw new PersistenceException("Entry %s does not match %s", type, entry);                     
            }
            root.write(node, item, entry, name);
         }
      }
   }
}
