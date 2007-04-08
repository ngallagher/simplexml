/*
 * MethodPart.java April 2007
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

package simple.xml.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * The <code>MethodPart</code> interface is used to provide a point 
 * of contact with an object. Typically this will be used to get a
 * method from an object which is contains an XML annotation. This
 * provides the type the method is associated with, this type is
 * either the method return type or the single value parameter.
 * 
 * @author Niall Gallagher
 */ 
interface MethodPart {
   
   /**
    * This is the annotation associated with the point of contact.
    * This will be an XML annotation that describes how the contact
    * should be serializaed and deserialized from the object.
    *
    * @return this provides the annotation associated with this
    */
   public Annotation getAnnotation();
   
   /**
    * This will provide the contact type. The contact type is the
    * class that is either the method return type or the single
    * value parameter type associated with the method.
    *
    * @return this returns the type that this contact represents
    */ 
   public Class getType();
   
   /**
    * This is the method for this point of contact. This is what
    * will be invoked by the serialization or deserialization 
    * process when an XML element or attribute is to be used.
    * 
    * @return this returns the method associated with this
    */
   public Method getMethod();
}
