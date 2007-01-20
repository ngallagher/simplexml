/*
 * Persister.java July 2006
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import simple.xml.Serializer;
import simple.xml.filter.Filter;
import simple.xml.filter.PlatformFilter;
import simple.xml.stream.NodeBuilder;
import simple.xml.stream.InputNode;
import simple.xml.stream.OutputNode;
import simple.xml.stream.Format;

/**
 * The <code>Persister</code> object is used to provide an implementation
 * of a serializer. This implements the <code>Serializer</code> interface
 * and enables objects to be persisted and loaded from various sources. 
 * This implementation makes use of <code>Filter</code> objects to
 * replace template variables within the source XML document.
 * <p>
 * Deserialization is performed by passing an XML schema class into one
 * of the <code>read</code> methods along with the source of an XML stream.
 * The read method then reads the contents of the XML stream and builds
 * the object using annotations within the XML schema class.
 * <p>
 * Serialization is peformed by passing an object and an XML stream into
 * one of the <code>write</code> methods. The serialization process will
 * use the class of the provided object as the schema class. The object
 * is traversed and all fields are marshalled to the result stream.
 *
 * @author Niall Gallagher
 * 
 * @see simple.xml.Serializer
 */ 
public class Persister implements Serializer {

   /**
    * This is the strategy object used to load and resolve classes.
    */ 
   private Strategy strategy;
   
   /**
    * This filter is used to replace variables within templates.
    */
   private Filter filter;

   /**
    * Constructor for the <code>Persister</code> object. This is used
    * to create a serializer object that will use an empty filter.
    * This means that template variables will remain unchanged within
    * the XML document parsed when an object is deserialized.
    */
   public Persister() {
      this(new HashMap());           
   }

   /**
    * Constructor for the <code>Persister</code> object. This is used
    * to create a serializer object that will use a platform filter
    * object using the overrides within the provided map. This means
    * that template variables will be replaced firstly with mappings
    * from within the provided map, followed by system properties. 
    * 
    * @param filter this is the map that contains the overrides
    */
   public Persister(Map filter) {
      this(new PlatformFilter(filter));           
   }
        
   /**
    * Constructor for the <code>Persister</code> object. This is used
    * to create a serializer object that will use the provided filter.
    * This persister will replace all variables encountered when
    * deserializing an object with mappings found in the filter.
    * 
    * @param filter the filter used to replace template variables
    */
   public Persister(Filter filter) {
      this(new DefaultStrategy(), filter);
   }      

    /**
    * Constructor for the <code>Persister</code> object. This is used
    * to create a serializer object that will use a strategy object. 
    * This persister will use the provided <code>Strategy</code> to
    * intercept the XML elements in order to read and write persisent
    * data, such as the class name or version of the document.
    * 
    * @param strategy this is the strategy used to resolve classes
    */
   public Persister(Strategy strategy) {
      this(strategy, new HashMap());
   }     

   /**
    * Constructor for the <code>Persister</code> object. This is used
    * to create a serializer object that will use a platform filter
    * object using the overrides within the provided map. This means
    * that template variables will be replaced firstly with mappings
    * from within the provided map, followed by system properties. 
    * <p>
    * This persister will use the provided <code>Strategy</code> to
    * intercept the XML elements in order to read and write persisent
    * data, such as the class name or version of the document.
    * 
    * @param strategy this is the strategy used to resolve classes 
    * @param data this is the map that contains the overrides
    */
   public Persister(Strategy strategy, Map data) {
      this(strategy, new PlatformFilter(data));           
   }
        
   /**
    * Constructor for the <code>Persister</code> object. This is used
    * to create a serializer object that will use the provided filter.
    * This persister will replace all variables encountered when
    * deserializing an object with mappings found in the filter.
    * <p>
    * This persister will use the provided <code>Strategy</code> to
    * intercept the XML elements in order to read and write persisent
    * data, such as the class name or version of the document.
    * 
    * @param strategy this is the strategy used to resolve classes 
    * @param filter the filter used to replace template variables
    */
   public Persister(Strategy strategy, Filter filter) {
      this.strategy = strategy;           
      this.filter = filter;           
   }     
   
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
   public <T> T read(Class<? extends T> type, String source) throws Exception {
      return (T)read(type, new StringReader(source));            
   }
   
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
   public <T> T read(Class<? extends T> type, File source) throws Exception {
      return (T)read(type, new FileInputStream(source));           
   }
   
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
   public <T> T read(Class<? extends T> type, InputStream source) throws Exception {
      return (T)read(type, source, "utf-8");           
   }
   
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
   public <T> T read(Class<? extends T> type, InputStream source, String charset) throws Exception {
      return (T)read(type, new InputStreamReader(source, charset));           
   }
   
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
   public <T> T read(Class<? extends T> type, Reader source) throws Exception {
      return (T)read(type, NodeBuilder.read(source));
   }
   
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
   public <T> T read(Class<? extends T> type, InputNode source) throws Exception {
      return (T)read(type, source, filter);
   }

   /**
    * This <code>read</code> method will read the contents of the XML
    * document provided and convert it to an object of the specified
    * type. If the XML document cannot be deserialized or there is a
    * problem building the object graph an exception is thrown. The
    * object graph deserialized is returned.
    * 
    * @param type this is the XML schema class to be deserialized
    * @param source the document the object is deserialized from
    * @param filter this is the filter used by the templating engine
    * 
    * @return the object deserialized from the XML document given
    * 
    * @throws Exception if the object cannot be fully deserialized
    */
   private <T> T read(Class<? extends T> type, InputNode node, Filter filter) throws Exception {
      return (T)read(type, node, new Source(strategy, filter));
   }                      
           
   /**
    * This <code>read</code> method will read the contents of the XML
    * document provided and convert it to an object of the specified
    * type. If the XML document cannot be deserialized or there is a
    * problem building the object graph an exception is thrown. The
    * object graph deserialized is returned.
    * 
    * @param type this is the XML schema class to be deserialized
    * @param source the contextual object used for derserialization 
    * 
    * @return the object deserialized from the XML document given
    * 
    * @throws Exception if the object cannot be fully deserialized
    */
   private <T> T read(Class<? extends T> type, InputNode node, Source source) throws Exception {
      return (T)new Traverser(source).read(node, type);
   }
   
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
    * @param root this is where the serialized XML is written to
    * 
    * @throws Exception if the schema for the object is not valid
    */
   public void write(Object source, OutputNode root) throws Exception {
      write(source, root, filter);
   }

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
    * @param root this is where the serialized XML is written to
    * @param filter this is the filter object used for templating
    * 
    * @throws Exception if the schema for the object is not valid
    */   
   private void write(Object source, OutputNode root, Filter filter) throws Exception {   
      write(source, root, new Source(strategy, filter));
   }

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
    * @param root this is a contextual object used for serialization
    * 
    * @throws Exception if the schema for the object is not valid
    */     
   private void write(Object source, OutputNode node, Source root) throws Exception {
      new Traverser(root).write(node, source);
   }
   
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
   public void write(Object source, File out) throws Exception {
      write(source, new FileOutputStream(out));
   }
   
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
   public void write(Object source, OutputStream out) throws Exception {
	   write(source, out, "utf-8");
   }
   
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
   public void write(Object source, OutputStream out, String charset) throws Exception {
	   write(source, new OutputStreamWriter(out, charset));
   }
   
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
   public void write(Object source, Writer out) throws Exception {
	   write(source, NodeBuilder.write(out));
   }
}
