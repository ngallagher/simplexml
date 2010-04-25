#region License
//
// ArrayMatcher.cs May 2007
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
using System;
#endregion
package SimpleFramework.Xml.transform;
/// <summary>
/// The <c>ArrayMatcher</c> object performs matching of array
/// types to array transforms. This uses the array component type to
/// determine the transform to be used. All array transforms created
/// by this will be <c>ArrayTransform</c> object instances.
/// These will use a type transform for the array component to add
/// values to the individual array indexes. Also such transforms are
/// typically treated as a comma separated list of individual values.
/// </summary>
/// @author Niall Gallagher
/// <seealso>
/// SimpleFramework.Xml.transform.ArrayTransform
/// </seealso>
class ArrayMatcher : Matcher {
   /// <summary>
   /// This is the primary matcher that can resolve transforms.
   /// </summary>
   private readonly Matcher primary;
   /// <summary>
   /// Constructor for the <c>ArrayTransform</c> object. This
   /// is used to match array types to their respective transform
   /// using the <c>ArrayTransform</c> object. This will use
   /// a comma separated list of tokens to populate the array.
   /// </summary>
   /// <param name="primary">
   /// this is the primary matcher to be used
   /// </param>
   public ArrayMatcher(Matcher primary) {
      this.primary = primary;
   }
   /// <summary>
   /// This is used to match a <c>Transform</c> based on the
   /// array component type of an object to be transformed. This will
   /// attempt to match the transform using the fully qualified class
   /// name of the array component type. If a transform can not be
   /// found then this method will throw an exception.
   /// </summary>
   /// <param name="type">
   /// this is the array to find the transform for
   /// </param>
   public Transform Match(Class type) {
      Class entry = type.getComponentType();
      if(entry == char.class) {
         return new CharacterArrayTransform(entry);
      }
      if(entry == Character.class) {
         return new CharacterArrayTransform(entry);
      }
      if(entry == String.class) {
         return new StringArrayTransform();
      }
      return MatchArray(entry);
   }
   /// <summary>
   /// This is used to match a <c>Transform</c> based on the
   /// array component type of an object to be transformed. This will
   /// attempt to match the transform using the fully qualified class
   /// name of the array component type. If a transform can not be
   /// found then this method will throw an exception.
   /// </summary>
   /// <param name="entry">
   /// this is the array component type to be matched
   /// </param>
   public Transform MatchArray(Class entry) {
      Transform transform = primary.Match(entry);
      if(transform == null) {
         return null;
      }
      return new ArrayTransform(transform, entry);
   }
}
}
