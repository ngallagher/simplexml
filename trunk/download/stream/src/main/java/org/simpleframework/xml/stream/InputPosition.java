/*
 * InputPosition.java July 2006
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

import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.Location;

/**
 * The <code>InputPosition</code> object is used to acquire the line
 * number within the XML document. This allows debugging to be done
 * when a problem occurs with the source document. This object can 
 * be converted to a string using the <code>toString</code> method.
 *
 * @author Niall Gallagher
 */ 
class InputPosition implements Position {

   /**
    * This is the XML event that the position is acquired for.
    */         
   private XMLEvent source;
        
   /**
    * Constructor for the <code>InputPosition</code> object. This is
    * used to create a position description if the provided event 
    * is not null. This will return -1 if the specified event does
    * not provide any location information.    
    *
    * @param source this is the XML event to get the position of
    */ 
   public InputPosition(XMLEvent source) {
      this.source = source;
   }

   /**
    * This is the actual line number within the read XML document. 
    * The line number allows any problems within the source XML
    * document to be debugged if it does not match the schema. 
    * This will return -1 if the line number cannot be determined.
    *
    * @return this returns the line number of an XML event 
    */ 
   public int getLine() {
      Location line = getLocation();
      
      if(line != null) {
         return line.getLineNumber();
      }   
      return -1;
   }

   /**
    * This is used to acquire the location information from the
    * provided XML event. This is called only if the provided XML
    * event is not null. This provides the StAX line numbering.
    * 
    * @return this returns the StAX location information
    */ 
   private Location getLocation() {
      if(source != null) {
         return source.getLocation();
      }
      return null;
   }

   /**
    * This provides a textual description of the position the 
    * read cursor is at within the XML document. This allows the
    * position to be embedded within the exception thrown.
    *
    * @return this returns a textual description of the position
    */    
   public String toString() {
      return String.format("line %s", getLine());           
   }
}
