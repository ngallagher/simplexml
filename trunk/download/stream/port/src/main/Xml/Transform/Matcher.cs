#region License
//
// Matcher.cs May 2007
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
/// The <c>Matcher</c> is used to match a type with a transform
/// such that a string value can be read or written as that type. If
/// there is no match this will typically return a null to indicate
/// that another matcher should be delegated to. If there is an error
/// in performing the match an exception is thrown.
/// </summary>
/// @author Niall Gallagher
/// <seealso>
/// SimpleFramework.Xml.transform.Transformer
/// </seealso>
public interface Matcher {
   /// <summary>
   /// This is used to match a <c>Transform</c> using the type
   /// specified. If no transform can be acquired then this returns
   /// a null value indicating that no transform could be found.
   /// </summary>
   /// <param name="type">
   /// this is the type to acquire the transform for
   /// </param>
   /// <returns>
   /// returns a transform for processing the type given
   /// </returns>
   Transform Match(Class type);
}
}
