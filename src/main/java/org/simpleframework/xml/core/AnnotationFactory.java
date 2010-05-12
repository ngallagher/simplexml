/*
 * AnnotationFactory.java January 2010
 *
 * Copyright (C) 2010, Niall Gallagher <niallg@users.sf.net>
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
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;

/**
 * The <code>AnnotationFactory</code> is used to create annotations
 * using a given class. This will classify the provided type as
 * either a list, map, array, or a default object. Depending on the
 * type provided a suitable annotation will be created. Annotations
 * produced by this will have default attribute values.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.AnnotationHandler
 */
class AnnotationFactory {  
   
   /**
    * This is used to determine if the defaults are required.
    */
   private final boolean required;
   
   /**
    * Constructor for the <code>AnnotationFactory</code> object. This
    * is used to create a factory for annotations used to provide
    * the default annotations for generated labels. By default this
    * will set the requirement of annotations to true.
    */
   public AnnotationFactory() {
      this(true);
   }
   
   /**
    * Constructor for the <code>AnnotationFactory</code> object. This
    * is used to create a factory for annotations used to provide
    * the default annotations for generated labels.
    * 
    * @param required used to determine if the annotation is required
    */
   public AnnotationFactory(boolean required) {
      this.required = required;
   }
  
   /**
    * This is used to create an annotation for the provided type.
    * Annotations created are used to match the type provided. So
    * a <code>List</code> will have an <code>ElementList</code>
    * annotation for example. Matching the annotation to the
    * type ensures the best serialization for that type. 
    * 
    * @param type the type to create the annotation for
    * 
    * @return this returns the synthetic annotation to be used
    */
   public Annotation getInstance(Class type) throws Exception { 
      ClassLoader loader = getClassLoader();
      
      if(Map.class.isAssignableFrom(type)) {
         return getInstance(loader, ElementMap.class);
      }
      if(Collection.class.isAssignableFrom(type)) {
         return getInstance(loader, ElementList.class);
      }
      if(type.isArray()) {
         return getInstance(loader, ElementArray.class);
      }
      return getInstance(loader, Element.class);
   }
   
   /**
    * This will create a synthetic annotation using the provided 
    * interface. All attributes for the provided annotation will
    * have their default values. 
    * 
    * @param loader this is the class loader to load the annotation 
    * @param label this is the annotation interface to be used
    * 
    * @return this returns the synthetic annotation to be used
    */
   private Annotation getInstance(ClassLoader loader, Class label) throws Exception {
      AnnotationHandler handler = new AnnotationHandler(label, required);
      Class[] list = new Class[] {label};
      
      return (Annotation) Proxy.newProxyInstance(loader, list, handler);
   }
   
   /**
    * This is used to create a suitable class loader to be used to
    * load the synthetic annotation classes. The class loader
    * provided will be the same as the class loader that was used
    * to load this class.
    * 
    * @return this returns the class loader that is to be used
    */
   private ClassLoader getClassLoader() throws Exception {
      return AnnotationFactory.class.getClassLoader();
   }
}
