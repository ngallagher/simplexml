/*
 * Reflector.java April 2007
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * The <code>Reflector</code> object is used to determine the type
 * of a generic type. This is used when the type of an XML annotation
 * is not explicitly and the schema scanner needs to determine via
 * reflection what the generic parameters are of a specific type. In
 * particular this is used to determine the parameters within a list
 * annotated with the <code>ElementList</code> annotation. This also
 * has special handling for arrays within generic collections.
 * 
 * @author Niall Gallagher
 */
final class Reflector {
   
   /**
    * This method is used to acquire a generic parameter dependant 
    * from the specified field. This will acquire the field class and
    * attempt to extract the first generic parameter type from that  
    * field. If there is a generic parameter then the class of that 
    * parameter is returned from this method.
    * 
    * @param field this is the field to acquire the dependant class
    * 
    * @return this returns the generic parameter class declared
    */
   public static Class getDependant(Field field) {
      ParameterizedType type = getType(field);
      
      if(type != null) {
         return getClass(type);
      }
      return null;
   }
   
   /**
    * This method is used to acquire generic parameter dependants 
    * from the specified field. This will acquire the field class and
    * attempt to extract all of the generic parameter types from that  
    * field. If there is a generic parameter then the class of that 
    * parameter is returned from this method.
    * 
    * @param field this is the field to acquire the dependant types
    * 
    * @return this returns the generic parameter classes declared
    */
   public static Class[] getDependants(Field field) {
      ParameterizedType type = getType(field);
      
      if(type != null) {
         return getClasses(type);
      }
      return new Class[]{};
   }
   
   /**
    * This is used to acquire the parameterized types from the given
    * field. If the field class has been parameterized then this will
    * return the parameters that have been declared on that class.
    * 
    * @param field this is the field to acquire the parameters from
    * 
    * @return this will return the parameterized types for the field
    */
   private static ParameterizedType getType(Field field) {
      Type type = field.getGenericType();
         
      if(type instanceof ParameterizedType) {
         return (ParameterizedType) type;
      }
      return null;      
   }
   
   /**
    * This method is used to acquire a generic parameter dependant 
    * from the method return type. This will acquire the return type
    * and attempt to extract the first generic parameter type from 
    * that type. If there is a generic parameter then the class of 
    * that parameter is returned from this method.
    * 
    * @param method this is the method to acquire the dependant of   
    * 
    * @return this returns the generic parameter class declared
    */   
   public static Class getReturnDependant(Method method) {
      ParameterizedType type = getReturnType(method);
      
      if(type != null) {
         return getClass(type);
      }
      return null;
   }
   
   /**
    * This method is used to acquire a generic parameter dependant 
    * from the method return type. This will acquire the return type
    * and attempt to extract the first generic parameter type from 
    * that type. If there is a generic parameter then the class of 
    * that parameter is returned from this method.
    * 
    * @param method this is the method to acquire the dependant of   
    * 
    * @return this returns the generic parameter class declared
    */   
   public static Class[] getReturnDependants(Method method) {
      ParameterizedType type = getReturnType(method);
      
      if(type != null) {
         return getClasses(type);
      }
      return new Class[]{};
   }
   
   /**
    * This is used to acquire the parameterized types from the given
    * methods return class. If the return type class is parameterized
    * then this will return the parameters that have been declared on
    * that class, otherwise null is returned.
    * 
    * @param method this is the method to acquire the parameters from
    * 
    * @return this  returns the parameterized types for the method
    */
   private static ParameterizedType getReturnType(Method method) {
      Type type = method.getGenericReturnType();
      
      if(type instanceof ParameterizedType) {
         return (ParameterizedType) type;
      }
      return null;
   }
   
   /**
    * This method is used to acquire a generic parameter dependant 
    * from the specified parameter type. This will acquire the type
    * for the parameter at the specified index and attempt to extract
    * the first generic parameter type from that type. If there is a
    * generic parameter then the class of that parameter is returned
    * from this method, otherwise null is returned.
    * 
    * @param method this is the method to acquire the dependant of
    * @param index this is the index to acquire the parameter from    
    * 
    * @return this returns the generic parameter class declared
    */
   public static Class getParameterDependant(Method method, int index) {
      ParameterizedType type = getParameterType(method, index);
      
      if(type != null) {
         return getClass(type);
      }
      return null;
   }
   
