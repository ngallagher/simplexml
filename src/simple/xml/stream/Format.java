/*
 * Format.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General 
 * Public License along with this library; if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
 * Boston, MA  02111-1307  USA
 */

package simple.xml.stream;

/**
 * The <code>Format</code> object is used to provide information on 
 * how a generated XML document should be structured. The information
 * provided tells the formatter whether an XML prolog is required and
 * the number of spaces that should be used for indenting. Currently
 * each generated document has an XML version of "1.0".
 * <p>
 * Should a <code>Format</code> be created with an indent of zero or
 * less then no indentation is done, and the generated XML will be on
 * the same line. The prolog appears if the encoding specified is a
 * non null value, typically this should be "UTF-8".
 *
 * @author Niall Gallagher
 */ 
public class Format {

   /**
    * Represents the encoding that appears in the generated XML.
    */         
   private String encoding;
         
   /**
    * Represents the indent size to use for the generated XML.
    */ 
   private int indent;        

   /**
    * Constructor for the <code>Format</code> object. This creates an
    * object that is used to describe how the formatter should create
    * the XML document. This constructor uses an indent size of three.
    */ 
   public Format() {
      this(3);           
   }

   /**
    * Constructor for the <code>Format</code> object. This creates an
    * object that is used to describe how the formatter should create
    * the XML document. This constructor uses the specified indent
    * size and a null encoding, which means no prolog is generated.
    *
    * @param indent this is the number of spaces used in the indent
    */ 
   public Format(int indent) {
      this(indent, null);           
   }

   /**
    * Constructor for the <code>Format</code> object. This creates an
    * object that is used to describe how the formatter should create
    * the XML document. This constructor uses the specified indent
    * size and the encoding to use in the generated prolog.
    *
    * @param indent this is the number of spaces used in the indent
    * @param encoding this is the encoding to appear in the prolog
    */    
   public Format(int indent, String encoding) {
      this.encoding = encoding;           
      this.indent = indent;           
   }
   
   /**
    * This method returns the size of the indent to use for the XML
    * generated. The indent size represents the number of spaces that
    * are used for the indent, and indent of zero means no indenting.
    * 
    * @return returns the number of spaces to used for indenting
    */    
   public int getIndent() {
      return indent;            
   }   

   /**
    * This method returns the encoding to use for the generated XML.
    * This should typically be "UTF-8" but can be any encoding. If
    * a null encoding was specified then no prolog is generated.
    *
    * @return this returns the encoding to used for the prolog
    */ 
   public String getEncoding() {
      return encoding;           
   }
}
