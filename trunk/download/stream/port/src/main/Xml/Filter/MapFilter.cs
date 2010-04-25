#region License
//
// MapFilter.cs May 2006
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
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Filter {
   /// <summary>
   /// The <c>MapFilter</c> object is a filter that can make use
   /// of user specified mappings for replacement. This filter can be
   /// given a <c>Map</c> of name value pairs which will be used
   /// to resolve a value using the specified mappings. If there is
   /// no match found the filter will delegate to the provided filter.
   /// </summary>
   public class MapFilter<T> : Filter {
      /// <summary>
      /// This contains a collection of user specified mappings.
      /// </summary>
      private Dictionary<String, T> map;
      /// <summary>
      /// This will resolve the replacement if no mapping is found.
      /// </summary>
      private Filter filter;
      /// <summary>
      /// Constructor for the <c>MapFilter</c> object. This will
      /// use the specified mappings to resolve replacements. If this
      /// map does not contain a requested mapping null is resolved.
      /// </summary>
      /// <param name="map">
      /// this contains the user specified mappings
      /// </param>
      public MapFilter(Dictionary<String, T> map) : this(map, null) {
      }
      /// <summary>
      /// Constructor for the <c>MapFilter</c> object. This will
      /// use the specified mappings to resolve replacements. If this
      /// map does not contain a requested mapping the provided filter
      /// is used to resolve the replacement text.
      /// </summary>
      /// <param name="map">
      /// this contains the user specified mappings
      /// </param>
      /// <param name="filter">
      /// this is delegated to if the map fails
      /// </param>
      public MapFilter(Dictionary<String, T> map, Filter filter) {
         this.filter = filter;
         this.map = map;
      }
      /// <summary>
      /// Replaces the text provided with the value resolved from the
      /// specified <c>Map</c>. If the map fails this will
      /// delegate to the specified <c>Filter</c> if it is not
      /// a null object. If no match is found a null is returned.
      /// </summary>
      /// <param name="text">
      /// this is the text value to be replaced
      /// </param>
      /// <returns>
      /// this will return the replacement text resolved
      /// </returns>
      public String Replace(String text) {
         T value = default(T);

         if(map != null) {
            value = map[text];
         }
         if(value != null) {
            return value.ToString();
         }
         if(filter != null) {
            return filter.Replace(text);
         }
         return null;
      }
   }
}
