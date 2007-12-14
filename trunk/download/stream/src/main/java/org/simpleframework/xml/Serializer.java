/*
 * Serializer.java July 2006
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

package org.simpleframework.xml;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.io.File;

/**
 * The <code>Serializer</code> interface is used to represent objects
 * that can serialize and deserialize objects to an from XML. This 
 * exposes several <code>read</code> and <code>write</code> methods
 * that can read from and write to various sources. Typically an
 * object will be read from an XML file and written to some other 
 * file or stream. 
 * <p>
 * An implementation of the <code>Serializer</code> interface is free
 * to use any desired XML parsing framework. If a framework other 
 * than the Java streaming API for XML is required then it should be
 * wrapped within the <code>org.simpleframework.xml.stream</code> API,
 * which offers a framework neutral facade.
 * 
 * @author Niall Gallagher
 */
public interface Serializer {
   
   /**
    * This <code>read</code> method will read the contents of the XML
    * document from the provided source and convert it into an object
    * of the specified type. If the XML source cannot be deserialized
    * or there is a problem building the object graph an exception
    * is thrown. The instance deserialized is returned.
    * 
    * @param type this is the class type to be deserialized from XML
    * @param source this provides the source of the XML document
    * 
    * @return the object deserialized from the XML document 
    * 
    * @throws Exception if the object cannot be fully deserialized
    */
   public <T> T read(Class<? extends T> type, String source) throws Exception;
        
   /**
    * This <code>read</code> method will read the contents of the XML
    * document from the provided source and convert it into an object
    * of the specified type. If the XML source cannot be deserialized
    * or there is a problem building the object graph an exception
    * is thrown. The instance deserialized is returned.
    * 
    * @param type this is the class type to be deserialized from XML
    * @param source this provides the source of the XML document
    * 
    * @return the object deserialized from the XML document 
    * 
    * @throws Exception if the object cannot be fully deserialized
    */
   public <T> T read(Class<? extends T> type, File source) throws Exception;

   /**
    * This <code>read</code> method will read the contents of the XML
    * document from the provided source and convert it into an object
    * of the specified type. If the XML source cannot be deserialized
    * or there is a problem building the object graph an exception
    * is thrown. The instance deserialized is returned.
    * 
    * @param type this is the class type to be deserialized from XML
    * @param source this provides the source of the XML document
    * 
    * @return the object deserialized from the XML document 
    * 
    * @throws Exception if the object cannot be fully deserialized
    */
   public <T> T read(Class<? extends T> type, InputStream source) throws Exception;
   
   /**
    * This <code>read</code> method will read the contents of the XML
    * document from the provided source and convert it into an object
    * of the specified type. If the XML source cannot be deserialized
    * or there is a problem building the object graph an exception
    * is thrown. The instance deserialized is returned.
    * 
    * @param type this is the class type to be deserialized from XML
    * @param source this provides the source of the XML document
    * @param charset this is the character set to read the XML with
    * 
    * @return the object deserialized from the XML document 
    * 
    * @throws Exception if the object cannot be fully deserialized
    */   
   public <T> T read(Class<? extends T> type, InputStream source, String charset) throws Exception;

   /**
    * This <code>read</code> method will read the contents of the XML
    * document from the provided source and convert it into an object
    * of the specified type. If the XML source cannot be deserialized
    * or there is a problem building the object graph an exception
    * is thrown. The instance deserialized is returned.
    * 
    * @param type this is the class type to be deserialized from XML
    * @param source this provides the source of the XML document
    * 
    * @return the object deserialized from the XML document 
    * 
    * @throws Exception if the object cannot be fully deserialized
    */   
   public <T> T read(Class<? extends T> type, Reader source) throws Exception;
     
   /**
    * This <code>read</code> method will read the contents of the XML
    * document from the provided source and populate the object with
    * the values deserialized. This is used as a means of injecting an
    * object with values deserialized from an XML document. If the
    * XML source cannot be deserialized or there is a problem building
    * the object graph an exception is thrown.
    * 
    * @param value this is the object to deserialize the XML in to
    * @param source this provides the source of the XML document
    * 
    * @return the same instance provided is returned when finished  
    * 
    * @throws Exception if the object cannot be fully deserialized
    */
   public <T> T read(T value, String source) throws Exception;
        
