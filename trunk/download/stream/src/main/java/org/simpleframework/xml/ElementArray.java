/*
 * ElementArray.java July 2006
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
 * The <code>ElementArray</code> annotation represents a method or 
 * field that is an array of elements. The array deserialized is the
 * same type as the field or method, all entries within the array 
 * must be a compatible type. However, a <code>class</code> attribute 
 * can be used to override an entry, this must be an assignable type.
 * <pre>
 *
 *    &lt;array length="3"&gt;
 *       &lt;entry&gt;
 *          &lt;value&gt;example text value&lt;/value&gt;
 *       &lt;/entry&gt;
 *       &lt;entry&gt;
 *          &lt;value&gt;some other value&lt;/value&gt;
 *       &lt;/entry&gt;
 *       &lt;entry/&gt;
 *    &lt;/array&gt;
 * 
 * </pre>
 * All null objects within the array are represented as an empty XML
 * element so that they can be deserialized accurately. This ensures
 * that the length attribute of the array is respected, as well as 
 * the index position of all serialized entries. The length of the 
 * array must be specified for deserialization to instantiate the 
 * array before the array values are instantiated. This is required 
 * to account for cyclical references in the object graph.
 * 
 * @author Niall Gallagher
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ElementArray {
   
   /**
    * This represents the name of the XML element. Annotated fields
    * or methods can optionally provide the name of the element. If 
    * no name is provided then the name of the annotated field or 
    * method will be used in its place. The name is provided if the 
    * field or method name is not suitable as an XML element name.
    * 
    * @return the name of the XML element this represents
    */
   public String name() default "";
   
   /**
    * This is used to provide a name of the XML element representing
    * the entry within the array. An entry name is optional and is
    * used when the name needs to be overridden. This also ensures
    * that entry, regardless of type has the same root name.   
    * 
    * @return this returns the entry XML element for each value
    */
   public String entry() default "";
   
   /**
    * This is used to determine whether the element data is written
    * in a CDATA block or not. If this is set to true then the text
    * is written within a CDATA block, by default the text is output
    * as escaped XML. Typically this is useful when this annotation
    * is applied to an array of primitives, such as strings.
    * 
    * @return true if entries are to be wrapped in a CDATA block
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
