/*
 * Label.java July 2006
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

package simple.xml.load;

/**
 * The <code>Label</code> interface is used to describe an reference to
 * a field annotated with an XML schema annotation. Because each field
 * and annotation is different, there are different ways in which the
 * annotation can be accessed. This provides a uniform means for 
 * accessing the field annotation details and the field properties.
 * <p>
 * This also exposes a <code>Converter</code> object, which is used to
 * convert an XML node into a property that can be assigned to the 
 * annotated field. Each converter returned is specific to the label
 * and knows, based on the annotation, how to serialize the field.
 * 
 * @author Niall Gallagher
 */
interface Label {

   /**
    * This method returns a <code>Converter</code> which can be used to
    * convert an XML node into an object value and vice versa. The 
    * converter requires only the source object in order to perform
    * serialization or deserialization of the provided XML node.
    * 
    * @param root this is the source object for the serialization
    * 
    * @return this returns an object that can be used to convert
    *         XML elements to objects and vice versa
    */
   public Converter getConverter(Source root) throws Exception;

   /**
    * This is used to acquire the contact object for this label. The 
    * contact retrieved can be used to set any object or primitive that
    * has been deserialized, and can also be used to acquire values to
    * be serialized in the case of object persistance. All contacts 
    * that are retrieved from this method will be accessible. 
    * 
    * @return returns the field that this label is representing
    */
   public Contact getContact();

   /**
    * This acts as a convinience method used to determine the type of
    * the field this represents. This is used when an object is written
    * to XML. It determines whether a <code>class</code> attribute
    * is required within the serialized XML element, that is, if the
    * class returned by this is different from the actual value of the
    * object to be serialized then that type needs to be remembered.
    *  
    * @return this returns the type of the field class
    */
   public Class getType();
   
   /**
    * This is used to acquire the name of the XML attribute as taken
    * from the field annotation. Every XML annotation must contain a
    * name, so that it can be identified from the XML source. This
    * allows the class to be used as a schema for the XML document. 
    * 
    * @return returns the name of the annotation for the field
    */
   public String getName();
   
   /**
    * Determines whether the XML attribute or element is required. 
    * This ensures that if an XML element is missing from a document
    * that deserialization can continue. Also, in the process of
    * serialization, if a value is null it does not need to be 
    * written to the resulting XML document.
    * 
    * @return true if the label represents a some required data
    */
   public boolean isRequired();
}
