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
 * that appears as an XML element. Fields annotated with this can
 * be either primitive or compound, that is, represent an object
 * that can be serialized and deserialized. 
 * <pre>
 * 
 *    &lt;example class="demo.Example"&gt;
 *       &lt;data/&gt;
 *    &lt;example&gt;
 * 
 * </pre>
 * Each element may have any number of attributes and sub-elements
 * representing fields of the compound object it is converted to and
 * from. However, the <code>class</code> attribute is reserved by 
 * the serialization framework to represent the serialized type. 
 * 
 * @author Niall Gallagher
 */ 
@Retention(RetentionPolicy.RUNTIME)
public @interface Element {
   
   /**
    * This represents the name of the XML element. Annotated fields
    * must provide the name of the element they represent so that
    * that can be serialized and deserialized to and from the XML.
    * 
    * @return the name of the XML element this represents
    */
   public String name();
   
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
