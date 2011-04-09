/*
 * Initializer.java April 2009
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
import java.util.Set;

/**
 * The <code>Initializer</code> object is used to represent an single
 * constructor within an object. It contains the actual constructor
 * as well as the list of parameters. Each initializer will score its
 * weight when given a <code>Criteria</code> object. This allows
 * the deserialization process to find the most suitable one to
 * use when instantiating an object.
 * 
 * @author Niall Gallagher
 */
class Initializer {   
   
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
   private final Signature signature;

   /**
    * Constructor for the <code>Initializer</code> object. This is used
    * to create a factory like object used for instantiating objects.
    * Each initializer will score its suitability using the parameters
    * it is provided.
    * 
    * @param factory this is the factory used for instantiation
    * @param signature is the map of parameters that are declared
    */
   public Initializer(Constructor factory, Signature signature) {
      this.list = signature.getParameters();
      this.signature = signature;
      this.factory = factory;
   } 
   
   /**
    * This is used to determine if this <code>Initializer</code> is a
    * default constructor. If the class does contain a no argument
    * constructor then this will return true.
    * 
    * @return true if the class has a default constructor
    */
   public boolean isDefault() {
      return signature.size() == 0;
   }
   
   /**
    * This is used to acquire the named <code>Parameter</code> from
    * the initializer. A parameter is taken from the constructor which
    * contains annotations for each object that is required. These
    * parameters must have a matching field or method.
    * 
    * @param name this is the name of the parameter to be acquired
    * 
    * @return this returns the named parameter for the initializer
    */
   public Parameter getParameter(String name) {
      return signature.get(name);
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
    * @param context this is the context used to match parameters     
    * @param criteria this contains the criteria to be used
    * 
    * @return this returns the object that has been instantiated
    */
   public Object getInstance(Context context, Criteria criteria) throws Exception {
      Object[] values = list.toArray();
      
      for(int i = 0; i < list.size(); i++) {
         values[i] = getVariable(context, criteria, i);
      }
      return getInstance(values);
   }
   
   /**
    * This is used to acquire a variable from the criteria provided. 
    * In order to match the constructor correctly this will check to
    * see if the if the parameter is required. If it is required then
    * there must be a non null value or an exception is thrown.
    * 
    * @param context this is the context used to match parameters
    * @param criteria this is used to acquire the parameter value
    * @param index this is the index to acquire the value for
    * 
    * @return the value associated with the specified parameter
    */
   private Object getVariable(Context context, Criteria criteria, int index) throws Exception {
      Parameter parameter = list.get(index);
      String name = parameter.getName(context);
      Variable variable = criteria.remove(name);
      
      if(variable != null) {
         return variable.getValue();
      }
      return null;
   }
  
   /**
    * This is used to score this <code>Initializer</code> object so that
    * it can be weighed amongst other constructors. The initializer that
    * scores the highest is the one that is used for instantiation.
    * <p>
    * If any read only element or attribute is not a parameter in
    * the constructor then the constructor is discounted. This is
    * because there is no way to set the read only entity without a
    * constructor injection in to the instantiated object.
    * 
    * @param context this is the context used to match parameters
    * @param criteria this contains the criteria to be used
    * 
    * @return this returns the score based on the criteria provided
    */
   public double getScore(Context context, Criteria criteria) throws Exception {
      Signature match = signature.getSignature(context);
      
      for(String name : criteria) {
         Label label = criteria.resolve(name);
         
         if(label != null) {
            Set<String> options = label.getUnion(context);
            Parameter value = match.getParameter(name);
            Contact contact = label.getContact();

            for(String option : options) {
               if(value == null) {
                  value = match.getParameter(option);
               }
            }
            if(contact.isReadOnly()) {
               if(value == null) {
                  return -1.0;
               }               
            }
         }
      }
      return getPercentage(context, criteria);
   }
   
   /**
    * This is used to determine what percentage of available values
    * can be injected in to a constructor. Calculating the percentage
    * in this manner ensures that the best possible fit will be used
    * to construct the object. This also allows the object to define
    * what defaults it wishes to set for the values.
    * <p>
    * This will use a slight adjustment to ensure that if there are
    * many constructors with a 100% match on parameters, the one 
    * with the most values to be injected wins. This ensures the
    * most desirable constructor is chosen each time.
    * 
    * @param context this is the context used to match parameters     
    * @param criteria this is the criteria object containing values
    * 
    * @return this returns the percentage match for the values
    */
   private double getPercentage(Context context, Criteria criteria) throws Exception {
      double score = 0.0;
      
      for(Parameter value : list) {
         String name = value.getName(context);
         Label label = criteria.resolve(name);

         if(label == null) {
            if(value.isRequired()) {
               return -1;
            }  
            if(value.isPrimitive()) {
               return -1;
            }
         } else {
            score++;
         }
      }
      return getAdjustment(context, score);
   }
   
   /**
    * This will use a slight adjustment to ensure that if there are
    * many constructors with a 100% match on parameters, the one 
    * with the most values to be injected wins. This ensures the
    * most desirable constructor is chosen each time.
    *     
    * @param context this is the context used for serialization
    * @param score this is the score from the parameter matching
    * 
    * @return an adjusted score to account for the signature size
    */
   private double getAdjustment(Context context, double score) {
      double adjustment = list.size() / 1000.0;
      
      if(score > 0) {
         return adjustment + score / list.size();
      }
      return score / list.size();
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
    * This is used to acquire a descriptive name for the initializer.
    * Providing a name is useful in debugging and when exceptions are
    * thrown as it describes the constructor the initializer represents.
    * 
    * @return this returns the name of the constructor to be used
    */
   public String toString() {
      return factory.toString();
   }
}