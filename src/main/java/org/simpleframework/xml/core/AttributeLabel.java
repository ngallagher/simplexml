/*
 * AttributeLabel.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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
import java.util.Collections;
import java.util.Set;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

/**
 * The <code>AttributeLabel</code> object is used convert any value
 * retrieved from an XML attribute to a primitive object. This is
 * also used to convert from a primitive object to an XML attribute
 * using the <code>String.valueOf</code> method. 
 * 
 * @author Niall Gallagher
 */
class AttributeLabel implements Label {
   
   /**
    * This is the decorator that is associated with the attribute.
    */
   private Decorator decorator;
   
   /**
    * This contains the details of the annotated contact object.
    */
   private Introspector detail;

   /**
    * Represents the annotation used to label the field.
    */
   private Attribute label;
   
   /**
    * This is the type that the field object references. 
    */
   private Class type;
   
   /**
    * This is the name of the element for this label instance.
    */
   private String name;
   
   /**
    * This is the default value to use if the real value is null.
    */
   private String empty;
   
   /**
    * Constructor for the <code>AttributeLabel</code> object. This 
    * is used to create a label that can convert from an object to an
    * XML attribute and vice versa. This requires the annotation and
    * contact extracted from the XML schema class.
    * 
    * @param contact this is the field from the XML schema class
    * @param label represents the annotation for the field
    */
   public AttributeLabel(Contact contact, Attribute label) {
      this.detail = new Introspector(contact, this);
      this.decorator = new Qualifier(contact);
      this.type = contact.getType();
      this.empty = label.empty();
      this.name = label.name();      
      this.label = label; 
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
   public Type getType(Class type){
      return getContact();
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
    * This returns a <code>Set</code> of elements in a union. This
    * will typically be an empty set, and is never null. If this is
    * a label union then this will return the name of each label
    * within the group. Providing the labels for a union allows the
    * serialization process to determine the associated labels.
    * 
    * @return this returns the names of each of the elements
    */
   public Set<String> getUnion() throws Exception {
      return Collections.emptySet();
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
      return Collections.emptySet();
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
      return decorator;
   }
   
   /**
    * Creates a <code>Converter</code> that can convert an attribute
    * to a primitive object. This requires the context object used
    * for the current instance of XML serialization being performed.
    * 
    * @param context this is context object used for serialization
    */
   public Converter getConverter(Context context) throws Exception {
      String ignore = getEmpty(context);
      Type type = getContact();
      
      if(!context.isPrimitive(type)) {
         throw new AttributeException("Cannot use %s to represent %s", label, type);
      }
      return new Primitive(context, type, ignore);
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
   public String getEmpty(Context context) {
      if(detail.isEmpty(empty)) {
         return null;
      }
      return empty;
   }
   
   /**
    * This is used to acquire the name of the element or attribute
    * that is used by the class schema. The name is determined by
    * checking for an override within the annotation. If it contains
    * a name then that is used, if however the annotation does not
    * specify a name the the field or method name is used instead.
    *
    * @param context the context object used to style the name
    * 
    * @return returns the name that is used for the XML property
    */
   public String getName(Context context) throws Exception {
      Style style = context.getStyle();
      String name = detail.getName();
      
      return style.getAttribute(name);
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
   public String getName() throws Exception {
      return detail.getName();
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
      return label;
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
      return detail.getPath();
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
   public String getOverride(){
      return name;
   }
   
   /**
    * This is used to acquire the contact object for this label. The 
    * contact retrieved can be used to set any object or primitive that
    * has been deserialized, and can also be used to acquire values to
    * be serialized in the case of object persistance. All contacts
    * that are retrieved from this method will be accessible. 
    * 
    * @return returns the contact that this label is representing
    */   
   public Contact getContact() {
      return detail.getContact();
   }
   
   /**
    * This acts as a convenience method used to determine the type of
    * the contact this represents. This will be a primitive type of a
    * primitive type from the <code>java.lang</code> primitives.
    * 
    * @return this returns the type of the contact class
    */  
   public Class getType() {
      return type;
   }
   
   /**
    * This is typically used to acquire the entry value as acquired
    * from the annotation. However given that the annotation this
    * represents does not have a entry attribute this will always
    * provide a null value for the entry string.
    * 
    * @return this will always return null for the entry value 
    */
   public String getEntry() {
      return null;
   }
   
   /**
    * This is used to acquire the dependent type for this label. 
    * This returns null as there are no dependents to the attribute
    * annotation as it can only hold primitives with no dependents.
    * 
    * @return this is used to return the dependent type or null
    */
   public Type getDependent() {
      return null;
   }
   
   /**
    * This method is used to determine if the label represents an
    * attribute. This is used to style the name so that elements
    * are styled as elements and attributes are styled as required.
    * 
    * @return this is used to determine if this is an attribute
    */
   public boolean isAttribute() {
      return true;
   }
   
   /**
    * This is used to determine whether the attribute is required. 
    * This ensures that if an attribute is missing from a document
    * that deserialization can continue. Also, in the process of
    * serialization, if a value is null it does not need to be 
    * written to the resulting XML document.
    * 
    * @return true if the label represents a some required data
    */  
   public boolean isRequired() {
      return label.required();
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
      return false;
   }
   
   /**
    * Because the attribute can contain only simple text values it
    * is never required to specified as anything other than text.
    * Therefore this will always return false as CDATA does not
    * apply to the attribute values.
    *
    * @return this will always return false for XML attributes
    */
   public boolean isData() {
      return false;
   }
   
   /**
    * This method is used by the deserialization process to check
    * to see if an annotation is inline or not. If an annotation
    * represents an inline XML entity then the deserialization
    * and serialization process ignores overrides and special 
    * attributes. By default all attributes are not inline items.
    * 
    * @return this always returns false for attribute labels
    */
   public boolean isInline() {
      return false;
   }
   
   /**
    * This is used to describe the annotation and method or field
    * that this label represents. This is used to provide error
    * messages that can be used to debug issues that occur when
    * processing a method. This will provide enough information
    * such that the problem can be isolated correctly. 
    * 
    * @return this returns a string representation of the label
    */
   public String toString() {
      return detail.toString();
   }
}
