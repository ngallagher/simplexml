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
   boolean isEmpty();
   
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
   boolean isPrimitive();
   Class getSuper();
   Class getType();
   String getName();
   Root getRoot();
   Order getOrder() ;
   DefaultType getAccess();
   Namespace getNamespace();
   NamespaceList getNamespaceList();
   List<MethodDetail> getMethods();
   List<FieldDetail> getFields();
   Annotation[] getAnnotations();
   Constructor[] getConstructors();
   
}
