/*
 * Transform.java May 2007
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

package org.simpleframework.xml.transform;

/**
 * A <code>Transform</code> represents a an object used to transform
 * an object to and from a string value. This is typically used when
 * either an <code>Attribute</code> or <code>Element</code> annotation
 * is used to mark the field of a type that does not contain any of
 * the XML annotations, and so does not represent an XML structure.
 * For example take the following annotation.
 * <pre>
 * 
 *    &#64;Text
 *    private Date date;
 *    
 * </pre> 
 * The above annotation marks an object from the Java class libraries
 * which does not contain any XML annotations. During serialization
 * and deserialization of such types a transform is used to process
 * the object such that it can be written and read to and from XML.
 * 
 * @author Niall Gallagher
 */
interface Transform<T> {
   
   /**
    * This method is used to convert the string value given to an
    * appropriate representation. This is used when an object is
    * being deserialized from the XML document and the value for
    * the string representation is required.
    * 
    * @param value this is the string representation of the value
    * 
    * @return this returns an appropriate instanced to be used
    */
    public T read(String value) throws Exception;
    
    /**
     * This method is used to convert the provided value into an XML
     * usable format. This is used in the serialization process when
     * there is a need to convert a field value in to a string so 
     * that that value can be written as a valid XML entity.
     * 
     * @param value this is the value to be converted to a string
     * 
     * @return this is the string representation of the given value
     */
    public String write(T value) throws Exception;
}
