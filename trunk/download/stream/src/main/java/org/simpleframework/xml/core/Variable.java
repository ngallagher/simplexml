/*
 * Variable.java December 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Position;
import org.simpleframework.xml.stream.Style;

/**
 * The <code>Variable</code> object is used to represent a variable 
 * for a method or field of a deserialized object. It has the value
 * for the field or method as well as the details from the annotation.
 * This is used by the <code>Collector</code> to populate an object
 * once all the values for that object have been collected. 
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Collector
 */
class Variable implements Label {
   
   /**
    * This is the object that has been deserialized from the XML.
    */
   private final Object value;
   
   /**
    * This contains the details for the annotated field or method.
    */
   private final Label label;
   
   /**
    * Constructor for the <code>Variable</code> object. This is used
    * to create an object that holds a deserialized value, as well as
    * the details of the annotated method or field it is to be set to.
    * This allows the value to be repeatedly deserialized.
    * 
    * @param label this is the label for the field or method used
    * @param value the deserialized object for the method or field
    */
   public Variable(Label label, Object value) {
      this.label = label;
      this.value = value;
   }
   
   /**
    * This is used to acquire the <code>Label</code> that the type
    * provided is represented by. Typically this will return the
    * same instance. However, in the case of unions this will
    * look for an individual label to match the type provided.
    * 
    * @param type this is the type to acquire the label for
    * 
    * @return this returns the label represented by this type
    */
   public Label getLabel(Class type) {
      return this;
   }
   
   /**
    * This is used to acquire the <code>Type</code> that the type
    * provided is represented by. Typically this will return the
    * field or method represented by the label. However, in the 
    * case of unions this will provide an override type.
    * 
    * @param type this is the class to acquire the type for
    * 
    * @return this returns the type represented by this class
    */
   public Type getType(Class type) throws Exception {
      return label.getType(type);
   }
   
   /**
    * This returns a <code>Set</code> of elements in a union. This
    * will typically be an empty set, and is never null. If this is
    * a label union then this will return the name of each label
    * within the group. Providing the labels for a union allows the
    * serialization process to determine the associated labels.
    * 
    * @return this returns the names of each of the elements
    */
   public Set<String> getUnion() throws Exception {
      return label.getUnion();
   }
   
   /**
    * This returns a <code>Set</code> of elements in a union. This
    * will typically be an empty set, and is never null. If this is
    * a label union then this will return the name of each label
    * within the group. Providing the labels for a union allows the
    * serialization process to determine the associated labels.
    *
    * @param context this is used to style the element names
    * 
    * @return this returns the names of each of the elements
    */
   public Set<String> getUnion(Context context) throws Exception {
      return label.getUnion(context);
   }
   
