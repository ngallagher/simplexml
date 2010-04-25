#region License
//
// Template.cs February 2001
//
// Copyright (C) 2001, Niall Gallagher <niallg@users.sf.net>
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
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// This is primarily used to replace the <c>StringBuffer</c>
   /// class, as a way for the <c>TemplateEngine</c> to store the
   /// data for a specific region within the parse data that constitutes
   /// a desired value. The methods are not synchronized so it enables
   /// the characters to be taken quicker than the string buffer class.
   /// </summary>
   class Template {
      /// <summary>
      /// This is used to quicken <c>toString</c>.
      /// </summary>
      protected String cache;
      /// <summary>
      /// The characters that this buffer has accumulated.
      /// </summary>
      protected char[] buf;
      /// <summary>
      /// This is the number of characters this has stored.
      /// </summary>
      protected int count;
      /// <summary>
      /// Constructor for <c>Template</c>. The default
      /// <c>Template</c> stores 16 characters before a
      /// <c>resize</c> is needed to append extra characters.
      /// </summary>
      public Template(){
         this(16);
      }
      /// <summary>
      /// This creates a <c>Template</c> with a specific
      /// default size. The buffer will be created the with the
      /// length specified. The <c>Template</c> can grow
      /// to accomodate a collection of characters larger the the
      /// size spacified.
      /// </summary>
      /// <param name="size">
      /// initial size of this <c>Template</c>
      /// </param>
      public Template(int size){
         this.buf = new char[size];
      }
      /// <summary>
      /// This will add a <c>char</c> to the end of the buffer.
      /// The buffer will not overflow with repeated uses of the
      /// <c>append</c>, it uses an <c>ensureCapacity</c>
      /// method which will allow the buffer to dynamically grow in
      /// size to accomodate more characters.
      /// </summary>
      /// <param name="c">
      /// the <c>char</c> to be appended
      /// </param>
      public void Append(char c) {
         EnsureCapacity(count+ 1);
         buf[count++] = c;
      }
      /// <summary>
      /// This will add a <c>String</c> to the end of the buffer.
      /// The buffer will not overflow with repeated uses of the
      /// <c>append</c>, it uses an <c>ensureCapacity</c>
      /// method which will allow the buffer to dynamically grow in
      /// size to accomodate large <c>String</c> objects.
      /// </summary>
      /// <param name="str">
      /// the <c>String</c> to be appended to this
      /// </param>
      public void Append(String str) {
         EnsureCapacity(count+ str.Length());
         str.getChars(0,str.Length(),buf,count);
         count += str.Length();
      }
      /// <summary>
      /// This will add a <c>Template</c> to the end of this.
      /// The buffer will not overflow with repeated uses of the
      /// <c>append</c>, it uses an <c>ensureCapacity</c>
      /// method which will allow the buffer to dynamically grow in
      /// size to accomodate large <c>Template</c> objects.
      /// </summary>
      /// <param name="text">
      /// the <c>Template</c> to be appended
      /// </param>
      public void Append(Template text) {
         Append(text.buf, 0, text.count);
      }
      /// <summary>
      /// This will add a <c>char</c> to the end of the buffer.
      /// The buffer will not overflow with repeated uses of the
      /// <c>append</c>, it uses an <c>ensureCapacity</c>
      /// method which will allow the buffer to dynamically grow in
      /// size to accomodate large <c>char</c> arrays.
      /// </summary>
      /// <param name="c">
      /// the <c>char</c> array to be appended to this
      /// </param>
      /// <param name="off">
      /// the read offset for the array
      /// </param>
      /// <param name="len">
      /// the number of characters to append to this
      /// </param>
      public void Append(char[] c, int off, int len) {
         EnsureCapacity(count+ len);
         System.arraycopy(c,off,buf,count,len);
         count+=len;
      }
      /// <summary>
      /// This will add a <c>String</c> to the end of the buffer.
      /// The buffer will not overflow with repeated uses of the
      /// <c>append</c>, it uses an <c>ensureCapacity</c>
      /// method which will allow the buffer to dynamically grow in
      /// size to accomodate large <c>String</c> objects.
      /// </summary>
      /// <param name="str">
      /// the <c>String</c> to be appended to this
      /// </param>
      /// <param name="off">
      /// the read offset for the <c>String</c>
      /// </param>
      /// <param name="len">
      /// the number of characters to append to this
      /// </param>
      public void Append(String str, int off, int len) {
         EnsureCapacity(count+ len);
         str.getChars(off,len,buf,count);
         count += len;
      }
      /// <summary>
      /// This will add a <c>Template</c> to the end of this.
      /// The buffer will not overflow with repeated uses of the
      /// <c>append</c>, it uses an <c>ensureCapacity</c>
      /// method which will allow the buffer to dynamically grow in
      /// size to accomodate large <c>Template</c> objects.
      /// </summary>
      /// <param name="text">
      /// the <c>Template</c> to be appended
      /// </param>
      /// <param name="off">
      /// the read offset for the <c>Template</c>
      /// </param>
      /// <param name="len">
      /// the number of characters to append to this
      /// </param>
      public void Append(Template text, int off, int len) {
         Append(text.buf, off, len);
      }
      /// <summary>
      /// This ensure that there is enough space in the buffer to allow
      /// for more characters to be added. If the buffer is already
      /// larger than min then the buffer will not be expanded at all.
      /// </summary>
      /// <param name="min">
      /// the minimum size needed for this buffer
      /// </param>
      public void EnsureCapacity(int min) {
         if(buf.length < min) {
            int size = buf.length * 2;
            int max = Math.max(min, size);
            char[] temp = new char[max];
            System.arraycopy(buf, 0, temp, 0, count);
            buf = temp;
         }
      }
      /// <summary>
      /// This will empty the <c>Template</c> so that the
      /// <c>toString</c> paramater will return <c>null</c>.
      /// This is used so that the same <c>Template</c> can be
      /// recycled for different tokens.
      /// </summary>
      public void Clear() {
         cache = null;
         count = 0;
      }
      /// <summary>
      /// This will return the number of bytes that have been appended
      /// to the <c>Template</c>. This will return zero after
      /// the clear method has been invoked.
      /// </summary>
      /// <returns>
      /// the number of characters within this buffer object
      /// </returns>
      public int Length() {
         return count;
      }
      /// <summary>
      /// This will return the characters that have been appended to the
      /// <c>Template</c> as a <c>String</c> object.
      /// If the <c>String</c> object has been created before then
      /// a cached <c>String</c> object will be returned. This
      /// method will return <c>null</c> after clear is invoked.
      /// </summary>
      /// <returns>
      /// the characters appended as a <c>String</c>
      /// </returns>
      public String ToString() {
         return new String(buf,0,count);
      }
   }
}
