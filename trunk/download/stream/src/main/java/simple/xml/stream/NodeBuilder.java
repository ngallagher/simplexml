/*
 * NodeBuilder.java July 2006
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

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLEventReader;
import java.io.Reader;
import java.io.Writer;

/**
 * The <code>NodeBuilder</code> object is used to create either an
 * input node or an output node for a given source or destination. 
 * If an <code>InputNode</code> is required for reading an XML
 * document then a reader must be provided to read the content from.
 * <p>
 * If an <code>OutputNode</code> is required then a destination is
 * required. The provided output node can be used to generate well
 * formed XML to the specified writer. 
 * 
 * @author Niall Gallagher
 */ 
public final class NodeBuilder {
 
   /**
    * This is the XML input factory used to create XML readers.
    */         
   private static XMLInputFactory factory;

   static {
      factory = XMLInputFactory.newInstance();                    
   }
        
   /**
    * This is used to create an <code>InputNode</code> that can be 
    * used to read XML from the specified reader. The reader will
    * be positioned at the root element in the XML document.
    *
    * @param source this contains the contents of the XML source
    *
    * @throws Exception thrown if there is an I/O exception
    */   
   public static InputNode read(Reader source) throws Exception {
      return read(factory.createXMLEventReader(source));   
   }

   /**
    * This is used to create an <code>InputNode</code> that can be 
    * used to read XML from the specified reader. The reader will
    * be positioned at the root element in the XML document.
    *
    * @param source this contains the contents of the XML source
    *
    * @throws Exception thrown if there is an I/O exception
    */     
   private static InputNode read(XMLEventReader source) throws Exception {
      return new NodeReader(source).readRoot();           
   }
   
   /**
    * This is used to create an <code>OutputNode</code> that can be
    * used to write a well formed XML document. The writer specified
    * will have XML elements, attributes, and text written to it as
    * output nodes are created and populated.
    * 
    * @param result this contains the result of the generated XML
    *
    * @throws Exception this is thrown if there is an I/O error
    */ 
   public static OutputNode write(Writer result) throws Exception {
      return write(result, new Format());
   }

   /**
    * This is used to create an <code>OutputNode</code> that can be
    * used to write a well formed XML document. The writer specified
    * will have XML elements, attributes, and text written to it as
    * output nodes are created and populated.
    * 
    * @param result this contains the result of the generated XML
    * @param format this is the format to use for the document
    *
    * @throws Exception this is thrown if there is an I/O error
    */ 
   public static OutputNode write(Writer result, Format format) throws Exception {
      return new NodeWriter(result, format).writeRoot();
   }   
}