   /**
    * This is used to acquire the value associated with the variable.
    * Once fully deserialized the value is used to set the value for 
    * a field or method of the object. This value can be repeatedly
    * read if the <code>Converter</code> is acquired a second time.
    * 
    * @return this returns the value that has been deserialized
    */
   public Object getValue() {
      return value;
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
   public Decorator getDecorator() throws Exception {
      return label.getDecorator();
   }
   
   /**
    * This method returns a <code>Converter</code> which can be used to
    * convert an XML node into an object value and vice versa. The 
    * converter requires only the context object in order to perform
    * serialization or deserialization of the provided XML node.
    * 
    * @param context this is the context object for the serialization
    * 
    * @return this returns an object that is used for conversion
    */
   public Converter getConverter(Context context) throws Exception {
      Converter reader = label.getConverter(context);
      
      if(reader instanceof Adapter) {
         return reader;
      }
      return new Adapter(reader, value);
   }
   
   /**
    * This is used to acquire the name of the element or attribute
    * that is used by the class schema. The name is determined by
    * checking for an override within the annotation. If it contains
    * a name then that is used, if however the annotation does not
    * specify a name the the field or method name is used instead.
    * 
    * @param context this is the context used to style the name
    * 
    * @return returns the name that is used for the XML property
    */
   public String getName(Context context) throws Exception{
      String name = label.getName(context);
      Style style = context.getStyle();
      
      return style.getElement(name);
   }
   
   /**
    * This is used to provide a configured empty value used when the
    * annotated value is null. This ensures that XML can be created
    * with required details regardless of whether values are null or
    * not. It also provides a means for sensible default values.
    *
    * @param context this is the context object for the serialization
    * 
    * @return this returns the string to use for default values
    */
   public Object getEmpty(Context context) throws Exception {
      return label.getEmpty(context);
   }
   
   /**
    * This is used to acquire the contact object for this label. The 
    * contact retrieved can be used to set any object or primitive that
    * has been deserialized, and can also be used to acquire values to
    * be serialized in the case of object persistence. All contacts 
    * that are retrieved from this method will be accessible. 
    * 
    * @return returns the field that this label is representing
    */
   public Contact getContact() {
      return label.getContact();
   }
   
   /**
    * This returns the dependent type for the annotation. This type
    * is the type other than the annotated field or method type that
    * the label depends on. For the <code>ElementList</code> and 
    * the <code>ElementArray</code> this is the component type that
    * is deserialized individually and inserted into the container. 
    * 
    * @return this is the type that the annotation depends on
    */
   public Type getDependent() throws Exception {
      return label.getDependent();
   }
   
   /**
    * This is used to either provide the entry value provided within
    * the annotation or compute a entry value. If the entry string
    * is not provided the the entry value is calculated as the type
    * of primitive the object is as a simplified class name.
    * 
    * @return this returns the name of the XML entry element used 
    */
   public String getEntry() throws Exception {
      return label.getEntry();
   }

   /**
    * This is used to acquire the name of the element or attribute
    * that is used by the class schema. The name is determined by
    * checking for an override within the annotation. If it contains
    * a name then that is used, if however the annotation does not
    * specify a name the the field or method name is used instead.
    * 
    * @return returns the name that is used for the XML property
    */
   public String getName() throws Exception{
      return label.getName();
   }
   
   /**
    * This acquires the annotation associated with this label. This
    * is typically the annotation acquired from the field or method.
    * However, in the case of unions this will return the actual
    * annotation within the union group that this represents.
    * 
    * @return this returns the annotation that this represents
    */
   public Annotation getAnnotation() {
      return label.getAnnotation();
   }
   
   /**
    * This method is used to return the path where this is located.
    * The path is an XPath expression that allows serialization to
    * locate the XML entity within the document. If there is no
    * path then the XML entity is written within the current context.
    * An empty path is identified as a null value.
    * 
    * @return the XPath expression identifying the location
    */
   public String getPath() {
      return label.getPath();
   }
   
   /**
    * This is used to acquire the name of the element or attribute
    * as taken from the annotation. If the element or attribute
    * explicitly specifies a name then that name is used for the
    * XML element or attribute used. If however no overriding name
    * is provided then the method or field is used for the name. 
    * 
    * @return returns the name of the annotation for the contact
    */
   public String getOverride() {
      return label.getOverride();
   }
   
   /**
    * This acts as a convenience method used to determine the type of
    * the field this represents. This is used when an object is written
    * to XML. It determines whether a <code>class</code> attribute
    * is required within the serialized XML element, that is, if the
    * class returned by this is different from the actual value of the
    * object to be serialized then that type needs to be remembered.
    *  
    * @return this returns the type of the field class
    */
   public Class getType() {
      return label.getType();
   }
   
   /**
    * This is used to determine whether the annotation requires it
    * and its children to be written as a CDATA block. This is done
    * when a primitive or other such element requires a text value
    * and that value needs to be encapsulated within a CDATA block.
    * 
    * @return this returns true if the element requires CDATA
    */
   public boolean isData() {
      return label.isData();
   }
   
   /**
    * This is used to determine whether the label represents an
    * inline XML entity. The <code>ElementList</code> annotation
    * and the <code>Text</code> annotation represent inline 
    * items. This means that they contain no containing element
    * and so can not specify overrides or special attributes.
    * 
    * @return this returns true if the annotation is inline
    */
   public boolean isInline() {
      return label.isInline();
   }
   
   /**
    * This method is used to determine if the label represents an
    * attribute. This is used to style the name so that elements
    * are styled as elements and attributes are styled as required.
    * 
    * @return this is used to determine if this is an attribute
    */
   public boolean isAttribute() {
      return label.isAttribute();
   }
   
   /**
    * This is used to determine if the label is a collection. If the
    * label represents a collection then any original assignment to
    * the field or method can be written to without the need to 
    * create a new collection. This allows obscure collections to be
    * used and also allows initial entries to be maintained.
    * 
    * @return true if the label represents a collection value
    */
   public boolean isCollection() {
      return label.isCollection();
   }
   
   /**
    * Determines whether the XML attribute or element is required. 
    * This ensures that if an XML element is missing from a document
    * that deserialization can continue. Also, in the process of
    * serialization, if a value is null it does not need to be 
    * written to the resulting XML document.
    * 
    * @return true if the label represents a some required data
    */
   public boolean isRequired() {
      return label.isRequired();
   }
   
   /**
    * This is used to describe the annotation and method or field
    * that this label represents. This is used to provide error
    * messages that can be used to debug issues that occur when
    * processing a method. This should provide enough information
    * such that the problem can be isolated correctly. 
    * 
    * @return this returns a string representation of the label
    */
   public String toString() {
      return label.toString();
   }
   
   /**
    * The <code>Adapter</code> object is used to call the repeater
    * with the original deserialized object. Using this object the
    * converter interface can be used to perform repeat reads for
    * the object. This must be given a <code>Repeater</code> in 
    * order to invoke the repeat read method.
    * 
    * @author Niall Gallagher
    */
   private class Adapter implements Repeater {
      
      /**
       * This is the converter object used to perform a repeat read.
       */
      private final Converter reader;
      
      /**
       * This is the originally deserialized object value to use.
       */
      private final Object value;
      
      /**
       * Constructor for the <code>Adapter</code> object. This will
       * create an adapter between the converter an repeater such
       * that the reads will read from the XML to the original.
       * 
       * @param reader this is the converter object to be used      
       * @param value this is the originally deserialized object
       */
      public Adapter(Converter reader, Object value) {
         this.reader = reader;
         this.value = value;
      }
      
      /**
       * This <code>read</code> method will perform a read using the
       * provided object with the repeater. Reading with this method
       * ensures that any additional XML elements within the source
       * will be added to the value.
       * 
       *  @param node this is the node that contains the extra data
       *  
       *  @return this will return the original deserialized object
       */
      public Object read(InputNode node)throws Exception {
         return read(node, value);
      }
      
      /**
       * This <code>read</code> method will perform a read using the
       * provided object with the repeater. Reading with this method
       * ensures that any additional XML elements within the source
       * will be added to the value.
       * 
       *  @param node this is the node that contains the extra data
       *  
       *  @return this will return the original deserialized object
       */
      public Object read(InputNode node, Object value) throws Exception {
         Position line = node.getPosition();
         String name = node.getName();         
         
         if(reader instanceof Repeater) {
            Repeater repeat = (Repeater) reader;
            
            return repeat.read(node, value);
         }
         throw new PersistenceException("Element '%s' declared twice at %s", name, line);
      }
      
      /**
       * This <code>read</code> method will perform a read using the
       * provided object with the repeater. Reading with this method
       * ensures that any additional XML elements within the source
       * will be added to the value.
       * 
       *  @param node this is the node that contains the extra data
       *  
       *  @return this will return the original deserialized object
       */
      public boolean validate(InputNode node) throws Exception {
         Position line = node.getPosition();
         String name = node.getName();         
         
         if(reader instanceof Repeater) {
            Repeater repeat = (Repeater) reader;
            
            return repeat.validate(node);
         }
         throw new PersistenceException("Element '%s' declared twice at %s", name, line);
      }
      
      /**
       * This <code>write</code> method acts like any other write
       * in that it passes on the node and source object to write.
       * Typically this will not be used as the repeater object is
       * used for repeat reads of scattered XML elements.
       * 
       * @param node this is the node to write the data to
       * @param value this is the source object to be written
       */
      public void write(OutputNode node, Object value) throws Exception {
         write(node, value);
      }
   }
}
