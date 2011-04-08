/*
 * Creator.java December 2009
 *
 * Copyright (C) 2009, Niall Gallagher <niallg@users.sf.net>
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

import java.util.List;

/**
 * The <code>Creator</code> object is responsible for instantiating 
 * objects using either the default no argument constructor or one
 * that takes deserialized values as parameters. This also exposes 
 * the parameters and constructors used to instantiate the object.
 * 
 * @author Niall Gallagher
 */
interface Creator {

   /**
    * This is used to determine if this <code>Creator</code> has a
    * default constructor. If the class does contain a no argument
    * constructor then this will return true.
    * 
    * @return true if the class has a default constructor
    */
   public boolean isDefault(); 
   
   /**
    * This is used to instantiate the object using the default no
    * argument constructor. If for some reason the object can not be
    * instantiated then this will throw an exception with the reason.
    * 
    * @param context this is the context used to match parameters
    * 
    * @return this returns the object that has been instantiated
    */
   public Object getInstance(Context context) throws Exception; 
   
   /**
    * This is used to instantiate the object using a constructor that
    * takes deserialized objects as arguments. The object that have
    * been deserialized can be taken from the <code>Criteria</code>
    * object which contains the deserialized values.
    *
    * @param context this is the context used to match parameters
    * @param criteria this contains the criteria to be used
    * 
    * @return this returns the object that has been instantiated
    */
   public Object getInstance(Context context, Criteria criteria) throws Exception;

   /**
    * This is used to acquire the named <code>Parameter</code> from
    * the creator. A parameter is taken from the constructor which
    * contains annotations for each object that is required. These
    * parameters must have a matching field or method.
    * 
    * @param name this is the name of the parameter to be acquired
    * 
    * @return this returns the named parameter for the creator
    */
   public Parameter getParameter(String name);
   
   /**
    * This is used to acquire all parameters annotated for the class
    * schema. Providing all parameters ensures that they can be
    * validated against the annotated methods and fields to ensure
    * that each parameter is valid and has a corrosponding match.
    * 
    * @return this returns the parameters declared in the schema     
    */
   public List<Parameter> getParameters();
   
   /**
    * This is used to acquire the <code>Initializer</code> objects
    * used to create an instance of the object. Each represents a
    * constructor and contains the parameters to the constructor. 
    * This is primarily used to validate each constructor against the
    * fields and methods annotated to ensure they are compatible.
    * 
    * @return this returns a list of initializers for the creator
    */
   public List<Initializer> getInitializers();
}
