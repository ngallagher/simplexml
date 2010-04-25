#region License
//
// DateTransform.cs May 2007
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
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
/// <summary>
/// The <c>DateTransform</c> object is used to transform date
/// values to and from string representations, which will be inserted
/// in the generated XML document as the value place holder. The
/// value must be readable and writable in the same format. Fields
/// and methods annotated with the XML attribute annotation will use
/// this to persist and retrieve the value to and from the XML source.
/// </code>
///    &#64;Attribute
///    private Date date;
/// </code>
/// As well as the XML attribute values using transforms, fields and
/// methods annotated with the XML element annotation will use this.
/// Aside from the obvious difference, the element annotation has an
/// advantage over the attribute annotation in that it can maintain
/// any references using the <c>CycleStrategy</c> object.
/// </summary>
/// @author Niall Gallagher
class DateTransform<T : Date> : Transform<T> {
   /// <summary>
   /// This represents the constructor used for creating the date.
   /// </summary>
   private readonly DateFactory<T> factory;
   /// <summary>
   /// Constructor for the <c>DateTransform</c> object. This is
   /// used to create a transform using a specified date format. The
   /// format should typically contain enough information to create
   /// the date using a different locale or time zone between read and
   /// write operations.
   /// </summary>
   /// <param name="type">
   /// this is the type of date to be created by this
   /// </param>
   public DateTransform(Class<T> type) {
      this.factory = new DateFactory<T>(type);
   }
   /// <summary>
   /// This method is used to convert the string value given to an
   /// appropriate representation. This is used when an object is
   /// being deserialized from the XML document and the value for
   /// the string representation is required.
   /// </summary>
   /// <param name="text">
   /// the string representation of the date value
   /// </param>
   /// <returns>
   /// this returns an appropriate instanced to be used
   /// </returns>
   public synchronized T Read(String text) {
      Date date = DateType.getDate(text);
      Long time = date.getTime();
      return factory.getInstance(time);
   }
   /// <summary>
   /// This method is used to convert the provided value into an XML
   /// usable format. This is used in the serialization process when
   /// there is a need to convert a field value in to a string so
   /// that that value can be written as a valid XML entity.
   /// </summary>
   /// <param name="date">
   /// this is the value to be converted to a string
   /// </param>
   /// <returns>
   /// this is the string representation of the given date
   /// </returns>
   public synchronized String Write(T date) {
      return DateType.getText(date);
   }
}
}
