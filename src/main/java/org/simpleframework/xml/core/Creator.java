/*
 * Creator.java December 2009
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
    * @return this returns the object that has been instantiated
    */
   public Object getInstance() throws Exception; 
   
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
   public Object getInstance(Criteria criteria) throws Exception;

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
    * This is used to acquire all of the <code>Builder</code> objects
    * used to create an instance of the object. Each represents a
    * constructor and contains the parameters to the constructor. 
    * This is primarily used to validate each constructor against the
    * fields and methods annotated to ensure they are compatible.
    * 
    * @return this returns a list of builders for the creator
    */
   public List<Builder> getBuilders();
}
