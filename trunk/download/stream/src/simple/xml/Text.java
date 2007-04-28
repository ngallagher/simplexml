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

package simple.xml;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

/**
 * The <code>Text</code> annotation is used to represent a field 
 * that appears as text within an XML element. Fields annotated with
 * this must be primitive values. A restriction on this annotation
 * is that it can only appear once within a schema class, and it 
 * can not appear with the <code>Element</code> annotation.
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
 */ 
@Retention(RetentionPolicy.RUNTIME)
public @interface Text {
   
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
