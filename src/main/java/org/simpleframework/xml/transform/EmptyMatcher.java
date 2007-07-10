/*
 * EmptyMatcher.java May 2007
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
 * The <code>EmptyMatcher</code> object is used as a delegate type 
 * that is used when no user specific matcher is specified. This
 * ensures that no transform is resolved for a specified type, and
 * allows the normal resolution of the stock transforms.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.transform.Transformer
 */
class EmptyMatcher implements Matcher {

   /**
    * This method is used to return a null value for the transform.
    * Returning a null value allows the normal resolution of the
    * stock transforms to be used when no matcher is specified.
    * 
    * @param type this is the type that is expecting a transform
    * 
    * @return this transform will always return a null value
    */
   public Transform match(Class type) throws Exception {
      return null;
   }
}
