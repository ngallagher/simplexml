#region License
//
// TemplateEngine.cs May 2005
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
   /// The <c>TemplateEngine</c> object is used to create strings
   /// which have system variable names replaced with their values.
   /// This is used by the <c>Source</c> context object to ensure
   /// that values taken from an XML element or attribute can be values
   /// values augmented with system or environment variable values.
   /// </code>
   ///    tools=${java.home}/lib/tools.jar
   /// </code>
   /// Above is an example of the use of an system variable that
   /// has been inserted into a plain Java properties file. This will
   /// be converted to the full path to tools.jar when the system
   /// variable "java.home" is replaced with the matching value.
   /// </summary>
   class TemplateEngine {
      /// <summary>
      /// This is used to store the text that are to be processed.
      /// </summary>
      private Template source;
      /// <summary>
      /// This is used to accumulate the bytes for the variable name.
      /// </summary>
      private Template name;
      /// <summary>
      /// This is used to accumulate the transformed text value.
      /// </summary>
      private Template text;
      /// <summary>
      /// This is the filter used to replace templated variables.
      /// </summary>
      private Filter filter;
      /// <summary>
      /// This is used to keep track of the buffer seek offset.
      /// </summary>
      private int off;
      /// <summary>
      /// Constructor for the <c>TemplateEngine</c> object. This is
      /// used to create a parsing buffer, which can be used to replace
      /// filter variable names with their corrosponding values.
      /// </summary>
      /// <param name="filter">
      /// this is the filter used to provide replacements
      /// </param>
      public TemplateEngine(Filter filter) {
         this.source = new Template();
         this.name = new Template();
         this.text = new Template();
         this.filter = filter;
      }
      /// <summary>
      /// This method is used to append the provided text and then it
      /// converts the buffered text to return the corrosponding text.
      /// The contents of the buffer remain unchanged after the value
      /// is buffered. It must be cleared if used as replacement only.
      /// </summary>
      /// <param name="value">
      /// this is the value to append to the buffer
      /// </param>
      /// <returns>
      /// returns the value of the buffer after the append
      /// </returns>
      public String Process(String value) {
         if(value.indexOf('$') < 0) {
            return value;
         }
         try {
            source.Append(value);
            Parse();
            return text.ToString();
         }finally {
            Clear();
         }
      }
      /// <summary>
      /// This extracts the value from the Java properties text. This
      /// will basically ready any text up to the first occurance of
      /// an equal of a terminal. If a terminal character is read
      /// this returns without adding the terminal to the value.
      /// </summary>
      public void Parse() {
         while(off < source.count){
            char next = source.buf[off++];
            if(next == '$') {
               if(off < source.count)
                  if(source.buf[off++] == '{') {
                     Name();
                     continue;
                  } else {
                     off--;
                  }
            }
            text.Append(next);
         }
      }
      /// <summary>
      /// This method is used to extract text from the property value
      /// that matches the pattern "${ *TEXT }". Such patterns within
      /// the properties file are considered to be system
      /// variables, this will replace instances of the text pattern
      /// with the matching system variable, if a matching
      /// variable does not exist the value remains unmodified.
      /// </summary>
      public void Name() {
         while(off < source.count) {
            char next = source.buf[off++];
            if(next == '}') {
               Replace();
               break;
            } else {
               name.Append(next);
            }
         }
         if(name.Length() >0){
            text.Append("${");
            text.Append(name);
         }
      }
      /// <summary>
      /// This will replace the accumulated for an system variable
      /// name with the value of that system variable. If a value
      /// does not exist for the variable name, then the name is put
      /// into the value so that the value remains unmodified.
      /// </summary>
      public void Replace() {
         if(name.Length() > 0) {
            Replace(name);
         }
         name.Clear();
      }
      /// <summary>
      /// This will replace the accumulated for an system variable
      /// name with the value of that system variable. If a value
      /// does not exist for the variable name, then the name is put
      /// into the value so that the value remains unmodified.
      /// </summary>
      /// <param name="name">
      /// this is the name of the system variable
      /// </param>
      public void Replace(Template name) {
         Replace(name.ToString());
      }
      /// <summary>
      /// This will replace the accumulated for an system variable
      /// name with the value of that system variable. If a value
      /// does not exist for the variable name, then the name is put
      /// into the value so that the value remains unmodified.
      /// </summary>
      /// <param name="name">
      /// this is the name of the system variable
      /// </param>
      public void Replace(String name) {
         String value = filter.Replace(name);
         if(value == null) {
            text.Append("${");
            text.Append(name);
            text.Append("}");
         }else {
            text.Append(value);
         }
      }
      /// <summary>
      /// This method is used to clear the contents of the buffer. This
      /// includes the contents of all buffers used to transform the
      /// value of the buffered text with system variable values.
      /// Once invoked the instance can be reused as a clean buffer.
      /// </summary>
      public void Clear() {
         name.Clear();
         text.Clear();
         source.Clear();
         off = 0;
      }
   }
}
