/*
 * Contract.java April 2007
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

package org.simpleframework.xml.graph;

/**
 * The <code>Contract</code> object is used to expose the attribute
 * names used by the cycle strategy. This ensures that reading and
 * writing of the XML document is done in a consistent manner. Each
 * attribute is used to mark special meta-data for the object graph. 
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.graph.CycleStrategy
 */
class Contract {
             
   /**
    * This is used to specify the length of array instances.
    */
   private String length;
   
   /**
    * This is the label used to mark the type of an object.
    */
   private String label;
   
   /**
    * This is the attribute used to mark the identity of an object.
    */
   private String mark;
   
   /**
    * Thsi is the attribute used to refer to an existing instance.
    */
   private String refer;
   
   /**
    * Constructor for the <code>Syntax</code> object. This is used
    * to expose the attribute names used by the strategy. All the
    * names can be acquired and shared by the read and write graph
    * objects, which ensures consistency between the two objects.
    * 
    * @param mark this is used to mark the identity of an object
    * @param refer this is used to refer to an existing object
    * @param label this is used to specify the class for the field
    * @param length this is the length attribute used for arrays
    */   
   public Contract(String mark, String refer, String label, String length){  
      this.length = length;
      this.label = label;
      this.refer = refer;
      this.mark = mark;
   }
   
   /**
    * This is returns the attribute used to store information about
    * the type to the XML document. This attribute name is used to 
    * add data to XML elements to enable the deserialization process
    * to know the exact instance to use when creating a type.
    * 
    * @return the name of the attribute used to store the type
    */
   public String getLabel() {
      return label;
   }
   
   /**
    * This returns the attribute used to store references within the
    * serialized XML document. The reference attribute is added to
    * the serialized XML element so that cycles in the object graph 
    * can be recreated. This is an optional attribute.
    * 
    * @return this returns the name of the reference attribute
    */
   public String getReference() {
      return refer;
   }
   
   /**
    * This returns the attribute used to store the identities of all
    * objects serialized to the XML document. The identity attribute
    * stores a unique identifiers, which enables this strategy to
    * determine an objects identity within the serialized XML.
    * 
    * @return this returns the name of the identity attribute used
    */
   public String getIdentity() {
      return mark;
   }
   
   /**
    * This returns the attribute used to store the array length in
    * the serialized XML document. The array length is required so
    * that the deserialization process knows how to construct the
    * array before any of the array elements are deserialized.
    * 
    * @return this returns the name of the array length attribute
    */
   public String getLength() {
      return length;
   } 
} 
