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
 * the number of spaces that should be used for indenting. The prolog
 * specified will be written directly before the XML document.
 * <p>
 * Should a <code>Format</code> be created with an indent of zero or
 * less then no indentation is done, and the generated XML will be on
 * the same line. The prolog can contain any legal XML heading, which
 * can domain a DTD declaration and XML comments if required.
 *
 * @author Niall Gallagher
 */ 
public class Format {

   /**
    * Represents the prolog that appears in the generated XML.
    */         
   private String prolog;
         
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
    * size and a null prolog, which means no prolog is generated.
    *
    * @param indent this is the number of spaces used in the indent
    */ 
   public Format(int indent) {
      this(indent, null);           
   }
   
   /**
    * Constructor for the <code>Format</code> object. This creates an
    * object that is used to describe how the formatter should create
    * the XML document. This constructor uses the specified prolog 
    * that is to be inserted at the start of the XML document.
    *
    * @param prolog this is the prolog for the generated XML document
    */    
   public Format(String prolog) {
      this(3, prolog);          
   }
   
   /**
    * Constructor for the <code>Format</code> object. This creates an
    * object that is used to describe how the formatter should create
    * the XML document. This constructor uses the specified indent
    * size and the text to use in the generated prolog.
    *
    * @param indent this is the number of spaces used in the indent
    * @param prolog this is the prolog for the generated XML document
    */    
   public Format(int indent, String prolog) {
      this.prolog = prolog;           
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
    * This method returns the prolog that is to be used at the start
    * of the generated XML document. This allows a DTD or a version
    * to be specified at the start of a document. If this returns
    * null then no prolog is written to the start of the XML document.
    *
    * @return this returns the prolog for the start of the document    
    */ 
   public String getProlog() {
      return prolog;           
   }
}
