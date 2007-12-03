/*
 * Order.java November 2007
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

package org.simpleframework.xml;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

/**
 * The <code>Order</code> annotation is used to specify the order of
 * appearance of XML elements and attributes. When used it ensures 
 * that on serialization the XML generated is predictable. By default
 * serialization of fields is done in declaration order. 
 * 
 * @author Niall Gallagher
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {

   /**
    * Specifies the appearance order of the XML elements within the
    * generated document. This overrides the default order used, 
    * which is the declaration order within the class. If an element 
    * is not specified within this array then its order will be the
    * appearance order directly after the last specified element.
    * 
    * @return an ordered array of elements representing order
    */
   public String[] elements() default {};
   
   /**
    * Specifies the appearance order of the XML attributes within 
    * the generated document. This overrides the default order used, 
    * which is the declaration order within the class. If an attribute 
    * is not specified within this array then its order will be the
    * appearance order directly after the last specified attribute.
    * 
    * @return an ordered array of attributes representing order
    */
   public String[] attributes() default {};
}
