#region License
//
// StackFilter.cs May 2006
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
   /// The <c>StackFilter</c> object provides a filter that can
   /// be given a collection of filters which can be used to resolve a
   /// replacement. The order of the resolution used for this filter
   /// is last in first used. This order allows the highest priority
   /// filter to be added last within the stack.
   /// </summary>
   public class StackFilter : Filter {
      /// <summary>
      /// This is used to store the filters that are used.
      /// </summary>
      private List<Filter> stack;
      /// <summary>
      /// Constructor for the <c>StackFilter</c> object. This will
      /// create an empty filter that initially resolves null for all
      /// replacements requested. As filters are pushed into the stack
      /// the <c>replace</c> method can resolve replacements.
      /// </summary>
      public StackFilter() {
         this.stack = new List<Filter>();
      }
      /// <summary>
      /// This pushes the the provided <c>Filter</c> on to the top
      /// of the stack. The last filter pushed on to the stack has the
      /// highes priority in the resolution of a replacement value.
      /// </summary>
      /// <param name="filter">
      /// this is a filter to be pushed on to the stack
      /// </param>
      public void Push(Filter filter) {
         stack.Add(filter);
      }
      /// <summary>
      /// Replaces the text provided with the value resolved from the
      /// stacked filters. This attempts to resolve a replacement from
      /// the top down. So the last <c>Filter</c> pushed on to
      /// the stack will be the first filter queried for a replacement.
      /// </summary>
      /// <param name="text">
      /// this is the text value to be replaced
      /// </param>
      /// <returns>
      /// this will return the replacement text resolved
      /// </returns>
      public String Replace(String text) {
         for(int i = 0; i < stack.Count; i++) {
            String value = stack[i].Replace(text);

            if(value != null){
               return value;
            }
         }
         return null;
      }
   }
}
