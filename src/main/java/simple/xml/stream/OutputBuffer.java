/*
 * OutputBuffer.java June 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
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

import java.io.IOException;
import java.io.Writer;

/** 
 * This is primarily used to replace the <code>StringBuffer</code> 
 * class, as a way for the <code>Formatter</code> to store the start
 * tag for an XML element. This enables the start tag of the current
 * element to be removed without disrupting any of the other nodes
 * within the document. Once the contents of the output buffer have
 * been filled its contents can be emitted to the writer object.
 *
 * @author Niall Gallagher
 */
class OutputBuffer {      

   /** 
    * The characters that this buffer has accumulated.
    */
   private StringBuilder text;
   
   /** 
    * Constructor for <code>OutputBuffer</code>. The default 
    * <code>OutputBuffer</code> stores 16 characters before a
    * resize is needed to append extra characters. 
    */
   public OutputBuffer() {
      this.text = new StringBuilder();     
   }
   
   /** 
    * This will add a <code>char</code> to the end of the buffer.
    * The buffer will not overflow with repeated uses of the 
    * <code>append</code>, it uses an <code>ensureCapacity</code>
    * method which will allow the buffer to dynamically grow in 
    * size to accomodate more characters.
    *
    * @param ch the character to be appended to the buffer
    */
   public void append(char ch){
      text.append(ch);
   }

   /** 
    * This will add a <code>String</code> to the end of the buffer.
    * The buffer will not overflow with repeated uses of the 
    * <code>append</code>, it uses an <code>ensureCapacity</code> 
    * method which will allow the buffer to dynamically grow in 
    * size to accomodate large string objects.
    *
    * @param value the string to be appended to this output buffer
    */  
   public void append(String value){
      text.append(value);
   }
   
   /** 
    * This will add a <code>char</code> to the end of the buffer.
    * The buffer will not overflow with repeated uses of the 
    * <code>append</code>, it uses an <code>ensureCapacity</code> 
    * method which will allow the buffer to dynamically grow in 
    * size to accomodate large character arrays.
    *
    * @param value the character array to be appended to this
    * @param off the read offset for the array to begin reading
    * @param len the number of characters to append to this
    */   
   public void append(char[] value, int off, int len){
      text.append(value, off, len);
   }
   
   /** 
    * This will add a <code>String</code> to the end of the buffer.
    * The buffer will not overflow with repeated uses of the 
    * <code>append</code>, it uses an <code>ensureCapacity</code>
    * method which will allow the buffer to dynamically grow in 
    * size to accomodate large string objects.
    *
    * @param value the string to be appended to the output buffer
    * @param off the offset to begin reading from the string
    * @param len the number of characters to append to this
    */   
   public void append(String value, int off, int len){
      text.append(value, off, len);
   }
   
   /**
    * This method is used to write the contents of the buffer to the
    * specified <code>Writer</code> object. This is used when the
    * XML element is to be committed to the resulting XML document.
    * 
    * @param out this is the writer to write the buffered text to
    * 
    * @throws IOException thrown if there is an I/O problem
    */
   public void write(Writer out) throws IOException {
      out.append(text);      
   }
   
   /** 
    * This will empty the <code>OutputBuffer</code> so that it does
    * not contain any content. This is used to that when the buffer
    * is written to a specified <code>Writer</code> object nothing
    * is written out. This allows XML elements to be removed.
    */
   public void clear(){     
      text.setLength(0);
   }
}   

