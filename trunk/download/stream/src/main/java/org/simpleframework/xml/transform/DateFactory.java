/*
 * DateTransform.java May 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
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

package org.simpleframework.xml.transform;

import java.lang.reflect.Constructor;
import java.util.Date;

/**
 * The <code>DateFactory</code> object is used to create instances
 * or subclasses of the <code>Date</code> object. This will create
 * the instances of the date objects using a constructor that takes
 * a single <code>long</code> parameter value. 
 * 
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.transform.DateTransform
 */
class DateFactory<T extends Date> {
   
   /**
    * This is used to create instances of the date object required.
    */
   private final Constructor<T> factory;
   
   /**
    * Constructor for the <code>DateFactory</code> object. This is
    * used to create instances of the specified type. All objects
    * created by this instance must take a single long parameter.
    * 
    * @param type this is the date implementation to be created
    */
   public DateFactory(Class<T> type) throws Exception {
      this(type, long.class);
   }
   
   /**
    * Constructor for the <code>DateFactory</code> object. This is
    * used to create instances of the specified type. All objects
    * created by this instance must take the specified parameter.
    * 
    * @param type this is the date implementation to be created
    * @param list is basically the list of accepted parameters
    */
   public DateFactory(Class<T> type, Class... list) throws Exception {
      this.factory = type.getDeclaredConstructor(list);
   }
   
   /**
    * This is used to create instances of the date using a delegate
    * date. A <code>long</code> parameter is extracted from the 
    * given date an used to instantiate a date of the required type.
    * 
    * @param list this is the type used to provide the long value
    * 
    * @return this returns an instance of the required date type
    */
   public T getInstance(Object... list) throws Exception {
      return factory.newInstance(list);
   }
}