   /**
    * This <code>read</code> method will read the contents of the XML
    * document from the provided source and populate the object with
    * the values deserialized. This is used as a means of injecting an
    * object with values deserialized from an XML document. If the
    * XML source cannot be deserialized or there is a problem building
    * the object graph an exception is thrown.
    * 
    * @param value this is the object to deserialize the XML in to
    * @param source this provides the source of the XML document
    * 
    * @return the same instance provided is returned when finished 
    * 
    * @throws Exception if the object cannot be fully deserialized
    */
   public <T> T read(T value, File source) throws Exception;

   /**
    * This <code>read</code> method will read the contents of the XML
    * document from the provided source and populate the object with
    * the values deserialized. This is used as a means of injecting an
    * object with values deserialized from an XML document. If the
    * XML source cannot be deserialized or there is a problem building
    * the object graph an exception is thrown.
    * 
    * @param value this is the object to deserialize the XML in to
    * @param source this provides the source of the XML document
    * 
    * @return the same instance provided is returned when finished 
    * 
    * @throws Exception if the object cannot be fully deserialized
    */
   public <T> T read(T value, InputStream source) throws Exception;
   
   /**
    * This <code>read</code> method will read the contents of the XML
    * document from the provided source and populate the object with
    * the values deserialized. This is used as a means of injecting an
    * object with values deserialized from an XML document. If the
    * XML source cannot be deserialized or there is a problem building
    * the object graph an exception is thrown.
    * 
    * @param value this is the object to deserialize the XML in to
    * @param source this provides the source of the XML document
    * @param charset this is the character set to read the XML with
    * 
    * @return the same instance provided is returned when finished 
    * 
    * @throws Exception if the object cannot be fully deserialized
    */   
   public <T> T read(T value, InputStream source, String charset) throws Exception;

   /**
    * This <code>read</code> method will read the contents of the XML
    * document from the provided source and populate the object with
    * the values deserialized. This is used as a means of injecting an
    * object with values deserialized from an XML document. If the
    * XML source cannot be deserialized or there is a problem building
    * the object graph an exception is thrown.
    * 
    * @param value this is the object to deserialize the XML in to
    * @param source this provides the source of the XML document
    * 
    * @return the same instance provided is returned when finished 
    * 
    * @throws Exception if the object cannot be fully deserialized
    */   
   public <T> T read(T value, Reader source) throws Exception;
   
   /**
    * This <code>validate</code> method will validate the contents of
    * the XML document against the specified XML class schema. This is
    * used to perform a read traversal of the class schema such that 
    * the document can be tested against it. This is preferred to
    * reading the document as it does not instantiate the objects or
    * invoke any callback methods, thus making it a safe validation.
    * 
    * @param type this is the class type to be validated against XML
    * @param source this provides the source of the XML document
    * 
    * @return true if the document matches the class XML schema 
    * 
    * @throws Exception if the class XML schema does not fully match
    */
   public boolean validate(Class type, String source) throws Exception;
        
   /**
    * This <code>validate</code> method will validate the contents of
    * the XML document against the specified XML class schema. This is
    * used to perform a read traversal of the class schema such that 
    * the document can be tested against it. This is preferred to
    * reading the document as it does not instantiate the objects or
    * invoke any callback methods, thus making it a safe validation.
    * 
    * @param type this is the class type to be validated against XML
    * @param source this provides the source of the XML document
    * 
    * @return true if the document matches the class XML schema 
    * 
    * @throws Exception if the class XML schema does not fully match
    */
   public boolean validate(Class type, File source) throws Exception;

