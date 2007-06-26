/*
 * Attribute.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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
 * The <code>Attribute</code> annotation represents a serializable XML
 * attribute within an XML element. An object annotated with this is
 * typically a primitive or enumerated type. Conversion from the 
 * attribute to primitive type is done with a <code>Transform</code>
 * object. If a suitable transform can be found then this will convert
 * the attribute string value to an object instance, which can be 
 * assigned to the annotated field, or passed to the annotated method.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.transform.Transformer
 */ 
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {

   /**
    * This represents the name of the XML attribute. Annotated fields
    * or methods can optionally provide the name of the XML attribute
    * they represent. If a name is not provided then the field or 
    * method name is used in its place. A name can be specified if 
    * the field or method name is not suitable for the XML attribute.
    * 
    * @return the name of the XML attribute this represents
    */
   public String name() default "";
   
   /**
    * This is used to provide a default value for the attribute if
    * the annotated field or method is null. This ensures the the
    * serialzation process writes the attribute with a value even
    * if the value is null, and allows deserialization to determine
    * whether the value within the object was null or not.
    * 
    * @return this returns the default attribute value to use
    */
   public String empty() default "";

   /**
    * Determines whether the attribute is required within an XML
    * element. Any field marked as not required will not have its
    * value set when the object is deserialized. If an object is to
    * be serialized only a null attribute will not appear as XML.
    * 
    * @return true if the attribute is required, false otherwise
    */
   public boolean required() default true;   
}
