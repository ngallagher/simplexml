/*
 * Matcher.java May 2007
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

/**
 * The <code>Matcher</code> is used to match a type with a transform
 * such that a string value can be read or written as that type. If
 * there is no match this will typically return a null to indicate
 * that another matcher should be delegated to. If there is an error
 * in performing the match an exception is thrown.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.transform.Transformer
 */
interface Matcher {

   /**
    * This is used to match a <code>Transform</code> using the type
    * specified. If no transform can be acquired then this returns
    * a null value indicating that no transform could be found.
    * 
    * @param type this is the type to acquire the transform for
    * 
    * @return returns a transform for processing the type given
    * 
    * @throws Exception thrown if a transform could not be found
    */ 
   public Transform match(Class type) throws Exception;
}
