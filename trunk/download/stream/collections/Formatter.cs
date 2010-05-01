#region License
//
// Formatter.cs July 2006
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
using System.IO;
using System;
#endregion

namespace SimpleFramework.Xml.Stream {

   /// <summary>
   /// The <c>Formatter</c> object is used to format output as XML
   /// indented with a configurable indent level. This is used to write
   /// start and end tags, as well as attributes and values to the given
   /// writer. The output is written directly to the stream with and
   /// indentation for each element appropriate to its position in the
   /// document hierarchy. If the indent is set to zero then no indent
   /// is performed and all XML will appear on the same line.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Stream.Indenter
   /// </seealso>
   class Formatter {

      /// <summary>
      /// Represents the prefix used when declaring an XML namespace.
      /// </summary>
      private static readonly char[] Namespace = { 'x', 'm', 'l', 'n', 's' };

      /// <summary>
      /// Represents the XML escape sequence for the less than sign.
      /// </summary>
      private static readonly char[] Less = { '&', 'l', 't', ';' };

      /// <summary>
      /// Represents the XML escape sequence for the greater than sign.
      /// </summary>
      private static readonly char[] Greater = { '&', 'g', 't', ';' };

      /// <summary>
      /// Represents the XML escape sequence for the double quote.
      /// </summary>
      private static readonly char[] Double = { '&', 'q', 'u', 'o', 't', ';' };

      /// <summary>
      /// Represents the XML escape sequence for the single quote.
      /// </summary>
      private static readonly char[] Single = { '&', 'a', 'p', 'o', 's', ';' };

      /// <summary>
      /// Represents the XML escape sequence for the ampersand sign.
      /// </summary>
      private static readonly char[] And = { '&', 'a', 'm', 'p', ';' };

      /// <summary>
      /// This is used to open a comment section within the document.
      /// </summary>
      private static readonly char[] Open = { '<', '!', '-', '-', ' ' };

      /// <summary>
      /// This is used to close a comment section within the document.
      /// </summary>
      private static readonly char[] Close = { ' ', '-', '-', '>' };

      /// <summary>
      /// Output buffer used to write the generated XML result to.
      /// </summary>
      private OutputBuffer buffer;

      /// <summary>
      /// This is the writer that is used to write the XML document.
      /// </summary>
      private TextWriter result;

      /// <summary>
      /// Creates the indentations that are used buffer the XML file.
      /// </summary>
      private Indenter indenter;

      /// <summary>
      /// Represents the prolog to insert at the start of the document.
      /// </summary>
      private String prolog;

      /// <summary>
      /// Represents the last type of content that was written.
      /// </summary>
      private Tag last;

      /// <summary>
      /// Constructor for the <c>Formatter</c> object. This creates
      /// an object that can be used to write XML in an indented format
      /// to the specified writer. The XML written will be well formed.
      /// </summary>
      /// <param name="result">
      /// This is where the XML should be written to.
      /// </param>
      /// <param name="format">
      /// This is the format object to use.
      /// </param>
      public Formatter(TextWriter result, Format format) {
         this.indenter = new Indenter(format);
         this.buffer = new OutputBuffer();
         this.prolog = format.Prolog;
         this.result = result;
      }

      /// <summary>
      /// This is used to write a prolog to the specified output. This is
      /// only written if the specified <c>Format</c> object has
      /// been given a non null prolog. If no prolog is specified then no
      /// prolog is written to the generated XML.
      /// </summary>
      public void WriteProlog() {
         if(prolog != null) {
            Write(prolog);
            Write("\n");
         }
      }

      /// <summary>
      /// This is used to write any comments that have been set. The
      /// comment will typically be written at the start of an element
      /// to describe the purpose of the element or include debug data
      /// that can be used to determine any issues in serialization.
      /// </summary>
      /// <param name="comment">
      /// This is the comment that is to be written.
      /// </param>
      public void WriteComment(String comment) {
         String text = indenter.Top();

         if(last == Tag.Start) {
            Append('>');
         }
         if(text != null) {
            Append(text);
            Append(Open);
            Append(comment);
            Append(Close);
         }
         last = Tag.Comment;
      }

      /// <summary>
      /// This method is used to write a start tag for an element. If a
      /// start tag was written before this then it is closed. Before
      /// the start tag is written an indent is generated and placed in
      /// front of the tag, this is done for all but the first start tag.
      /// </summary>
      /// <param name="name">
      /// This is the name of the start tag to be written.
      /// </param>
      public void WriteStart(String name, String prefix) {
         String text = indenter.Push();

         if(last == Tag.Start) {
            Append('>');
         }
         Flush();
         Append(text);
         Append('<');

         if(!IsEmpty(prefix)) {
            Append(prefix);
            Append(':');
         }
         Append(name);
         last = Tag.Start;
      }

