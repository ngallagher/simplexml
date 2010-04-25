#region License
//
// EmptyMatcher.cs May 2007
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
/// The <c>EmptyMatcher</c> object is used as a delegate type
/// that is used when no user specific matcher is specified. This
/// ensures that no transform is resolved for a specified type, and
/// allows the normal resolution of the stock transforms.
/// </summary>
/// @author Niall Gallagher
/// <seealso>
/// SimpleFramework.Xml.transform.Transformer
/// </seealso>
class EmptyMatcher : Matcher {
   /// <summary>
   /// This method is used to return a null value for the transform.
   /// Returning a null value allows the normal resolution of the
   /// stock transforms to be used when no matcher is specified.
   /// </summary>
   /// <param name="type">
   /// this is the type that is expecting a transform
   /// </param>
   /// <returns>
   /// this transform will always return a null value
   /// </returns>
   public Transform Match(Class type) {
      return null;
   }
}
}
