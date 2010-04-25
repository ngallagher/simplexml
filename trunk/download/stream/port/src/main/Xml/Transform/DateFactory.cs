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
/// The <c>DateFactory</c> object is used to create instances
/// or subclasses of the <c>Date</c> object. This will create
/// the instances of the date objects using a constructor that takes
/// a single <c>long</c> parameter value.
/// </summary>
/// @author Niall Gallagher
/// <seealso>
/// SimpleFramework.Xml.transform.DateTransform
/// </seealso>
class DateFactory<T : Date> {
   /// <summary>
   /// This is used to create instances of the date object required.
   /// </summary>
   private readonly Constructor<T> factory;
   /// <summary>
   /// Constructor for the <c>DateFactory</c> object. This is
   /// used to create instances of the specified type. All objects
   /// created by this instance must take a single long parameter.
   /// </summary>
   /// <param name="type">
   /// this is the date implementation to be created
   /// </param>
   public DateFactory(Class<T> type) {
      this(type, long.class);
   }
   /// <summary>
   /// Constructor for the <c>DateFactory</c> object. This is
   /// used to create instances of the specified type. All objects
   /// created by this instance must take the specified parameter.
   /// </summary>
   /// <param name="type">
   /// this is the date implementation to be created
   /// </param>
   /// <param name="list">
   /// is basically the list of accepted parameters
   /// </param>
   public DateFactory(Class<T> type, Class... list) {
      this.factory = type.getDeclaredConstructor(list);
   }
   /// <summary>
   /// This is used to create instances of the date using a delegate
   /// date. A <c>long</c> parameter is extracted from the
   /// given date an used to instantiate a date of the required type.
   /// </summary>
   /// <param name="list">
   /// this is the type used to provide the long value
   /// </param>
   /// <returns>
   /// this returns an instance of the required date type
   /// </returns>
   public T GetInstance(Object... list) {
      return factory.newInstance(list);
   }
}
}
