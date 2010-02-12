/*
 * Builder.java April 2009
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

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * The <code>Builder</code> object is used to represent an single
 * constructor within an object. It contains the actual constructor
 * as well as the list of parameters. Each builder will score its
 * weight when given a <code>Criteria</code> object. This allows
 * the deserialization process to find the most suitable one to
 * use when instantiating an object.
 * 
 * @author Niall Gallagher
 */
class Builder {   
   
   /**
    * This is the list of parameters in the order of declaration. 
    */
   private final List<Parameter> list;

   /**
    * This is the factory that is used to instantiate the object.
    */
   private final Constructor factory;
   
   /**
    * This is the map that contains the parameters to be used.
    */
   private final Index index;

   /**
    * Constructor for the <code>Builder</code> object. This is used
    * to create a factory like object used for instantiating objects.
    * Each builder will score its suitability using the parameters
    * it is provided.
    * 
    * @param factory this is the factory used for instantiation
    * @param index this is the map of parameters that are declared
    */
   public Builder(Constructor factory, Index index) {
      this.list = index.getParameters();
      this.factory = factory;
      this.index = index;
   } 
   
   /**
    * This is used to determine if this <code>Builder</code> is a
    * default constructor. If the class does contain a no argument
    * constructor then this will return true.
    * 
    * @return true if the class has a default constructor
    */
   public boolean isDefault() {
      return index.size() == 0;
   }
   
   /**
    * This is used to acquire the named <code>Parameter</code> from
    * the builder. A parameter is taken from the constructor which
    * contains annotations for each object that is required. These
    * parameters must have a matching field or method.
    * 
    * @param name this is the name of the parameter to be acquired
    * 
    * @return this returns the named parameter for the builder
    */
   public Parameter getParameter(String name) {
      return index.get(name);
   }
   
   /**
    * This is used to instantiate the object using the default no
    * argument constructor. If for some reason the object can not be
    * instantiated then this will throw an exception with the reason.
    * 
    * @return this returns the object that has been instantiated
    */
   public Object getInstance() throws Exception {
      if(!factory.isAccessible()) {
         factory.setAccessible(true);
      } 
      return factory.newInstance();
   }
   
   /**
    * This is used to instantiate the object using a constructor that
    * takes deserialized objects as arguments. The object that have
    * been deserialized can be taken from the <code>Criteria</code>
    * object which contains the deserialized values.
    * 
    * @param criteria this contains the criteria to be used
    * 
    * @return this returns the object that has been instantiated
    */
   public Object getInstance(Criteria criteria) throws Exception {
      Object[] values = list.toArray();
      
      for(int i = 0; i < list.size(); i++) {
         String name = list.get(i).getName();
         Variable variable = criteria.remove(name);
         Object value = variable.getValue();
         
         values[i] = value;
      }
      return getInstance(values);
   }
   
   /**
    * This is used to instantiate the object using a constructor that
    * takes deserialized objects as arguments. The objects that have
    * been deserialized are provided in declaration order so they can
    * be passed to the constructor to instantiate the object.
    * 
    * @param list this is the list of objects used for instantiation
    * 
    * @return this returns the object that has been instantiated
    */
   private Object getInstance(Object[] list) throws Exception {
      if(!factory.isAccessible()) {
         factory.setAccessible(true);
      } 
      return factory.newInstance(list);
   }
  
   /**
    * This is used to score this <code>Builder</code> object so that
    * it can be weighed amongst other constructors. The builder that
    * scores the highest is the one that is used for instantiation.
    * 
    * @param criteria this contains the criteria to be used
    * 
    * @return this returns the score based on the criteria provided
    */
   public int score(Criteria criteria) throws Exception {
      int score = 0;
      
      for(int i = 0; i < list.size(); i++) {
         String name = list.get(i).getName();
         Label label = criteria.get(name);
         
         if(label == null) {
            return -1;
         }
         score++;
      }
      return score;
   }
   
   /**
    * This is used to acquire a descriptive name for the builder.
    * Providing a name is useful in debugging and when exceptions are
    * thrown as it describes the constructor the builder represents.
    * 
    * @return this returns the name of the constructor to be used
    */
   public String toString() {
      return factory.toString();
   }
}