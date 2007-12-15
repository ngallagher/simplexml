/*
 * Primitive.java July 2006
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

package org.simpleframework.xml.load;

import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 * The <code>Primitive</code> object is used to provide serialization
 * for primitive objects. This can serialize and deserialize any
 * primitive object and enumerations. Primitive values are converted
 * to text using the <code>String.valueOf</code> method. Enumerated
 * types are converted using the <code>Enum.valueOf</code> method.
 * <p>
 * Text within attributes and elements can contain template variables
 * similar to those found in Apache <cite>Ant</cite>. This allows
 * values such as system properties, environment variables, and user
 * specified mappings to be inserted into the text in place of the
 * template reference variables.
 * <pre>
 * 
 *    &lt;example attribute="${value}&gt;
 *       &lt;text&gt;Text with a ${variable}&lt;/text&gt;
 *    &lt;/example&gt;
 * 
 * </pre>
 * In the above XML element the template variable references will be
 * checked against the <code>Filter</code> object used by the source
 * serialization object. If they corrospond to a filtered value then
 * they are replaced, if not the text remains unchanged.
 *
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.filter.Filter
 */ 
class Primitive implements Converter {

   /**
    * This is used to convert the string values to primitives.
    */         
   private final PrimitiveFactory factory;
        
   /**
    * The source object is used to perform text value filtering.
    */ 
   private final Source root;
   
   /**
    * This the value used to represent a null primitive value.
    */
   private final String empty;
   
   /**
    * Constructor for the <code>Primitive</code> object. This is used
    * to convert an XML node to a primitive object and vice versa. To
    * perform deserialization the primitive object requires the source
    * object used for the instance of serialization to peformed.
    *
    * @param root the source object used for the serialization
    * @param type this is the type of primitive this represents
    */ 
   public Primitive(Source root, Class type) {
      this(root, type, null);          
   }
   
   /**
    * Constructor for the <code>Primitive</code> object. This is used
    * to convert an XML node to a primitive object and vice versa. To
    * perform deserialization the primitive object requires the source
    * object used for the instance of serialization to peformed.
    *
    * @param root the source object used for the serialization
    * @param type this is the type of primitive this represents
    * @param empty this is the value used to represent a null value
    */ 
   public Primitive(Source root, Class type, String empty) {
      this.factory = new PrimitiveFactory(root, type);   
      this.empty = empty;
      this.root = root;           
   }

   /**
    * This <code>read</code> method will extract the text value from
    * the node and replace any template variables before converting
    * it to a primitive value. This uses the <code>Source</code>
    * object used for this instance of serialization to replace all
    * template variables with values from the source filter.
    *
    * @param node this is the node to be converted to a primitive
    *
    * @return this returns the primitive that has been deserialized
    */ 
   public Object read(InputNode node) throws Exception{
      if(node.isElement()) {
         return readElement(node);
      }
      return readTemplate(node);
   }  
   
   /**
    * This <code>read</code> method will extract the text value from
    * the node and replace any template variables before converting
    * it to a primitive value. This uses the <code>Source</code>
    * object used for this instance of serialization to replace all
    * template variables with values from the source filter.
    *
    * @param node this is the node to be converted to a primitive
    *
    * @return this returns the primitive that has been deserialized
    */ 
   private Object readElement(InputNode node) throws Exception {
      Type type = factory.getInstance(node);
      
      if(!type.isReference()) {
         return readElement(node, type);
      }
      return type.getInstance();
   }
   
   /**
    * This <code>read</code> methos will extract the text value from
    * the node and replace any template variables before converting
    * it to a primitive value. This uses the <code>Source</code>
    * object used for this instance of serialization to replace all
    * template variables with values from the source filter.
    *
    * @param node this is the node to be converted to a primitive
    *
    * @return this returns the primitive that has been deserialized
    */ 
   private Object readElement(InputNode node, Type type) throws Exception {
      Object value = readTemplate(node);
      
      if(value != null) {
         return type.getInstance(value);
      }
      return value;
   }

   /**
    * This <code>read</code> methos will extract the text value from
    * the node and replace any template variables before converting
    * it to a primitive value. This uses the <code>Source</code>
    * object used for this instance of serialization to replace all
    * template variables with values from the source filter.
    *
    * @param node this is the node to be converted to a primitive
    *
    * @return this returns the primitive that has been deserialized
    */ 
   private Object readTemplate(InputNode node) throws Exception{
      String value = node.getValue();
      
      if(value == null) {
         return null;
      }
      if(empty != null && value.equals(empty)) {
         return empty;         
      }
      return readTemplate(value);
   }
   
   /**
    * This <code>read</code> methos will extract the text value from
    * the node and replace any template variables before converting
    * it to a primitive value. This uses the <code>Source</code>
    * object used for this instance of serialization to replace all
    * template variables with values from the source filter.
    *
    * @param value this is the value to be processed as a template
    *
    * @return this returns the primitive that has been deserialized
    */ 
   private Object readTemplate(String value) throws Exception {
      String text = root.getProperty(value);
      
      if(text != null) {
         return factory.getInstance(text);
      }
      return null;
   }  
   
   /**
    * This <code>validate</code> method will validate the primitive 
    * by checking the node text. If the value is a reference then 
    * this will not extract any value from the node. Transformation
    * of the extracted value is not done as it can not account for
    * template variables. Thus any text extracted is valid.
    *
    * @param node this is the node to be validated as a primitive
    *
    * @return this returns the primitive that has been validated
    */ 
   public boolean validate(InputNode node) throws Exception {
      if(node.isElement()) {
         validateElement(node);
      } else {
         node.getValue();
      }
      return true;
   }
   
   /**
    * This <code>validateElement</code> method validates a primitive 
    * by checking the node text. If the value is a reference then 
    * this will not extract any value from the node. Transformation
    * of the extracted value is not done as it can not account for
    * template variables. Thus any text extracted is valid.
    *
    * @param node this is the node to be validated as a primitive
    *
    * @return this returns the primitive that has been validated
    */ 
   private boolean validateElement(InputNode node) throws Exception {
      Type type = factory.getInstance(node);
      
      if(!type.isReference()) {         
         type.getInstance(type);
      }
      return true;
   }
   
   /**
    * This <code>write</code> method will serialize the contents of
    * the provided object to the given XML element. This will use
    * the <code>String.valueOf</code> method to convert the object to
    * a string if the object represents a primitive, if however the
    * object represents an enumerated type then the text value is
    * created using <code>Enum.name</code>.
    *
    * @param source this is the object to be serialized
    * @param node this is the XML element to have its text set
    */  
   public void write(OutputNode node, Object source) throws Exception {
      String text = factory.getText(source);
    
      if(text != null) {
         node.setValue(text);
      }  
   }
}
