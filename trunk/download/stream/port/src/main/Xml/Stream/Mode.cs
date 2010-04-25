#region License
//
// Mode.cs May 2007
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
namespace SimpleFramework.Xml.Stream {
   /// <summary>
   /// The <c>Mode</c> enumeration is used to specify the output
   /// mode for XML text. This is used by the <c>OutputNode</c>
   /// to describe if element text will be escaped or wrapped in a
   /// CDATA block. The mode is a three state object, the third of the
   /// states indicates whether an explicit state has been set or not.
   /// If a specific state has not been set then the node will inherit
   /// its output mode from the last parent to have it set.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Stream.OutputNode
   /// </seealso>
   public enum Mode {
      /// <summary>
      /// Indicates that data written will be within a CDATA block.
      /// </summary>
      DATA,
      /// <summary>
      /// Indicates that data written will be escaped if required.
      /// </summary>
      ESCAPE,
      /// <summary>
      /// Indicates that the mode will be inherited from its parent.
      /// </summary>
      INHERIT;
   }
}
