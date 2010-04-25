#region License
//
// DateType.cs May 2007
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
/// The <c>DateType</c> enumeration provides a set of known date
/// formats supported by the date transformer. This allows the XML
/// representation of a date to come in several formats, from most
/// accurate to least. Enumerating the dates ensures that resolution
/// of the format is fast by enabling inspection of the date string.
/// </summary>
/// @author Niall Gallagher
enum DateType {
   /// <summary>
   /// This is the default date format used by the date transform.
   /// </summary>
   FULL("yyyy-MM-dd HH:mm:ss.S z"),
   /// <summary>
   /// This is the date type without millisecond resolution.
   /// </summary>
   LONG("yyyy-MM-dd HH:mm:ss z"),
   /// <summary>
   /// This date type enables only the specific date to be used.
   /// </summary>
   NORMAL("yyyy-MM-dd z"),
   /// <summary>
   /// This is the shortest format that relies on the date locale.
   /// </summary>
   SHORT("yyyy-MM-dd");
   /// <summary>
   /// This is the date formatter that is used to parse the date.
   /// </summary>
   private DateFormat format;
   /// <summary>
   /// Constructor for the <c>DateType</c> enumeration. This
   /// will accept a simple date format pattern, which is used to
   /// parse an input string and convert it to a usable date.
   /// </summary>
   /// <param name="format">
   /// this is the format to use to parse the date
   /// </param>
   private DateType(String format) {
      this.format = new DateFormat(format);
   }
   /// <summary>
   /// Acquires the date format from the date type. This is then
   /// used to parse the date string and convert it to a usable
   /// date. The format returned is synchronized for safety.
   /// </summary>
   /// <returns>
   /// this returns the date format to be used
   /// </returns>
   public DateFormat GetFormat() {
      return format;
   }
   /// <summary>
   /// This is used to convert the date to a string value. The
   /// string value can then be embedded in to the generated XML in
   /// such a way that it can be recovered as a <c>Date</c>
   /// when the value is transformed by the date transform.
   /// </summary>
   /// <param name="date">
   /// this is the date that is converted to a string
   /// </param>
   /// <returns>
   /// this returns the string to represent the date
   /// </returns>
   public String GetText(Date date) {
      DateFormat format = FULL.Format;
      return format.GetText(date);
   }
   /// <summary>
   /// This is used to convert the string to a date value. The
   /// date value can then be recovered from the generated XML by
   /// parsing the text with one of the known date formats. This
   /// allows bidirectional transformation of dates to strings.
   /// </summary>
   /// <param name="text">
   /// this is the date that is converted to a date
   /// </param>
   /// <returns>
   /// this returns the date parsed from the string value
   /// </returns>
   public Date GetDate(String text) {
      DateType type = GetType(text);
      DateFormat format = type.Format;
      return format.GetDate(text);
   }
   /// <summary>
   /// This is used to acquire a date type using the specified text
   /// as input. This will perform some checks on the raw string to
   /// match it to the appropriate date type. Resolving the date type
   /// in this way ensures that only one date type needs to be used.
   /// </summary>
   /// <param name="text">
   /// this is the text to be matched with a date type
   /// </param>
   /// <returns>
   /// the most appropriate date type for the given string
   /// </returns>
   public DateType GetType(String text) {
      int length = text.length();
      if(length > 23) {
         return FULL;
      }
      if(length > 20) {
         return LONG;
      }
      if(length > 11) {
         return NORMAL;
      }
      return SHORT;
   }
   /// <summary>
   /// The <c>DateFormat</c> provides a synchronized means for
   /// using the simple date format object. It ensures that should
   /// there be many threads trying to gain access to the formatter
   /// that they will not collide causing a race condition.
   /// </summary>
   private static class DateFormat {
      /// <summary>
      /// This is the simple date format used to parse the string.
      /// </summary>
      private SimpleDateFormat format;
      /// <summary>
      /// Constructor for the <c>DateFormat</c> object. This will
      /// wrap a simple date format, providing access to the conversion
      /// functions which allow date to string and string to date.
      /// </summary>
      /// <param name="format">
      /// this is the pattern to use for the date type
      /// </param>
      public DateFormat(String format) {
         this.format = new SimpleDateFormat(format);
      }
      /// <summary>
      /// This is used to provide a transformation from a date to a string.
      /// It ensures that there is a bidirectional transformation process
      /// which allows dates to be serialized and deserialized with XML.
      /// </summary>
      /// <param name="date">
      /// this is the date to be converted to a string value
      /// </param>
      /// <returns>
      /// returns the string that has be converted from a date
      /// </returns>
      public synchronized String GetText(Date date) {
         return format.format(date);
      }
      /// <summary>
      /// This is used to provide a transformation from a string to a date.
      /// It ensures that there is a bidirectional transformation process
      /// which allows dates to be serialized and deserialized with XML.
      /// </summary>
      /// <param name="text">
      /// this is the string to be converted to a date value
      /// </param>
      /// <returns>
      /// returns the date that has be converted from a string
      /// </returns>
      public synchronized Date GetDate(String text) {
         return format.parse(text);
      }
   }
}
}
