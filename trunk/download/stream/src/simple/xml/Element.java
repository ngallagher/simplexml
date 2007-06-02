/*
 * Element.java July 2006
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

package simple.xml;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

/**
 * The <code>Element</code> annotation is used to represent a field
 * or method that appears as an XML element. Fields or methods that
 * are annotated with this can be either primitive or compound, that
 * is, represent an object that can be serialized and deserialized.
 * Below is an example of the serialized format for a compond object. 
 * <pre>
 * 
 *    &lt;example class="demo.Example"&gt;
 *       &lt;data/&gt;
 *    &lt;example&gt;
 * 
 * </pre>
 * Each element may have any number of attributes and sub-elements
 * representing fields or methods of that compound object. Attribute
 * and element names can be acquired from the annotation or, if the
 * annotation does not explicitly declare a name, it is taken from
 * the annotated field or method. There are exceptions in some cases,
 * for example, the <code>class</code> attribute is reserved by the
 * serialization framework to represent the serialized type. 
 * 
 * @author Niall Gallagher
 */ 
@Retention(RetentionPolicy.RUNTIME)
public @interface Element {
   
   /**
    * This represents the name of the XML element. Annotated fields
    * can optionally provide the name of the element. If no name is
    * provided then the name of the annotated field or method will
    * be used in its place. The name is provided if the field or
    * method name is not suitable as an XML element name.
    * 
    * @return the name of the XML element this represents
    */
   public String name() default "";
   
   /**
    * This is used to determine whether the element data is written
    * in a CDATA block or not. If this is set to true then the text
    * is written within a CDATA block, by default the text is output
    * as escaped XML. Typically this is useful for primitives only.
    * 
    * @return true if the data is to be wrapped in a CDATA block
    */
   public boolean data() default false;
   
   /**
    * Determines whether the element is required within the XML
    * document. Any field marked as not required will not have its
    * value set when the object is deserialized. If an object is to
    * be serialized only a null attribute will not appear as XML.
    * 
    * @return true if the element is required, false otherwise
    */
   public boolean required() default true;   
}
