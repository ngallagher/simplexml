/*
 * DefaultStrategy.java July 2006
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

import org.simpleframework.xml.stream.Node;
import org.simpleframework.xml.stream.NodeMap;
import java.lang.reflect.Array;
import java.util.Map;

/**
 * The <code>DefaultStrategy</code> object is used by the persister if
 * no strategy is specified. This will make use of a "class" attribute
 * by default to resolve the class to use for a given element during
 * the deserialization process. For the serialization process the 
 * "class" attribute will be added to the element specified. If there
 * is a need to use an attribute name other than "class" then the
 * name of the attribute to use can be specified.
 * 
 * @author Niall Gallagher
 */
class DefaultStrategy implements Strategy {

   /**
    * This is used to specify the size of an array element instance.
    */
   private static final String LENGTH = "length";
   
   /**   
    * This is the attribute that is used to determine the real type.
    */
   private static final String LABEL = "class";
   
   /**
    * This is the attribute that is used to determine an array size.
    */
   private String length;
   
   /**   
    * This is the attribute that is used to determine the real type.
    */   
   private String label;
   
   /**
    * Constructor for the <code>DefaultStrategy</code> object. This 
    * is used to create a strategy that can resolve and load class
    * objects for deserialization using a "class" attribute. Also
    * for serialization this will add the appropriate "class" value.
    */
   public DefaultStrategy() {
      this(LABEL, LENGTH);           
   }        
   
   /**
    * Constructor for the <code>DefaultStrategy</code> object. This 
    * is used to create a strategy that can resolve and load class
    * objects for deserialization using the specified attribute. 
    * The attribute value can be any legal XML attribute name.
    * 
    * @param label this is the name of the attribute to use
    * @param length this is used to determine the array length
    */
   public DefaultStrategy(String label, String length) {
      this.length = length;
      this.label = label;         
   }
   
   /**
    * This is used to resolve and load a class for the given element.
    * Resolution of the class to used is done by inspecting the
    * XML element provided. If there is a "class" attribute on the
    * element then its value is used to resolve the class to use.
    * If no such attribute exists on the element this returns null.
    * 
    * @param field this is the type of the XML element expected
    * @param node this is the element used to resolve an override
    * @param map this is used to maintain contextual information
    * 
    * @return returns the class that should be used for the object
    * 
    * @throws Exception thrown if the class cannot be resolved
    */
   public Type getRoot(Class field, NodeMap node, Map map) throws Exception {
      return getElement(field, node, map);
   }  
   
   /**
    * This is used to resolve and load a class for the given element.
    * Resolution of the class to used is done by inspecting the
    * XML element provided. If there is a "class" attribute on the
    * element then its value is used to resolve the class to use.
    * If no such attribute exists on the element this returns null.
    * 
    * @param field this is the type of the XML element expected
    * @param node this is the element used to resolve an override
    * @param map this is used to maintain contextual information
    * 
    * @return returns the class that should be used for the object
    * 
    * @throws Exception thrown if the class cannot be resolved
    */
   public Type getElement(Class field, NodeMap node, Map map) throws Exception {
      Class type = getType(field, node);
      
      if(field.isArray()) {
         return getArray(type, node);   
      }
      if(field != type) {
         return new ClassType(type);
      }
      return null;
   }
   
   /**
    * This is used to resolve and load a class for the given element.
    * Resolution of the class to used is done by inspecting the
    * XML element provided. If there is a "class" attribute on the
    * element then its value is used to resolve the class to use.
    * This also expects a "length" attribute for the array length.
    * 
    * @param type this is the type of the XML element expected
    * @param node this is the element used to resolve an override
    * 
    * @return returns the class that should be used for the object
    * 
    * @throws Exception thrown if the class cannot be resolved
    */   
   private Type getArray(Class type, NodeMap node) throws Exception {      
      Node entry = node.remove(length);
      int size = 0;
      
      if(entry != null) {
         String value = entry.getValue();
         size = Integer.parseInt(value);
      }      
      return new ArrayType(type, size);
   }
   
