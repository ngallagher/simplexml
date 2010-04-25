#region License
//
// Scanner.cs July 2006
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
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Scanner</c> object performs the reflective inspection
   /// of a class and builds a map of attributes and elements for each
   /// annotated field. This acts as a cachable container for reflection
   /// actions performed on a specific type. When scanning the provided
   /// class this inserts the scanned field as a <c>Label</c> in to
   /// a map so that it can be retrieved by name. Annotations classified
   /// as attributes have the <c>Attribute</c> annotation, all other
   /// annotated fields are stored as elements.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Schema
   /// </seealso>
   class Scanner {
      /// <summary>
      /// This method acts as a pointer to the types commit process.
      /// </summary>
      private ClassScanner scanner;
      /// <summary>
      /// This is the default access type to be used for this scanner.
      /// </summary>
      private DefaultType access;
      /// <summary>
      /// This is used to store all labels that are XML attributes.
      /// </summary>
      private LabelMap attributes;
      /// <summary>
      /// This is used to store all labels that are XML elements.
      /// </summary>
      private LabelMap elements;
      /// <summary>
      /// This is used to compare the annotations being scanned.
      /// </summary>
      private Comparer comparer;
      /// <summary>
      /// This is the version label used to read the version attribute.
      /// </summary>
      private Label version;
      /// <summary>
      /// This is used to store all labels that are XML text values.
      /// </summary>
      private Label text;
      /// <summary>
      /// This is the name of the class as taken from the root class.
      /// </summary>
      private String name;
      /// <summary>
      /// This is the type that is being scanned by this scanner.
      /// </summary>
      private Class type;
      /// <summary>
      /// This is used to specify whether the type is a primitive class.
      /// </summary>
      private bool primitive;
      /// <summary>
      /// Constructor for the <c>Scanner</c> object. This is used
      /// to scan the provided class for annotations that are used to
      /// build a schema for an XML file to follow.
      /// </summary>
      /// <param name="type">
      /// this is the type that is scanned for a schema
      /// </param>
      public Scanner(Class type) {
         this.scanner = new ClassScanner(type);
         this.attributes = new LabelMap(this);
         this.elements = new LabelMap(this);
         this.comparer = new Comparer();
         this.type = type;
         this.Scan(type);
      }
      /// <summary>
      /// This is used to acquire the type that this scanner scans for
      /// annotations to be used in a schema. Exposing the class that
      /// this represents allows the schema it creates to be known.
      /// </summary>
      /// <returns>
      /// this is the type that this creator will represent
      /// </returns>
      public Class Type {
         get {
            return type;
         }
      }
      //public Class GetType() {
      //   return type;
      //}
      /// This is used to create the object instance. It does this by
      /// either delegating to the default no argument constructor or by
      /// using one of the annotated constructors for the object. This
      /// allows deserialized values to be injected in to the created
      /// object if that is required by the class schema.
      /// </summary>
      /// <returns>
      /// this returns the creator for the class object
      /// </returns>
      public Creator Creator {
         get {
            return scanner.Creator;
         }
      }
      //public Creator GetCreator() {
      //   return scanner.Creator;
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
            return scanner.Decorator;
         }
      }
      //public Decorator GetDecorator() {
      //   return scanner.Decorator;
      //}
      /// This method is used to return the <c>Caller</c> for this
      /// class. The caller is a means to deliver invocations to the
      /// object for the persister callback methods. It aggregates all of
      /// the persister callback methods in to a single object.
      /// </summary>
      /// <returns>
      /// this returns a caller used for delivering callbacks
      /// </returns>
      public Caller GetCaller(Context context) {
         return new Caller(this, context);
      }
      /// <summary>
      /// Returns a <c>LabelMap</c> that contains the details for
      /// all fields marked as XML attributes. This returns a new map
      /// each time the method is called, the goal is to ensure that any
      /// object using the label map can manipulate it without changing
      /// the core details of the schema, allowing it to be cached.
      /// </summary>
      /// <param name="context">
      /// this is the context used to style the names
      /// </param>
      /// <returns>
      /// map with the details extracted from the schema class
      /// </returns>
      public LabelMap GetAttributes(Context context) {
         return attributes.Clone(context);
      }
      /// <summary>
      /// Returns a <c>LabelMap</c> that contains the details for
      /// all fields marked as XML elements. The annotations that are
      /// considered elements are the <c>ElementList</c> and the
      /// <c>Element</c> annotations. This returns a copy of the
      /// details extracted from the schema class so this can be cached.
      /// </summary>
      /// <param name="context">
      /// this is the context used to style the names
      /// </param>
      /// <returns>
      /// a map containing the details for XML elements
      /// </returns>
      public LabelMap GetElements(Context context) {
         return elements.Clone(context);
      }
      /// <summary>
      /// This is the <c>Version</c> for the scanned class. It
      /// allows the deserialization process to be configured such that
      /// if the version is different from the schema class none of
      /// the fields and methods are required and unmatched elements
      /// and attributes will be ignored.
      /// </summary>
      /// <returns>
      /// this returns the version of the class that is scanned
      /// </returns>
      public Version Revision {
         get {
            if(version != null) {
               return version.Contact.getAnnotation(Version.class);
            }
            return null;
         }
      }
      //public Version GetRevision() {
      //   if(version != null) {
      //      return version.Contact.getAnnotation(Version.class);
      //   }
      //   return null;
      //}
      /// This returns the <c>Label</c> that represents the version
      /// annotation for the scanned class. Only a single version can
      /// exist within the class if more than one exists an exception is
      /// thrown. This will read only floating point types such as double.
      /// </summary>
      /// <returns>
      /// this returns the label used for reading the version
      /// </returns>
      public Label Version {
         get {
            return version;
         }
      }
      //public Label GetVersion() {
      //   return version;
      //}
      /// This returns the <c>Label</c> that represents the text
      /// annotation for the scanned class. Only a single text annotation
      /// can be used per class, so this returns only a single label
      /// rather than a <c>LabelMap</c> object. Also if this is
      /// not null then the elements label map must be empty.
      /// </summary>
      /// <returns>
      /// this returns the text label for the scanned class
      /// </returns>
      public Label Text {
         get {
            return text;
         }
      }
      //public Label GetText() {
      //   return text;
      //}
      /// This returns the name of the class processed by this scanner.
      /// The name is either the name as specified in the last found
      /// <c>Root</c> annotation, or if a name was not specified
      /// within the discovered root then the Java Bean class name of
      /// the last class annotated with a root annotation.
      /// </summary>
      /// <returns>
      /// this returns the name of the object being scanned
      /// </returns>
      public String Name {
         get {
            return name;
         }
      }
      //public String GetName() {
      //   return name;
      //}
      /// This method is used to retrieve the schema class commit method
      /// during the deserialization process. The commit method must be
      /// marked with the <c>Commit</c> annotation so that when the
      /// object is deserialized the persister has a chance to invoke the
      /// method so that the object can build further data structures.
      /// </summary>
      /// <returns>
      /// this returns the commit method for the schema class
      /// </returns>
      public Function Commit {
         get {
            return scanner.Commit;
         }
      }
      //public Function GetCommit() {
      //   return scanner.Commit;
      //}
      /// This method is used to retrieve the schema class validation
      /// method during the deserialization process. The validation method
      /// must be marked with the <c>Validate</c> annotation so that
      /// when the object is deserialized the persister has a chance to
      /// invoke that method so that object can validate its field values.
      /// </summary>
      /// <returns>
      /// this returns the validate method for the schema class
      /// </returns>
      public Function Validate {
         get {
            return scanner.Validate;
         }
      }
      //public Function GetValidate() {
      //   return scanner.Validate;
      //}
      /// This method is used to retrieve the schema class persistence
      /// method. This is invoked during the serialization process to
      /// get the object a chance to perform an nessecary preparation
      /// before the serialization of the object proceeds. The persist
      /// method must be marked with the <c>Persist</c> annotation.
      /// </summary>
      /// <returns>
      /// this returns the persist method for the schema class
      /// </returns>
      public Function Persist {
         get {
            return scanner.Persist;
         }
      }
      //public Function GetPersist() {
      //   return scanner.Persist;
      //}
      /// This method is used to retrieve the schema class completion
      /// method. This is invoked after the serialization process has
      /// completed and gives the object a chance to restore its state
      /// if the persist method required some alteration or locking.
      /// This is marked with the <c>Complete</c> annotation.
      /// </summary>
      /// <returns>
      /// returns the complete method for the schema class
      /// </returns>
      public Function Complete {
         get {
            return scanner.Complete;
         }
      }
      //public Function GetComplete() {
      //   return scanner.Complete;
      //}
      /// This method is used to retrieve the schema class replacement
      /// method. The replacement method is used to substitute an object
      /// that has been deserialized with another object. This allows
      /// a seamless delegation mechanism to be implemented. This is
      /// marked with the <c>Replace</c> annotation.
      /// </summary>
      /// <returns>
      /// returns the replace method for the schema class
      /// </returns>
      public Function Replace {
         get {
            return scanner.Replace;
         }
      }
      //public Function GetReplace() {
      //   return scanner.Replace;
      //}
      /// This method is used to retrieve the schema class replacement
      /// method. The replacement method is used to substitute an object
      /// that has been deserialized with another object. This allows
      /// a seamless delegation mechanism to be implemented. This is
      /// marked with the <c>Replace</c> annotation.
      /// </summary>
      /// <returns>
      /// returns the replace method for the schema class
      /// </returns>
      public Function Resolve {
         get {
            return scanner.Resolve;
         }
      }
      //public Function GetResolve() {
      //   return scanner.Resolve;
      //}
      /// This is used to determine whether the scanned class represents
      /// a primitive type. A primitive type is a type that contains no
      /// XML annotations and so cannot be serialized with an XML form.
      /// Instead primitives a serialized using transformations.
      /// </summary>
      /// <returns>
      /// this returns true if no XML annotations were found
      /// </returns>
      public bool IsPrimitive() {
         return primitive;
      }
      /// <summary>
      /// This is used to determine whether the scanned class represents
      /// a primitive type. A primitive type is a type that contains no
      /// XML annotations and so cannot be serialized with an XML form.
      /// Instead primitives a serialized using transformations.
      /// </summary>
      /// <returns>
      /// this returns true if no XML annotations were found
      /// </returns>
      public bool IsEmpty() {
         Root root = scanner.Root;
         if(!elements.IsEmpty()) {
            return false;
         }
         if(!attributes.IsEmpty()) {
            return false;
         }
         if(text != null) {
            return false;
         }
         return root == null;
      }
      /// <summary>
      /// This method is used to determine whether strict mappings are
      /// required. Strict mapping means that all labels in the class
      /// schema must match the XML elements and attributes in the
      /// source XML document. When strict mapping is disabled, then
      /// XML elements and attributes that do not exist in the schema
      /// class will be ignored without breaking the parser.
      /// </summary>
      /// <returns>
      /// true if strict parsing is enabled, false otherwise
      /// </returns>
      public bool IsStrict() {
         return scanner.IsStrict();
      }
      /// <summary>
      /// This is used to scan the specified object to extract the fields
      /// and methods that are to be used in the serialization process.
      /// This will acquire all fields and getter setter pairs that have
      /// been annotated with the XML annotations.
      /// </summary>
      /// <param name="type">
      /// this is the object type that is to be scanned
      /// </param>
      public void Scan(Class type) {
         Root(type);
         Order(type);
         Access(type);
         Field(type);
         Method(type);
         Validate(type);
      }
      /// <summary>
      /// This is used to validate the configuration of the scanned class.
      /// If a <c>Text</c> annotation has been used with elements
      /// then validation will fail and an exception will be thrown.
      /// </summary>
      /// <param name="type">
      /// this is the object type that is being scanned
      /// </param>
      public void Validate(Class type) {
         Creator creator = scanner.Creator;
         Order order = scanner.Order;
         ValidateElements(type, order);
         ValidateAttributes(type, order);
         ValidateParameters(creator);
         ValidateText(type);
      }
      /// <summary>
      /// This is used to validate the configuration of the scanned class.
      /// If a <c>Text</c> annotation has been used with elements
      /// then validation will fail and an exception will be thrown.
      /// </summary>
      /// <param name="type">
      /// this is the object type that is being scanned
      /// </param>
      public void ValidateText(Class type) {
         if(text != null) {
            if(!elements.IsEmpty()) {
               throw new TextException("Elements used with %s in %s", text, type);
            }
         }  else {
            primitive = IsEmpty();
         }
      }
      /// <summary>
      /// This is used to validate the configuration of the scanned class.
      /// If an ordered element is specified but does not refer to an
      /// existing element then this will throw an exception.
      /// </summary>
      /// <param name="type">
      /// this is the object type that is being scanned
      /// </param>
      public void ValidateElements(Class type, Order order) {
         Creator factory = scanner.Creator;
         List<Builder> builders = factory.getBuilders();
         for(Builder builder : builders) {
            ValidateConstructor(builder, elements);
         }
         if(order != null) {
            for(String name : order.elements()) {
               Label label = elements.get(name);
               if(label == null) {
                  throw new ElementException("Ordered element '%s' missing for %s", name, type);
               }
            }
         }
      }
      /// <summary>
      /// This is used to validate the configuration of the scanned class.
      /// If an ordered attribute is specified but does not refer to an
      /// existing attribute then this will throw an exception.
      /// </summary>
      /// <param name="type">
      /// this is the object type that is being scanned
      /// </param>
      public void ValidateAttributes(Class type, Order order) {
         Creator factory = scanner.Creator;
         List<Builder> builders = factory.getBuilders();
         for(Builder builder : builders) {
            ValidateConstructor(builder, elements);
         }
         if(order != null) {
            for(String name : order.attributes()) {
               Label label = attributes.get(name);
               if(label == null) {
                  throw new AttributeException("Ordered attribute '%s' missing for %s", name, type);
               }
            }
         }
      }
      /// <summary>
      /// This is used to ensure that readonly methods and fields have a
      /// constructor parameter that allows the value to be injected in
      /// to. Validating the constructor in this manner ensures that the
      /// class schema remains fully serializable and deserializable.
      /// </summary>
      /// <param name="builder">
      /// this is the builder to validate the labels with
      /// </param>
      /// <param name="map">
      /// this is the map that contains the labels to validate
      /// </param>
      public void ValidateConstructor(Builder builder, LabelMap map) {
         for(Label label : map) {
            if(label != null) {
               Contact contact = label.Contact;
               String name = label.GetName();
               if(contact.isReadOnly()) {
                  Parameter value = builder.getParameter(name);
                  if(value == null) {
                     throw new ConstructorException("No match found for %s in %s", contact, type);
                  }
               }
            }
         }
      }
      /// <summary>
      /// This is used to ensure that for each parameter in the builder
      /// there is a matching method or field. This ensures that the
      /// class schema is fully readable and writable. If not method or
      /// field annotation exists for the parameter validation fails.
      /// </summary>
      /// <param name="creator">
      /// this is the creator to validate the labels with
      /// </param>
      public void ValidateParameters(Creator creator) {
         List<Parameter> list = creator.getParameters();
         for(Parameter parameter : list) {
            String name = parameter.GetName();
            Label label = elements.get(name);
            if(label == null) {
               label = attributes.get(name);
            }
            if(label == null) {
               throw new ConstructorException("Parameter '%s' does not have a match in %s", name, type);
            }
         }
      }
      /// <summary>
      /// This is used to acquire the optional <c>Root</c> from the
      /// specified class. The root annotation provides information as
      /// to how the object is to be parsed as well as other information
      /// such as the name of the object if it is to be serialized.
      /// </summary>
      /// <param name="type">
      /// this is the type of the class to be inspected
      /// </param>
      public void Root(Class<?> type) {
         String real = type.getSimpleName();
         Root root = scanner.Root;
         String text = real;
         if(root != null) {
            text = root.name();
            if(IsEmpty(text)) {
               text = Reflector.GetName(real);
            }
            name = text.intern();
         }
      }
      /// <summary>
      /// This is used to acquire the optional order annotation to provide
      /// order to the elements and attributes for the generated XML. This
      /// acts as an override to the order provided by the declaration of
      /// the types within the object.
      /// </summary>
      /// <param name="type">
      /// this is the type to be scanned for the order
      /// </param>
      public void Order(Class<?> type) {
         Order order = scanner.Order;
         if(order != null) {
            for(String name : order.elements()) {
               elements.put(name, null);
            }
            for(String name : order.attributes()) {
               attributes.put(name, null);
            }
         }
      }
      /// <summary>
      /// This is used to determine the access type for the class. The
      /// access type is specified by the <c>DefaultType</c>
      /// enumeration. Setting a default access tells this scanner to
      /// synthesize an XML annotation for all fields or methods that
      /// do not have associated annotations.
      /// </summary>
      /// <param name="type">
      /// this is the type to acquire the default type for
      /// </param>
      public void Access(Class<?> type) {
         Default holder = scanner.Default;
         if(holder != null) {
            access = holder.value();
         }
      }
      /// <summary>
      /// This method is used to determine if a root annotation value is
      /// an empty value. Rather than determining if a string is empty
      /// be comparing it to an empty string this method allows for the
      /// value an empty string represents to be changed in future.
      /// </summary>
      /// <param name="value">
      /// this is the value to determine if it is empty
      /// </param>
      /// <returns>
      /// true if the string value specified is an empty value
      /// </returns>
      public bool IsEmpty(String value) {
         return value.length() == 0;
      }
      /// <summary>
      /// This is used to acquire the contacts for the annotated fields
      /// within the specified class. The field contacts are added to
      /// either the attributes or elements map depending on annotation.
      /// </summary>
      /// <param name="type">
      /// this is the object type that is to be scanned
      /// </param>
      public void Field(Class type) {
         ContactList list = new FieldScanner(type, access);
         for(Contact contact : list) {
            Scan(contact, contact.getAnnotation());
         }
      }
      /// <summary>
      /// This is used to acquire the contacts for the annotated fields
      /// within the specified class. The field contacts are added to
      /// either the attributes or elements map depending on annotation.
      /// </summary>
      /// <param name="type">
      /// this is the object type that is to be scanned
      /// </param>
      public void Method(Class type) {
         ContactList list = new MethodScanner(type, access);
         for(Contact contact : list) {
            Scan(contact, contact.getAnnotation());
         }
      }
      /// <summary>
      /// This reflectively checks the annotation to determine the type
      /// of annotation it represents. If it represents an XML schema
      /// annotation it is used to create a <c>Label</c> which can
      /// be used to represent the field within the context object.
      /// </summary>
      /// <param name="field">
      /// the field that the annotation comes from
      /// </param>
      /// <param name="label">
      /// the annotation used to model the XML schema
      /// </param>
      public void Scan(Contact field, Annotation label) {
         if(label instanceof Attribute) {
            Process(field, label, attributes);
         }
         if(label instanceof ElementList) {
            Process(field, label, elements);
         }
         if(label instanceof ElementArray) {
            Process(field, label, elements);
         }
         if(label instanceof ElementMap) {
            Process(field, label, elements);
         }
         if(label instanceof Element) {
            Process(field, label, elements);
         }
         if(label instanceof Version) {
            Version(field, label);
         }
         if(label instanceof Text) {
            Text(field, label);
         }
      }
      /// <summary>
      /// This is used to process the <c>Text</c> annotations that
      /// are present in the scanned class. This will set the text label
      /// for the class and an ensure that if there is more than one
      /// text label within the class an exception is thrown.
      /// </summary>
      /// <param name="field">
      /// the field the annotation was extracted from
      /// </param>
      /// <param name="type">
      /// the annotation extracted from the field
      /// </param>
      public void Text(Contact field, Annotation type) {
         Label label = LabelFactory.getInstance(field, type);
         if(text != null) {
            throw new TextException("Multiple text annotations in %s", type);
         }
         text = label;
      }
      /// <summary>
      /// This is used to process the <c>Text</c> annotations that
      /// are present in the scanned class. This will set the text label
      /// for the class and an ensure that if there is more than one
      /// text label within the class an exception is thrown.
      /// </summary>
      /// <param name="field">
      /// the field the annotation was extracted from
      /// </param>
      /// <param name="type">
      /// the annotation extracted from the field
      /// </param>
      public void Version(Contact field, Annotation type) {
         Label label = LabelFactory.getInstance(field, type);
         if(version != null) {
            throw new AttributeException("Multiple version annotations in %s", type);
         }
         version = label;
      }
      /// <summary>
      /// This is used when all details from a field have been gathered
      /// and a <c>Label</c> implementation needs to be created.
      /// This will build a label instance based on the field annotation.
      /// If a label with the same name was already inserted then it is
      /// ignored and the value for that field will not be serialized.
      /// </summary>
      /// <param name="field">
      /// the field the annotation was extracted from
      /// </param>
      /// <param name="type">
      /// the annotation extracted from the field
      /// </param>
      /// <param name="map">
      /// this is used to collect the label instance created
      /// </param>
      public void Process(Contact field, Annotation type, LabelMap map) {
         Label label = LabelFactory.getInstance(field, type);
         String name = label.GetName();
         if(map.get(name) != null) {
            throw new PersistenceException("Annotation of name '%s' declared twice", name);
         }
         map.put(name, label);
         Validate(label, name);
      }
      /// <summary>
      /// This is used to validate the <c>Parameter</c> object that
      /// exist in the constructors. Validation is performed against the
      /// annotated methods and fields to ensure that they match up.
      /// </summary>
      /// <param name="field">
      /// this is the annotated method or field to validate
      /// </param>
      /// <param name="name">
      /// this is the name of the parameter to validate with
      /// </param>
      public void Validate(Label field, String name) {
         Creator factory = scanner.Creator;
         Parameter parameter = factory.getParameter(name);
         if(parameter != null) {
            Validate(field, parameter);
         }
      }
      /// <summary>
      /// This is used to validate the <c>Parameter</c> object that
      /// exist in the constructors. Validation is performed against the
      /// annotated methods and fields to ensure that they match up.
      /// </summary>
      /// <param name="field">
      /// this is the annotated method or field to validate
      /// </param>
      /// <param name="parameter">
      /// this is the parameter to validate with
      /// </param>
      public void Validate(Label field, Parameter parameter) {
         Contact contact = field.Contact;
         Annotation label = contact.getAnnotation();
         Annotation match = parameter.getAnnotation();
         String name = field.GetName();
         if(!comparer.Equals(label, match)) {
            throw new ConstructorException("Annotation does not match for '%s' in %s", name, type);
         }
         Class expect = contact.Type;
         if(expect != parameter.Type) {
            throw new ConstructorException("Parameter does not match field for '%s' in %s", name, type);
         }
      }
   }
}
