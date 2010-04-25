#region License
//
// Order.cs November 2007
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
namespace SimpleFramework.Xml {
   /// <summary>
   /// The <c>Order</c> annotation is used to specify the order of
   /// appearance of XML elements and attributes. When used it ensures
   /// that on serialization the XML generated is predictable. By default
   /// serialization of fields is done in declaration order.
   /// </summary>
   [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
   public class Order : System.Attribute {
      private String[] elements;
      private String[] attributes;
      /// <summary>
      /// Specifies the appearance order of the XML elements within the
      /// generated document. This overrides the default order used,
      /// which is the declaration order within the class. If an element
      /// is not specified within this array then its order will be the
      /// appearance order directly after the last specified element.
      /// </summary>
      /// <returns>
      /// an ordered array of elements representing order
      /// </returns>
      public String[] Elements {
         get {
            return elements;
         }
         set {
            elements = value;
         }
      }
      /// <summary>
      /// Specifies the appearance order of the XML attributes within
      /// the generated document. This overrides the default order used,
      /// which is the declaration order within the class. If an attribute
      /// is not specified within this array then its order will be the
      /// appearance order directly after the last specified attribute.
      /// </summary>
      /// <returns>
      /// an ordered array of attributes representing order
      /// </returns>
      public String[] Attributes {
         get {
            return attributes;
         }
         set {
            attributes = value;
         }
      }
   }
}