   /**
    * This is used to resolve and load a class for the given element.
    * Resolution of the class to used is done by inspecting the
    * XML element provided. If there is a "class" attribute on the
    * element then its value is used to resolve the class to use.
    * If no such attribute exists the specified field is returned,
    * or if the field type is an array then the component type.
    * 
    * @param field this is the type of the XML element expected
    * @param node this is the element used to resolve an override
    * 
    * @return returns the class that should be used for the object
    * 
    * @throws Exception thrown if the class cannot be resolved
    */   
   private Class getType(Class field, NodeMap node) throws Exception {      
      Node entry = node.remove(label);      
      Class type = field;
      
      if(field.isArray()) {
         type = field.getComponentType();
      }
      if(entry != null) {
         String name = entry.getValue();
         type = getClass(name);
      }    
      return type;
   }     
   
   /**
    * This is used to attach a attribute to the provided element
    * that is used to identify the class. The attribute name is
    * "class" and has the value of the fully qualified class 
    * name for the object provided. This will only be invoked
    * if the object class is different from the field class.
    *  
    * @param field this is the declared class for the field used
    * @param value this is the instance variable being serialized
    * @param node this is the element used to represent the value
    * @param map this is used to maintain contextual information
    * 
    * @return this returns true if serialization is complete
    */
   public boolean setRoot(Class field, Object value, NodeMap node, Map map){
      return setElement(field, value, node, map);
   }   
   
   /**
    * This is used to attach a attribute to the provided element
    * that is used to identify the class. The attribute name is
    * "class" and has the value of the fully qualified class 
    * name for the object provided. This will only be invoked
    * if the object class is different from the field class.
    *
    * @param field this is the declared class for the field used
    * @param value this is the instance variable being serialized
    * @param node this is the element used to represent the value
    * @param map this is used to maintain contextual information
    * 
    * @return this returns true if serialization is complete
    */   
   public boolean setElement(Class field, Object value, NodeMap node, Map map){
      Class type = value.getClass();
      Class real = type;
      
      if(type.isArray()) {
         real = setArray(field, value, node);
      }
      if(type != field) {
         node.put(label, real.getName());
      }       
      return false;
   }
   
   /**
    * This is used to add a length attribute to the element due to
    * the fact that the serialized value is an array. The length
    * of the array is acquired and inserted in to the attributes.
    * 
    * @param field this is the field type for the array to set
    * @param value this is the actual value for the array to set
    * @param node this is the map of attributes for the element
    * 
    * @return returns the array component type that is set
    */
   private Class setArray(Class field, Object value, NodeMap node){
      int size = Array.getLength(value);
      
      if(length != null) {       
         node.put(length, String.valueOf(size));
      }
      return field.getComponentType();
   }
   
   /**
    * This method is used to acquire the class of the specified name.
    * Loading is performed by the thread context class loader as this
    * will ensure that the class loading strategy can be changed as
    * requirements dictate. Typically the thread context class loader
    * can handle all serialization requirements.
    * 
    * @param name this is the name of the class that is to be loaded
    * 
    * @return this returns the class that has been loaded by this
    */
   private Class getClass(String type) throws Exception {
      ClassLoader loader = getClassLoader();
      
      if(loader == null) {
         loader = getCallerClassLoader();
      }
      return loader.loadClass(type);      
   }
   
   /**
    * This is used to acquire the caller class loader for this object.
    * Typically this is only used if the thread context class loader
    * is set to null. This ensures that there is at least some class
    * loader available to the strategy to load the class.
    * 
    * @return this returns the loader that loaded this class     
    */
   private ClassLoader getCallerClassLoader() throws Exception {
      return getClass().getClassLoader();
   }

   /**
    * This is used to acquire the thread context class loader. This
    * is the default class loader used by the cycle strategy. When
    * using the thread context class loader the caller can switch the
    * class loader in use, which allows class loading customization.
    * 
    * @return this returns the loader used by the calling thread
    */
   private static ClassLoader getClassLoader() throws Exception {
      return Thread.currentThread().getContextClassLoader();
   }
}
