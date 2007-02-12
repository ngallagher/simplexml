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

package simple.xml.load;

import simple.xml.stream.InputNode;
import simple.xml.stream.OutputNode;
import simple.xml.stream.Position;
import simple.xml.Root;

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
final class Traverser {

   /**
    * This is the root object used for the traversal performed.
    */
   private Source root;
        
   /**
    * Constructor for the <code>Traverser</code> object. This creates
    * a traverser that can be used to perform serialization or
    * or deserialization of an object. This requires a source object.
    * 
    * @param root the source object used for the traversal
    */
   public Traverser(Source root) {
      this.root = root;           
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

      return read(node, value);
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
   private Object read(InputNode node, Object value) throws Exception {
      Class type = value.getClass();           
      Root label = getRoot(type);

      if(label == null) {
         throw new RootException("No root annotation defined for %s", type);              
      }      
      Position line = node.getPosition();
      String name = node.getName();
      String root = label.name();
      
      if(!root.equalsIgnoreCase(name)) {
         throw new RootException("Annotation %s does not match XML element %s at %s", label, name, line);              
      } 
      return value;
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
      Root label = getRoot(type);

      if(label == null) {
         throw new RootException("No root annotation defined for %s", type);              
      }
      write(node, source, expect, label.name());
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
   private void write(OutputNode node, Object source, Class expect, String name) throws Exception {
      OutputNode child = node.getChild(name);
      Class type = source.getClass();
     
      if(node != null) {
         root.setOverride(expect, source, child);
      }                         
      Composite composite = getComposite(type);

      composite.write(child, source);
      child.commit();  
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
   private Composite getComposite(Class type) {
      return new Composite(root, type);
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
   private Root getRoot(Class type) throws Exception {
      while(type != null){
         if(type.isAnnotationPresent(Root.class)) {
            return (Root)type.getAnnotation(Root.class);
         }
         type = type.getSuperclass();
      }
      return null;
   }
}
