/*
 * Version.java July 2008
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

package org.simpleframework.xml;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The <code>Version</code> annotation is used to specify an attribute
 * that is used to represent a revision of the class XML schema. This
 * annotation can annotate only floating point types such as double, 
 * float, and the java primitive object types. This can not be used to
 * annotate strings, enumerations or other primitive types.
 * 
 * @author Niall Gallagher
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Version {
   
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
    * This represents the revision of the class. A revision is used
    * by the deserialization process to determine how to match the
    * annotated fields and methods to the XML elements and attributes.
    * If the version deserialized is different to the annotated 
    * revision then annotated fields and methods are not required 
    * and if there are excessive XML nodes they are ignored.
    * 
    * @return this returns the version of the XML class schema
    */
   public double revision() default 1.0;
   
   /**
    * Determines whether the version is required within an XML
    * element. Any field marked as not required will not have its
    * value set when the object is deserialized. This is written
    * only if the version is not the same as the default version.
    * 
    * @return true if the version is required, false otherwise
    */
   public boolean required() default false;
}
