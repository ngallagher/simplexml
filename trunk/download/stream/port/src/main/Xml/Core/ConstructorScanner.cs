#region License
//
// ConstructorScanner.cs July 2009
//
// Copyright (C) 2009, Niall Gallagher <niallg@users.sf.net>
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
   /// The <c>ConstructorScanner</c> object is used to scan all
   /// all constructors that have XML annotations for their parameters.
   /// parameters. Each constructor scanned is converted in to a
   /// <c>Builder</c> object. In order to ensure consistency
   /// amongst the annotated parameters each named parameter must have
   /// the exact same type and annotation attributes across the
   /// constructors. This ensures a consistent XML representation.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Scanner
   /// </seealso>
   class ConstructorScanner {
      /// <summary>
      /// This contains a list of all the builders for the class.
      /// </summary>
      private List<Builder> list;
      /// <summary>
      /// This represents the default no argument constructor used.
      /// </summary>
      private Builder primary;
      /// <summary>
      /// This is used to acquire a parameter by the parameter name.
      /// </summary>
      private Index index;
      /// <summary>
      /// This is the type that is scanner for annotated constructors.
      /// </summary>
      private Class type;
      /// <summary>
      /// Constructor for the <c>ConstructorScanner</c> object.
      /// This is used to scan the specified class for constructors that
      /// can be used to instantiate the class. Only constructors that
      /// have all parameters annotated will be considered.
      /// </summary>
      /// <param name="type">
      /// this is the type that is to be scanned
      /// </param>
      public ConstructorScanner(Class type) {
         this.list = new ArrayList<Builder>();
         this.index = new Index(type);
         this.type = type;
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
            return new ClassCreator(list, index, primary);
         }
      }
      //public Creator GetCreator() {
      //   return new ClassCreator(list, index, primary);
      //}
      /// This is used to scan the specified class for constructors that
      /// can be used to instantiate the class. Only constructors that
      /// have all parameters annotated will be considered.
      /// </summary>
      /// <param name="type">
      /// this is the type that is to be scanned
      /// </param>
      public void Scan(Class type) {
         Constructor[] array = type.getDeclaredConstructors();
         if(!IsInstantiable(type)) {
            throw new ConstructorException("Can not construct inner %s", type);
         }
         for(Constructor factory: array){
            Index index = new Index(type);
            if(!type.isPrimitive()) {
               Scan(factory, index);
            }
         }
      }
      /// <summary>
      /// This is used to scan the specified constructor for annotations
      /// that it contains. Each parameter annotation is evaluated and
      /// if it is an XML annotation it is considered to be a valid
      /// parameter and is added to the parameter map.
      /// </summary>
      /// <param name="factory">
      /// this is the constructor that is to be scanned
      /// </param>
      /// <param name="map">
      /// this is the parameter map that contains parameters
      /// </param>
      public void Scan(Constructor factory, Index map) {
         Annotation[][] labels = factory.getParameterAnnotations();
         Class[] types = factory.getParameterTypes();
         for(int i = 0; i < types.length; i++) {
            for(int j = 0; j < labels[i].length; j++) {
               Parameter value = Process(factory, labels[i][j], i);
               if(value != null) {
                  String name = value.getName();
                  if(map.containsKey(name)) {
                     throw new PersistenceException("Parameter '%s' is a duplicate in %s", name, factory);
                  }
                  index.put(name, value);
                  map.put(name, value);
               }
            }
         }
         if(types.length == map.size()) {
            Build(factory, map);
         }
      }
      /// <summary>
      /// This is used to build the <c>Builder</c> object that is
      /// to be used to instantiate the object. The builder contains
      /// the constructor at the parameters in the declaration order.
      /// </summary>
      /// <param name="factory">
      /// this is the constructor that is to be scanned
      /// </param>
      /// <param name="map">
      /// this is the parameter map that contains parameters
      /// </param>
      public void Build(Constructor factory, Index map) {
         Builder builder = new Builder(factory, map);
         if(builder.isDefault()) {
            primary = builder;
         }
         list.add(builder);
      }
      /// <summary>
      /// This is used to create a <c>Parameter</c> object which is
      /// used to represent a parameter to a constructor. Each parameter
      /// contains an annotation an the index it appears in.
      /// </summary>
      /// <param name="factory">
      /// this is the constructor the parameter is in
      /// </param>
      /// <param name="label">
      /// this is the annotation used for the parameter
      /// </param>
      /// <param name="ordinal">
      /// this is the position the parameter appears at
      /// </param>
      /// <returns>
      /// this returns the parameter for the constructor
      /// </returns>
      public Parameter Process(Constructor factory, Annotation label, int ordinal) {
         if(label instanceof Attribute) {
            return Create(factory, label, ordinal);
         }
         if(label instanceof ElementList) {
            return Create(factory, label, ordinal);
         }
         if(label instanceof ElementArray) {
            return Create(factory, label, ordinal);
         }
         if(label instanceof ElementMap) {
            return Create(factory, label, ordinal);
         }
         if(label instanceof Element) {
            return Create(factory, label, ordinal);
         }
         return null;
      }
      /// <summary>
      /// This is used to create a <c>Parameter</c> object which is
      /// used to represent a parameter to a constructor. Each parameter
      /// contains an annotation an the index it appears in.
      /// </summary>
      /// <param name="factory">
      /// this is the constructor the parameter is in
      /// </param>
      /// <param name="label">
      /// this is the annotation used for the parameter
      /// </param>
      /// <param name="ordinal">
      /// this is the position the parameter appears at
      /// </param>
      /// <returns>
      /// this returns the parameter for the constructor
      /// </returns>
      public Parameter Create(Constructor factory, Annotation label, int ordinal) {
         Parameter value = ParameterFactory.getInstance(factory, label, ordinal);
         String name = value.getName();
         if(index.containsKey(name)) {
            Validate(value, name);
         }
         return value;
      }
      /// <summary>
      /// This is used to validate the parameter against all the other
      /// parameters for the class. Validating each of the parameters
      /// ensures that the annotations for the parameters remain
      /// consistent throughout the class.
      /// </summary>
      /// <param name="parameter">
      /// this is the parameter to be validated
      /// </param>
      /// <param name="name">
      /// this is the name of the parameter to validate
      /// </param>
      public void Validate(Parameter parameter, String name) {
         Parameter other = index.get(name);
         Annotation label = other.getAnnotation();
         if(!parameter.getAnnotation().equals(label)) {
            throw new MethodException("Annotations do not match for '%s' in %s", name, type);
         }
         Class expect = other.Type;
         if(expect != parameter.Type) {
            throw new MethodException("Method types do not match for '%s' in %s", name, type);
         }
      }
      /// <summary>
      /// This is used to determine if the class is an inner class. If
      /// the class is a inner class and not static then this returns
      /// false. Only static inner classes can be instantiated using
      /// reflection as they do not require a "this" argument.
      /// </summary>
      /// <param name="type">
      /// this is the class that is to be evaluated
      /// </param>
      /// <returns>
      /// this returns true if the class is a static inner
      /// </returns>
      public bool IsInstantiable(Class type) {
         int modifiers = type.getModifiers();
         if(Modifier.isStatic(modifiers)) {
            return true;
         }
         return !type.isMemberClass();
      }
   }
}
