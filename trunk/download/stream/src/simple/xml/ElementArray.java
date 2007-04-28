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
 * The <code>ElementArray</code> annotation represents a field that 
 * is an array of elements. The array object deserialized is the
 * same type as the field, all entries within the array should be a
 * compatible type. However, a <code>class</code> attribute can be 
 * used to override an entry, this entry must be an assignable type.
 * <pre>
 * 
 *    &lt;array length='3'&gt;
 *       &lt;entry&gt;one&lt;/entry&gt;
 *       &lt;entry&gt;two&lt;/entry&gt;
 *       &lt;entry&gt;three&lt;/entry&gt;
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
    * must provide the name of the element they represent so that
    * that can be serialized and deserialized to and from the XML.
    * 
    * @return the name of the XML element this represents
    */
   public String name();
   
   /**
    * This is used to provide a parent XML element for each of the
    * values within the array. This esentially wraps the entity to
    * be serialized such that there is an extra XML element present.
    * This is used to ensure that null values can be represented.  
    * 
    * @return this returns the parent XML element for each value
    */
   public String parent();
   
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
