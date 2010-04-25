#region License
//
// Collector.cs December 2007
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
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Collector</c> object is used to store variables for
   /// a deserialized object. Each variable contains the label and value
   /// for a field or method. The <c>Composite</c> object uses
   /// this to store deserialized values before committing them to the
   /// objects methods and fields.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Composite
   /// </seealso>
   class Collector : Criteria {
      /// <summary>
      /// This is the registry containing all the variables collected.
      /// </summary>
      private readonly Registry registry;
      /// <summary>
      /// This is the context object used by the serialization process.
      /// </summary>
      private readonly Context context;
      /// <summary>
      /// Constructor for the <c>Collector</c> object. This is
      /// used to store variables for an objects fields and methods.
      /// Each variable is stored using the name of the label.
      /// </summary>
      /// <param name="context">
      /// this is the context for the deserialization
      /// </param>
      public Collector(Context context) {
         this.registry = new Registry();
         this.context = context;
      }
      /// <summary>
      /// This is used to get the <c>Variable</c> that represents
      /// a deserialized object. The variable contains all the meta
      /// data for the field or method and the value that is to be set
      /// on the method or field.
      /// </summary>
      /// <param name="name">
      /// this is the name of the variable to be acquired
      /// </param>
      /// <returns>
      /// this returns the named variable if it exists
      /// </returns>
      public Variable Get(String name) {
         return registry.Get(name);
      }
      /// <summary>
      /// This is used to remove the <c>Variable</c> from this
      /// criteria object. When removed, the variable will no longer be
      /// used to set the method or field when the <c>commit</c>
      /// method is invoked.
      /// </summary>
      /// <param name="name">
      /// this is the name of the variable to be removed
      /// </param>
      /// <returns>
      /// this returns the named variable if it exists
      /// </returns>
      public Variable Remove(String name) {
         return registry.Remove(name);
      }
      /// <summary>
      /// This is used to acquire an iterator over the named variables.
      /// Providing an <c>Iterator</c> allows the criteria to be
      /// used in a for each loop. This is primarily for convenience.
      /// </summary>
      /// <returns>
      /// this returns an iterator of all the variable names
      /// </returns>
      public Iterator<String> Iterator() {
         return registry.Iterator();
      }
      /// <summary>
      /// This is used to create a <c>Variable</c> and set it for
      /// this criteria. The variable can be retrieved at a later stage
      /// using the name of the label. This allows for repeat reads as
      /// the variable can be used to acquire the labels converter.
      /// </summary>
      /// <param name="label">
      /// this is the label used to create the variable
      /// </param>
      /// <param name="value">
      /// this is the value of the object to be read
      /// </param>
      public void Set(Label label, Object value) {
         Variable variable = new Variable(label, value);
         if(label != null) {
            String name = label.GetName(context);
            String real = label.GetName();
            if(!registry.containsKey(name)) {
               registry.put(real, variable);
               registry.put(name, variable);
            }
         }
      }
      /// <summary>
      /// This is used to set the values for the methods and fields of
      /// the specified object. Invoking this performs the population
      /// of an object being deserialized. It ensures that each value
      /// is set after the XML element has been fully read.
      /// </summary>
      /// <param name="source">
      /// this is the object that is to be populated
      /// </param>
      public void Commit(Object source) {
         Collection<Variable> set = registry.values();
         for(Variable entry : set) {
            Contact contact = entry.getContact();
            Object value = entry.getValue();
            contact.Set(source, value);
         }
      }
      /// <summary>
      /// The <c>Registry</c> object is used to store variables
      /// for the collector. All variables are stored under its name so
      /// that they can be later retrieved and used to populate the
      /// object when deserialization of all variables has finished.
      /// </summary>
      private class Registry : HashMap<String, Variable> {
         /// <summary>
         /// This is used to iterate over the names of the variables
         /// in the registry. This is primarily used for convenience
         /// so that the variables can be acquired in a for each loop.
         /// </summary>
         /// <returns>
         /// an iterator containing the names of the variables
         /// </returns>
         public Iterator<String> Iterator() {
            return keySet().Iterator();
         }
      }
   }
}
