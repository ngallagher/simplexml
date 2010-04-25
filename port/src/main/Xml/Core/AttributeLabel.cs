#region License
//
// AttributeLabel.cs July 2006
//
// Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied. See the License for the specific language governing
// permissions and limitations under the License.
//
#endregion
#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>AttributeLabel</c> object is used convert any value
   /// retrieved from an XML attribute to a primitive object. This is
   /// also used to convert from a primitive object to an XML attribute
   /// using the <c>String.valueOf</c> method.
   /// </summary>
   class AttributeLabel : Label {
      /// <summary>
      /// This is the decorator that is associated with the attribute.
      /// </summary>
      private Decorator decorator;
      /// <summary>
      /// This contains the details of the annotated contact object.
      /// </summary>
      private Signature detail;
      /// <summary>
      /// Represents the annotation used to label the field.
      /// </summary>
      private Attribute label;
      /// <summary>
      /// This is the type that the field object references.
      /// </summary>
      private Class type;
      /// <summary>
      /// This is the name of the element for this label instance.
      /// </summary>
      private String name;
      /// <summary>
      /// This is the default value to use if the real value is null.
      /// </summary>
      private String empty;
      /// <summary>
      /// Constructor for the <c>AttributeLabel</c> object. This
      /// is used to create a label that can convert from an object to an
      /// XML attribute and vice versa. This requires the annotation and
      /// contact extracted from the XML schema class.
      /// </summary>
      /// <param name="contact">
      /// this is the field from the XML schema class
      /// </param>
      /// <param name="label">
      /// represents the annotation for the field
      /// </param>
      public AttributeLabel(Contact contact, Attribute label) {
         this.detail = new Signature(contact, this);
         this.decorator = new Qualifier(contact);
         this.type = contact.Type;
         this.empty = label.empty();
         this.name = label.name();
         this.label = label;
      }
      /// <summary>
      /// This is used to acquire the <c>Decorator</c> for this.
      /// A decorator is an object that adds various details to the
      /// node without changing the overall structure of the node. For
      /// example comments and namespaces can be added to the node with
      /// a decorator as they do not affect the deserialization.
      /// </summary>
      /// <returns>
      /// this returns the decorator associated with this
      /// </returns>
      public Decorator Decorator {
         get {
            return decorator;
         }
      }
      //public Decorator GetDecorator() {
      //   return decorator;
      //}
      /// Creates a <c>Converter</c> that can convert an attribute
      /// to a primitive object. This requires the context object used
      /// for the current instance of XML serialization being performed.
      /// </summary>
      /// <param name="context">
      /// this is context object used for serialization
      /// </param>
      public Converter GetConverter(Context context) {
         String ignore = GetEmpty(context);
         Type type = Contact;
         if(!context.isPrimitive(type)) {
            throw new AttributeException("Cannot use %s to represent %s", label, type);
         }
         return new Primitive(context, type, ignore);
      }
      /// <summary>
      /// This is used to provide a configured empty value used when the
      /// annotated value is null. This ensures that XML can be created
      /// with required details regardless of whether values are null or
      /// not. It also provides a means for sensible default values.
      /// </summary>
      /// <param name="context">
      /// this is the context object for the serialization
      /// </param>
      /// <returns>
      /// this returns the string to use for default values
      /// </returns>
      public String GetEmpty(Context context) {
         if(detail.IsEmpty(empty)) {
            return null;
         }
         return empty;
      }
      /// <summary>
      /// This is used to acquire the name of the element or attribute
      /// that is used by the class schema. The name is determined by
      /// checking for an override within the annotation. If it contains
      /// a name then that is used, if however the annotation does not
      /// specify a name the the field or method name is used instead.
      /// </summary>
      /// <param name="context">
      /// the context object used to style the name
      /// </param>
      /// <returns>
      /// returns the name that is used for the XML property
      /// </returns>
      public String GetName(Context context) {
         Style style = context.getStyle();
         String name = detail.GetName();
         return style.getAttribute(name);
      }
      /// <summary>
      /// This is used to acquire the name of the element or attribute
      /// that is used by the class schema. The name is determined by
      /// checking for an override within the annotation. If it contains
      /// a name then that is used, if however the annotation does not
      /// specify a name the the field or method name is used instead.
      /// </summary>
      /// <returns>
      /// returns the name that is used for the XML property
      /// </returns>
      public String Name {
         get {
            return detail.GetName();
         }
      }
      //public String GetName() {
      //   return detail.GetName();
      //}
      /// This is used to acquire the name of the element or attribute
      /// as taken from the annotation. If the element or attribute
      /// explicitly specifies a name then that name is used for the
      /// XML element or attribute used. If however no overriding name
      /// is provided then the method or field is used for the name.
      /// </summary>
      /// <returns>
      /// returns the name of the annotation for the contact
      /// </returns>
      public String Override {
         get {
            return name;
         }
      }
      //public String GetOverride() {
      //   return name;
      //}
      /// This is used to acquire the contact object for this label. The
      /// contact retrieved can be used to set any object or primitive that
      /// has been deserialized, and can also be used to acquire values to
      /// be serialized in the case of object persistance. All contacts
      /// that are retrieved from this method will be accessible.
      /// </summary>
      /// <returns>
      /// returns the contact that this label is representing
      /// </returns>
      public Contact Contact {
         get {
            return detail.Contact;
         }
      }
      //public Contact GetContact() {
      //   return detail.Contact;
      //}
      /// This acts as a convenience method used to determine the type of
      /// the contact this represents. This will be a primitive type of a
      /// primitive type from the <c>java.lang</c> primitives.
      /// </summary>
      /// <returns>
      /// this returns the type of the contact class
      /// </returns>
      public Class Type {
         get {
            return type;
         }
      }
      //public Class GetType() {
      //   return type;
      //}
      /// This is typically used to acquire the entry value as acquired
      /// from the annotation. However given that the annotation this
      /// represents does not have a entry attribute this will always
      /// provide a null value for the entry string.
      /// </summary>
      /// <returns>
      /// this will always return null for the entry value
      /// </returns>
      public String Entry {
         get {
            return null;
         }
      }
      //public String GetEntry() {
      //   return null;
      //}
      /// This is used to acquire the dependent type for this label.
      /// This returns null as there are no dependents to the attribute
      /// annotation as it can only hold primitives with no dependents.
      /// </summary>
      /// <returns>
      /// this is used to return the dependent type or null
      /// </returns>
      public Type Dependent {
         get {
            return null;
         }
      }
      //public Type GetDependent() {
      //   return null;
      //}
      /// This method is used to determine if the label represents an
      /// attribute. This is used to style the name so that elements
      /// are styled as elements and attributes are styled as required.
      /// </summary>
      /// <returns>
      /// this is used to determine if this is an attribute
      /// </returns>
      public bool IsAttribute() {
         return true;
      }
      /// <summary>
      /// This is used to determine whether the attribute is required.
      /// This ensures that if an attribute is missing from a document
      /// that deserialization can continue. Also, in the process of
      /// serialization, if a value is null it does not need to be
      /// written to the resulting XML document.
      /// </summary>
      /// <returns>
      /// true if the label represents a some required data
      /// </returns>
      public bool IsRequired() {
         return label.required();
      }
      /// <summary>
      /// This is used to determine if the label is a collection. If the
      /// label represents a collection then any original assignment to
      /// the field or method can be written to without the need to
      /// create a new collection. This allows obscure collections to be
      /// used and also allows initial entries to be maintained.
      /// </summary>
      /// <returns>
      /// true if the label represents a collection value
      /// </returns>
      public bool IsCollection() {
         return false;
      }
      /// <summary>
      /// Because the attribute can contain only simple text values it
      /// is never required to specified as anything other than text.
      /// Therefore this will always return false as CDATA does not
      /// apply to the attribute values.
      /// </summary>
      /// <returns>
      /// this will always return false for XML attributes
      /// </returns>
      public bool IsData() {
         return false;
      }
      /// <summary>
      /// This method is used by the deserialization process to check
      /// to see if an annotation is inline or not. If an annotation
      /// represents an inline XML entity then the deserialization
      /// and serialization process ignores overrides and special
      /// attributes. By default all attributes are not inline items.
      /// </summary>
      /// <returns>
      /// this always returns false for attribute labels
      /// </returns>
      public bool IsInline() {
         return false;
      }
      /// <summary>
      /// This is used to describe the annotation and method or field
      /// that this label represents. This is used to provide error
      /// messages that can be used to debug issues that occur when
      /// processing a method. This will provide enough information
      /// such that the problem can be isolated correctly.
      /// </summary>
      /// <returns>
      /// this returns a string representation of the label
      /// </returns>
      public String ToString() {
         return detail.ToString();
      }
   }
}
