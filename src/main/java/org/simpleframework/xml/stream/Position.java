/*
 * Position.java July 2006
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

package org.simpleframework.xml.stream;

/**
 * The <code>Position</code> object is used to acquire the position
 * of the read cursor within the XML file. This allows exceptions to
 * be thrown with the line number so that the XML can be debugged. 
 * 
 * @author Niall Gallagher
 */ 
public interface Position {

   /**
    * This is the actual line number within the read XML document. 
    * The line number allows any problems within the source XML
    * document to be debugged if it does not match the schema. 
    * This will return -1 if the line number cannot be determined.
    *
    * @return this returns the line number of an XML event 
    */        
   public int getLine();

   /**
    * This provides a textual description of the position the 
    * read cursor is at within the XML document. This allows the
    * position to be embedded within the exception thrown.
    *
    * @return this returns a textual description of the position
    */ 
   public String toString();   
}
