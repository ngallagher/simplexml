/*
 * Entry.java July 2007
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

import org.simpleframework.xml.ElementMap;

/**
 * The <code>Entry</code> object is used to provide configuration for
 * the serialization and deserialization of a map. Values taken from
 * the <code>ElementMap</code> annotation provide a means to specify
 * how to read and write the map as an XML element. Key and value
 * objects can be written as composite or primitive values. Primitive
 * key values can be written as attributes of the resulting entry
 * and value objects can be written inline if desired.
 * 
 * @author Niall Gallagher
 */
class Entry {
   
   /**
    * Provides the default name for entry XML elements of the map.
    */
   private static final String DEFAULT_NAME = "entry";
     
   /**
    * Represents the annotation that the map object is labeled with.
    */
   private ElementMap label;
   
   /**
    * Provides the point of contact in the object to the map.
    */
   private Contact contact;
   
   /**
    * Determines whether the key object is written as an attribute.
    */
   private boolean attribute;
   
   /**
    * Provides the class XML schema used for the value objects.
    */
   private Class valueType;
   
   /**
    * Provides the class XML schema used for the key objects.
    */
   private Class keyType;  
   
   /**
    * Specifies the name of the XML entry element used by the map.
    */
   private String entry;
   
   /**
    * Specifies the name of the XML value element used by the map.
    */
   private String value;
   
   /**
    * Specifies the name of the XML key node used by the map.
    */
   private String key;

   /**
    * Constructor for the <code>Entry</code> object. This takes the
    * element map annotation that provides configuration as to how
    * the map is serialized and deserialized from the XML document. 
    * The entry object provides a convenient means to access the XML
    * schema configuration using defaults where necessary.
    * 
    * @param contact this is the point of contact to the map object
    * @param label the annotation the map method or field uses
    */
   public Entry(Contact contact, ElementMap label) {  
      this.attribute = label.attribute();   
      this.entry = label.entry();
      this.value = label.value();
      this.key = label.key();
      this.key = label.key();
      this.contact = contact;
      this.label = label;
   }
   
   /**
    * This is used to acquire the dependant key for the annotated
    * map. This will simply return the type that the map object is
    * composed to hold. This must be a serializable type, that is,
    * it must be a composite or supported primitive type.
    * 
    * @return this returns the key object type for the map object
    */
   protected Class getKeyType() throws Exception  {
      if(keyType != null) {
         return keyType;
      }
      Class keyType = label.keyType();
      
      if(keyType == void.class) {
         keyType = getDependant(0);
      }
      return keyType;
   }
   
   /**
    * This is used to get the key converter for the entry. This knows
    * whether the key type is a primitive or composite object and will
    * provide the appropriate converter implementation. This allows 
    * the root composite map converter to concern itself with only the
    * details of the surrounding entry object. 
    * 
    * @param root this is the root context for the serialization
    * 
    * @return returns the converter used for serializing the key
    */
   public Converter getKey(Source root) throws Exception {
      Class type = getKeyType();

      if(Factory.isPrimitive(type)) {        
         return new PrimitiveKey(root, this, type);
      }
      return new CompositeKey(root, this, type);
   }   
   
   /**
    * Represents whether the key value is to be an attribute or an
    * element. This allows the key to be embedded within the entry
    * XML element allowing for a more compact representation. Only
    * primitive key objects can be represented as an attribute. For
    * example a <code>java.util.Date</code> or a string could be
    * represented as an attribute key for the generated XML. 
    *  
    * @return true if the key is to be inlined as an attribute
    */
   public boolean isAttribute() {
      return attribute;
   }
   
   /**
    * This is used to acquire the dependant value for the annotated
    * map. This will simply return the type that the map object is
    * composed to hold. This must be a serializable type, that is,
    * it must be a composite or supported primitive type.
    * 
    * @return this returns the value object type for the map object
    */
   protected Class getValueType() throws Exception {
      if(valueType != null) {
         return valueType;
      }
      Class valueType = label.valueType();
      
      if(valueType == void.class) {
         valueType = getDependant(1);
      }
      return valueType;
   }
   
   /**
    * This is used to get the value converter for the entry. This knows
    * whether the value type is a primitive or composite object and will
    * provide the appropriate converter implementation. This allows 
    * the root composite map converter to concern itself with only the
    * details of the surrounding entry object. 
    * 
    * @param root this is the root context for the serialization
    * 
    * @return returns the converter used for serializing the value
    */
   public Converter getValue(Source root) throws Exception {
      Class value = getValueType();
          
      if(Factory.isPrimitive(value)) {
         return new PrimitiveValue(root, this, value);
      }
      return new CompositeValue(root, this, value);
   }
   
   /**
    * Represents whether the value is to be written as an inline text
    * value within the element. This is only possible if the key has
    * been specified as an attribute. Also, the value can only be
    * inline if there is no wrapping value XML element specified.
    * 
    * @return this returns true if the value can be written inline
    */
   public boolean isInline() throws Exception {
      return isAttribute();
   }
   
   /**
    * Provides the dependant class for the map as taken from the 
    * specified index. This allows the entry to fall back on generic
    * declarations of the map if no explicit dependant types are 
    * given within the element map annotation.
    * 
    * @param index this is the index to acquire the parameter from
    * 
    * @return this returns the generic type at the specified index
    */
   private Class getDependant(int index) throws Exception {
      Class[] list = contact.getDependants();
      
      if(list.length < index) {
         throw new PersistenceException("Could not find dependant at index %s", index);
      }
      return list[index];
   }
   
   /**
    * This is used to provide a key XML element for each of the
    * keys within the map. This essentially wraps the entity to
    * be serialized such that there is an extra XML element present.
    * This can be used to override the default names of primitive
    * keys, however it can also be used to wrap composite keys. 
    * 
    * @return this returns the key XML element for each key
    */
   public String getKey() throws Exception {
      if(key == null) {
         return key;
      }    
      if(isEmpty(key)) {
         key = null;
      }      
      return key;
   } 
   
   /**
    * This is used to provide a value XML element for each of the
    * values within the map. This essentially wraps the entity to
    * be serialized such that there is an extra XML element present.
    * This can be used to override the default names of primitive
    * values, however it can also be used to wrap composite values. 
    * 
    * @return this returns the value XML element for each value
    */
   public String getValue() throws Exception {
      if(value == null) {
         return value;
      }
      if(isEmpty(value)) {
         value = null;
      }
      return value;
   }
   
   /**
    * This is used to provide a the name of the entry XML element 
    * that wraps the key and value elements. If specified the entry
    * value specified will be used instead of the default name of 
    * the element. This is used to ensure the resulting XML is 
    * configurable to the requirements of the generated XML. 
    * 
    * @return this returns the entry XML element for each entry
    */
   public String getEntry() throws Exception {
      if(entry == null) {
         return entry;
      }
      if(isEmpty(entry)) {
         entry = DEFAULT_NAME;
      }      
      return entry;
   } 
   
   /**
    * This method is used to determine if a root annotation value is
    * an empty value. Rather than determining if a string is empty
    * be comparing it to an empty string this method allows for the
    * value an empty string represents to be changed in future.
    * 
    * @param value this is the value to determine if it is empty
    * 
    * @return true if the string value specified is an empty value
    */
   private boolean isEmpty(String value) {
      return value.length() == 0;
   }  
}
