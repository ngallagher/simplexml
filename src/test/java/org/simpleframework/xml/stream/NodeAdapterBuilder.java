/*
 * NodeAdapterBuilder.java January 2010
 *
 * Copyright (C) 2010, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml.stream;

import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.w3c.dom.Document;

/**
 * Read with only the DOM w3c libraries as an experiment to getting Simple
 * working with Google Android.
 * 
 * @author Niall Gallagher
 */
public class NodeAdapterBuilder {
   public static InputNode read(Document document) throws Exception{
      EventReader stream = new DocumentReader(document);
      NodeReader reader = new NodeReader(stream);
      return reader.readRoot();
   }
   public static OutputNode write(Writer writer) throws Exception{
      return write(writer, new Format());
   }
   public static OutputNode write(Writer writer, Format format) throws Exception{
      return new NodeWriter(writer, format).writeRoot();
   }
    private static class InputAttribute implements InputNode {
       private InputNode parent;
       private String name;
       private String value;  
       public InputAttribute(InputNode parent, Attribute source) {
          this.name = source.getName();
          this.value = source.getValue();     
       }
       public InputAttribute(InputNode parent, String name, String value) {
         this.parent = parent;
          this.value = value;
          this.name = name;           
       }
       public InputNode getParent() {
          return parent;
       }
       public Position getPosition() {
          return new Position(){
            public int getLine() {
               return -1;
            }   
          };         
       }
       public String getName() {
          return name;
       }
       public String getPrefix() {
          return null;
       }
       public String getReference() {
          return null;
       }
       public String getValue() {
          return value;
       }
       public boolean isRoot() {
          return false;
       }
       public boolean isElement() {
          return false;           
       } 
       public InputNode getAttribute(String name) {
          return null;
       }
       public NodeMap<InputNode> getAttributes() {
          return new InputNodeMap(this);
       }
       public InputNode getNext() {
          return null;           
       }
       public InputNode getNext(String name) {
          return null;           
       }
       public void skip() {
          return;           
       }
       public boolean isEmpty() {
          return false;
       }
       public String toString() {
          return String.format("attribute %s='%s'", name, value);
       }
    }
   private static class InputNodeMap extends LinkedHashMap<String, InputNode> implements NodeMap<InputNode> {
      private final InputNode source; 
      protected InputNodeMap(InputNode source) {
         this.source = source;            
      }  
      public InputNodeMap(InputNode source, NodeEvent element) {
         this.source = source;           
         this.put(element);   
      }
      public InputNode getNode() {
          return source;
      }    
      public String getName() {
         return source.getName();           
      } 
      private void put(NodeEvent element) {
         for(Attribute value : element) {
            InputNode node = new InputAttribute(source, value);
            String name = node.getName();
            if(name != null) {
               put(name, node);
            }
         }
      }
      public InputNode put(String name, String value) {
         InputNode node = new InputAttribute(source, name, value);
         if(name != null) {
            put(name, node);
         }
         return node;
      }
      public InputNode remove(String name) {
         return super.remove(name);
      }  
      public InputNode get(String name) {
         return super.get(name);
      }
      public Iterator<String> iterator() {
         return keySet().iterator();
      }
   }
   private static class InputElement implements InputNode {
      private final NodeEvent element;
      private final InputNodeMap map;
      private final NodeReader reader;
      private final InputNode parent;
      public InputElement(InputNode parent, NodeReader reader, NodeEvent element) {
         this.map = new InputNodeMap(this, element);      
         this.element = element;
         this.reader = reader;           
         this.parent = parent;
      }
      public InputNode getParent() {
         return parent;
      }
      public Position getPosition() {
         return new Position(){
            public int getLine(){
               return -1;
            }
         };
      }  
      public String getName() {
         return element.getName();           
      }
      public String getPrefix() {
         return element.getPrefix();
      }
      public String getReference() {
         return element.getReference();
      }
      public boolean isRoot() {
         return reader.isRoot(this);
      }
      public boolean isElement() {
         return true;           
      } 
      public InputNode getAttribute(String name) {
         return map.get(name);
      }
      public NodeMap<InputNode> getAttributes() {
         return map;
      }
      public String getValue() throws Exception {
         return reader.readValue(this);           
      }
      public InputNode getNext() throws Exception {
         return reader.readElement(this);
      }
      public InputNode getNext(String name) throws Exception {
         return reader.readElement(this, name);
      }
      public void skip() throws Exception {
         reader.skipElement(this);           
      }
      public boolean isEmpty() throws Exception {
         if(!map.isEmpty()) {
            return false;
         }
         return reader.isEmpty(this);           
      }
      public String toString() {
         return String.format("element %s", getName());
      }
   }
   private static class NodeReader {
      private final EventReader reader; 
      private final InputStack stack;
      public NodeReader(EventReader reader) {
         this.stack = new InputStack();
         this.reader = reader;            
      }   
      public InputNode readRoot() throws Exception {
         if(stack.isEmpty()) {
            return readElement(null);
         }
         return null;
      }
      public boolean isRoot(InputNode node) {
         return stack.bottom() == node;        
      }
      public InputNode readElement(InputNode from) throws Exception {
         if(!stack.isRelevant(from)) {         
            return null; 
         }
         NodeEvent event = reader.next();
         while(event != null) {
            if(event.isEnd()) {
               if(stack.pop() == from) {
                  return null;
               }               
            } else if(event.isStart()) {
               return readStart(from, event);                 
            }
            event = reader.next();
         }
         return null; 
      }
      public InputNode readElement(InputNode from, String name) throws Exception {
         if(!stack.isRelevant(from)) {        
            return null; 
        }
        NodeEvent event = reader.peek();
        while(event != null) {
           if(event.isEnd()) { 
              if(stack.top() == from) {
                 return null;
              } else {
                 stack.pop();
              }
           } else if(event.isStart()) {
              if(isName(event, name)) {
                 return readElement(from);
              }   
              break;
           }
           event = reader.next();
           event = reader.peek();
        }
        return null;
      }
      private InputNode readStart(InputNode from, NodeEvent event) throws Exception {
         InputElement input = new InputElement(from, this, event);
         return stack.push(input);
      }
      private boolean isName(NodeEvent event, String name) {
         String local = event.getName();
         return local.equals(name);
      }
      public String readValue(InputNode from) throws Exception {
         StringBuilder value = new StringBuilder();
         while(stack.top() == from) {         
            NodeEvent event = reader.peek();
            if(!event.isText()) {
               if(value.length() == 0) {
                  return null;
               }
               return value.toString();                    
            } 
            String text = event.getValue();
            value.append(text);
            reader.next();         
         }         
         return null;
      } 
      public boolean isEmpty(InputNode from) throws Exception {
         if(stack.top() == from) {         
            NodeEvent event = reader.peek();
            if(event.isEnd()) {
               return true;
            }
         }
         return false;
      }
      public void skipElement(InputNode from) throws Exception {
         while(readElement(from) != null);           
      }  
   }
}
