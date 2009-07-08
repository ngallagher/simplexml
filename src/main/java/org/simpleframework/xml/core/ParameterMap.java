/*
 * ParameterMap.java April 2009
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

import java.util.LinkedHashMap;

/**
 * The <code>ParameterMap</code> object is used to represent a map
 * of parameters iterable in declaration order. This is used so
 * that parameters can be acquired by name for validation. It is
 * also used to create an array of <code>Parameter</code> objects
 * that can be used to acquire the correct deserialized values
 * to use in order to instantiate the object.
 * 
 * @author Niall Gallagher
 */
class ParameterMap extends LinkedHashMap<String, Parameter> {
    
   /**
    * This represents a blank array of parameter objects.
    */
   private final Parameter[] blank;
   
   /**
    * Constructor for the <code>ParameterMap</code> object. This
    * is used to create a hash map that can be used to acquire
    * parameters by name. It also provides the parameters in
    * declaration order within a for each loop;
    */
   public ParameterMap() {
      this.blank = new Parameter[]{};
   }
    
   /**
    * This is used to acquire an array of <code>Parameter</code>
    * objects in declaration order. This array will help with the
    * resolution of the correct constructor to use on deserialization
    * of the XML. It also provides a faster method of iteration.
    * 
    * @return this returns the parameters in declaration order
    */
   public Parameter[] list() {
      return values().toArray(blank);
   }
}