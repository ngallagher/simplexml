/*
 * UnionMapLabel.java March 2011
 *
 * Copyright (C) 2011, Niall Gallagher <niallg@users.sf.net>
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

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.UnionMap;
import org.simpleframework.xml.strategy.Type;

/**
 * The <code>UnionMapLabel</code> acts as an adapter for an internal
 * label. Each annotation within the union can be acquired by type 
 * so that deserialization can dynamically switch the converter used.
 * Each union label can be used in place of any other, this means 
 * that regardless of which union is matched it can be used.
 * <p>
 * Each instance of this <code>Label</code> is given the union and
 * the primary label it represents. This allows the label extract each
 * other label within the union group. The <code>Converter</code> 
 * created by this can therefore acquire any label instance required. 
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.UnionMap
 */
class UnionMapLabel implements Label {
   
   /**
    * This is used to extract the individual unions in the group.
    */
   private final GroupExtractor extractor;
   
   /**
    * This is the contact that this label is associated with.
    */
   private final Contact contact;
   
   /**
    * This is the label that this acts as an adapter to.
    */
   private final Label label;

   /**
    * Constructor for the <code>UnionMapLabel</code> object. This 
    * is given the union this represents as well as the individual
    * element it will act as an adapter for. This allows the union
    * label to acquire any other label within the group.
    * 
    * @param contact this is the contact associated with the union
    * @param union this is the union annotation this represents
    * @param element this is the individual annotation used
    */
   public UnionMapLabel(Contact contact, UnionMap union, ElementMap element) throws Exception {
      this.extractor = new GroupExtractor(contact, union);
      this.label = new ElementMapLabel(contact, element);
      this.contact = contact;
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
      return contact;
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
    * This is used to acquire the <code>Type</code> that the type
    * provided is represented by. Typically this will return the
    * field or method represented by the label. However, in the 
    * case of unions this will provide an override type.
    * 
    * @param type this is the class to acquire the type for
    * 
    * @return this returns the type represented by this class
    */
   public Type getType(Class type) {
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
      Type type = getContact();
      
      if(type == null) {
         throw new UnionException("Union %s was not declared on a field or method", label);
      }
      return new CompositeUnionMap(context, extractor, type);
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
      return extractor.getNames();
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
      return extractor.getNames(context);
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
   public String getName(Context context) throws Exception {
      return label.getName(context);
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
   public String getName() throws Exception {
      return label.getName();
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
}
