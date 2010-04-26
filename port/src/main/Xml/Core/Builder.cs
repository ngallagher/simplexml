#region License
//
// Builder.cs April 2009
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
   /// The <c>Builder</c> object is used to represent an single
   /// constructor within an object. It contains the actual constructor
   /// as well as the list of parameters. Each builder will score its
   /// weight when given a <c>Criteria</c> object. This allows
   /// the deserialization process to find the most suitable one to
   /// use when instantiating an object.
   /// </summary>
   class Builder {
      /// <summary>
      /// This is the list of parameters in the order of declaration.
      /// </summary>
      private readonly List<Parameter> list;
      /// <summary>
      /// This is the factory that is used to instantiate the object.
      /// </summary>
      private readonly Constructor factory;
      /// <summary>
      /// This is the map that contains the parameters to be used.
      /// </summary>
      private readonly Index index;
      /// <summary>
      /// Constructor for the <c>Builder</c> object. This is used
      /// to create a factory like object used for instantiating objects.
      /// Each builder will score its suitability using the parameters
      /// it is provided.
      /// </summary>
      /// <param name="factory">
      /// this is the factory used for instantiation
      /// </param>
      /// <param name="index">
      /// this is the map of parameters that are declared
      /// </param>
      public Builder(Constructor factory, Index index) {
         this.list = index.Parameters;
         this.factory = factory;
         this.index = index;
      }
      /// <summary>
      /// This is used to determine if this <c>Builder</c> is a
      /// default constructor. If the class does contain a no argument
      /// constructor then this will return true.
      /// </summary>
      /// <returns>
      /// true if the class has a default constructor
      /// </returns>
      public bool IsDefault() {
         return index.size() == 0;
      }
      /// <summary>
      /// This is used to acquire the named <c>Parameter</c> from
      /// the builder. A parameter is taken from the constructor which
      /// contains annotations for each object that is required. These
      /// parameters must have a matching field or method.
      /// </summary>
      /// <param name="name">
      /// this is the name of the parameter to be acquired
      /// </param>
      /// <returns>
      /// this returns the named parameter for the builder
      /// </returns>
      public Parameter GetParameter(String name) {
         return index.get(name);
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
            if(!factory.isAccessible()) {
               factory.setAccessible(true);
            }
            return factory.newInstance();
         }
      }
      //public Object GetInstance() {
      //   if(!factory.isAccessible()) {
      //      factory.setAccessible(true);
      //   }
      //   return factory.newInstance();
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
         Object[] values = list.toArray();
         for(int i = 0; i < list.size(); i++) {
            String name = list.get(i).getName();
            Variable variable = criteria.remove(name);
            Object value = variable.getValue();
            values[i] = value;
         }
         return GetInstance(values);
      }
      /// <summary>
      /// This is used to instantiate the object using a constructor that
      /// takes deserialized objects as arguments. The objects that have
      /// been deserialized are provided in declaration order so they can
      /// be passed to the constructor to instantiate the object.
      /// </summary>
      /// <param name="list">
      /// this is the list of objects used for instantiation
      /// </param>
      /// <returns>
      /// this returns the object that has been instantiated
      /// </returns>
      public Object GetInstance(Object[] list) {
         if(!factory.isAccessible()) {
            factory.setAccessible(true);
         }
         return factory.newInstance(list);
      }
      /// <summary>
      /// This is used to score this <c>Builder</c> object so that
      /// it can be weighed amongst other constructors. The builder that
      /// scores the highest is the one that is used for instantiation.
      /// </summary>
      /// <param name="criteria">
      /// this contains the criteria to be used
      /// </param>
      /// <returns>
      /// this returns the score based on the criteria provided
      /// </returns>
      public int Score(Criteria criteria) {
         int score = 0;
         for(int i = 0; i < list.size(); i++) {
            String name = list.get(i).getName();
            Label label = criteria.get(name);
            if(label == null) {
               return -1;
            }
            score++;
         }
         return score;
      }
      /// <summary>
      /// This is used to acquire a descriptive name for the builder.
      /// Providing a name is useful in debugging and when exceptions are
      /// thrown as it describes the constructor the builder represents.
      /// </summary>
      /// <returns>
      /// this returns the name of the constructor to be used
      /// </returns>
      public String ToString() {
         return factory.ToString();
      }
   }
}
