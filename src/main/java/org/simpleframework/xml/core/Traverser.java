/*
 * Traverser.java July 2006
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

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Style;

/**
 * The <code>Traverser</code> object is used to traverse the XML class
 * schema and either serialize or deserialize an object. This is the
 * root of all serialization and deserialization operations. It uses
 * the <code>Root</code> annotation to ensure that the XML schema
 * matches the provided XML element. If no root element is defined the
 * serialization and deserialization cannot be performed.
 *
 * @author Niall Gallagher
 */ 
class Traverser {

   /**
    * This is the context object used for the traversal performed.
    */
   private final Context context;
   
   /**
    * This is the style that is used to style the XML roots.
    */
   private final Style style;
        
   /**
    * Constructor for the <code>Traverser</code> object. This creates
    * a traverser that can be used to perform serialization or
    * or deserialization of an object. This requires a source object.
    * 
    * @param context the context object used for the traversal
    */
   public Traverser(Context context) {
      this.style = context.getStyle();
      this.context = context;           
   }
   
   /**
    * This <code>read</code> method is used to deserialize an object 
    * from the provided XML element. The class provided acts as the
    * XML schema definition used to control the deserialization. If
    * the XML schema does not have a <code>Root</code> annotation 
    * this throws an exception. Also if the root annotation name is
    * not the same as the XML element name an exception is thrown.  
    * 
    * @param node this is the node that is to be deserialized
    * @param type this is the XML schema class to be used
    * 
    * @return an object deserialized from the XML element 
    * 
    * @throws Exception if the XML schema does not match the node
    */
   public Object read(InputNode node, Class type) throws Exception {
      Composite factory = getComposite(type);           
      Object value = factory.read(node);
      Class real = value.getClass();

      return read(node, real, value);
   }
   
   /**
    * This <code>read</code> method will read the contents of the XML
    * document from the provided source and populate the object with
    * the values deserialized. This is used as a means of injecting an
    * object with values deserialized from an XML document. If the
    * XML source cannot be deserialized or there is a problem building
    * the object graph an exception is thrown. 
    * 
    * @param node this is the node that is to be deserialized
    * @param value this is the value that is to be deserialized
    * 
    * @return an object deserialized from the XML element 
    * 
    * @throws Exception if the XML schema does not match the node
    */
   public Object read(InputNode node, Object value) throws Exception {
      Class type = value.getClass();
      Composite factory = getComposite(type);        
      Object real = factory.read(node, value);
      
      return read(node, type, real);
   }
   
   /**
    * This <code>read</code> method is used to deserialize an object 
    * from the provided XML element. The class provided acts as the
    * XML schema definition used to control the deserialization. If
    * the XML schema does not have a <code>Root</code> annotation 
    * this throws an exception. Also if the root annotation name is
    * not the same as the XML element name an exception is thrown.  
    * 
    * @param node this is the node that is to be deserialized
    * @param value this is the XML schema object to be used
    * 
    * @return an object deserialized from the XML element 
    * 
    * @throws Exception if the XML schema does not match the XML
    */ 
   private Object read(InputNode node, Class type, Object value) throws Exception {
      String root = getName(type);
     
      if(root == null) {
         throw new RootException("Root annotation required for %s", type);
      }
      return value;
   }
   
   /**
    * This <code>validate</code> method will validate the contents of
    * the XML document against the specified XML class schema. This is
    * used to perform a read traversal of the class schema such that 
    * the document can be tested against it. This is preferred to
    * reading the document as it does not instantiate the objects or
    * invoke any callback methods, thus making it a safe validation.
    * 
    * @param type this is the class type to be validated against XML
    * @param node this provides the source of the XML document
    * 
    * @return true if the document matches the class XML schema 
    * 
    * @throws Exception if the class XML schema does not fully match
    */
   public boolean validate(InputNode node, Class type) throws Exception {
      Composite factory = getComposite(type);
      String root = getName(type);
      
      if(root == null) {
         throw new RootException("Root annotation required for %s", type);
      }
      return factory.validate(node);
   }
   
   /**
    * This <code>write</code> method is used to convert the provided
    * object to an XML element. This creates a child node from the
    * given <code>OutputNode</code> object. Once this child element 
    * is created it is populated with the fields of the source object
    * in accordance with the XML schema class.  
    * 
    * @param source this is the object to be serialized to XML
    * 
    * @throws Exception thrown if there is a problem serializing
    */
   public void write(OutputNode node, Object source) throws Exception {
      write(node, source, source.getClass());
   }

   /**
    * This <code>write</code> method is used to convert the provided
    * object to an XML element. This creates a child node from the
    * given <code>OutputNode</code> object. Once this child element 
    * is created it is populated with the fields of the source object
    * in accordance with the XML schema class.  
    * 
    * @param source this is the object to be serialized to XML
    * @param expect this is the class that is expected to be written
    * 
    * @throws Exception thrown if there is a problem serializing
    */
   public void write(OutputNode node, Object source, Class expect) throws Exception {
      Class type = source.getClass();      
      String root = getName(type);

      if(root == null) {
         throw new RootException("Root annotation required for %s", type);
      }
      write(node, source, expect, root);
   }
   
   /**
    * This <code>write</code> method is used to convert the provided
    * object to an XML element. This creates a child node from the
    * given <code>OutputNode</code> object. Once this child element 
    * is created it is populated with the fields of the source object
    * in accordance with the XML schema class.  
    * 
    * @param source this is the object to be serialized to XML
    * @param expect this is the class that is expected to be written
    * @param name this is the name of the root annotation used 
    * 
    * @throws Exception thrown if there is a problem serializing
    */
   public void write(OutputNode node, Object source, Class expect, String name) throws Exception {
      OutputNode child = node.getChild(name);
      
      if(source != null) {
         Class type = source.getClass();
     
         if(!context.setOverride(expect, source, child)) {
            Converter convert = getComposite(type);
            Decorator decorator = getDecorator(type);
            
            decorator.decorate(child);
            convert.write(child, source);         
         }
      }         
      child.commit();      
   }
   
   /**
    * This will acquire the <code>Decorator</code> for the type.
    * A decorator is an object that adds various details to the
    * node without changing the overall structure of the node. For
    * example comments and namespaces can be added to the node with
    * a decorator as they do not affect the deserialization.
    * 
    * @param type this is the type to acquire the decorator for 
    *
    * @return this returns the decorator associated with this
    */
   private Decorator getDecorator(Class type) throws Exception {
      return context.getDecorator(type);
   }
   
   /**
    * This will create a <code>Composite</code> object using the XML 
    * schema class provided. This makes use of the source object that
    * this traverser has been given to create a composite converter. 
    * 
    * @param type this is the XML schema class to be used
    * 
    * @return a converter for the specified XML schema class
    */
   private Composite getComposite(Class type) throws Exception {
      return new Composite(context, type);
   }
   
   /**
    * Extracts the <code>Root</code> annotation from the provided XML
    * schema class. If no annotation exists in the provided class the
    * super class is checked and so on until the <code>Object</code>
    * is encountered, if no annotation is found this returns null.
    *  
    * @param type this is the XML schema class to use
    * 
    * @return this returns the root annotation for the XML schema
    */   
   protected String getName(Class type) throws Exception {
      String root = Factory.getName(type);
      String name = style.getElement(root);
      
      return name;
   }
}
