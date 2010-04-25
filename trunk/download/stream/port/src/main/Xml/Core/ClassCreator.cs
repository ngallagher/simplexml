#region License
//
// ClassCreator.cs December 2009
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
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>ClassCreator</c> is responsible for instantiating
   /// objects using either the default no argument constructor or one
   /// that takes deserialized values as parameters. This also exposes
   /// the parameters and constructors used to instantiate the object.
   /// </summary>
   class ClassCreator : Creator {
      /// <summary>
      /// This contains a list of all the builders for the class.
      /// </summary>
      private readonly List<Builder> list;
      /// <summary>
      /// This represents the default no argument constructor used.
      /// </summary>
      private readonly Builder primary;
      /// <summary>
      /// This is used to acquire a parameter by the parameter name.
      /// </summary>
      private readonly Index index;
      /// <summary>
      /// This is the type this builder creates instances of.
      /// </summary>
      private readonly Class type;
      /// <summary>
      /// Constructor for the <c>ClassCreator</c> object. This is
      /// used to create an object that contains all information that
      /// relates to the construction of an instance.
      /// </summary>
      /// <param name="list">
      /// this contains the list of all constructors
      /// </param>
      /// <param name="index">
      /// this contains all parameters for each constructor
      /// </param>
      /// <param name="primary">
      /// this is the default no argument constructor
      /// </param>
      public ClassCreator(List<Builder> list, Index index, Builder primary) {
         this.type = index.Type;
         this.primary = primary;
         this.index = index;
         this.list = list;
      }
      /// <summary>
      /// This is used to determine if this <c>Creator</c> has a
      /// default constructor. If the class does contain a no argument
      /// constructor then this will return true.
      /// </summary>
      /// <returns>
      /// true if the class has a default constructor
      /// </returns>
      public bool IsDefault() {
         return primary != null;
      }
      /// <summary>
      /// This is used to instantiate the object using the default no
      /// argument constructor. If for some reason the object can not be
      /// instantiated then this will throw an exception with the reason.
      /// </summary>
      /// <returns>
      /// this returns the object that has been instantiated
      /// </returns>
      public Object Instance {
         get {
            return primary.Instance;
         }
      }
      //public Object GetInstance() {
      //   return primary.Instance;
      //}
      /// This is used to instantiate the object using a constructor that
      /// takes deserialized objects as arguments. The object that have
      /// been deserialized can be taken from the <c>Criteria</c>
      /// object which contains the deserialized values.
      /// </summary>
      /// <param name="criteria">
      /// this contains the criteria to be used
      /// </param>
      /// <returns>
      /// this returns the object that has been instantiated
      /// </returns>
      public Object GetInstance(Criteria criteria) {
         Builder builder = GetBuilder(criteria);
         if(builder == null) {
            throw new PersistenceException("Constructor not matched for %s", type);
         }
         return builder.GetInstance(criteria);
      }
      /// <summary>
      /// This is used to acquire a <c>Builder</c> which is used
      /// to instantiate the object. If there is no match for the builder
      /// then the default constructor is provided.
      /// </summary>
      /// <param name="criteria">
      /// this contains the criteria to be used
      /// </param>
      /// <returns>
      /// this returns the builder that has been matched
      /// </returns>
      public Builder GetBuilder(Criteria criteria) {
         Builder result = primary;
         int max = 0;
         for(Builder builder : list) {
            int score = builder.score(criteria);
            if(score > max) {
               result = builder;
               max = score;
            }
         }
         return result;
      }
      /// <summary>
      /// This is used to acquire the named <c>Parameter</c> from
      /// the creator. A parameter is taken from the constructor which
      /// contains annotations for each object that is required. These
      /// parameters must have a matching field or method.
      /// </summary>
      /// <param name="name">
      /// this is the name of the parameter to be acquired
      /// </param>
      /// <returns>
      /// this returns the named parameter for the creator
      /// </returns>
      public Parameter GetParameter(String name) {
         return index.get(name);
      }
      /// <summary>
      /// This is used to acquire all parameters annotated for the class
      /// schema. Providing all parameters ensures that they can be
      /// validated against the annotated methods and fields to ensure
      /// that each parameter is valid and has a corresponding match.
      /// </summary>
      /// <returns>
      /// this returns the parameters declared in the schema
      /// </returns>
      public List<Parameter> Parameters {
         get {
            return index.Parameters;
         }
      }
      //public List<Parameter> GetParameters() {
      //   return index.Parameters;
      //}
      /// This is used to acquire all of the <c>Builder</c> objects
      /// used to create an instance of the object. Each represents a
      /// constructor and contains the parameters to the constructor.
      /// This is primarily used to validate each constructor against the
      /// fields and methods annotated to ensure they are compatible.
      /// </summary>
      /// <returns>
      /// this returns a list of builders for the creator
      /// </returns>
      public List<Builder> Builders {
         get {
            return list;
         }
      }
      //public List<Builder> GetBuilders() {
      //   return list;
      //}
      /// This is used to acquire a description of the creator. This is
      /// useful when debugging an issue as it allows a representation
      /// of the instance to be viewed with the class it represents.
      /// </summary>
      /// <returns>
      /// this returns a visible description of the creator
      /// </returns>
      public String ToString() {
         return String.format("creator for %s", type);
      }
   }
}
