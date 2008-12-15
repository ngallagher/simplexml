/*
 * Schema.java July 2006
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

package org.simpleframework.xml.core;

import org.simpleframework.xml.Version;

/**
 * The <code>Schema</code> object is used to track which fields within
 * an object have been visited by a converter. This object is nessecary
 * for processing <code>Composite</code> objects. In particular it is
 * nessecary to keep track of which required nodes have been visited 
 * and which have not, if a required not has not been visited then the
 * XML source does not match the XML class schema and serialization
 * must fail before processing any further. 
 * 
 * @author Niall Gallagher
 */ 
class Schema {
   
   /**
    * This is the decorator associated with this schema object.
    */
   private Decorator decorator;

   /**
    * Contains a map of all attributes present within the schema.
    */
   private LabelMap attributes;
   
   /**
    * Contains a map of all elements present within the schema.
    */
   private LabelMap elements;
   
   /**
    * This is the version annotation for the XML class schema.
    */
   private Version revision;
   
   /**
    * This is the pointer to the schema class replace method.
    */
   private Caller caller;
   
   /**
    * This is the version label used to read the version attribute.
    */
   private Label version;
   
   /**
    * This is used to represent a text value within the schema.
    */
   private Label text;
   
   /**
    * This is used to specify whether the type is a primitive class.
    */
   private boolean primitive;

   /**
    * Constructor for the <code>Schema</code> object. This is used 
    * to wrap the element and attribute XML annotations scanned from
    * a class schema. The schema tracks all fields visited so that
    * a converter can determine if all fields have been serialized.
    * 
    * @param schema this contains all labels scanned from the class
    * @param context this is the context object for serialization
    */
   public Schema(Scanner schema, Context context) throws Exception {   
      this.attributes = schema.getAttributes(context);
      this.elements = schema.getElements(context);
      this.caller = schema.getCaller(context);
      this.revision = schema.getRevision();
      this.decorator = schema.getDecorator();
      this.primitive = schema.isPrimitive();
      this.version = schema.getVersion();
      this.text = schema.getText();
   }
   
   /**
    * This is used to determine whether the scanned class represents
    * a primitive type. A primitive type is a type that contains no
    * XML annotations and so cannot be serialized with an XML form.
    * Instead primitives a serialized using transformations.
    * 
    * @return this returns true if no XML annotations were found
    */
   public boolean isPrimitive() {
      return primitive;
   }
   
   /**
    * This returns the <code>Label</code> that represents the version
    * annotation for the scanned class. Only a single version can
    * exist within the class if more than one exists an exception is
    * thrown. This will read only floating point types such as double.
    * 
    * @return this returns the label used for reading the version
    */
   public Label getVersion() {
      return version;
   }
   
   /**
    * This is the <code>Version</code> for the scanned class. It 
    * allows the deserialization process to be configured such that
    * if the version is different from the schema class none of
    * the fields and methods are required and unmatched elements
    * and attributes will be ignored.
    * 
    * @return this returns the version of the class that is scanned
    */
   public Version getRevision() {
      return revision;
   }
   
   /**
    * This is used to acquire the <code>Decorator</code> for this.
    * A decorator is an object that adds various details to the
    * node without changing the overall structure of the node. For
    * example comments and namespaces can be added to the node with
    * a decorator as they do not affect the deserialization.
    * 
    * @return this returns the decorator associated with this
    */
   public Decorator getDecorator() {
      return decorator;
   }
   
   /**
    * This is used to acquire the <code>Caller</code> object. This
    * is used to call the callback methods within the object. If the
    * object contains no callback methods then this will return an
    * object that does not invoke any methods that are invoked. 
    * 
    * @return this returns the caller for the specified type
    */
   public Caller getCaller() {
      return caller;
   }
   
   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields marked as XML attributes. Labels contained within
    * this map are used to convert primitive types only.
    * 
    * @return map with the details extracted from the schema class
    */ 
   public LabelMap getAttributes() {
      return attributes;
   }   
   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields marked as XML elements. The annotations that are
    * considered elements are the <code>ElementList</code> and the
    * <code>Element</code> annotations. 
    * 
    * @return a map containing the details for XML elements
    */
   public LabelMap getElements() {
      return elements;
   }
   
   /**
    * This returns the <code>Label</code> that represents the text
    * annotation for the scanned class. Only a single text annotation
    * can be used per class, so this returns only a single label
    * rather than a <code>LabelMap</code> object. Also if this is
    * not null then the elements label map will be empty.
    * 
    * @return this returns the text label for the scanned class
    */
   public Label getText() {
      return text;
   }
}
