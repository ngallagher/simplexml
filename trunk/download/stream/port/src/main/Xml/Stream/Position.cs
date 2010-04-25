#region License
//
// Position.cs July 2006
//
// Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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
namespace SimpleFramework.Xml.Stream {
   /// <summary>
   /// The <c>Position</c> object is used to acquire the position
   /// of the read cursor within the XML file. This allows exceptions to
   /// be thrown with the line number so that the XML can be debugged.
   /// </summary>
   public interface Position {
      /// <summary>
      /// This is the actual line number within the read XML document.
      /// The line number allows any problems within the source XML
      /// document to be debugged if it does not match the schema.
      /// This will return -1 if the line number cannot be determined.
      /// </summary>
      /// <returns>
      /// this returns the line number of an XML event
      /// </returns>
      public int Line {
         get;
      }
      //public int GetLine();
      /// This provides a textual description of the position the
      /// read cursor is at within the XML document. This allows the
      /// position to be embedded within the exception thrown.
      /// </summary>
      /// <returns>
      /// this returns a textual description of the position
      /// </returns>
      public String ToString();
   }
}
