#region License
//
// Match.cs March 2002
//
// Copyright (C) 2001, Niall Gallagher <niallg@users.sf.net>
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
namespace SimpleFramework.Xml.Util {
   /// <summary>
   /// This object is stored within a <c>Resolver</c> so that it
   /// can be retrieved using a string that matches its pattern. Any
   /// object that : this can be inserted into the resolver and
   /// retrieved using a string that matches its pattern. For example
   /// take the following pattern "*.html" this will match the string
   /// "/index.html" or "readme.html". This object should be extended
   /// to add more XML attributes and elements, which can be retrieved
   /// when the <c>Match</c> object is retrieve from a resolver.
   /// </summary>
   public interface Match {
      /// <summary>
      /// This is the pattern string that is used by the resolver. A
      /// pattern can consist of a "*" character and a "?" character
      /// to match the pattern. Implementations of this class should
      /// provide the pattern so that it can be used for resolution.
      /// </summary>
      /// <returns>
      /// this returns the pattern that is to be matched
      /// </returns>
      public String Pattern {
         get;
      }
}
