#region License
//
// CacheLabel.cs July 2007
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
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>CacheLabel</c> object is used to acquire details from an
   /// inner label object so that details can be retrieved repeatedly without
   /// the need to perform any logic for extracting the values. This ensures
   /// that a class XML schema requires only initial processing the first
   /// time the class XML schema is required.
   /// </summary>
   class CacheLabel : Label {
      /// <summary>
      /// This is the decorator that is associated with the label.
      /// </summary>
      private readonly Decorator decorator;
      /// <summary>
      /// This is the contact used to set and get the value for the node.
      /// </summary>
      private readonly Contact contact;
      /// <summary>
      /// This is used to represent the label class that this will use.
      /// </summary>
      private readonly Class type;
      /// <summary>
      /// This is used to represent the name of the entry item use.
      /// </summary>
      private readonly String entry;
      /// <summary>
      /// This is used to represent the name override for the annotation.
      /// </summary>
      private readonly String override;
      /// <summary>
      /// This is used to represent the name of the annotated element.
      /// </summary>
      private readonly String name;
      /// <summary>
      /// This is the label the this cache is wrapping the values for.
      /// </summary>
      private readonly Label label;
      /// <summary>
      /// This is used to represent the dependent type to be used.
      /// </summary>
      private readonly Type depend;
      /// <summary>
      /// This is used to represent whether the data is written as data.
      /// </summary>
      private readonly bool data;
      /// <summary>
      /// This is used to determine the styling of the label name.
      /// </summary>
      private readonly bool attribute;
      /// <summary>
      /// This is used to represent whether the entity is required or not.
      /// </summary>
      private readonly bool required;
      /// <summary>
      /// This is used to determine if the label represents a collection.
      /// </summary>
      private readonly bool collection;
      /// <summary>
      /// This is used to determine whether the entity is inline or not.
      /// </summary>
      private readonly bool inline;
      /// <summary>
      /// Constructor for the <c>CacheLabel</c> object. This is used
      /// to create a <c>Label</c> that acquires details from another
      /// label in such a way that any logic involved in acquiring details
      /// is performed only once.
      /// </summary>
      /// <param name="label">
      /// this is the label to acquire the details from
      /// </param>
      public CacheLabel(Label label) {
         this.decorator = label.Decorator;
         this.attribute = label.IsAttribute();
         this.collection = label.IsCollection();
         this.contact = label.Contact;
         this.depend = label.Dependent;
         this.required = label.IsRequired();
         this.override = label.Override;
         this.inline = label.IsInline();
         this.type = label.Type;
         this.name = label.GetName();
         this.entry = label.Entry;
         this.data = label.IsData();
         this.label = label;
      }
      /// <summary>
      /// This is used to acquire the contact object for this label. The
      /// contact retrieved can be used to set any object or primitive that
      /// has been deserialized, and can also be used to acquire values to
      /// be serialized in the case of object persistence. All contacts
      /// that are retrieved from this method will be accessible.
      /// </summary>
      /// <returns>
      /// returns the field that this label is representing
      /// </returns>
      public Contact Contact {
         get {
            return contact;
         }
      }
      //public Contact GetContact() {
      //   return contact;
      //}
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
      /// This method returns a <c>Converter</c> which can be used to
      /// convert an XML node into an object value and vice versa. The
      /// converter requires only the context object in order to perform
      /// serialization or deserialization of the provided XML node.
      /// </summary>
      /// <param name="context">
      /// this is the context object for the serialization
      /// </param>
      /// <returns>
      /// this returns an object that is used for conversion
      /// </returns>
      public Converter GetConverter(Context context) {
         return label.GetConverter(context);
      }
      /// <summary>
      /// This is used to acquire the name of the element or attribute
      /// that is used by the class schema. The name is determined by
      /// checking for an override within the annotation. If it contains
      /// a name then that is used, if however the annotation does not
      /// specify a name the the field or method name is used instead.
      /// </summary>
      /// <param name="context">
      /// this is the context used to style the name
      /// </param>
      /// <returns>
      /// returns the name that is used for the XML property
      /// </returns>
      public String GetName(Context context) {
         Style style = context.getStyle();
         if(attribute) {
            return style.getAttribute(name);
         }
         return style.getElement(name);
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
      public Object GetEmpty(Context context) {
         return label.GetEmpty(context);
      }
      /// <summary>
      /// This returns the dependent type for the annotation. This type
      /// is the type other than the annotated field or method type that
      /// the label depends on. For the <c>ElementList</c> and
      /// the <c>ElementArray</c> this is the component type that
      /// is deserialized individually and inserted into the container.
      /// </summary>
      /// <returns>
      /// this is the type that the annotation depends on
      /// </returns>
      public Type Dependent {
         get {
            return depend;
         }
      }
      //public Type GetDependent() {
      //   return depend;
      //}
      /// This is used to either provide the entry value provided within
      /// the annotation or compute a entry value. If the entry string
      /// is not provided the the entry value is calculated as the type
      /// of primitive the object is as a simplified class name.
      /// </summary>
      /// <returns>
      /// this returns the name of the XML entry element used
      /// </returns>
      public String Entry {
         get {
            return entry;
         }
      }
      //public String GetEntry() {
      //   return entry;
      //}
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
            return name;
         }
      }
      //public String GetName() {
      //   return name;
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
            return override;
         }
      }
      //public String GetOverride() {
      //   return override;
      //}
      /// This acts as a convenience method used to determine the type of
      /// the field this represents. This is used when an object is written
      /// to XML. It determines whether a <c>class</c> attribute
      /// is required within the serialized XML element, that is, if the
      /// class returned by this is different from the actual value of the
      /// object to be serialized then that type needs to be remembered.
      /// </summary>
      /// <returns>
      /// this returns the type of the field class
      /// </returns>
      public Class Type {
         get {
            return type;
         }
      }
      //public Class GetType() {
      //   return type;
      //}
      /// This is used to determine whether the annotation requires it
      /// and its children to be written as a CDATA block. This is done
      /// when a primitive or other such element requires a text value
      /// and that value needs to be encapsulated within a CDATA block.
      /// </summary>
      /// <returns>
      /// this returns true if the element requires CDATA
      /// </returns>
      public bool IsData() {
         return data;
      }
      /// <summary>
      /// This is used to determine whether the label represents an
      /// inline XML entity. The <c>ElementList</c> annotation
      /// and the <c>Text</c> annotation represent inline
      /// items. This means that they contain no containing element
      /// and so can not specify overrides or special attributes.
      /// </summary>
      /// <returns>
      /// this returns true if the annotation is inline
      /// </returns>
      public bool IsInline() {
         return inline;
      }
      /// <summary>
      /// This method is used to determine if the label represents an
      /// attribute. This is used to style the name so that elements
      /// are styled as elements and attributes are styled as required.
      /// </summary>
      /// <returns>
      /// this is used to determine if this is an attribute
      /// </returns>
      public bool IsAttribute() {
         return attribute;
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
         return collection;
      }
      /// <summary>
      /// Determines whether the XML attribute or element is required.
      /// This ensures that if an XML element is missing from a document
      /// that deserialization can continue. Also, in the process of
      /// serialization, if a value is null it does not need to be
      /// written to the resulting XML document.
      /// </summary>
      /// <returns>
      /// true if the label represents a some required data
      /// </returns>
      public bool IsRequired() {
         return required;
      }
      /// <summary>
      /// This is used to describe the annotation and method or field
      /// that this label represents. This is used to provide error
      /// messages that can be used to debug issues that occur when
      /// processing a method. This should provide enough information
      /// such that the problem can be isolated correctly.
      /// </summary>
      /// <returns>
      /// this returns a string representation of the label
      /// </returns>
      public String ToString() {
         return label.ToString();
      }
   }
}
