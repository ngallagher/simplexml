#region License
//
// MethodScanner.cs April 2007
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
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   import static SimpleFramework.Xml.DefaultType.PROPERTY;
   /// <summary>
   /// The <c>MethodScanner</c> object is used to scan an object
   /// for matching get and set methods for an XML annotation. This will
   /// scan for annotated methods starting with the most specialized
   /// class up the class hierarchy. Thus, annotated methods can be
   /// overridden in a type specialization.
   /// <p>
   /// The annotated methods must be either a getter or setter method
   /// following the Java Beans naming conventions. This convention is
   /// such that a method must begin with "get", "set", or "is". A pair
   /// of set and get methods for an annotation must make use of the
   /// same type. For instance if the return type for the get method
   /// was <c>String</c> then the set method must have a single
   /// argument parameter that takes a <c>String</c> type.
   /// <p>
   /// For a method to be considered there must be both the get and set
   /// methods. If either method is missing then the scanner fails with
   /// an exception. Also, if an annotation marks a method which does
   /// not follow Java Bean naming conventions an exception is thrown.
   /// </summary>
   class MethodScanner : ContactList {
      /// <summary>
      /// This is a factory used for creating property method parts.
      /// </summary>
      private readonly MethodPartFactory factory;
      /// <summary>
      /// This is used to acquire the hierarchy for the class scanned.
      /// </summary>
      private readonly Hierarchy hierarchy;
      /// <summary>
      /// This is the default access type to be used for this scanner.
      /// </summary>
      private readonly DefaultType access;
      /// <summary>
      /// This is used to collect all the set methods from the object.
      /// </summary>
      private readonly PartMap write;
      /// <summary>
      /// This is used to collect all the get methods from the object.
      /// </summary>
      private readonly PartMap read;
      /// <summary>
      /// This is the type of the object that is being scanned.
      /// </summary>
      private readonly Class type;
      /// <summary>
      /// Constructor for the <c>MethodScanner</c> object. This is
      /// used to create an object that will scan the specified class
      /// such that all bean property methods can be paired under the
      /// XML annotation specified within the class.
      /// </summary>
      /// <param name="type">
      /// this is the type that is to be scanned for methods
      /// </param>
      public MethodScanner(Class type) {
         this(type, null);
      }
      /// <summary>
      /// Constructor for the <c>MethodScanner</c> object. This is
      /// used to create an object that will scan the specified class
      /// such that all bean property methods can be paired under the
      /// XML annotation specified within the class.
      /// </summary>
      /// <param name="type">
      /// this is the type that is to be scanned for methods
      /// </param>
      public MethodScanner(Class type, DefaultType access) {
         this.factory = new MethodPartFactory();
         this.hierarchy = new Hierarchy(type);
         this.write = new PartMap();
         this.read = new PartMap();
         this.access = access;
         this.type = type;
         this.Scan(type);
      }
      /// <summary>
      /// This method is used to scan the class hierarchy for each class
      /// in order to extract methods that contain XML annotations. If
      /// a method is annotated it is converted to a contact so that
      /// it can be used during serialization and deserialization.
      /// </summary>
      /// <param name="type">
      /// this is the type to be scanned for methods
      /// </param>
      public void Scan(Class type) {
         for(Class next : hierarchy) {
            Scan(next, access);
         }
         for(Class next : hierarchy) {
            Scan(next, type);
         }
         Build();
         Validate();
      }
      /// <summary>
      /// This is used to scan the declared methods within the specified
      /// class. Each method will be checked to determine if it contains
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
         Method[] list = type.getDeclaredMethods();
         for(Method method : list) {
            Scan(method);
         }
      }
      /// <summary>
      /// This is used to scan all annotations within the given method.
      /// Each annotation is checked against the set of supported XML
      /// annotations. If the annotation is one of the XML annotations
      /// then the method is considered for acceptance as either a
      /// get method or a set method for the annotated property.
      /// </summary>
      /// <param name="method">
      /// the method to be scanned for XML annotations
      /// </param>
      public void Scan(Method method) {
         Annotation[] list = method.getDeclaredAnnotations();
         for(Annotation label : list) {
            Scan(method, label);
         }
      }
      /// <summary>
      /// This is used to scan all the methods of the class in order to
      /// determine if it should have a default annotation. If the method
      /// should have a default XML annotation then it is added to the
      /// list of contacts to be used to form the class schema.
      /// </summary>
      /// <param name="type">
      /// this is the type to have its methods scanned
      /// </param>
      /// <param name="access">
      /// this is the default access type for the class
      /// </param>
      public void Scan(Class type, DefaultType access) {
         Method[] list = type.getDeclaredMethods();
         if(access == PROPERTY) {
            for(Method method : list) {
               Class value = factory.GetType(method);
               if(value != null) {
                  Process(method);
               }
            }
         }
      }
      /// <summary>
      /// This reflectively checks the annotation to determine the type
      /// of annotation it represents. If it represents an XML schema
      /// annotation it is used to create a <c>Contact</c> which
      /// can be used to represent the method within the source object.
      /// </summary>
      /// <param name="method">
      /// the method that the annotation comes from
      /// </param>
      /// <param name="label">
      /// the annotation used to model the XML schema
      /// </param>
      public void Scan(Method method, Annotation label) {
         if(label instanceof Attribute) {
            Process(method, label);
         }
         if(label instanceof ElementList) {
            Process(method, label);
         }
         if(label instanceof ElementArray) {
            Process(method, label);
         }
         if(label instanceof ElementMap) {
            Process(method, label);
         }
         if(label instanceof Element) {
            Process(method, label);
         }
         if(label instanceof Transient) {
            Remove(method, label);
         }
         if(label instanceof Version) {
            Process(method, label);
         }
         if(label instanceof Text) {
            Process(method, label);
         }
      }
      /// <summary>
      /// This is used to classify the specified method into either a get
      /// or set method. If the method is neither then an exception is
      /// thrown to indicate that the XML annotations can only be used
      /// with methods following the Java Bean naming conventions. Once
      /// the method is classified is is added to either the read or
      /// write map so that it can be paired after scanning is complete.
      /// </summary>
      /// <param name="method">
      /// this is the method that is to be classified
      /// </param>
      /// <param name="label">
      /// this is the annotation applied to the method
      /// </param>
      public void Process(Method method, Annotation label) {
         MethodPart part = factory.GetInstance(method, label);
         MethodType type = part.GetMethodType();
         if(type == MethodType.GET) {
            Process(part, read);
         }
         if(type == MethodType.IS) {
            Process(part, read);
         }
         if(type == MethodType.SET) {
            Process(part, write);
         }
      }
      /// <summary>
      /// This is used to classify the specified method into either a get
      /// or set method. If the method is neither then an exception is
      /// thrown to indicate that the XML annotations can only be used
      /// with methods following the Java Bean naming conventions. Once
      /// the method is classified is is added to either the read or
      /// write map so that it can be paired after scanning is complete.
      /// </summary>
      /// <param name="method">
      /// this is the method that is to be classified
      /// </param>
      public void Process(Method method) {
         MethodPart part = factory.GetInstance(method);
         MethodType type = part.GetMethodType();
         if(type == MethodType.GET) {
            Process(part, read);
         }
         if(type == MethodType.IS) {
            Process(part, read);
         }
         if(type == MethodType.SET) {
            Process(part, write);
         }
      }
      /// <summary>
      /// This is used to determine whether the specified method can be
      /// inserted into the given <c>PartMap</c>. This ensures
      /// that only the most specialized method is considered, which
      /// enables annotated methods to be overridden in subclasses.
      /// </summary>
      /// <param name="method">
      /// this is the method part that is to be inserted
      /// </param>
      /// <param name="map">
      /// this is the part map used to contain the method
      /// </param>
      public void Process(MethodPart method, PartMap map) {
         String name = method.GetName();
         if(name != null) {
            map.put(name, method);
         }
      }
      /// <summary>
      /// This method is used to remove a particular method from the list
      /// of contacts. If the <c>Transient</c> annotation is used
      /// by any method then this method must be removed from the schema.
      /// In particular it is important to remove methods if there are
      /// defaults applied to the class.
      /// </summary>
      /// <param name="method">
      /// this is the method that is to be removed
      /// </param>
      /// <param name="label">
      /// this is the label associated with the method
      /// </param>
      public void Remove(Method method, Annotation label) {
         MethodPart part = factory.GetInstance(method, label);
         MethodType type = part.GetMethodType();
         if(type == MethodType.GET) {
            Remove(part, read);
         }
         if(type == MethodType.IS) {
            Remove(part, read);
         }
         if(type == MethodType.SET) {
            Remove(part, write);
         }
      }
      /// <summary>
      /// This is used to remove the method part from the specified map.
      /// Removal is performed using the name of the method part. If it
      /// has been scanned and added to the map then it will be removed
      /// and will not form part of the class schema.
      /// </summary>
      /// <param name="part">
      /// this is the part to be removed from the map
      /// </param>
      /// <param name="map">
      /// this is the map to removed the method part from
      /// </param>
      public void Remove(MethodPart part, PartMap map) {
         String name = part.GetName();
         if(name != null) {
            map.Remove(name);
         }
      }
      /// <summary>
      /// This method is used to pair the get methods with a matching set
      /// method. This pairs methods using the Java Bean method name, the
      /// names must match exactly, meaning that the case and value of
      /// the strings must be identical. Also in order for this to succeed
      /// the types for the methods and the annotation must also match.
      /// </summary>
      public void Build() {
         for(String name : read) {
            MethodPart part = read.get(name);
            if(part != null) {
               Build(part, name);
            }
         }
      }
      /// <summary>
      /// This method is used to pair the get methods with a matching set
      /// method. This pairs methods using the Java Bean method name, the
      /// names must match exactly, meaning that the case and value of
      /// the strings must be identical. Also in order for this to succeed
      /// the types for the methods and the annotation must also match.
      /// </summary>
      /// <param name="read">
      /// this is a get method that has been extracted
      /// </param>
      /// <param name="name">
      /// this is the Java Bean methods name to be matched
      /// </param>
      public void Build(MethodPart read, String name) {
         MethodPart match = write.Take(name);
         if(match != null) {
            Build(read, match);
         } else {
            Build(read); // read only
         }
      }
      /// <summary>
      /// This method is used to create a read only contact. A read only
      /// contact object is used when there is constructor injection used
      /// by the class schema. So, read only methods can be used in a
      /// fully serializable and deserializable object.
      /// </summary>
      /// <param name="read">
      /// this is the part to add as a read only contact
      /// </param>
      public void Build(MethodPart read) {
         add(new MethodContact(read));
      }
      /// <summary>
      /// This method is used to pair the get methods with a matching set
      /// method. This pairs methods using the Java Bean method name, the
      /// names must match exactly, meaning that the case and value of
      /// the strings must be identical. Also in order for this to succeed
      /// the types for the methods and the annotation must also match.
      /// </summary>
      /// <param name="read">
      /// this is a get method that has been extracted
      /// </param>
      /// <param name="write">
      /// this is the write method to compare details with
      /// </param>
      public void Build(MethodPart read, MethodPart write) {
         Annotation label = read.GetAnnotation();
         String name = read.GetName();
         if(!write.GetAnnotation().equals(label)) {
            throw new MethodException("Annotations do not match for '%s' in %s", name, type);
         }
         Class type = read.GetType();
         if(type != write.GetType()) {
            throw new MethodException("Method types do not match for %s in %s", name, type);
         }
         add(new MethodContact(read, write));
      }
      /// <summary>
      /// This is used to validate the object once all the get methods
      /// have been matched with a set method. This ensures that there
      /// is not a set method within the object that does not have a
      /// match, therefore violating the contract of a property.
      /// </summary>
      public void Validate() {
         for(String name : write) {
            MethodPart part = write.get(name);
            if(part != null) {
               Validate(part, name);
            }
         }
      }
      /// <summary>
      /// This is used to validate the object once all the get methods
      /// have been matched with a set method. This ensures that there
      /// is not a set method within the object that does not have a
      /// match, therefore violating the contract of a property.
      /// </summary>
      /// <param name="write">
      /// this is a get method that has been extracted
      /// </param>
      /// <param name="name">
      /// this is the Java Bean methods name to be matched
      /// </param>
      public void Validate(MethodPart write, String name) {
         MethodPart match = read.Take(name);
         Method method = write.getMethod();
         if(match == null) {
            throw new MethodException("No matching get method for %s in %s", method, type);
         }
      }
      /// <summary>
      /// The <c>PartMap</c> is used to contain method parts using
      /// the Java Bean method name for the part. This ensures that the
      /// scanned and extracted methods can be acquired using a common
      /// name, which should be the parsed Java Bean method name.
      /// </summary>
      /// <seealso>
      /// SimpleFramework.Xml.Core.MethodPart
      /// </seealso>
      private class PartMap : LinkedHashMap<String, MethodPart> : Iterable<String>{
         /// <summary>
         /// This returns an iterator for the Java Bean method names for
         /// the <c>MethodPart</c> objects that are stored in the
         /// map. This allows names to be iterated easily in a for loop.
         /// </summary>
         /// <returns>
         /// this returns an iterator for the method name keys
         /// </returns>
         public Iterator<String> Iterator() {
            return keySet().Iterator();
         }
         /// <summary>
         /// This is used to acquire the method part for the specified
         /// method name. This will remove the method part from this map
         /// so that it can be checked later to ensure what remains.
         /// </summary>
         /// <param name="name">
         /// this is the method name to get the method with
         /// </param>
         /// <returns>
         /// this returns the method part for the given key
         /// </returns>
         public MethodPart Take(String name) {
            return Remove(name);
         }
      }
   }
}
