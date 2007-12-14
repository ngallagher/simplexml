/*
 * Factory.java July 2006
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
import java.lang.reflect.Modifier;
import java.beans.Introspector;

/**
 * The <code>Factory</code> object provides a base class for factories 
 * used to produce field values from XML elements. The goal of this 
 * type of factory is to make use of the <code>Strategy</code> object
 * to determine the type of the field value. The strategy class must be 
 * assignable to the field class type, that is, it must extend it or
 * implement it if it represents an interface. If the strategy returns
 * a null <code>Type</code> then the subclass implementation determines 
 * the type used to populate the object field value.
 * 
 * @author Niall Gallagher
 */
abstract class Factory {
   
   /**
    * This is the source object used for the serialization process.
    */
   protected Source source;
   
   /**
    * This is the field type that the class must be assignable to.
    */
   protected Class field;        

   /**
    * Constructor for the <code>Factory</code> object. This is given 
    * the class type for the field that this factory will determine
    * the actual type for. The actual type must be assignable to the
    * field type to insure that any instance can be set. 
    * 
    * @param source the contextual object used by the persister
    * @param field this is the field type to determine the value of
    */
   protected Factory(Source source, Class field) {
      this.source = source;           
      this.field = field;           
   }

   /**
    * This is used to get a possible override from the provided node.
    * If the node provided is an element then this checks for a  
    * specific class override using the <code>Strategy</code> object.
    * If the strategy cannot resolve a class then this will return 
    * null. If the resolved <code>Type</code> is not assignable to 
    * the field then this will thrown an exception.
    * 
    * @param node this is the node used to search for the override
    * 
    * @return this returns null if no override type can be found
    * 
    * @throws Exception if the override type is not compatible
    */
   public Type getOverride(InputNode node) throws Exception {
      Type type = getConversion(node);      

      if(type != null) { 
         Class real = type.getType();
     
         if(!isCompatible(field, real)) {
            throw new InstantiationException("Type %s is not compatible with %s", real, field);              
         }
      }         
      return type; 
   }
   
   /**
    * This method is used to set the override class within an element.
    * This delegates to the <code>Strategy</code> implementation, which
    * depending on the implementation may add an attribute of a child
    * element to describe the type of the object provided to this.
    * 
    * @param field this is the class of the field type being serialized
    * @param node the XML element that is to be given the details
    *
    * @throws Exception thrown if an error occurs within the strategy
    */
   public boolean setOverride(Class field, Object value, OutputNode node) throws Exception {
      if(!field.isPrimitive()) {
         return source.setOverride(field, value, node);
      }
      return false;
   }

   /**
    * This performs the conversion from the element node to a type. This
    * is where the <code>Strategy</code> object is consulted and asked
    * for a class that will represent the provided XML element. This will,
    * depending on the strategy implementation, make use of attributes
    * and/or elements to determine the type for the field.
    * 
    * @param node this is the element used to extract the override
    * 
    * @return this returns null if no override type can be found
    * 
    * @throws Exception thrown if the override class cannot be loaded    
    */ 
   public Type getConversion(InputNode node) throws Exception {
      return source.getOverride(field, node);
   }
   
   /**
    * This creates a <code>Scanner</code> object that can be used to
    * examine the fields within the XML class schema. The scanner
    * maintains information when a field from within the scanner is
    * visited, this allows the serialization and deserialization
    * process to determine if all required XML annotations are used.
    * 
    * @param type the schema class the scanner is created for
    * 
    * @return a scanner that can maintains information on the type
    * 
    * @throws Exception if the class contains an illegal schema 
    */ 
   private static Scanner getScanner(Class type) throws Exception {
      return ScannerFactory.getInstance(type);
   }
   
   /**
    * This is used to acquire the name of the specified type using
    * the <code>Root</code> annotation for the class. This will 
    * use either the name explicitly provided by the annotation or
    * it will use the name of the class that the annotation was
    * placed on if there is no explicit name for the root.
    * 
    * @param type this is the type to acquire the root name for
    * 
    * @return this returns the name of the type from the root
    * 
    * @throws Exception if the class contains an illegal schema
    */
   public static String getName(Class type) throws Exception {
      Scanner schema = getScanner(type);
      String name = schema.getName();
      
      if(name != null) {
         return name;
      }
      return getClassName(type);
   }
   
   /**
    * This returns the name of the class specified. If there is a root
    * annotation on the type, then this is ignored in favour of the 
    * actual class name. This is typically used when the type is a
    * primitive or if there is no <code>Root</code> annotation present. 
    * 
    * @param type this is the type to acquire the root name for
    * 
    * @return this returns the name of the type from the root
    */
   public static String getClassName(Class type) throws Exception {
      if(type.isArray()) {
         type = type.getComponentType();
      }      
      String name = type.getSimpleName();
      
      if(type.isPrimitive()) {
         return name;
      }
      return Introspector.decapitalize(name);
   }
   
   /**
    * This is used to determine whether the scanned class represents
    * a primitive type. A primitive type is a type that contains no
    * XML annotations and so cannot be serialized with an XML form.
    * Instead primitives a serialized using transformations.
    *
    * @param type this is the type to determine if it is primitive
    * 
    * @return this returns true if no XML annotations were found
    */
   public static boolean isPrimitive(Class type) throws Exception {
      return getScanner(type).isPrimitive();
   }
   
   /**
    * This is used to determine whether the provided base class can be
    * assigned from the issued type. For an override to be compatible
    * with the field type an instance of the override type must be 
    * assignable to the field value. 
    * 
    * @param field this is the field value present the the object    
    * @param type this is the specialized type that will be assigned
    * 
    * @return true if the field type can be assigned the type value
    */
   public static boolean isCompatible(Class field, Class type) {
      if(field.isArray()) {
         field = field.getComponentType();
      }
      return field.isAssignableFrom(type);           
   }

   /**
    * This is used to determine whether the type given is instantiable,
    * that is, this determines if an instance of that type can be
    * created. If the type is an interface or an abstract class then 
    * this will return false.
    * 
    * @param type this is the type to check the modifiers of
    * 
    * @return false if the type is an interface or an abstract class
    */
   public static boolean isInstantiable(Class type) {
      int modifiers = type.getModifiers();

      if(Modifier.isAbstract(modifiers)) {
         return false;              
      }              
      return !Modifier.isInterface(modifiers);
   }      
}           
