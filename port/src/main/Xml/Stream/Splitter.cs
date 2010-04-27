#region License
//
// Splitter.cs July 2008
//
// Copyright (C) 2008, Niall Gallagher <niallg@users.sf.net>
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
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   /// <summary>
   /// The <c>Splitter</c> object is used split up a string in to
   /// tokens that can be used to create a camel case or hyphenated text
   /// representation of the string. This will preserve acronyms and
   /// numbers and splits tokens by case and character type. Examples
   /// of how a string would be splitted are as follows.
   /// </code>
   ///    CamelCaseString = "Camel" "Case" "String"
   ///    hyphenated-text = "hyphenated" "text"
   ///    URLAcronym      = "URL" "acronym"
   ///    RFC2616.txt     = "RFC" "2616" "txt"
   /// </code>
   /// By splitting strings in to individual words this allows the
   /// splitter to be used to assemble the words in a way that adheres
   /// to a specific style. Each style can then be applied to an XML
   /// document to give it a consistent format.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Stream.Style
   /// </seealso>
   internal abstract class Splitter {
      /// <summary>
      /// This is the string builder used to build the processed text.
      /// </summary>
      protected StringBuilder builder;
      /// <summary>
      /// This is the original text that is to be split in to words.
      /// </summary>
      protected char[] text;
      /// <summary>
      /// This is the number of characters to be considered for use.
      /// </summary>
      protected int count;
      /// <summary>
      /// This is the current read offset of the text string.
      /// </summary>
      protected int off;
      /// <summary>
      /// Constructor of the <c>Splitter</c> object. This is used
      /// to split the provided string in to individual words so that
      /// they can be assembled as a styled token, which can represent
      /// an XML attribute or element.
      /// </summary>
      /// <param name="source">
      /// this is the source that is to be split
      /// </param>
      public Splitter(String source) {
         this.builder = new StringBuilder();
         this.text = source.ToCharArray();
         this.count = text.Length;
      }
      /// <summary>
      /// This is used to process the internal string and convert it in
      /// to a styled string. The styled string can then be used as an
      /// XML attribute or element providing a consistent format to the
      /// document that is being generated.
      /// </summary>
      /// <returns>
      /// the string that has been converted to a styled string
      /// </returns>
      public String Process() {
         while(off < count) {
            while(off < count) {
               char ch = text[off];
               if(!IsSpecial(ch)) {
                  break;
               }
               off++;
            }
            if(!Acronym()) {
               Token();
               Number();
            }
         }
         return builder.ToString();
      }
      /// <summary>
      /// This is used to extract a token from the source string. Once a
      /// token has been extracted the <c>Commit</c> method is
      /// called to add it to the string being build. Each time this is
      /// called a token, if extracted, will be committed to the string.
      /// Before being committed the string is parsed for styling.
      /// </summary>
      public void Token() {
         int mark = off;
         while(mark < count) {
            char ch = text[mark];
            if(!IsLetter(ch)) {
               break;
            }
            if(mark > off) {
               if(IsUpper(ch)) {
                  break;
               }
            }
            mark++;
         }
         if(mark > off) {
            Parse(text, off, mark - off);
            Commit(text, off, mark - off);
         }
         off = mark;
      }
      /// <summary>
      /// This is used to extract a acronym from the source string. Once
      /// a token has been extracted the <c>Commit</c> method is
      /// called to add it to the string being build. Each time this is
      /// called a token, if extracted, will be committed to the string.
      /// </summary>
      /// <returns>
      /// true if an acronym was extracted from the source
      /// </returns>
      public bool Acronym() {
         int mark = off;
         int size = 0;
         while(mark < count) {
            char ch = text[mark];
            if(IsUpper(ch)) {
               size++;
            } else {
               break;
            }
            mark++;
         }
         if(size > 1) {
            if(mark < count) {
               char ch = text[mark-1];
               if(IsUpper(ch)) {
                  mark--;
               }
            }
            Commit(text, off, mark - off);
            off = mark;
         }
         return size > 1;
      }
      /// <summary>
      /// This is used to extract a number from the source string. Once
      /// a token has been extracted the <c>Commit</c> method is
      /// called to add it to the string being build. Each time this is
      /// called a token, if extracted, will be committed to the string.
      /// </summary>
      /// <returns>
      /// true if an number was extracted from the source
      /// </returns>
      public bool Number() {
         int mark = off;
         int size = 0;
         while(mark < count) {
            char ch = text[mark];
            if(IsDigit(ch)) {
               size++;
            } else {
               break;
            }
            mark++;
         }
         if(size > 0) {
            Commit(text, off, mark - off);
         }
         off = mark;
         return size > 0;
      }
      /// <summary>
      /// This is used to determine if the provided string evaluates to
      /// a letter character. This delegates to <c>System.Char</c>
      /// so that the full range of unicode characters are considered.
      /// </summary>
      /// <param name="ch">
      /// this is the character that is to be evaluated
      /// </param>
      /// <returns>
      /// this returns true if the character is a letter
      /// </returns>
      public bool IsLetter(char ch) {
         return Char.IsLetter(ch);
      }
      /// <summary>
      /// This is used to determine if the provided string evaluates to
      /// a symbol character. This delegates to <c>System.Char</c>
      /// so that the full range of unicode characters are considered.
      /// </summary>
      /// <param name="ch">
      /// this is the character that is to be evaluated
      /// </param>
      /// <returns>
      /// this returns true if the character is a symbol
      /// </returns>
      public bool IsSpecial(char ch) {
         return !Char.IsLetter(ch) && !Char.IsDigit(ch);
      }
      /// <summary>
      /// This is used to determine if the provided string evaluates to
      /// a digit character. This delegates to <c>Character</c>
      /// so that the full range of unicode characters are considered.
      /// </summary>
      /// <param name="ch">
      /// this is the character that is to be evaluated
      /// </param>
      /// <returns>
      /// this returns true if the character is a digit
      /// </returns>
      public bool IsDigit(char ch) {
         return Char.IsDigit(ch);
      }
      /// <summary>
      /// This is used to determine if the provided string evaluates to
      /// an upper case letter. This delegates to <c>System.Char</c>
      /// so that the full range of unicode characters are considered.
      /// </summary>
      /// <param name="ch">
      /// this is the character that is to be evaluated
      /// </param>
      /// <returns>
      /// this returns true if the character is upper case
      /// </returns>
      public bool IsUpper(char ch) {
         return Char.IsUpper(ch);
      }
      /// <summary>
      /// This is used to convert the provided character to an upper
      /// case character. This delegates to <c>System.Char</c> to
      /// perform the conversion so unicode characters are considered.
      /// </summary>
      /// <param name="ch">
      /// this is the character that is to be converted
      /// </param>
      /// <returns>
      /// the character converted to upper case
      /// </returns>
      public char ToUpper(char ch) {
         return Char.ToUpper(ch);
      }
      /// <summary>
      /// This is used to convert the provided character to a lower
      /// case character. This delegates to <c>System.Char</c> to
      /// perform the conversion so unicode characters are considered.
      /// </summary>
      /// <param name="ch">
      /// this is the character that is to be converted
      /// </param>
      /// <returns>
      /// the character converted to lower case
      /// </returns>
      public char ToLower(char ch) {
         return Char.ToLower(ch);
      }
      /// <summary>
      /// This is used to parse the provided text in to the style that
      /// is required. Manipulation of the text before committing it
      /// ensures that the text adheres to the required style.
      /// </summary>
      /// <param name="text">
      /// this is the text buffer to acquire the token from
      /// </param>
      /// <param name="off">
      /// this is the offset in the buffer token starts at
      /// </param>
      /// <param name="len">
      /// this is the length of the token to be parsed
      /// </param>
      public virtual void Parse(char[] text, int off, int len);
      /// <summary>
      /// This is used to commit the provided text in to the style that
      /// is required. Committing the text to the buffer assembles the
      /// tokens resulting in a complete token.
      /// </summary>
      /// <param name="text">
      /// this is the text buffer to acquire the token from
      /// </param>
      /// <param name="off">
      /// this is the offset in the buffer token starts at
      /// </param>
      /// <param name="len">
      /// this is the length of the token to be committed
      /// </param>
      public virtual void Commit(char[] text, int off, int len);
   }
}
