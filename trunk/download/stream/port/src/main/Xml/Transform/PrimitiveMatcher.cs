#region License
//
// PrimitiveMatcher.cs May 2007
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
/// The <c>PrimitiveMatcher</c> object is used to resolve the
/// primitive types to a stock transform. This will basically use
/// a transform that is used with the primitives language object.
/// This will always return a suitable transform for a primitive.
/// </summary>
/// @author Niall Gallagher
/// <seealso>
/// SimpleFramework.Xml.transform.DefaultMatcher
/// </seealso>
class PrimitiveMatcher : Matcher {
   /// <summary>
   /// Constructor for the <c>PrimitiveMatcher</c> object. The
   /// primitive matcher is used to resolve a transform instance to
   /// convert primitive types to an from strings. If a match is not
   /// found with this matcher then an exception is thrown.
   /// </summary>
   public PrimitiveMatcher() {
      super();
   }
   /// <summary>
   /// This method is used to match the specified type to primitive
   /// transform implementations. If this is given a primitive then
   /// it will always return a suitable <c>Transform</c>. If
   /// however it is given an object type an exception is thrown.
   /// </summary>
   /// <param name="type">
   /// this is the primitive type to be transformed
   /// </param>
   /// <returns>
   /// this returns a stock transform for the primitive
   /// </returns>
   public Transform Match(Class type) {
      if(type == int.class) {
         return new IntegerTransform();
      }
      if(type == bool.class) {
         return new BooleanTransform();
      }
      if(type == long.class) {
         return new LongTransform();
      }
      if(type == double.class) {
         return new DoubleTransform();
      }
      if(type == float.class) {
         return new FloatTransform();
      }
      if(type == short.class) {
         return new ShortTransform();
      }
      if(type == byte.class) {
         return new ByteTransform();
      }
      if(type == char.class) {
         return new CharacterTransform();
      }
      return null;
   }
}
}
