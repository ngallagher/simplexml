#region License
//
// Comparer.cs December 2009
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
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Comparer</c> is used to compare annotations on the
   /// attributes of that annotation. Unlike the <c>equals</c>
   /// method, this can ignore some attributes based on the name of the
   /// attributes. This is useful if some annotations have overridden
   /// values, such as the field or method name.
   /// </summary>
   class Comparer {
      /// <summary>
      /// This is the default attribute to ignore for the comparer.
      /// </summary>
      private const String NAME = "name";
      /// <summary>
      /// This is the list of names to ignore for this instance.
      /// </summary>
      private readonly String[] ignore;
      /// <summary>
      /// Constructor for the <c>Comparer</c> object. This is
      /// used to create a comparer that has a default set of names
      /// to be ignored during the comparison of annotations.
      /// </summary>
      public Comparer() {
         this(NAME);
      }
      /// <summary>
      /// Constructor for the <c>Comparer</c> object. This is
      /// used to create a comparer that has a default set of names
      /// to be ignored during the comparison of annotations.
      /// </summary>
      /// <param name="ignore">
      /// this is the set of attributes to be ignored
      /// </param>
      public Comparer(String... ignore) {
         this.ignore = ignore;
      }
      /// <summary>
      /// This is used to determine if two annotations are equals based
      /// on the attributes of the annotation. The comparison done can
      /// ignore specific attributes, for instance the name attribute.
      /// </summary>
      /// <param name="left">
      /// this is the left side of the comparison done
      /// </param>
      /// <param name="right">
      /// this is the right side of the comparison done
      /// </param>
      /// <returns>
      /// this returns true if the annotations are equal
      /// </returns>
      public bool Equals(Annotation left, Annotation right) {
         Class type = left.annotationType();
         Method[] list = type.getDeclaredMethods();
         for(Method method : list) {
            if(!IsIgnore(method)) {
               Object value = method.invoke(left);
               Object other = method.invoke(right);
               if(!value.Equals(other)) {
                  return false;
               }
            }
         }
         return true;
      }
      /// <summary>
      /// This is used to determine if the method for an attribute is
      /// to be ignore. To determine if it should be ignore the method
      /// name is compared against the list of attributes to ignore.
      /// </summary>
      /// <param name="method">
      /// this is the method to be evaluated
      /// </param>
      /// <returns>
      /// this returns true if the method should be ignored
      /// </returns>
      public bool IsIgnore(Method method) {
         String name = method.getName();
         if(ignore != null) {
            for(String value : ignore) {
               if(name.Equals(value)) {
                  return true;
               }
            }
         }
         return false;
      }
   }
}
