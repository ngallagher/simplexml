/*
 * ElementInlineList.java July 2006
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
 * The <code>ElementInlineList</code> annotation represents a field 
 * that is a list of elements stored in a <code>Collection</code> 
 * object. The collection object deserialized is typically of the 
 * same type as the field. However, if the type is not instantiable
 * then it is given one from the Java Collections framework.
 * <pre>  
 *    
 *    &lt;element name="one"/&gt;
 *    &lt;element name="two"/&gt;
 *    &lt;element name="three"/&gt;
 *    &lt;element name="four"/&gt; 
 * 
 * </pre>
 * Caution must be taken when using this annotation as the type of
 * list can not be deserialized exactly if the type is not concrete.
 * Thus, if type is importand then the annotated field should be an
 * instantiable type. If not a default type is used if possible. 
 * Also the <code>CycleStrategy</code> can not maintain references
 * to collections that are annotated as inline.
 * 
 * @author Niall Gallagher
 * 
 * @see simple.xml.ElementList
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ElementInlineList {
   
   /**
    * Represents the type of object the element list contains. This
    * type is used to deserialize the XML elements from the list. 
    * The object typically represents the deserialized type, but can
    * represent a subclass of the type deserialized as determined
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
