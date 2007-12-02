/*
 * NodeReader.java July 2006
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

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.XMLEventReader;

/**
 * The <code>NodeReader</code> object is used to read elements from
 * the specified XML event reader. This reads input node objects
 * that represent elements within the source XML document. This will
 * allow details to be read using input node objects, as long as
 * the end elements for those input nodes have not been ended.
 * <p>
 * For example, if an input node represented the root element of a
 * document then that input node could read all elements within the
 * document. However, if the input node represented a child element
 * then it would only be able to read its children.
 *
 * @author Niall Gallagher
 */ 
class NodeReader {

   /**
    * Represents the XML event reader used to read all elements.
    */ 
   private final XMLEventReader reader;     
   
   /**
    * This stack enables the reader to keep track of elements.
    */ 
   private final InputStack stack;
   
   /**
    * Constructor for the <code>NodeReader</code> object. This is used
    * to read XML events a input node objects from the event reader.
    *
    * @param reader this is the event reader for the XML document
    */ 
   public NodeReader(XMLEventReader reader) {
      this.stack = new InputStack();
      this.reader = reader;            
   }        
   
   /**
    * Returns the root input node for the document. This is returned
    * only if no other elements have been read. Once the root element
    * has been read from the event reader this will return null.
    *
    * @return this returns the root input node for the document
    */ 
   public InputNode readRoot() throws Exception {
      if(stack.isEmpty()) {
         return readElement(null);
      }
      return null;
   }
   
   /**
    * This method is used to determine if this node is the root 
    * node for the XML document. The root node is the first node
    * in the document and has no sibling nodes. This is false
    * if the node has a parent node or a sibling node.
    * 
    * @return true if this is the root node within the document
    */
   public boolean isRoot(InputNode node) {
      return stack.bottom() == node;        
   }
   
   /**
    * Returns the next input node from the XML document, if it is a
    * child element of the specified input node. This essentially
    * determines whether the end tag has been read for the specified
    * node, if so then null is returned. If however the specified
    * node has not had its end tag read then this returns the next
    * element, if that element is a child of the that node.
    *
    * @param from this is the input node to read with 
    *
    * @return this returns the next input node from the document
    */ 
   public InputNode readElement(InputNode from) throws Exception {
      if(!stack.isRelevant(from)) {         
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
    * Returns the next input node from the XML document, if it is a
    * child element of the specified input node. This essentially
    * the same as the <code>readElement(InputNode)</code> object 
    * except that this will not read the element if it does not have
    * the name specified. This essentially acts as a peak function.
    *
    * @param from this is the input node to read with 
    * @param name this is the name expected from the next element
    *
    * @return this returns the next input node from the document
    */ 
   public InputNode readElement(InputNode from, String name) throws Exception {
      if(!stack.isRelevant(from)) {        
         return null; 
     }
     XMLEvent event = reader.peek();
          
     while(event != null) {
        if(event.isEndElement()) { 
           if(stack.top() == from) {
              return null;
           } else {
              stack.pop();
           }
        } else if(event.isStartElement()) {
           if(isName(event, name)) {
              return readElement(from);
           }   
           break;
        }
        event = reader.nextEvent();
        event = reader.peek();
     }
     return null;
   }
   
   /**
    * This is used to convert the start element to an input node.
    * This will push the created input node on to the stack. The
    * input node created contains a reference to this reader. so
    * that it can be used to read child elements and values.
    * 
    * @param from this is the parent element for the start event
    * @param event this is the start element to be wrapped
    *
    * @return this returns an input node for the given element
    */    
   private InputNode readStart(InputNode from, XMLEvent event) throws Exception {
      StartElement start = event.asStartElement();
      InputElement input = new InputElement(from, this, start);
               
      return stack.push(input);
   }

   /**
    * This is used to determine the name of the node specified. The
    * name of the node is determined to be the name of the element
    * if that element is converts to a valid StAX start element.
    * 
    * @param node this is the StAX node to acquire the name from
    * @param name this is the name of the node to check against
    * 
    * @return true if the specified node has the given local name
    */
   private boolean isName(XMLEvent node, String name) {
      StartElement start = node.asStartElement();
      String local = start.getName().getLocalPart();
      
      return local.equals(name);
   }
   
   /**
    * Read the contents of the characters between the specified XML
    * element tags, if the read is currently at that element. This 
    * allows characters associated with the element to be used. If
    * the specified node is not the current node, null is returned.
    *
    * @param from this is the input node to read the value from
    *
    * @return this returns the characters from the specified node
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
   
   /**
    * This is used to determine if this input node is empty. An
    * empty node is one with no attributes or children. This can
    * be used to determine if a given node represents an empty
    * entity, with which no extra data can be extracted.
    * 
    * @param from this is the input node to read the value from
    * 
    * @return this returns true if the node is an empty element
    * 
    * @throws Exception thrown if there was a parse error
    */
   public boolean isEmpty(InputNode from) throws Exception {
      if(stack.top() == from) {         
         XMLEvent event = reader.peek();

         if(event.isEndElement()) {
            return true;
         }
      }
      return false;
   }

   /**
    * This method is used to skip an element within the XML document.
    * This will simply read each element from the document until
    * the specified element is at the top of the stack. When the
    * specified element is at the top of the stack this returns.
    *
    * @param from this is the element to skip from the XML document
    */ 
   public void skipElement(InputNode from) throws Exception {
      while(readElement(from) != null);           
   }  
}

