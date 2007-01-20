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
 * The <code>ElementList</code> annotation represents a field that is
 * a list of elements stored in a <code>Collection</code> object. The
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
    * must provide the name of the element they represent so that
    * that can be serialized and deserialized to and from the XML.
    * 
    * @return the name of the XML element this represents
    */
   public String name();

   /**
    * Represents the type of object the element list contains. This
    * type is used to deserialize the XML elements from the list. 
    * The object typically represents the deserialized type, but can
    * represent a super class of the type deserialized as determined
    * by the <code>class</code> attribute value for the list. 
    * 
    * @return the type of the element deserialized from the XML
    */
   public Class type();
   
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
