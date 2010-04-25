#region License
//
// ClassScanner.cs July 2008
//
// Copyright (C) 2008, Niall Gallagher <niallg@users.sf.net>
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
   /// The <c>ClassScanner</c> performs the reflective inspection
   /// of a class and extracts all the class level annotations. This will
   /// also extract the methods that are annotated. This ensures that the
   /// callback methods can be invoked during the deserialization process.
   /// Also, this will read the namespace annotations that are used.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Scanner
   /// </seealso>
   class ClassScanner  {
      /// <summary>
      /// This is the namespace decorator associated with this scanner.
      /// </summary>
      private NamespaceDecorator decorator;
      /// <summary>
      /// This is the scanner that is used to acquire the constructors.
      /// </summary>
      private ConstructorScanner scanner;
      /// <summary>
      /// This is the namespace associated with the scanned class.
      /// </summary>
      private Namespace namespace;
      /// <summary>
      /// This function acts as a pointer to the types commit process.
      /// </summary>
      private Function commit;
      /// <summary>
      /// This function acts as a pointer to the types validate process.
      /// </summary>
      private Function validate;
      /// <summary>
      /// This function acts as a pointer to the types persist process.
      /// </summary>
      private Function persist;
      /// <summary>
      /// This function acts as a pointer to the types complete process.
      /// </summary>
      private Function complete;
      /// <summary>
      /// This function is used as a pointer to the replacement method.
      /// </summary>
      private Function replace;
      /// <summary>
      /// This function is used as a pointer to the resolution method.
      /// </summary>
      private Function resolve;
      /// <summary>
      /// This is used to determine if there is a default type for this.
      /// </summary>
      private Default access;
      /// <summary>
      /// This is the optional order annotation for the scanned class.
      /// </summary>
      private Order order;
      /// <summary>
      /// This is the optional root annotation for the scanned class.
      /// </summary>
      private Root root;
      /// <summary>
      /// Constructor for the <c>ClassScanner</c> object. This is
      /// used to scan the provided class for annotations that are used
      /// to build a schema for an XML file to follow.
      /// </summary>
      /// <param name="type">
      /// this is the type that is scanned for a schema
      /// </param>
      public ClassScanner(Class type) {
         this.scanner = new ConstructorScanner(type);
         this.decorator = new NamespaceDecorator();
         this.Scan(type);
      }
      /// <summary>
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
            return decorator;
         }
      }
      //public Decorator GetDecorator() {
      //   return decorator;
      //}
      /// This is used to provide the <c>Default</c> annotation
      /// that holds the default access type to use for the class. This
      /// is important in determining how the serializer will deal with
      /// methods or fields in the class that do not have annotations.
      /// </summary>
      /// <returns>
      /// this returns the default annotation for access type
      /// </returns>
      public Default Default {
         get {
            return access;
         }
      }
      //public Default GetDefault() {
      //   return access;
      //}
      /// This returns the order annotation used to determine the order
      /// of serialization of attributes and elements. The order is a
      /// class level annotation that can be used only once per class
      /// XML schema. If none exists then this will return null.
      ///  of the class processed by this scanner.
      /// </summary>
      /// <returns>
      /// this returns the name of the object being scanned
      /// </returns>
      public Order Order {
         get {
            return order;
         }
      }
      //public Order GetOrder() {
      //   return order;
      //}
      /// This returns the root of the class processed by this scanner.
      /// The root determines the type of deserialization that is to
      /// be performed and also contains the name of the root element.
      /// </summary>
      /// <returns>
      /// this returns the name of the object being scanned
      /// </returns>
      public Root Root {
         get {
            return root;
         }
      }
      //public Root GetRoot() {
      //   return root;
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
            return commit;
         }
      }
      //public Function GetCommit() {
      //   return commit;
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
            return validate;
         }
      }
      //public Function GetValidate() {
      //   return validate;
      //}
      /// This method is used to retrieve the schema class persistence
      /// method. This is invoked during the serialization process to
      /// get the object a chance to perform an necessary preparation
      /// before the serialization of the object proceeds. The persist
      /// method must be marked with the <c>Persist</c> annotation.
      /// </summary>
      /// <returns>
      /// this returns the persist method for the schema class
      /// </returns>
      public Function Persist {
         get {
            return persist;
         }
      }
      //public Function GetPersist() {
      //   return persist;
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
            return complete;
         }
      }
      //public Function GetComplete() {
      //   return complete;
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
            return replace;
         }
      }
      //public Function GetReplace() {
      //   return replace;
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
            return resolve;
         }
      }
      //public Function GetResolve() {
      //   return resolve;
      //}
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
         if(root != null) {
            return root.strict();
         }
         return true;
      }
      /// <summary>
      /// Scan the fields and methods such that the given class is scanned
      /// first then all super classes up to the root <c>Object</c>.
      /// All fields and methods from the most specialized classes override
      /// fields and methods from higher up the inheritance hierarchy. This
      /// means that annotated details can be overridden.
      /// </summary>
      /// <param name="type">
      /// the class to extract method and class annotations
      /// </param>
      public void Scan(Class type) {
         Class real = type;
         while(type != null) {
            Global(type);
            Scope(type);
            Scan(real, type);
            type = type.getSuperclass();
         }
         Process(real);
      }
      /// <summary>
      /// This is used to acquire the annotations that apply globally to
      /// the scanned class. Global annotations are annotations that
      /// are applied to the class, such annotations will be used to
      /// determine characteristics for the fields and methods of the
      /// class, which the serializer uses in the serialization process.
      /// </summary>
      /// <param name="type">
      /// this is the type to extract the annotations from
      /// </param>
      public void Global(Class type) {
         if(namespace == null) {
            Namespace(type);
         }
         if(root == null) {
            Root(type);
         }
         if(order == null) {
            Order(type);
         }
         if(access == null) {
            Access(type);
         }
      }
      /// <summary>
      /// This is used to scan the specified class for methods so that
      /// the persister callback annotations can be collected. These
      /// annotations help object implementations to validate the data
      /// that is injected into the instance during deserialization.
      /// </summary>
      /// <param name="real">
      /// this is the actual type of the scanned class
      /// </param>
      /// <param name="type">
      /// this is a type from within the class hierarchy
      /// </param>
      public void Scan(Class real, Class type) {
         Method[] method = type.getDeclaredMethods();
         for(int i = 0; i < method.length; i++) {
            Scan(method[i]);
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
         if(type.isAnnotationPresent(Root.class)) {
            root = type.getAnnotation(Root.class);
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
         if(type.isAnnotationPresent(Order.class)) {
            order = type.getAnnotation(Order.class);
         }
      }
      /// <summary>
      /// This is used to extract the <c>Default</c> annotation from
      /// the class. If this annotation is present it provides the access
      /// type that should be used to determine default fields and methods.
      /// If it is not present no default annotations will be applied.
      /// </summary>
      /// <param name="type">
      /// this is the type to extract the annotation from
      /// </param>
      public void Access(Class<?> type) {
         if(type.isAnnotationPresent(Default.class)) {
            access = type.getAnnotation(Default.class);
         }
      }
      /// <summary>
      /// This is use to scan for <c>Namespace</c> annotations on
      /// the class. Once a namespace has been located then it is used
      /// to populate the internal namespace decorator. This can then be
      /// used to decorate any output node that requires it.
      /// </summary>
      /// <param name="type">
      /// this is the XML schema class to scan for namespaces
      /// </param>
      public void Namespace(Class<?> type) {
         if(type.isAnnotationPresent(Namespace.class)) {
            namespace = type.getAnnotation(Namespace.class);
            if(namespace != null) {
               decorator.Add(namespace);
            }
         }
      }
      /// <summary>
      /// This is use to scan for <c>NamespaceList</c> annotations
      /// on the class. Once a namespace list has been located then it is
      /// used to populate the internal namespace decorator. This can then
      /// be used to decorate any output node that requires it.
      /// </summary>
      /// <param name="type">
      /// this is the XML class to scan for namespace lists
      /// </param>
      public void Scope(Class<?> type) {
         if(type.isAnnotationPresent(NamespaceList.class)) {
            NamespaceList scope = type.getAnnotation(NamespaceList.class);
            Namespace[] list = scope.value();
            for(Namespace name : list) {
               decorator.Add(name);
            }
         }
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
      public void Process(Class type) {
         if(namespace != null) {
            decorator. = namespace;
         }
      }
      /// <summary>
      /// Scans the provided method for a persister callback method. If
      /// the method contains an method annotated as a callback that
      /// method is stored so that it can be invoked by the persister
      /// during the serialization and deserialization process.
      /// </summary>
      /// <param name="method">
      /// this is the method to scan for callback methods
      /// </param>
      public void Scan(Method method) {
         if(commit == null) {
            Commit(method);
         }
         if(validate == null) {
            Validate(method);
         }
         if(persist == null) {
            Persist(method);
         }
         if(complete == null) {
            Complete(method);
         }
         if(replace == null) {
            Replace(method);
         }
         if(resolve == null) {
            Resolve(method);
         }
      }
      /// <summary>
      /// This method is used to check the provided method to determine
      /// if it contains the <c>Replace</c> annotation. If the
      /// method contains the required annotation it is stored so that
      /// it can be invoked during the deserialization process.
      /// </summary>
      /// <param name="method">
      /// this is the method checked for the annotation
      /// </param>
      public void Replace(Method method) {
         Annotation mark = method.getAnnotation(Replace.class);
         if(mark != null) {
            replace = GetFunction(method);
         }
      }
      /// <summary>
      /// This method is used to check the provided method to determine
      /// if it contains the <c>Resolve</c> annotation. If the
      /// method contains the required annotation it is stored so that
      /// it can be invoked during the deserialization process.
      /// </summary>
      /// <param name="method">
      /// this is the method checked for the annotation
      /// </param>
      public void Resolve(Method method) {
         Annotation mark = method.getAnnotation(Resolve.class);
         if(mark != null) {
            resolve = GetFunction(method);
         }
      }
      /// <summary>
      /// This method is used to check the provided method to determine
      /// if it contains the <c>Commit</c> annotation. If the
      /// method contains the required annotation it is stored so that
      /// it can be invoked during the deserialization process.
      /// </summary>
      /// <param name="method">
      /// this is the method checked for the annotation
      /// </param>
      public void Commit(Method method) {
         Annotation mark = method.getAnnotation(Commit.class);
         if(mark != null) {
            commit = GetFunction(method);
         }
      }
      /// <summary>
      /// This method is used to check the provided method to determine
      /// if it contains the <c>Validate</c> annotation. If the
      /// method contains the required annotation it is stored so that
      /// it can be invoked during the deserialization process.
      /// </summary>
      /// <param name="method">
      /// this is the method checked for the annotation
      /// </param>
      public void Validate(Method method) {
         Annotation mark = method.getAnnotation(Validate.class);
         if(mark != null) {
            validate = GetFunction(method);
         }
      }
      /// <summary>
      /// This method is used to check the provided method to determine
      /// if it contains the <c>Persist</c> annotation. If the
      /// method contains the required annotation it is stored so that
      /// it can be invoked during the deserialization process.
      /// </summary>
      /// <param name="method">
      /// this is the method checked for the annotation
      /// </param>
      public void Persist(Method method) {
         Annotation mark = method.getAnnotation(Persist.class);
         if(mark != null) {
            persist = GetFunction(method);
         }
      }
      /// <summary>
      /// This method is used to check the provided method to determine
      /// if it contains the <c>Complete</c> annotation. If the
      /// method contains the required annotation it is stored so that
      /// it can be invoked during the deserialization process.
      /// </summary>
      /// <param name="method">
      /// this is the method checked for the annotation
      /// </param>
      public void Complete(Method method) {
         Annotation mark = method.getAnnotation(Complete.class);
         if(mark != null) {
            complete = GetFunction(method);
         }
      }
      /// <summary>
      /// This is used to acquire a <c>Function</c> object for the
      /// method provided. The function returned will allow the callback
      /// method to be invoked when given the context and target object.
      /// </summary>
      /// <param name="method">
      /// this is the method that is to be invoked
      /// </param>
      /// <returns>
      /// this returns the function that is to be invoked
      /// </returns>
      public Function GetFunction(Method method) {
         bool contextual = IsContextual(method);
         if(!method.isAccessible()) {
            method.setAccessible(true);
         }
         return new Function(method, contextual);
      }
      /// <summary>
      /// This is used to determine whether the annotated method takes a
      /// contextual object. If the method takes a <c>Map</c> then
      /// this returns true, otherwise it returns false.
      /// </summary>
      /// <param name="method">
      /// this is the method to check the parameters of
      /// </param>
      /// <returns>
      /// this returns true if the method takes a map object
      /// </returns>
      public bool IsContextual(Method method) {
         Class[] list = method.getParameterTypes();
         if(list.length == 1) {
            return Map.class.equals(list[0]);
         }
         return false;
      }
   }
}
