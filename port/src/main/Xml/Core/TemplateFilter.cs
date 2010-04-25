#region License
//
// TemplateFilter.cs May 2005
//
// Copyright (C) 2005, Niall Gallagher <niallg@users.sf.net>
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
using SimpleFramework.Xml.Filter;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>TemplateFilter</c> class is used to provide variables
   /// to the template engine. This template acquires variables from two
   /// different sources. Firstly this will consult the user contextual
   /// <c>Context</c> object, which can contain variables that have
   /// been added during the deserialization process. If a variable is
   /// not present from this context it asks the <c>Filter</c> that
   /// has been specified by the user.
   /// </summary>
   class TemplateFilter : Filter {
      /// <summary>
      /// This is the template context object used by the persister.
      /// </summary>
      private Context context;
      /// <summary>
      /// This is the filter object provided to the persister.
      /// </summary>
      private Filter filter;
      /// <summary>
      /// Constructor for the <c>TemplateFilter</c> object. This
      /// creates a filter object that acquires template values from
      /// two different contexts. Firstly the <c>Context</c> is
      /// queried for a variables followed by the <c>Filter</c>.
      /// </summary>
      /// <param name="context">
      /// this is the context object for the persister
      /// </param>
      /// <param name="filter">
      /// the filter that has been given to the persister
      /// </param>
      public TemplateFilter(Context context, Filter filter) {
         this.context = context;
         this.filter = filter;
      }
      /// <summary>
      /// This will acquire the named variable value if it exists. If
      /// the named variable cannot be found in either the context or
      /// the user specified filter then this returns null.
      /// </summary>
      /// <param name="name">
      /// this is the name of the variable to acquire
      /// </param>
      /// <returns>
      /// this returns the value mapped to the variable name
      /// </returns>
      public String Replace(String name) {
         Object value = context.GetAttribute(name);
         if(value != null) {
            return value.toString();
         }
         return filter.Replace(name);
      }
   }
}
