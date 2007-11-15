/*
 * CompositeInlineMap.java July 2007
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
import java.util.Map;

/**
 * The <code>CompositeMap</code> is used to serialize and deserialize
 * maps to and from a source XML document. The structure of the map in
 * the XML format is determined by the annotation. Keys can be either
 * attributes or elements, and values can be inline. This can perform
 * serialization and deserialization of the key and value objects 
 * whether the object types are primitive or composite.
 * <pre>
 * 
 *    &lt;map&gt;
 *       &lt;entry key='1'&gt;           
 *          &lt;value&gt;one&lt;/value&gt;
 *       &lt;/entry&gt;
 *       &lt;entry key='2'&gt;
 *          &lt;value&gt;two&lt;/value&gt;
 *       &lt;/entry&gt;      
 *    &lt;/map&gt;
 *    
 * </pre>
 * For the above XML element map the element <code>entry</code> is 
 * used to wrap the key and value such that they can be grouped. This
 * element does not represent any real object. The names of each of
 * the XML elements serialized and deserialized can be configured.
 *  
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.load.Entry
 */
class CompositeInlineMap implements Repeater {
      
   /**
    * The factory used to create suitable map object instances.
    */
   private final MapFactory factory;
   
   /**
    * This is the type that the value objects are instances of. 
    */
   private final Converter value;
   
   /**
    * This is the name of the entry wrapping the key and value.
    */
   private final Converter key;  
   
   /**
    * The entry object contains the details on how to write the map.
    */
   private final Entry entry;
   
   /**
    * This is the name of the elements of the inline map entries.
    */
   private final String name;
    
   /**
    * Constructor for the <code>CompositeMap</code> object. This will
    * create a converter that is capable of writing map objects to 
    * and from XML. The resulting XML is configured by an annotation
    * such that key values can attributes and values can be inline. 
    * 
    * @param root this is the root context for the serialization
    * @param entry this provides configuration for the resulting XML
    * @param type this is the map type that is to be converted
    */
   public CompositeInlineMap(Source root, Entry entry, Class type) throws Exception {
      this.factory = new MapFactory(root, type);
      this.value = entry.getValue(root);
      this.key = entry.getKey(root);
      this.name = entry.getEntry();
      this.entry = entry;
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
    * 
    * @return this returns the item to attach to the object contact
    */
   public Object read(InputNode node) throws Exception{
      Object value = factory.getInstance();
      Map table = (Map) value;
      
      if(table != null) {
         return read(node, table);
      }
      return null;
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
    * 
    * @return this returns the item to attach to the object contact
    */
   public Object read(InputNode node, Object value) throws Exception {
      Map map = (Map) value;
      
      if(map != null) {
         return read(node, map);
      }
      return read(node);
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
    * @param table this is the map object that is to be populated
    * 
    * @return this returns the item to attach to the object contact
    */
   private Object read(InputNode node, Map map) throws Exception {
      InputNode from = node.getParent();
      String name = node.getName();                
      
      while(node != null) {
         Object key = readKey(node);
         Object value = readValue(node);
            
         if(key != null) {
            map.put(key, value);
         }
         node = from.getNext(name);
      }
      return map;
   }
   
   /**
    * This <code>readKey</code> method will read the key from the XML 
    * node whether the key is primitive or composite. If the key is 
    * a composite value then it is read as a root object, which 
    * means <code>Root</code> annotation must be present.
    * 
    * @param node this is the XML element that is to be deserialized
    * 
    * @return this returns the key to use for the resulting map
    */
   private Object readKey(InputNode node) throws Exception {
      return key.read(node);
   }
   
   /**
    * This <code>readValue</code> method will read the value from the 
    * XML node whether the value is primitive or composite. If the 
    * value is a composite value then it is read as a root object, 
    * which means <code>Root</code> annotation must be present.
    * 
    * @param node this is the XML element that is to be deserialized
    * 
    * @return this returns the value to use for the resulting map
    */
   private Object readValue(InputNode node) throws Exception {  
      Position line = node.getPosition();
      String name = entry.getValue();
      
      if(name != null) {
         node = node.getNext(name);
      }               
      if(node == null) {
         throw new ElementException("Could not find element '%s' at %s", name, line);
      }
      return value.read(node);
   }

   /**
    * This <code>write</code> method will write the key value pairs
    * within the provided map to the specified XML node. This will 
    * write each entry type must contain a key and value so that
    * the entry can be deserialized in to the map as a pair. If the
    * key or value object is composite it is read as a root object 
    * so its <code>Root</code> annotation must be present.
    * 
    * @param node this is the node the map is to be written to
    * @param source this is the source map that is to be written 
    */
   public void write(OutputNode node, Object source) throws Exception {               
      OutputNode parent = node.getParent();      
      Map table = (Map) source;
      
      if(!node.isCommitted()) {
         node.remove();
      }
      write(parent, table);
   }
   
   /**
    * This <code>write</code> method will write the key value pairs
    * within the provided map to the specified XML node. This will 
    * write each entry type must contain a key and value so that
    * the entry can be deserialized in to the map as a pair. If the
    * key or value object is composite it is read as a root object 
    * so its <code>Root</code> annotation must be present.
    * 
    * @param node this is the node the map is to be written to
    * @param map this is the source map that is to be written 
    */
   public void write(OutputNode node, Map map) throws Exception {   
      for(Object key : map.keySet()) {
         String name = entry.getEntry();
         OutputNode next = node.getChild(name);
         Object value = map.get(key);
            
         if(value != null) {
            writeKey(next, key);            
            writeValue(next, value);
         }         
      }
   }
   
   /**
    * This <code>writeKey</code> method will write the key to the  
    * XML node whether the key is primitive or composite. If the 
    * key is a composite key then it is written as a root object, 
    * which means <code>Root</code> annotation must be present.
    * 
    * @param node this is the XML element that is to be serialized
    * @param item this is the key to be written to the XML element
    */
   private void writeKey(OutputNode node, Object item) throws Exception {
      key.write(node, item);     
   }
   
   /**
    * This <code>writeValue</code> method will write the value to the 
    * XML node whether the value is primitive or composite. If the 
    * value is a composite value then it is written as a root object, 
    * which means <code>Root</code> annotation must be present.
    * 
    * @param node this is the XML element that is to be serialized
    * @param item this is the value to be written to the XML element
    */
   private void writeValue(OutputNode node, Object item) throws Exception {
      String name = entry.getValue();
      
      if(name != null) {
         node = node.getChild(name); // give it the wrapper node
      }    
      value.write(node, item);
   }
}