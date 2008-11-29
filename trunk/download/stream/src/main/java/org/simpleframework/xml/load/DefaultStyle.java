/*
 * DefaultStyle.java July 2008
 *
 * Copyright (C) 2008, Niall Gallagher <niallg@users.sf.net>
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

package org.simpleframework.xml.load;

import org.simpleframework.xml.stream.Style;

/**
 * The <code>DefaultStyle</code> object is used to represent a style
 * that does not modify the tokens passed in to it. This is used if
 * there is no style specified or if there is no need to convert the
 * XML elements an attributes to a particular style. This is also 
 * the most performant style as it does not require cache lookups.
 * 
 * @author Niall Gallagher
 */
class DefaultStyle implements Style {

   /**
    * This is basically a pass through method. It will return the
    * same string as is passed in to it. This ensures the best
    * performant when no styling is required for the XML document.
    * 
    * @param name this is the token that is to be styled by this
    * 
    * @return this returns the same string that is passed in
    */
   public String getAttribute(String name) {
      return name;
   }

   /**
    * This is basically a pass through method. It will return the
    * same string as is passed in to it. This ensures the best
    * performant when no styling is required for the XML document.
    * 
    * @param name this is the token that is to be styled by this
    * 
    * @return this returns the same string that is passed in
    */
   public String getElement(String name) {
      return name;
   }
}
