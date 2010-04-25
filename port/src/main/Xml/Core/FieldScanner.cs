#region License
//
// FieldScanner.cs April 2007
//
// Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
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
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   import static SimpleFramework.Xml.DefaultType.FIELD;
   /// <summary>
   /// The <c>FieldScanner</c> object is used to scan an class for
   /// fields marked with an XML annotation. All fields that contain an
   /// XML annotation are added as <c>Contact</c> objects to the
   /// list of contacts for the class. This scans the object by checking
   /// the class hierarchy, this allows a subclass to override a super
   /// class annotated field, although this should be used rarely.
   /// </summary>
   class FieldScanner : ContactList {
      /// <summary>
      /// This is used to create the synthetic annotations for fields.
      /// </summary>
      private readonly AnnotationFactory factory;
      /// <summary>
      /// This is used to acquire the hierarchy for the class scanned.
      /// </summary>
      private readonly Hierarchy hierarchy;
      /// <summary>
      /// This is the default access type to be used for this scanner.
      /// </summary>
      private readonly DefaultType access;
      /// <summary>
      /// This is used to determine which fields have been scanned.
      /// </summary>
      private readonly ContactMap done;
      /// <summary>
      /// Constructor for the <c>FieldScanner</c> object. This is
      /// used to perform a scan on the specified class in order to find
      /// all fields that are labeled with an XML annotation.
      /// </summary>
      /// <param name="type">
      /// this is the schema class that is to be scanned
      /// </param>
      public FieldScanner(Class type) {
         this(type, null);
      }
      /// <summary>
      /// Constructor for the <c>FieldScanner</c> object. This is
      /// used to perform a scan on the specified class in order to find
      /// all fields that are labeled with an XML annotation.
      /// </summary>
      /// <param name="type">
      /// this is the schema class that is to be scanned
      /// </param>
      /// <param name="access">
      /// this is the access type for the class
      /// </param>
      public FieldScanner(Class type, DefaultType access) {
         this.factory = new AnnotationFactory();
         this.hierarchy = new Hierarchy(type);
         this.done = new ContactMap();
         this.access = access;
         this.Scan(type);
      }
      /// <summary>
      /// This method is used to scan the class hierarchy for each class
      /// in order to extract fields that contain XML annotations. If
      /// the field is annotated it is converted to a contact so that
      /// it can be used during serialization and deserialization.
      /// </summary>
      /// <param name="type">
      /// this is the type to be scanned for fields
      /// </param>
      public void Scan(Class type) {
         for(Class next : hierarchy) {
            Scan(next, access);
         }
         for(Class next : hierarchy) {
            Scan(next, type);
         }
         Build();
      }
      /// <summary>
      /// This is used to scan the declared fields within the specified
      /// class. Each method will be check to determine if it contains
      /// an XML element and can be used as a <c>Contact</c> for
      /// an entity within the object.
      /// </summary>
      /// <param name="real">
      /// this is the actual type of the object scanned
      /// </param>
      /// <param name="type">
      /// this is one of the super classes for the object
      /// </param>
      public void Scan(Class type, Class real) {
         Field[] list = type.getDeclaredFields();
         for(Field field : list) {
            Scan(field);
         }
      }
      /// <summary>
      /// This is used to scan all annotations within the given field.
      /// Each annotation is checked against the set of supported XML
      /// annotations. If the annotation is one of the XML annotations
      /// then the field is considered for acceptance as a contact.
      /// </summary>
      /// <param name="field">
      /// the field to be scanned for XML annotations
      /// </param>
      public void Scan(Field field) {
         Annotation[] list = field.getDeclaredAnnotations();
         for(Annotation label : list) {
            Scan(field, label);
         }
      }
      /// <summary>
      /// This is used to scan all the fields of the class in order to
      /// determine if it should have a default annotation. If the field
      /// should have a default XML annotation then it is added to the
      /// list of contacts to be used to form the class schema.
      /// </summary>
      /// <param name="type">
      /// this is the type to have its fields scanned
      /// </param>
      /// <param name="access">
      /// this is the default access type for the class
      /// </param>
      public void Scan(Class type, DefaultType access) {
         Field[] list = type.getDeclaredFields();
         if(access == FIELD) {
            for(Field field : list) {
               Class real = field.getType();
               if(real != null) {
                  Process(field, real);
               }
            }
         }
      }
      /// <summary>
      /// This reflectively checks the annotation to determine the type
      /// of annotation it represents. If it represents an XML schema
      /// annotation it is used to create a <c>Contact</c> which
      /// can be used to represent the field within the source object.
      /// </summary>
      /// <param name="field">
      /// the field that the annotation comes from
      /// </param>
      /// <param name="label">
      /// the annotation used to model the XML schema
      /// </param>
      public void Scan(Field field, Annotation label) {
         if(label instanceof Attribute) {
            Process(field, label);
         }
         if(label instanceof ElementList) {
            Process(field, label);
         }
         if(label instanceof ElementArray) {
            Process(field, label);
         }
         if(label instanceof ElementMap) {
            Process(field, label);
         }
         if(label instanceof Element) {
            Process(field, label);
         }
         if(label instanceof Transient) {
            Remove(field, label);
         }
         if(label instanceof Version) {
            Process(field, label);
         }
         if(label instanceof Text) {
            Process(field, label);
         }
      }
      /// <summary>
      /// This method is used to process the field an annotation given.
      /// This will check to determine if the field is accessible, if it
      /// is not accessible then it is made accessible so that private
      /// member fields can be used during the serialization process.
      /// </summary>
      /// <param name="field">
      /// this is the field to be added as a contact
      /// </param>
      /// <param name="type">
      /// this is the type to acquire the annotation
      /// </param>
      public void Process(Field field, Class type) {
         Annotation label = factory.GetInstance(type);
         if(label != null) {
            Process(field, label);
         }
      }
      /// <summary>
      /// This method is used to process the field an annotation given.
      /// This will check to determine if the field is accessible, if it
      /// is not accessible then it is made accessible so that private
      /// member fields can be used during the serialization process.
      /// </summary>
      /// <param name="field">
      /// this is the field to be added as a contact
      /// </param>
      /// <param name="label">
      /// this is the XML annotation used by the field
      /// </param>
      public void Process(Field field, Annotation label) {
         Contact contact = new FieldContact(field, label);
         if(!field.isAccessible()) {
            field.setAccessible(true);
         }
         done.put(field, contact);
      }
      /// <summary>
      /// This is used to remove a field from the map of processed fields.
      /// A field is removed with the <c>Transient</c> annotation
      /// is used to indicate that it should not be processed by the
      /// scanner. This is required when default types are used.
      /// </summary>
      /// <param name="field">
      /// this is the field to be removed from the map
      /// </param>
      /// <param name="label">
      /// this is the label associated with the field
      /// </param>
      public void Remove(Field field, Annotation label) {
         done.Remove(field);
      }
      /// <summary>
      /// This is used to build a list of valid contacts for this scanner.
      /// Valid contacts are fields that are either defaulted or those
      /// that have an explicit XML annotation. Any field that has been
      /// marked as transient will not be considered as valid.
      /// </summary>
      public void Build() {
         for(Contact contact : done) {
            add(contact);
         }
      }
   }
}
