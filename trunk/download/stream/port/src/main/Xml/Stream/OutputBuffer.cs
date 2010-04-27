#region License
//
// OutputBuffer.cs June 2007
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
using System.Text;
using System.IO;
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   /// <summary>
   /// This is primarily used to replace the <c>StringBuffer</c>
   /// class, as a way for the <c>Formatter</c> to store the start
   /// tag for an XML element. This enables the start tag of the current
   /// element to be removed without disrupting any of the other nodes
   /// within the document. Once the contents of the output buffer have
   /// been filled its contents can be emitted to the writer object.
   /// </summary>
   class OutputBuffer {
      /// <summary>
      /// The characters that this buffer has accumulated.
      /// </summary>
      private StringBuilder text;
      /// <summary>
      /// Constructor for <c>OutputBuffer</c>. The default
      /// <c>OutputBuffer</c> stores 16 characters before a
      /// resize is needed to append extra characters.
      /// </summary>
      public OutputBuffer() {
         this.text = new StringBuilder();
      }
      /// <summary>
      /// This will add a <c>char</c> to the end of the buffer.
      /// The buffer will not overflow with repeated uses of the
      /// <c>append</c>, it uses an <c>ensureCapacity</c>
      /// method which will allow the buffer to dynamically grow in
      /// size to accommodate more characters.
      /// </summary>
      /// <param name="ch">
      /// the character to be appended to the buffer
      /// </param>
      public void Append(char ch) {
         text.Append(ch);
      }
      /// <summary>
      /// This will add a <c>String</c> to the end of the buffer.
      /// The buffer will not overflow with repeated uses of the
      /// <c>append</c>, it uses an <c>ensureCapacity</c>
      /// method which will allow the buffer to dynamically grow in
      /// size to accommodate large string objects.
      /// </summary>
      /// <param name="value">
      /// the string to be appended to this output buffer
      /// </param>
      public void Append(String value) {
         text.Append(value);
      }
      /// <summary>
      /// This will add a <c>char</c> array to the buffer.
      /// The buffer will not overflow with repeated uses of the
      /// <c>append</c>, it uses an <c>ensureCapacity</c>
      /// method which will allow the buffer to dynamically grow in
      /// size to accommodate large character arrays.
      /// </summary>
      /// <param name="value">
      /// the character array to be appended to this
      /// </param>
      public void Append(char[] value) {
         text.Append(value, 0, value.Length);
      }
      /// <summary>
      /// This will add a <c>char</c> array to the buffer.
      /// The buffer will not overflow with repeated uses of the
      /// <c>append</c>, it uses an <c>ensureCapacity</c>
      /// method which will allow the buffer to dynamically grow in
      /// size to accommodate large character arrays.
      /// </summary>
      /// <param name="value">
      /// the character array to be appended to this
      /// </param>
      /// <param name="off">
      /// the read offset for the array to begin reading
      /// </param>
      /// <param name="len">
      /// the number of characters to append to this
      /// </param>
      public void Append(char[] value, int off, int len) {
         text.Append(value, off, len);
      }
      /// <summary>
      /// This will add a <c>String</c> to the end of the buffer.
      /// The buffer will not overflow with repeated uses of the
      /// <c>append</c>, it uses an <c>ensureCapacity</c>
      /// method which will allow the buffer to dynamically grow in
      /// size to accommodate large string objects.
      /// </summary>
      /// <param name="value">
      /// the string to be appended to the output buffer
      /// </param>
      /// <param name="off">
      /// the offset to begin reading from the string
      /// </param>
      /// <param name="len">
      /// the number of characters to append to this
      /// </param>
      public void Append(String value, int off, int len) {
         text.Append(value, off, len);
      }
      /// <summary>
      /// This method is used to write the contents of the buffer to the
      /// specified <c>Writer</c> object. This is used when the
      /// XML element is to be committed to the resulting XML document.
      /// </summary>
      /// <param name="out">
      /// this is the writer to write the buffered text to
      /// </param>
      public void Write(TextWriter writer) {
         writer.Write(text);
      }
      /// <summary>
      /// This will empty the <c>OutputBuffer</c> so that it does
      /// not contain any content. This is used to that when the buffer
      /// is written to a specified <c>Writer</c> object nothing
      /// is written out. This allows XML elements to be removed.
      /// </summary>
      public void Clear() {
         text.Clear();
      }
   }
}
