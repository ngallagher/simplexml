#region License
//
// ConvertException.cs January 2010
//
// Copyright (C) 2010, Niall Gallagher <niallg@users.sf.net>
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
   /// The <c>ConvertException</c> is thrown when there is a
   /// problem converting an object. Such an exception can occur if an
   /// annotation is use incorrectly, or if a <c>Converter</c>
   /// can not be instantiated. Messages provided to this exception are
   /// formatted similar to the <c>PrintStream.printf</c> method.
   /// </summary>
   public class ConvertException : Exception {
      /// <summary>
      /// Constructor for the <c>ConvertException</c> object.
      /// This constructor takes a format string an a variable number of
      /// object arguments, which can be inserted into the format string.
      /// </summary>
      /// <param name="text">
      /// a format string used to present the error message
      /// </param>
      /// <param name="list">
      /// a list of arguments to insert into the string
      /// </param>
      public ConvertException(String text, Object... list) {
         super(String.format(text, list));
      }
   }
}
