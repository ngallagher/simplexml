#region License
//
// ElementArrayLabel.cs July 2006
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
   /// The <c>ElementArrayLabel</c> represents a label that is used
   /// to represent an XML element array in a class schema. This element
   /// array label can be used to convert an XML node into an array of
   /// composite or primitive objects. If the array is of primitive types
   /// then the <c>entry</c> attribute must be specified so that
   /// the primitive values can be serialized in a structured manner.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.ElementArray
   /// </seealso>
   class ElementArrayLabel : Label {
      /// <summary>
      /// This is the decorator that is associated with the element.
      /// </summary>
      private Decorator decorator;
      /// <summary>
      /// This references the annotation that the field uses.
      /// </summary>
      private ElementArray label;
      /// <summary>
      /// This contains the details of the annotated contact object.
      /// </summary>
      private Signature detail;
      /// <summary>
      /// This is the type of array this label will represent.
      /// </summary>
      private Class type;
      /// <summary>
      /// This is the name of the XML entry from the annotation.
      /// </summary>
      private String entry;
      /// <summary>
      /// This is the name of the element for this label instance.
      /// </summary>
      private String name;
      /// <summary>
      /// Constructor for the <c>ElementArrayLabel</c> object. This
      /// creates a label object, which can be used to convert an element
      /// node to an array of XML serializable objects.
      /// </summary>
      /// <param name="contact">
      /// this is the contact that this label represents
      /// </param>
      /// <param name="label">
      /// the annotation that contains the schema details
      /// </param>
      public ElementArrayLabel(Contact contact, ElementArray label) {
         this.detail = new Signature(contact, this);
         this.decorator = new Qualifier(contact);
         this.type = contact.Type;
         this.entry = label.entry();
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
      /// This will create a <c>Converter</c> for transforming an XML
      /// element into an array of XML serializable objects. The XML schema
      /// class for these objects must present the element array annotation.
      /// </summary>
      /// <param name="context">
      /// this is the context object used for serialization
      /// </param>
      /// <returns>
      /// this returns the converter for creating a collection
      /// </returns>
      public Converter GetConverter(Context context) {
         String entry = GetEntry(context);
         if(!type.isArray()) {
            throw new InstantiationException("Type is not an array %s for %s", type, label);
         }
         return GetConverter(context, entry);
      }
      /// <summary>
      /// This will create a <c>Converter</c> for transforming an XML
      /// element into an array of XML serializable objects. The XML schema
      /// class for these objects must present the element array annotation.
      /// </summary>
      /// <param name="context">
      /// this is the context object used for serialization
      /// </param>
      /// <param name="name">
      /// this is the name of the entry XML element to use
      /// </param>
      /// <returns>
      /// this returns the converter for creating a collection
      /// </returns>
      public Converter GetConverter(Context context, String name) {
         Type entry = Dependent;
         Type type = Contact;
         if(!context.isPrimitive(entry)) {
            return new CompositeArray(context, type, entry, name);
         }
         return new PrimitiveArray(context, type, entry, name);
      }
      /// <summary>
      /// This is used to either provide the entry value provided within
      /// the annotation or compute a entry value. If the entry string
      /// is not provided the the entry value is calculated as the type
      /// of primitive the object is as a simplified class name.
      /// </summary>
      /// <param name="context">
      /// this is the context used to style the entry
      /// </param>
      /// <returns>
      /// this returns the name of the XML entry element used
      /// </returns>
      public String GetEntry(Context context) {
         Style style = context.getStyle();
         String entry = GetEntry();
         return style.getElement(entry);
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
         String name = detail.GetName();
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
         Type array = new ClassType(type);
         Factory factory = new ArrayFactory(context, array);
         if(!label.empty()) {
            return factory.getInstance();
         }
         return null;
      }
      /// <summary>
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
            if(detail.IsEmpty(entry)) {
               entry = detail.GetEntry();
            }
            return entry;
         }
      }
      //public String GetEntry() {
      //   if(detail.IsEmpty(entry)) {
      //      entry = detail.GetEntry();
      //   }
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
            return detail.GetName();
         }
      }
      //public String GetName() {
      //   return detail.GetName();
      //}
      /// This is used to acquire the dependent type for the annotated
      /// array. This will simply return the type that the array is
      /// composed to hold. This must be a serializable type, that is,
      /// a type that is annotated with the <c>Root</c> class.
      /// </summary>
      /// <returns>
      /// this returns the component type for the array
      /// </returns>
      public Type Dependent {
         get {
            Class entry = type.getComponentType();
            if(entry == null) {
               return new ClassType(type);
            }
            return new ClassType(entry);
         }
      }
      //public Type GetDependent() {
      //   Class entry = type.getComponentType();
      //   if(entry == null) {
      //      return new ClassType(type);
      //   }
      //   return new ClassType(entry);
      //}
      /// This acts as a convenience method used to determine the type of
      /// contact this represents. This is used when an object is written
      /// to XML. It determines whether a <c>class</c> attribute
      /// is required within the serialized XML element, that is, if the
      /// class returned by this is different from the actual value of the
      /// object to be serialized then that type needs to be remembered.
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
      /// This is used to acquire the contact object for this label. The
      /// contact retrieved can be used to set any object or primitive that
      /// has been deserialized, and can also be used to acquire values to
      /// be serialized in the case of object persistence. All contacts
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
      /// This method is used to determine if the label represents an
      /// attribute. This is used to style the name so that elements
      /// are styled as elements and attributes are styled as required.
      /// </summary>
      /// <returns>
      /// this is used to determine if this is an attribute
      /// </returns>
      public bool IsAttribute() {
         return false;
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
      /// This is used to determine whether the XML element is required.
      /// This ensures that if an XML element is missing from a document
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
      /// This is used to determine whether the annotation requires it
      /// and its children to be written as a CDATA block. This is done
      /// when a primitive or other such element requires a text value
      /// and that value needs to be encapsulated within a CDATA block.
      /// </summary>
      /// <returns>
      /// this returns true if the element requires CDATA
      /// </returns>
      public bool IsData() {
         return label.data();
      }
      /// <summary>
      /// This method is used by the deserialization process to check
      /// to see if an annotation is inline or not. If an annotation
      /// represents an inline XML entity then the deserialization
      /// and serialization process ignores overrides and special
      /// attributes. By default element arrays are not inline.
      /// </summary>
      /// <returns>
      /// this always returns false for array labels
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
