/*
 * Detail.java July 2012
 *
 * Copyright (C) 2012, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;

import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

interface Detail {
   /**
    * This method is used to determine whether strict mappings are
    * required. Strict mapping means that all labels in the class
    * schema must match the XML elements and attributes in the
    * source XML document. When strict mapping is disabled, then
    * XML elements and attributes that do not exist in the schema
    * class will be ignored without breaking the parser.
    *
    * @return true if strict parsing is enabled, false otherwise
    */ 
   boolean isStrict();
   
   /**
    * This is used to determine if the generated annotations are
    * required or not. By default generated parameters are required.
    * Setting this to false means that null values are accepted
    * by all defaulted fields or methods depending on the type.
    * 
    * @return this is used to determine if defaults are required
    */
   boolean isRequired();
   
   /**
    * This is used to determine if the class is an inner class. If
    * the class is a inner class and not static then this returns
    * false. Only static inner classes can be instantiated using
    * reflection as they do not require a "this" argument.
    * 
    * @return this returns true if the class is a static inner
    */
   boolean isInstantiable();
   
   /**
    * This is used to determine whether this detail represents a
    * primitive type. A primitive type is any type that does not
    * extend <code>Object</code>, examples are int, long and double.
    * 
    * @return this returns true if no XML annotations were found
    */
   boolean isPrimitive();
   
   /**
    * This is used to acquire the super type for the class that is
    * represented by this detail. If the super type for the class
    * is <code>Object</code> then this will return null.
    * 
    * @return returns the super type for this class or null
    */
   Class getSuper();
   
   /**
    * This returns the type represented by this detail. The type is
    * the class that has been scanned for annotations, methods and
    * fields. All super types of this are represented in the detail.
    * 
    * @return the type that this detail object represents
    */
   Class getType();
   
   /**
    * This returns the name of the class represented by this detail.
    * The name is either the name as specified in the last found
    * <code>Root</code> annotation, or if a name was not specified
    * within the discovered root then the Java Bean class name of
    * the last class annotated with a root annotation.
    * 
    * @return this returns the name of the object being scanned
    */
   String getName();
   
   /**
    * This returns the <code>Root</code> annotation for the class.
    * The root determines the type of deserialization that is to
    * be performed and also contains the name of the root element. 
    * 
    * @return this returns the name of the object being scanned
    */
   Root getRoot();
   
   /**
    * This returns the order annotation used to determine the order
    * of serialization of attributes and elements. The order is a
    * class level annotation that can be used only once per class
    * XML schema. If none exists then this will return null.
    *  of the class processed by this scanner.
    * 
    * @return this returns the name of the object being scanned
    */
   Order getOrder();
   
   /**
    * This returns the <code>Default</code> annotation access type
    * that has been specified by this.
    * 
    * @return
    */
   DefaultType getAccess();
   
   /**
    * 
    * @return
    */
   Namespace getNamespace();
   
   /**
    * 
    * @return
    */
   NamespaceList getNamespaceList();
   
   /**
    * 
    * @return
    */
   List<MethodDetail> getMethods();
   
   /**
    * 
    * @return
    */
   List<FieldDetail> getFields();
   
   /**
    * 
    * @return
    */
   Annotation[] getAnnotations();
   
   /**
    * 
    * @return
    */
   Constructor[] getConstructors();
}
