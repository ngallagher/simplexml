#region License
//
// Convert.cs January 2010
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
   /// The <c>Convert</c> annotation is used to specify a converter
   /// class to use for serialization. This annotation is used when an
   /// object needs to be serialized but can not be annotated or when the
   /// object can not conform to an existing XML structure. In order to
   /// specify a <c>Converter</c> object a field or method can be
   /// annotated like the field below.
   /// </code>
   ///    &#64;Element
   ///    &#64;Convert(ExampleConverter.class)
   ///    private Example example;
   /// </code>
   /// Note that for the above field the <c>Element</c> annotation
   /// is required. If this is used with any other XML annotation such
   /// as the <c>ElementList</c> or <c>Text</c> annotation
   /// then an exception will be thrown. As well as field and methods
   /// this can be used to suggest a converter for a class. Take the
   /// class below which is annotated.
   /// </code>
   ///    &#64;Root
   ///    &#64;Convert(DemoConverter.class)
   ///    public class Demo {
   ///       ...
   ///    }
   /// </code>
   /// For the above class the specified converter will be used. This is
   /// useful when the class is used within a <c>java.util.List</c>
   /// or another similar collection. Finally, in order for this to work
   /// it must be used with the <c>AnnotationStrategy</c> which is
   /// used to scan for annotations in order to delegate to converters.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Util.AnnotationStrategy
   /// </seealso>
   [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
   public class Convert : System.Attribute {
      /// <summary>
      /// Specifies the <c>Converter</c> implementation to be used
      /// to convert the annotated object. The converter specified will
      /// be used to convert the object to XML by intercepting the
      /// serialization and deserialization process as it happens. A
      /// converter should typically be used to handle an object of
      /// a specific type.
      /// </summary>
      /// <returns>
      /// this returns the converter that has been specified
      /// </returns>
      public Class<? : Converter> value();
   }
}
