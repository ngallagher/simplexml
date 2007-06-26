/*
 * Text.java April 2007
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
 * The <code>Text</code> annotation is used to represent a field or
 * method that appears as text within an XML element. Methods and
 * fields annotated with this must represent primitive values, which
 * means that the type is converted to and from an XML representation
 * using a <code>Transform</code> object. For example, the primitive 
 * types typically annotated could be strings, integers, or dates.  
 * <p>
 * One restriction on this annotation is that it can only appear once 
 * within a schema class, and it can not appear with the another XML 
 * element annotations, such as the <code>Element</code> annotation. 
 * It can however appear with any number of <code>Attribute</code> 
 * annotations.
 * <pre>
 * 
 *    &lt;example one="value" two="value"&gt;
 *       Example text value       
 *    &lt;example&gt;
 * 
 * </pre>
 * Text values are used when an element containing attributes is
 * used to wrap a text value with no child elements. This can be
 * used in place of an element annotation to represent a primitive
 * which is wrapped in a surrounding XML element.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.transform.Transformer
 */ 
@Retention(RetentionPolicy.RUNTIME)
public @interface Text {
   
   /**
    * This is used to provide a default value for the text data if
    * the annotated field or method is null. This ensures the the
    * serialzation process writes the text data with a value even
    * if the value is null, and allows deserialization to determine
    * whether the value within the object was null or not.
    * 
    * @return this returns the default attribute value to use
    */
   public String empty() default "";
   
   /**
    * This is used to determine whether the text is written within 
    * CDATA block or not. If this is set to true then the text is
    * written within a CDATA block, by default the text is output
    * as escaped XML. Typically this is used for large text values.
    * 
    * @return true if the data is to be wrapped in a CDATA block
    */
   public boolean data() default false;
   
   /**
    * Determines whether the text value is required within the XML
    * document. Any field marked as not required mayl not have its
    * value set when the object is deserialized. If an object is to
    * be serialized only a null attribute will not appear in XML.
    * 
    * @return true if the element is required, false otherwise
    */
   public boolean required() default true;   
}