   /**
    * This method is used to acquire a generic parameter dependant 
    * from the specified parameter type. This will acquire the type
    * for the parameter at the specified index and attempt to extract
    * the first generic parameter type from that type. If there is a
    * generic parameter then the class of that parameter is returned
    * from this method, otherwise null is returned.
    * 
    * @param method this is the method to acquire the dependant of
    * @param index this is the index to acquire the parameter from    
    * 
    * @return this returns the generic parameter class declared
    */
   public static Class[] getParameterDependants(Method method, int index) {
      ParameterizedType type = getParameterType(method, index);
      
      if(type != null) {
         return getClasses(type);
      }
      return new Class[]{};
   }
   
   /**
    * This is used to acquire the parameterized types from the given
    * methods parameter class at the specified index position. If the
    * parameter class is parameterized this returns the parameters 
    * that have been declared on that class.
    * 
    * @param method this is the method to acquire the parameters from
    * @param index this is the index to acquire the parameter from     
    * 
    * @return this  returns the parameterized types for the method
    */
   private static ParameterizedType getParameterType(Method method, int index) {
      Type[] list = method.getGenericParameterTypes();
         
      if(list.length > index) {         
         Type type = list[index];
         
         if(type instanceof ParameterizedType) {
            return (ParameterizedType) type;
         }
      }
      return null;
   }
   
   /**
    * This is used to extract the class from the specified type. If
    * there are no actual generic type arguments to the specified
    * type then this will return null. Otherwise this will return 
    * the actual class, regardless of whether the class is an array.
    *  
    * @param type this is the type to extract the class from
    *  
    * @return this returns the class type from the first parameter
    */
   private static Class getClass(ParameterizedType type) {
      Type[] list = type.getActualTypeArguments();
      
      if(list.length > 0) {
         return getClass(list[0]);
      }      
      return null;      
   }
   
   /**
    * This is used to extract the class from the specified type. If
    * there are no actual generic type arguments to the specified
    * type then this will return null. Otherwise this will return 
    * the actual class, regardless of whether the class is an array.
    *  
    * @param type this is the type to extract the class from
    *  
    * @return this returns the class type from the first parameter
    */
   private static Class[] getClasses(ParameterizedType type) {
      Type[] list = type.getActualTypeArguments();
      Class[] types = new Class[list.length]; 
            
      for(int i = 0; i < list.length; i++) {
         types[i] = getClass(list[i]);
      }  
      return types;     
   }
   
   /**
    * This is used to extract the class from the specified type. If
    * there are no actual generic type arguments to the specified
    * type then this will return null. Otherwise this will return 
    * the actual class, regardless of whether the class is an array.
    *  
    * @param type this is the type to extract the class from
    *  
    * @return this returns the class type from the first parameter
    */
   private static Class getClass(Type type) {
      if(type instanceof Class) {
         return (Class) type;
      }
      return getGenericClass(type);
   }
   
   /**
    * This is used to extract the class from the specified type. If
    * there are no actual generic type arguments to the specified
    * type then this will return null. Otherwise this will return 
    * the actual class, regardless of whether the class is an array.
    *  
    * @param type this is the type to extract the class from
    *  
    * @return this returns the class type from the first parameter
    */
   private static Class getGenericClass(Type type) {
      if(type instanceof GenericArrayType) {
         return getArrayClass(type);
      }      
      return null;
   }
   
   /**
    * This is used to extract an array class from the specified. If
    * a class can be extracted from the type then the array class 
    * is created by reflective creating a zero length array with 
    * the component type of the array and returning the array class.
    *  
    * @param type this is the type to extract the class from
    *  
    * @return this returns the class type from the array type
    */
   private static Class getArrayClass(Type type) {
      GenericArrayType generic = (GenericArrayType) type;
      Type array = generic.getGenericComponentType();
      Class entry = getClass(array);
      
      if(entry != null) {
         return Array.newInstance(entry, 0).getClass();
      }
      return null;
   }
}