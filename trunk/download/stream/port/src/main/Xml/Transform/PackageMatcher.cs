#region License
//
// PackageMatcher.cs May 2007
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
/// The <c>PackageMatcher</c> object is used to match the stock
/// transforms to Java packages. This is used to match useful types
/// from the <c>java.lang</c> and <c>java.util</c> packages
/// as well as other Java packages. This matcher groups types by their
/// package names and attempts to search the stock transforms for a
/// suitable match. If no match can be found this throws an exception.
/// </summary>
/// @author Niall Gallagher
/// <seealso>
/// SimpleFramework.Xml.transform.DefaultMatcher
/// </seealso>
class PackageMatcher : Matcher {
   /// <summary>
   /// Constructor for the <c>PackageMatcher</c> object. The
   /// package matcher is used to resolve a transform instance to
   /// convert object types to an from strings. If a match cannot
   /// be found with this matcher then an exception is thrown.
   /// </summary>
   public PackageMatcher() {
      super();
   }
   /// <summary>
   /// This method attempts to perform a resolution of the transform
   /// based on its package prefix. This allows this matcher to create
   /// a logical group of transforms within a single method based on
   /// the types package prefix. If no transform can be found then
   /// this will throw an exception.
   /// </summary>
   /// <param name="type">
   /// this is the type to resolve a transform for
   /// </param>
   /// <returns>
   /// the transform that is used to transform that type
   /// </returns>
   public Transform Match(Class type) {
      String name = type.getName();
      if(name.startsWith("java.lang")) {
         return MatchLanguage(type);
      }
      if(name.startsWith("java.util")) {
         return MatchUtility(type);
      }
      if(name.startsWith("java.net")) {
         return MatchURL(type);
      }
      if(name.startsWith("java.io")) {
         return MatchFile(type);
      }
      if(name.startsWith("java.sql")) {
         return MatchSQL(type);
      }
      if(name.startsWith("java.math")) {
         return MatchMath(type);
      }
      return MatchEnum(type);
   }
   /// <summary>
   /// This is used to resolve <c>Transform</c> implementations
   /// that are <c>Enum</c> implementations. If the type is not
   /// an enumeration then this will return null.
   /// </summary>
   /// <param name="type">
   /// this is the type to resolve a stock transform for
   /// </param>
   /// <returns>
   /// this will return a transform for the specified type
   /// </returns>
   public Transform MatchEnum(Class type) {
      if(type.isEnum()) {
         return new EnumTransform(type);
      }
      return null;
   }
   /// <summary>
   /// This is used to resolve <c>Transform</c> implementations
   /// that relate to the <c>java.lang</c> package. If the type
   /// does not resolve to a valid transform then this method will
   /// throw an exception to indicate that no stock transform exists
   /// for the specified type.
   /// </summary>
   /// <param name="type">
   /// this is the type to resolve a stock transform for
   /// </param>
   /// <returns>
   /// this will return a transform for the specified type
   /// </returns>
   public Transform MatchLanguage(Class type) {
      if(type == Boolean.class) {
         return new BooleanTransform();
      }
      if(type == Integer.class) {
         return new IntegerTransform();
      }
      if(type == Long.class) {
         return new LongTransform();
      }
      if(type == Double.class) {
         return new DoubleTransform();
      }
      if(type == Float.class) {
         return new FloatTransform();
      }
      if(type == Short.class) {
         return new ShortTransform();
      }
      if(type == Byte.class) {
         return new ByteTransform();
      }
      if(type == Character.class) {
         return new CharacterTransform();
      }
      if(type == String.class) {
         return new StringTransform();
      }
      if(type == Class.class) {
         return new ClassTransform();
      }
      return null;
   }
   /// <summary>
   /// This is used to resolve <c>Transform</c> implementations
   /// that relate to the <c>java.math</c> package. If the type
   /// does not resolve to a valid transform then this method will
   /// throw an exception to indicate that no stock transform exists
   /// for the specified type.
   /// </summary>
   /// <param name="type">
   /// this is the type to resolve a stock transform for
   /// </param>
   /// <returns>
   /// this will return a transform for the specified type
   /// </returns>
   public Transform MatchMath(Class type) {
      if(type == BigDecimal.class) {
         return new BigDecimalTransform();
      }
      if(type == BigInteger.class) {
         return new BigIntegerTransform();
      }
      return null;
   }
   /// <summary>
   /// This is used to resolve <c>Transform</c> implementations
   /// that relate to the <c>java.util</c> package. If the type
   /// does not resolve to a valid transform then this method will
   /// throw an exception to indicate that no stock transform exists
   /// for the specified type.
   /// </summary>
   /// <param name="type">
   /// this is the type to resolve a stock transform for
   /// </param>
   /// <returns>
   /// this will return a transform for the specified type
   /// </returns>
   public Transform MatchUtility(Class type) {
      if(type == Date.class) {
         return new DateTransform(type);
      }
      if(type == Locale.class) {
         return new LocaleTransform();
      }
      if(type == Currency.class) {
         return new CurrencyTransform();
      }
      if(type == GregorianCalendar.class) {
         return new GregorianCalendarTransform();
      }
      if(type == TimeZone.class) {
         return new TimeZoneTransform();
      }
      return null;
   }
   /// <summary>
   /// This is used to resolve <c>Transform</c> implementations
   /// that relate to the <c>java.sql</c> package. If the type
   /// does not resolve to a valid transform then this method will
   /// throw an exception to indicate that no stock transform exists
   /// for the specified type.
   /// </summary>
   /// <param name="type">
   /// this is the type to resolve a stock transform for
   /// </param>
   /// <returns>
   /// this will return a transform for the specified type
   /// </returns>
   public Transform MatchSQL(Class type) {
      if(type == Time.class) {
         return new DateTransform(type);
      }
      if(type == java.sql.Date.class) {
         return new DateTransform(type);
      }
      if(type == Timestamp.class) {
         return new DateTransform(type);
      }
      return null;
   }
   /// <summary>
   /// This is used to resolve <c>Transform</c> implementations
   /// that relate to the <c>java.io</c> package. If the type
   /// does not resolve to a valid transform then this method will
   /// throw an exception to indicate that no stock transform exists
   /// for the specified type.
   /// </summary>
   /// <param name="type">
   /// this is the type to resolve a stock transform for
   /// </param>
   /// <returns>
   /// this will return a transform for the specified type
   /// </returns>
   public Transform MatchFile(Class type) {
      if(type == File.class) {
         return new FileTransform();
      }
      return null;
   }
   /// <summary>
   /// This is used to resolve <c>Transform</c> implementations
   /// that relate to the <c>java.net</c> package. If the type
   /// does not resolve to a valid transform then this method will
   /// throw an exception to indicate that no stock transform exists
   /// for the specified type.
   /// </summary>
   /// <param name="type">
   /// this is the type to resolve a stock transform for
   /// </param>
   /// <returns>
   /// this will return a transform for the specified type
   /// </returns>
   public Transform MatchURL(Class type) {
      if(type == URL.class) {
         return new URLTransform();
      }
      return null;
   }
}
}
