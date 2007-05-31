/*
 * ElementList.java July 2006
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
 * The <code>ElementList</code> annotation represents a method or
 * field that is a <code>Collection</code> for storing elements. The
 * collection object deserialized is typically of the same type as
 * the field. However, a <code>class</code> attribute can be used to
 * override the field type, however the type must be assignable.
 * <pre>
 * 
 *    &lt;list class="demo.ExampleList"&gt;
 *       &lt;element name="one"/&gt;
 *       &lt;element name="two"/&gt;
 *       &lt;element name="three"/&gt;  
 *    &lt;/list&gt;
 * 
 * </pre>
 * If a <code>class</code> attribute is not provided and the type of
 * the field is abstract, a suitable match is searched for from the
 * collections available from the Java collections framework.
 * 
 * @author Niall Gallagher
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ElementList {
   
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
    * Represents the type of object the element list contains. This
    * type is used to deserialize the XML elements from the list. 
    * The object typically represents the deserialized type, but can
    * represent a subclass of the type deserialized as determined
    * by the <code>class</code> attribute value for the list. 
    * 
    * @return the type of the element deserialized from the XML
    */
   public Class type() default void.class;
   
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
   
   /**
    * Determines whether the element list is inlined with respect
    * to the parent XML element. An inlined element list does not
    * contain an enclosing element. It is simple a sequence of 
    * elements that appear one after another within an element.
    * As such an inline element list must not have a name. 
    *
    * @return this returns true if the element list is inline
    */
   public boolean inline() default false;
}
