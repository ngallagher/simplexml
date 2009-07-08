/*
 * Parameter.java July 2009
 *
 * Copyright (C) 2009, Niall Gallagher <niallg@users.sf.net>
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

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;

/**
 * The <code>Parameter</code> is used to represent a constructor 
 * parameter. It contains the XML annotation used on the parameter
 * as well as the name of the parameter and its position index.
 * A parameter is used to validate against the annotated methods 
 * and fields and also to determine the deserialized values that
 * should be injected in to the constructor to instantiate it.
 * 
 * @author Niall Gallagher
 */
interface Parameter {
   
   /**
    * This is used to acquire the name of the parameter that this
    * represents. The name is determined using annotation and 
    * the name attribute of that annotation, if one is provided.
    * 
    * @return this returns the name of the annotated parameter
    */
   public String getName() throws Exception;
   
   /**
    * This is used to acquire the annotation that is used for the
    * parameter. The annotation provided will be an XML annotation
    * such as the <code>Element</code> or <code>Attribute</code>
    * annotation.
    * 
    * @return this returns the annotation used on the parameter
    */
   public Annotation getAnnotation();

   /**
    * This is used to acquire the annotated type class. The class
    * is the type that is to be deserialized from the XML. This
    * is used to validate against annotated fields and methods.
    * 
    * @return this returns the type used for the parameter
    */
   public Class getType();
   
   /**
    * This returns the index position of the parameter in the
    * constructor. This is used to determine the order of values
    * that are to be injected in to the constructor.
    * 
    * @return this returns the index for the parameter
    */
   public int getIndex();
}
