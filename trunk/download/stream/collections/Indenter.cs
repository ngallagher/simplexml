﻿#region License
//
// Indenter.cs July 2006
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
using System;
#endregion

namespace SimpleFramework.Xml.Stream {

   /// <summary>
   /// The <c>Indenter</c> is used create indent strings using the
   /// stack paradigm. This allows XML documents to be generated by
   /// pushing and popping indents onto the stack. This indenter caches
   /// all indent strings created so that when the same position on the
   /// stack is encountered the indent can be acquired quickly.
   /// <p>
   /// The indents created by this are all prefixed with the line feed
   /// character, which allows XML tags to span exclusive lines. If the
   /// indent size specified is zero or less then no spaces, or line
   /// feed will be added to the generated indent string.
   /// </summary>
   class Indenter {

      /// <summary>
      /// Provides a quick string cache that caches using by index.
      /// </summary>
      private Cache cache;

      /// <summary>
      /// Number of spaces that is used for each of the indents.
      /// </summary>
      private int indent;

      /// <summary>
      /// Represents the current number of spaces in the indent text.
      /// </summary>
      private int count;

      /// <summary>
      /// Represents the index within the cache to get the indent.
      /// </summary>
      private int index;

      /// <summary>
      /// Constructor for the <c>Indenter</c> object. This will
      /// create an indent that uses the specified number of spaces to
      /// create each entry pushed on to the stack. This uses a cache
      /// size of sixteen, which should be sufficient for most files.
      /// </summary>
      /// <param name="format">
      /// Determines the number of spaces per indent.
      /// </param>
      public Indenter(Format format) : this(format, 16) {
      }

      /// <summary>
      /// Constructor for the <c>Indenter</c> object. This will
      /// create an indent that uses the specified number of spaces to
      /// create each entry pushed on to the stack. This uses a cache
      /// of the specified size, which is used to optimize the object.
      /// </summary>
      /// <param name="format">
      /// Determines the number of spaces per indent.
      /// </param>
      /// <param name="size">
      /// This is the initial size of the indent cache.
      /// </param>
      private Indenter(Format format, int size) {
         this.cache = new Cache(size);
         this.indent = format.Indent;
      }

      /// <summary>
      /// This returns the current indent for this indenter. This should
      /// be used to write elements or comments that should be at the
      /// same indentation level as the XML element that will follow.
      /// </summary>
      /// <returns>
      /// This returns the current indentation level for this.
      /// </returns>
      public String Top() {
         return Indent(index);
      }

      /// <summary>
      /// This is used to push an indent on to the cache. The first
      /// indent created by this is an empty string, this is because an
      /// indent is not required for the start of an XML file. If there
      /// are multiple roots written to the same writer then the start
      /// and end tags of a root element will exist on the same line.
      /// </summary>
      /// <returns>
      /// This is used to push an indent on to the stack.
      /// </returns>
      public String Push() {
         String text = Indent(index++);

         if(indent > 0) {
            count += indent;
         }
         return text;
      }

      /// <summary>
      /// This is used to pop an indent from the cache. This reduces
      /// the length of the current indent and is typically used when
      /// an end tag is added to an XML document. If the number of pop
      /// requests exceeds the number of push requests then an empty
      /// string is returned from this method.
      /// </summary>
      /// <returns>
      /// This is used to pop an indent from the stack.
      /// </returns>
      public String Pop() {
         String text = Indent(--index);

         if(indent > 0) {
            count -= indent;
         }
         return text;
      }

      /// <summary>
      /// This is used to acquire the indent at the specified index. If
      /// the indent does not exist at the specified index then on is
      /// created using the current value of the indent. The very first
      /// indent taken from this will be an empty string value.
      /// </summary>
      /// <param name="index">
      /// This is the index to acquire the indent from.
      /// </param>
      /// <returns>
      /// This returns the indent from the specified index.
      /// </returns>
      public String Indent(int index) {
         if(indent > 0) {
            String text = cache.Get(index);

            if(text == null) {
               text = Create();
               cache.Set(index, text);
            }
            if(cache.Size() > 0) {
               return text;
            }
         }
         return "";
      }

      /// <summary>
      /// This is used to create an indent which can later be pushed on
      /// to the stack. If the number of spaces to be added is zero then
      /// this will return a single character string with a line feed.
      /// </summary>
      /// <returns>
      /// This will create an indent to be added to the stack
      /// </returns>
      public String Create() {
         char[] text = new char[count + 1];

         if(count > 0) {
            text[0] = '\n';

            for(int i = 1; i <= count; i++) {
               text[i] = ' ';
            }
            return new String(text);
         }
         return "\n";
      }

      /// <summary>
      /// The <c>Cache</c> object is used create an indexable list
      /// which allows the indenter to quickly acquire an indent using
      /// a stack position. This ensures that the indenter need only
      /// create an index once for a given stack position. The number of
      /// indents held within this cache can also be tracked.
      /// </summary>
      private class Cache {

         /// <summary>
         /// This is used to track indent strings within the cache.
         /// </summary>
         private String[] list;

         /// <summary>
         /// Represents the number of indent strings held by the cache.
         /// </summary>
         private int count;

         /// <summary>
         /// Constructor for the <c>Cache</c> object. This creates
         /// a cache of the specified size, the specified size acts as
         /// an initial size and the cache can be expanded on demand.
         /// </summary>
         /// <param name="size">
         /// The initial number of entries in the cache.
         /// </param>
         public Cache(int size) {
            this.list = new String[size];
         }

         /// <summary>
         /// This method is used to retrieve the number of indents that
         /// have been added to the cache. This is used to determine if
         /// an indent request is the first.
         /// </summary>
         /// <returns>
         /// This returns the number of indents in the cache.
         /// </returns>
         public int Size() {
            return count;
         }

         /// <summary>
         /// This method is used to add the specified indent on to the
         /// cache. The index allows the cache to act as a stack, when
         /// the index is specified it can be used to retrieve the same
         /// indent using that index.
         /// </summary>
         /// <param name="index">
         /// This is the position to add the index to.
         /// </param>
         /// <param name="text">
         /// This is the indent to add to the position.
         /// </param>
         public void Set(int index, String text) {
            if(index >= list.Length) {
               Resize(index * 2);
            }
            if(index > count) {
               count = index;
            }
            list[index] = text;
         }

         /// <summary>
         /// This method is used to retrieve an indent from the given
         /// position. This allows the indenter to use the cache as a
         /// stack, by increasing and decreasing the index as required.
         /// </summary>
         /// <param name="index">
         /// The position to retrieve the indent from.
         /// </param>
         /// <returns>
         /// This is the indent retrieve from the given index.
         /// </returns>
         public String Get(int index) {
            if(index < list.Length) {
               return list[index];
            }
            return null;
         }

         /// <summary>
         /// Should the number of indents to be cache grows larger than
         /// the default initial size then this will increase the size
         /// of the cache. This ensures that the indenter can handle an
         /// arbitrary number of indents for a given output.
         /// </summary>
         /// <param name="size">
         /// This is the size to expand the cache to.
         /// </param>
         public void Resize(int size) {
            String[] temp = new String[size];

            for(int i = 0; i < list.Length; i++) {
               temp[i] = list[i];
            }
            list = temp;
         }
      }
   }
}