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

package simple.xml.load;

import java.lang.reflect.Modifier;
import simple.xml.stream.InputNode;
import simple.xml.stream.OutputNode;

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
      Class type = value.getClass();
      
      if(!isPrimitive(type)) {
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
   
   /**
    * This method is used to determine whether the field type is a
    * primitive type. This check is required to ensure that primitive
    * elements do not consult the <code>Strategy</code> object for 
    * the field class. This improves the performance of serialization
    * and also ensures that the XML serialization is transparent.
    * 
    * @param type the type checked to determine if it is primitive
    * 
    * @return true if the type is primitive, false otherwise
    */   
   public static boolean isPrimitive(Class type) {
      if(type == String.class) {
         return true;              
      }
      if(type == Boolean.class) {
         return true;              
      }
      if(type == Integer.class) {
         return true;              
      }      
      if(type == Float.class) {
         return true;               
      }
      if(type == Long.class) {
         return true;              
      }
      if(type == Double.class) {
         return true;              
      }
      if(type == Byte.class) {
         return true;              
      }
      if(type == Short.class) {
         return true;              
      }
      if(type == Character.class) {
         return true;
      }
      if(type.isPrimitive()) {
         return true;                 
      }
      return type.isEnum();
   }
}           
