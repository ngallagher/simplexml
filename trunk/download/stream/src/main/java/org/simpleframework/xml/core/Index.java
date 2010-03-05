/*
 * Index.java April 2009
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
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The <code>Index</code> object is used to represent an index
 * of parameters iterable in declaration order. This is used so
 * that parameters can be acquired by name for validation. It is
 * also used to create an array of <code>Parameter</code> objects
 * that can be used to acquire the correct deserialized values
 * to use in order to instantiate the object.
 * 
 * @author Niall Gallagher
 */
class Index extends LinkedHashMap<String, Parameter> {
   
   /**
    * This is the type that the parameters are created for.
    */
   private final Class type;
   
   /**
    * Constructor for the <code>Index</code> object. This is 
    * used to create a hash map that can be used to acquire
    * parameters by name. It also provides the parameters in
    * declaration order within a for each loop.
    * 
    * @param type this is the type the map is created for
    */
   public Index(Class type) {
      this.type = type;
   }
   
   /**
    * This is used to acquire a <code>Parameter</code> using the
    * position of that parameter within the constructor. This 
    * allows a builder to determine which parameters to use..
    * 
    * @param ordinal this is the position of the parameter
    * 
    * @return this returns the parameter for the position
    */
   public Parameter getParameter(int ordinal) {
      return getParameters().get(ordinal);
   }
    
   /**
    * This is used to acquire an list of <code>Parameter</code>
    * objects in declaration order. This list will help with the
    * resolution of the correct constructor for deserialization
    * of the XML. It also provides a faster method of iteration.
    * 
    * @return this returns the parameters in declaration order
    */
   public List<Parameter> getParameters() {
      return new ArrayList<Parameter>(values());
   }
 
   /**
    * This is the type that this class map represents. It can be 
    * used to determine where the parameters stored are declared.
    * 
    * @return returns the type that the parameters are created for
    */
   public Class getType() {
      return type;
   }
}