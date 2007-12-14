/*
 * ElementMap.java August 2007
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
 * The <code>ElementMap</code> annotation represents a method or field
 * that is a <code>Map</code> for storing key value pairs. The map 
 * object deserialized is typically of the same type as the field. 
 * However, a <code>class</code> attribute can be used to override the 
 * field type, however the type must be assignable.
 * <pre>
 * 
 *    &lt;map class="java.util.HashMap"&gt;
 *       &lt;entry key="one"&gt;value one&lt;/entry&gt;
 *       &lt;entry key="two"&gt;value two&lt;/entry&gt;
 *       &lt;entry key="three"&gt;value three&lt;/entry&gt;  
 *    &lt;/map&gt;
 * 
 * </pre>
 * If a <code>class</code> attribute is not provided and the type or
 * the field or method is abstract, a suitable match is searched for 
 * from the maps available from the Java collections framework. This 
 * annotation can support both primitive and composite values and 
 * keys enabling just about any configuration to be used.
 * <pre>
 *
 *    &lt;map class="java.util.HashMap"&gt;
 *       &lt;entry key="1"&gt;
 *          &lt;value&gt;value one&lt;/value&gt;
 *       &lt;/entry&gt;
 *       &lt;entry key="2"&gt;
 *          &lt;value&gt;value two&lt;/value&gt;
 *       &lt;/entry&gt;
 *       &lt;entry key="3"&gt;
 *          &lt;value&gt;value three&lt;/value&gt;
 *       &lt;/entry&gt; 
 *    &lt;/map&gt;
 * 
 * </pre>
 * The above XML is an example of the output for an composite value
 * object. Composite and primitive values can be used without any
 * specified attributes, in such a case names for primitives are the
 * names of the objects they represent. Also, if desired these 
 * default names can be overridden using the provided attributes
 * making the resulting XML entirely configurable.
 * 
 * @author Niall Gallagher
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ElementMap {

   /**
    * This represents the name of the XML element. Annotated fields
    * can optionally provide the name of the element. If no name is
    * provided then the name of the annotated field or method will
    * be used in its place. The name is provided if the field or
    * method name is not suitable as an XML element name. Also, if
    * the list is inline then this must not be specified.
    * 
    * @return the name of the XML element this represents
    */
   public String name() default "";
   
   /**
    * This is used to provide a the name of the entry XML element 
    * that wraps the key and value elements. If specified the entry
    * value specified will be used instead of the default name of 
    * the element. This is used to ensure the resulting XML is 
    * configurable to the requirements of the generated XML. 
    * 
    * @return this returns the entry XML element for each entry
    */
   public String entry() default ""; 

   /**
    * This is used to provide a value XML element for each of the
    * values within the map. This essentially wraps the entity to
    * be serialized such that there is an extra XML element present.
    * This can be used to override the default names of primitive
    * values, however it can also be used to wrap composite values. 
    * 
    * @return this returns the value XML element for each value
    */
   public String value() default "";

   /**
    * This is used to provide a key XML element for each of the
    * keys within the map. This essentially wraps the entity to
    * be serialized such that there is an extra XML element present.
    * This can be used to override the default names of primitive
    * keys, however it can also be used to wrap composite keys. 
    * 
    * @return this returns the key XML element for each key
    */
   public String key() default "";
   
   /**
    * Represents the type of key the element map contains. This
    * type is used to deserialize the XML entry key from the map. 
    * The object typically represents the deserialized type, but can
    * represent a subclass of the type deserialized as determined
    * by the <code>class</code> attribute value for the map. If 
    * this is not specified then the type can be determined from the
    * generic parameter of the annotated <code>Map</code> object.
    * 
    * @return the type of the entry key deserialized from the XML
    */
   public Class keyType() default void.class;
   
   /**
    * Represents the type of value the element map contains. This
    * type is used to deserialize the XML entry value from the map. 
    * The object typically represents the deserialized type, but can
    * represent a subclass of the type deserialized as determined
    * by the <code>class</code> attribute value for the map. If 
    * this is not specified then the type can be determined from the
    * generic parameter of the annotated <code>Map</code> object.
    * 
    * @return the type of the entry value deserialized from the XML
    */
   public Class valueType() default void.class;

   /**
    * Represents whether the key value is to be an attribute or an
    * element. This allows the key to be embedded within the entry
    * XML element allowing for a more compact representation. Only
    * primitive key objects can be represented as an attribute. For
    * example a <code>java.util.Date</code> or a string could be
    * represented as an attribute key for the generated XML. 
    *  
    * @return true if the key is to be inlined as an attribute
    */
   public boolean attribute() default false;
   
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
