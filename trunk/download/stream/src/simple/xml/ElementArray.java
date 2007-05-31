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

package simple.xml;

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
 *    &lt;array length='3'&gt;
 *       &lt;string&gt;one&lt;/string&gt;
 *       &lt;string&gt;two&lt;/string&gt;
 *       &lt;string&gt;three&lt;/string&gt;
 *    &lt;/array&gt;
 * 
 * </pre>
 * All null objects within the array are ignored on serialization.
 * The length of the array must be specified for deserialization to
 * instantiate the array before the array values are instantiated.
 * This is required to account for cyclical references in the graph.
 * 
 * @author Niall Gallagher
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ElementArray {
   
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
    * This is used to provide a parent XML element for each of the
    * values within the array. This esentially wraps the entity to
    * be serialized such that there is an extra XML element present.
    * This is used to ensure that null values can be represented.  
    * 
    * @return this returns the parent XML element for each value
    */
   public String parent() default "";
   
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