   /**
    * This <code>validate</code> method will validate the contents of
    * the XML document against the specified XML class schema. This is
    * used to perform a read traversal of the class schema such that 
    * the document can be tested against it. This is preferred to
    * reading the document as it does not instantiate the objects or
    * invoke any callback methods, thus making it a safe validation.
    * 
    * @param type this is the class type to be validated against XML
    * @param source this provides the source of the XML document
    * 
    * @return true if the document matches the class XML schema 
    * 
    * @throws Exception if the class XML schema does not fully match
    */
   public boolean validate(Class type, InputStream source) throws Exception;
   
   /**
    * This <code>validate</code> method will validate the contents of
    * the XML document against the specified XML class schema. This is
    * used to perform a read traversal of the class schema such that 
    * the document can be tested against it. This is preferred to
    * reading the document as it does not instantiate the objects or
    * invoke any callback methods, thus making it a safe validation.
    * 
    * @param type this is the class type to be validated against XML
    * @param source this provides the source of the XML document
    * @param charset this is the charset to read the XML document as
    * 
    * @return true if the document matches the class XML schema 
    * 
    * @throws Exception if the class XML schema does not fully match
    */  
   public boolean validate(Class type, InputStream source, String charset) throws Exception;

   /**
    * This <code>validate</code> method will validate the contents of
    * the XML document against the specified XML class schema. This is
    * used to perform a read traversal of the class schema such that 
    * the document can be tested against it. This is preferred to
    * reading the document as it does not instantiate the objects or
    * invoke any callback methods, thus making it a safe validation.
    * 
    * @param type this is the class type to be validated against XML
    * @param source this provides the source of the XML document
    * 
    * @return true if the document matches the class XML schema 
    * 
    * @throws Exception if the class XML schema does not fully match
    */  
   public boolean validate(Class type, Reader source) throws Exception;
     
   /**
    * This <code>write</code> method will traverse the provided object
    * checking for field annotations in order to compose the XML data.
    * This uses the <code>getClass</code> method on the object to
    * determine the class file that will be used to compose the schema.
    * If there is no <code>Root</code> annotation for the class then
    * this will throw an exception. The root annotation is the only
    * annotation required for an object to be serialized.  
    * 
    * @param source this is the object that is to be serialized
    * @param out this is where the serialized XML is written to
    * 
    * @throws Exception if the schema for the object is not valid
    */
   public void write(Object source, File out) throws Exception;

   /**
    * This <code>write</code> method will traverse the provided object
    * checking for field annotations in order to compose the XML data.
    * This uses the <code>getClass</code> method on the object to
    * determine the class file that will be used to compose the schema.
    * If there is no <code>Root</code> annotation for the class then
    * this will throw an exception. The root annotation is the only
    * annotation required for an object to be serialized.  
    * 
    * @param source this is the object that is to be serialized
    * @param out this is where the serialized XML is written to
    * 
    * @throws Exception if the schema for the object is not valid
    */   
   public void write(Object source, OutputStream out) throws Exception;
   
   /**
    * This <code>write</code> method will traverse the provided object
    * checking for field annotations in order to compose the XML data.
    * This uses the <code>getClass</code> method on the object to
    * determine the class file that will be used to compose the schema.
    * If there is no <code>Root</code> annotation for the class then
    * this will throw an exception. The root annotation is the only
    * annotation required for an object to be serialized.  
    * 
    * @param source this is the object that is to be serialized
    * @param out this is where the serialized XML is written to
    * @param charset this is the character encoding to be used
    * 
    * @throws Exception if the schema for the object is not valid
    */   
   public void write(Object source, OutputStream out, String charset) throws Exception;
   
   /**
    * This <code>write</code> method will traverse the provided object
    * checking for field annotations in order to compose the XML data.
    * This uses the <code>getClass</code> method on the object to
    * determine the class file that will be used to compose the schema.
    * If there is no <code>Root</code> annotation for the class then
    * this will throw an exception. The root annotation is the only
    * annotation required for an object to be serialized.  
    * 
    * @param source this is the object that is to be serialized
    * @param out this is where the serialized XML is written to
    * 
    * @throws Exception if the schema for the object is not valid
    */   
   public void write(Object source, Writer out) throws Exception;
}