      /// <summary>
      /// This is used to write a name value attribute pair. If the last
      /// tag written was not a start tag then this throws an exception.
      /// All attribute values written are enclosed in double quotes.
      /// </summary>
      /// <param name="name">
      /// This is the name of the attribute to be written.
      /// </param>
      /// <param name="value">
      /// This is the value to assigne to the attribute.
      /// </param>
      public void WriteAttribute(String name, String value, String prefix) {
         if(last != Tag.Start) {
            throw new NodeException("Start element required");
         }
         Write(' ');
         Write(name, prefix);
         Write('=');
         Write('"');
         Escape(value);
         Write('"');
      }

      /// <summary>
      /// This is used to write the namespace to the element. This will
      /// write the special attribute using the prefix and reference
      /// specified. This will escape the reference if it is required.
      /// </summary>
      /// <param name="reference">
      /// This is the namespace URI reference to use.
      /// </param>
      /// <param name="prefix">
      /// This is the prefix to used for the namespace.
      /// </param>
      public void WriteNamespace(String reference, String prefix) {
         if(last != Tag.Start) {
            throw new NodeException("Start element required");
         }
         Write(' ');
         Write(Namespace);

         if(!IsEmpty(prefix)) {
            Write(':');
            Write(prefix);
         }
         Write('=');
         Write('"');
         Escape(reference);
         Write('"');
      }
      /// <summary>
      /// This is used to write the specified text value to the writer.
      /// If the last tag written was a start tag then it is closed.
      /// By default this will escape any illegal XML characters.
      /// </summary>
      /// <param name="text">
      /// This is the text to write to the output.
      /// </param>
      public void WriteText(String text) {
         WriteText(text, Mode.Escape);
      }

      /// <summary>
      /// This is used to write the specified text value to the writer.
      /// If the last tag written was a start tag then it is closed.
      /// This will use the output mode specified.
      /// </summary>
      /// <param name="text">
      /// This is the text to write to the output.
      /// </param>
      public void WriteText(String text, Mode mode) {
         if(last == Tag.Start) {
            Write('>');
         }
         if(mode == Mode.Data) {
            Data(text);
         } else {
            Escape(text);
         }
         last = Tag.Text;
      }

      /// <summary>
      /// This is used to write an end element tag to the writer. This
      /// will close the element with a short <c>/&gt;</c> if the
      /// last tag written was a start tag. However if an end tag or
      /// some text was written then a full end tag is written.
      /// </summary>
      /// <param name="name">
      /// This is the name of the element to be closed.
      /// </param>
      public void WriteEnd(String name, String prefix) {
         String text = indenter.Pop();

         if(last == Tag.Start) {
            Write('/');
            Write('>');
         } else {
            if(last != Tag.Text) {
               Write(text);
            }
            if(last != Tag.Start) {
               Write('<');
               Write('/');
               Write(name, prefix);
               Write('>');
            }
         }
         last = Tag.End;
      }

      /// <summary>
      /// This is used to write a character to the output stream without
      /// any translation. This is used when writing the start tags and
      /// end tags, this is also used to write attribute names.
      /// </summary>
      /// <param name="ch">
      /// This is the character to be written to the output.
      /// </param>
      public void Write(char ch) {
         buffer.Write(result);
         buffer.Clear();
         result.Write(ch);
      }

      /// <summary>
      /// This is used to write plain text to the output stream without
      /// any translation. This is used when writing the start tags and
      /// end tags, this is also used to write attribute names.
      /// </summary>
      /// <param name="plain">
      /// This is the text to be written to the output.
      /// </param>
      public void Write(char[] plain) {
         buffer.Write(result);
         buffer.Clear();
         result.Write(plain);
      }

      /// <summary>
      /// This is used to write plain text to the output stream without
      /// any translation. This is used when writing the start tags and
      /// end tags, this is also used to write attribute names.
      /// </summary>
      /// <param name="plain">
      /// This is the text to be written to the output.
      /// </param>
      public void Write(String plain) {
         buffer.Write(result);
         buffer.Clear();
         result.Write(plain);
      }

      /// <summary>
      /// This is used to write plain text to the output stream without
      /// any translation. This is used when writing the start tags and
      /// end tags, this is also used to write attribute names.
      /// </summary>
      /// <param name="plain">
      /// This is the text to be written to the output.
      /// </param>
      /// <param name="prefix">
      /// This is the namespace prefix to be written.
      /// </param>
      public void Write(String plain, String prefix) {
         buffer.Write(result);
         buffer.Clear();

         if(!IsEmpty(prefix)) {
            result.Write(prefix);
            result.Write(':');
         }
         result.Write(plain);
      }

      /// <summary>
      /// This is used to buffer a character to the output stream without
      /// any translation. This is used when buffering the start tags so
      /// that they can be reset without affecting the resulting document.
      /// </summary>
      /// <param name="ch">
      /// This is the character to be written to the output.
      /// </param>
      public void Append(char ch) {
         buffer.Append(ch);
      }

