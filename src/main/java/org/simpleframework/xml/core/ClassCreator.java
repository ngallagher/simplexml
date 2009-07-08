/*
 * ClassCreator.java December 2009
 *
 * Copyright (C) 2009, Niall Gallagher <niallg@users.sf.net>
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

package org.simpleframework.xml.core;

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
    * This is used to acquire a parameter by the parameter name.
    */
   private final ParameterMap map;
   
   /**
    * This represents the default no argument constructor used.
    */
   private final Builder primary;
   
   /**
    * Constructor for the <code>ClassCreator</code> object. This is
    * used to create an object that contains all information that
    * relates to the construction of an instance. 
    * 
    * @param list this contains the list of all constructors 
    * @param map this contains all parameters for each constructor
    * @param primary this is the default no argument constructor
    */
   public ClassCreator(List<Builder> list, ParameterMap map, Builder primary) {
      this.primary = primary;
      this.list = list;
      this.map = map;
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
    * @return this returns the object that has been instantiated
    */
   public Object getInstance() throws Exception {
      return primary.getInstance();
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
      return getBuilder(criteria).getInstance(criteria);
   }
   
   /**
    * This is used to acquire a <code>Builder</code> which is used
    * to instantiate the object. If there is no match for the builder
    * then the default constructor is provided.
    * 
    * @param criteria this contains the criteria to be used
    * 
    * @return this returns the builder that has been matched
    */
   private Builder getBuilder(Criteria criteria) throws Exception {
      Builder result = primary;
      int max = 0;
      
      for(Builder builder : list) {
         int score = builder.score(criteria);
         
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
      return map.get(name);
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
      return list;
   }
}