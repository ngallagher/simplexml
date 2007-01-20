/*
 * StateReader.java July 2006
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

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.XMLEventReader;

/**
 * The <code>StateReader</code> object is used to read elements from
 * the specified XML event reader. This reads input state objects
 * that represent elements within the source XML document. This will
 * allow details to be read using input state objects, as long as
 * the end elements for those input states have not been ended.
 * <p>
 * For example, if an input state represented the root element of a
 * document then that input state could read all elements within the
 * document. However, if the input state represented a child element
 * then it would only be able to read its children.
 *
 * @author Niall Gallagher
 */ 
final class NodeReader {

   /**
    * Represents the XML event reader used to read all elements.
    */ 
   private XMLEventReader reader;     
   
   /**
    * This stack enables the reader to keep track of elements.
    */ 
   private InputStack stack;
   
   /**
    * Constructor for the <code>StateReader</code> object. This is used
    * to read XML events a input state objects from the event reader.
    *
    * @param reader this is the event reader for the XML document
    */ 
   public NodeReader(XMLEventReader reader) {
      this.stack = new InputStack();
      this.reader = reader;            
   }        
   
   /**
    * Returns the root input state for the document. This is returned
    * only if no other elements have been read. Once the root element
    * has been read from the event reader this will return null.
    *
    * @return this returns the root input state for the document
    */ 
   public InputNode readRoot() throws Exception {
      if(stack.isEmpty()) {
         return readElement(null);
      }
      return null;
   }

   /**
    * Returns the next input state from the XML document, if it is a
    * child element of the specified input state. This essentially
    * determines whether the end tag has been read for the specified
    * state, if so then null is returned. If however the specified
    * state has not had its end tag read then this returns the next
    * element, if that element is a child of the that state.
    *
    * @param from this is the input state to read with 
    *
    * @return this returns the next input state from the document
    */ 
   public InputNode readElement(InputNode from) throws Exception {
      if(!stack.contains(from) && !stack.isEmpty()) {
         return null; 
      }
      XMLEvent event = reader.nextEvent();
      
      while(event != null) {
         if(event.isEndElement()) {
            if(stack.pop() == from) {
               return null;
            }               
         } else if(event.isStartElement()) {
            return readStart(from, event);                 
         }
         event = reader.nextEvent();
      }
      return null; 
   }

   /**
    * This is used to convert the start element to an input node.
    * This will push the created input node on to the stack. The
    * input node created contains a reference to this reader. so
    * that it can be used to read child elements and values.
    * 
    * @param start this is the start element to be wrapped
    *
    * @return this returns an input node for the given element
    */    
   private InputNode readStart(InputNode from, XMLEvent event) throws Exception {
      StartElement start = event.asStartElement();
      InputElement input = new InputElement(this, start);
               
      return stack.push(input);
   }
   
   /**
    * Read the contents of the characters between the specified XML
    * element tags, if the read is currently at that element. This 
    * allows characters associated with the element to be used. If
    * the specified state is not the current state, null is returned.
    *
    * @param from this is the input state to read the value from
    *
    * @return this returns the characters from the specified state
    */ 
   public String readValue(InputNode from) throws Exception {
      StringBuilder value = new StringBuilder();
      
      while(stack.top() == from) {         
         XMLEvent event = reader.peek();
         
         if(!event.isCharacters()) {
            if(value.length() == 0) {
               return null;
            }
            return value.toString();                    
         } 
         Characters text = event.asCharacters();
         String data = text.getData();
         
         value.append(data);
         reader.nextEvent();         
      }         
      return null;
   }     
}