      /// <summary>
      /// This is used to buffer characters to the output stream without
      /// any translation. This is used when buffering the start tags so
      /// that they can be reset without affecting the resulting document.
      /// </summary>
      /// <param name="plain">
      /// This is the string that is to be buffered.
      /// </param>
      public void Append(char[] plain) {
         buffer.Append(plain);
      }

      /// <summary>
      /// This is used to buffer characters to the output stream without
      /// any translation. This is used when buffering the start tags so
      /// that they can be reset without affecting the resulting document.
      /// </summary>
      /// <param name="plain">
      /// This is the string that is to be buffered.
      /// </param>
      public void Append(String plain) {
         buffer.Append(plain);
      }

      /// <summary>
      /// This method is used to write the specified text as a CDATA block
      /// within the XML element. This is typically used when the value is
      /// large or if it must be preserved in a format that will not be
      /// affected by other XML parsers. For large text values this is
      /// also faster than performing a character by character escaping.
      /// </summary>
      /// <param name="value">
      /// This is the text value to be written as CDATA.
      /// </param>
      public void Data(String value) {
         Write("<![CDATA[");
         Write(value);
         Write("]]>");
      }

      /// <summary>
      /// This is used to write the specified value to the output with
      /// translation to any symbol characters or non text characters.
      /// This will translate the symbol characters such as "&amp;",
      /// "&gt;", "&lt;", and "&quot;". This also writes any non text
      /// and non symbol characters as integer values like "&#123;".
      /// </summary>
      /// <param name="value">
      /// The text value to be escaped and written.
      /// </param>
      public void Escape(String value) {
         int size = value.Length;

         for(int i = 0; i < size; i++) {
            Escape(value[i]);
         }
      }

      /// <summary>
      /// This is used to write the specified value to the output with
      /// translation to any symbol characters or non text characters.
      /// This will translate the symbol characters such as "&amp;",
      /// "&gt;", "&lt;", and "&quot;". This also writes any non text
      /// and non symbol characters as integer values like "&#123;".
      /// </summary>
      /// <param name="ch">
      /// The text character to be escaped and written.
      /// </param>
      public void Escape(char ch) {
         char[] text = Symbol(ch);

         if(text != null) {
            Write(text);
         } else {
            Write(ch);
         }
      }

      /// <summary>
      /// This is used to flush the writer when the XML if it has been
      /// buffered. The flush method is used by the node writer after an
      /// end element has been written. Flushing ensures that buffering
      /// does not affect the result of the node writer.
      /// </summary>
      public void Flush() {
         buffer.Write(result);
         buffer.Clear();
         result.Flush();
      }

      /// <summary>
      /// This is used to convert the the specified character to unicode.
      /// This will simply get the decimal representation of the given
      /// character as a string so it can be written as an escape.
      /// </summary>
      /// <param name="ch">
      /// This is the character that is to be converted.
      /// </param>
      /// <returns>
      /// This is the decimal value of the given character.
      /// </returns>
      public String Unicode(char ch) {
         return Convert.ToDecimal(ch).ToString();
      }

      /// <summary>
      /// This method is used to determine if a root annotation value is
      /// an empty value. Rather than determining if a string is empty
      /// be comparing it to an empty string this method allows for the
      /// value an empty string represents to be changed in future.
      /// </summary>
      /// <param name="value">
      /// This is the value to determine if it is empty.
      /// </param>
      /// <returns>
      /// True if the string value specified is an empty value.
      /// </returns>
      public bool IsEmpty(String value) {
         if(value != null) {
            return value.Length == 0;
         }
         return true;
      }

      /// <summary>
      /// This is used to determine if the specified character is a text
      /// character. If the character specified is not a text value then
      /// this returls true, otherwise this returns false.
      /// </summary>
      /// <param name="ch">
      /// This is the character to be evaluated as text.
      /// </param>
      /// <returns>
      /// This returns the true if the character is textual.
      /// </returns>
      public bool IsText(char ch) {
         switch(ch) {
            case ' ':
            case '\n':
            case '\r':
            case '\t':
               return true;
         }
         if(ch > ' ' && ch <= 0x7E) {
            return ch != 0xF7;
         }
         return false;
      }

      /// <summary>
      /// This is used to convert the specified character to an XML text
      /// symbol if the specified character can be converted. If the
      /// character cannot be converted to a symbol null is returned.
      /// </summary>
      /// <param name="ch">
      /// This is the character that is to be converted.
      /// </param>
      /// <returns>
      /// This is the symbol character that has been resolved.
      /// </returns>
      public char[] Symbol(char ch) {
         switch(ch) {
            case '<':
               return Less;
            case '>':
               return Greater;
            case '"':
               return Double;
            case '\'':
               return Single;
            case '&':
               return And;
         }
         return null;
      }

      /// <summary>
      /// This is used to enumerate the different types of tag that can
      /// be written. Each tag represents a state for the writer. After
      /// a specific tag type has been written the state of the writer
      /// is updated. This is needed to write well formed XML text.
      /// </summary>
      private enum Tag {
         Comment,
         Start,
         Text,
         End
      }
   }
}
