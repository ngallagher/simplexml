/*
 * ClassCreator.java December 2009
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

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>ClassCreator</code> is responsible for instantiating 
 * objects using either the default no argument constructor or one
 * that takes deserialized values as parameters. This also exposes 
 * the parameters and constructors used to instantiate the object.
 * 
 * @author Niall Gallagher
 */
class ClassCreator implements Creator {
   
   /**
    * This contains a list of all the builders for the class.
    */
   private final List<Builder> list;   
   
   /**
    * This represents the default no argument constructor used.
    */
   private final Builder primary;
   
   /**
    * This is used to acquire a parameter by the parameter name.
    */
   private final Index index;
   
   /**
    * This is the type this builder creates instances of.
    */
   private final Class type;
   
   /**
    * Constructor for the <code>ClassCreator</code> object. This is
    * used to create an object that contains all information that
    * relates to the construction of an instance. 
    * 
    * @param list this contains the list of all constructors 
    * @param index this contains all parameters for each constructor
    * @param primary this is the default no argument constructor
    */
   public ClassCreator(List<Builder> list, Index index, Builder primary) {
      this.type = index.getType();
      this.primary = primary;
      this.index = index;
      this.list = list;
   }

   /**
    * This is used to determine if this <code>Creator</code> has a
    * default constructor. If the class does contain a no argument
    * constructor then this will return true.
    * 
    * @return true if the class has a default constructor
    */
   public boolean isDefault() {
      return primary != null;
   }
   
   /**
    * This is used to instantiate the object using the default no
    * argument constructor. If for some reason the object can not be
    * instantiated then this will throw an exception with the reason.
    * 
    * @param context this is the context used to match parameters
    * 
    * @return this returns the object that has been instantiated
    */
   public Object getInstance(Context context) throws Exception {
      return primary.getInstance(context);
   }
   
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
   public Object getInstance(Context context, Criteria criteria) throws Exception {
      Builder builder = getBuilder(context, criteria);
      
      if(builder == null) {
         throw new PersistenceException("Constructor not matched for %s", type);
      }
      return builder.getInstance(context, criteria);
   }
   
   /**
    * This is used to acquire a <code>Builder</code> which is used
    * to instantiate the object. If there is no match for the builder
    * then the default constructor is provided.
    * 
    * @param context this is the context used to match parameters
    * @param criteria this contains the criteria to be used
    * 
    * @return this returns the builder that has been matched
    */
   private Builder getBuilder(Context context, Criteria criteria) throws Exception {
      Builder result = primary;
      double max = 0.0;
      
      for(Builder builder : list) {
         double score = builder.getScore(context, criteria);
         
         if(score > max) {
            result = builder;
            max = score;
         }
      }
      return result;
   }

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
   public Parameter getParameter(String name) {
      return index.get(name);
   }
   
   /**
    * This is used to acquire all parameters annotated for the class
    * schema. Providing all parameters ensures that they can be
    * validated against the annotated methods and fields to ensure
    * that each parameter is valid and has a corresponding match.
    * 
    * @return this returns the parameters declared in the schema     
    */
   public List<Parameter> getParameters() {
      return index.getParameters();
   }
   
   /**
    * This is used to acquire all of the <code>Builder</code> objects
    * used to create an instance of the object. Each represents a
    * constructor and contains the parameters to the constructor. 
    * This is primarily used to validate each constructor against the
    * fields and methods annotated to ensure they are compatible.
    * 
    * @return this returns a list of builders for the creator
    */
   public List<Builder> getBuilders() {
      return new ArrayList<Builder>(list);
   }
   
   /**
    * This is used to acquire a description of the creator. This is
    * useful when debugging an issue as it allows a representation
    * of the instance to be viewed with the class it represents.
    * 
    * @return this returns a visible description of the creator
    */
   public String toString() {
      return String.format("creator for %s", type);
   }
}