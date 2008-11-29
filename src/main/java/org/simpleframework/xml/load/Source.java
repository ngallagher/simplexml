/*
 * Source.java July 2006
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

import org.simpleframework.xml.filter.Filter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Style;

/**
 * The <code>Source</code> object acts as a contextual object that is
 * used to store all information regarding an instance of serialization
 * or deserialization. This maintains the <code>Strategy</code> as
 * well as the <code>Filter</code> used to replace template variables.
 * When serialization and deserialization are performed the source is
 * required as it acts as a factory for objects used in the process.
 * <p>
 * For serialization the source object is required as a factory for
 * <code>Schema</code> objects, which are used to visit each field 
 * in the class that can be serialized. Also this can be used to get
 * any data entered into the session <code>Map</code> object.
 * <p>
 * When deserializing the source object provides the contextual data
 * used to replace template variables extracted from the XML source.
 * This is performed using the <code>Filter</code> object. Also, as 
 * in serialization it acts as a factory for the <code>Schema</code> 
 * objects used to examine the serializable fields of an object.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.filter.Filter
 */ 
class Source {

   /**
    * This is used to replace variables within the XML source.
    */
   private TemplateEngine engine;
   
   /**
    * This is the strategy used to resolve the element classes.
    */
   private Strategy strategy;

   /**
    * This is used to store the source context attribute values.
    */ 
   private Session session;
   
   /**
    * This is the filter used by this object for templating.
    */ 
   private Filter filter;
   
   /**
    * This is the style that is used by this source instance.
    */
   private Style style;
   
   /**
    * Constructor for the <code>Source</code> object. This is used to
    * maintain a context during the serialization process. It holds 
    * the <code>Strategy</code> and <code>Filter</code> used in the
    * serialization process. The same source instance is used for 
    * each XML element evaluated in a the serialization process. 
    * 
    * @param strategy this is used to resolve the classes used   
    * @param data this is used for replacing the template variables
    * @param style this is the style used for the serialization
    */       
   public Source(Strategy strategy, Filter data, Style style) {
      this.filter = new TemplateFilter(this, data);           
      this.engine = new TemplateEngine(filter);           
      this.session = new Session();
      this.strategy = strategy;
      this.style = style;
   } 
   
   /**
    * This is used to acquire the <code>Session</code> object that 
    * is used to store the values used within the serialization
    * process. This provides the internal map that is passed to all
    * of the call back methods so that is can be populated.
    * 
    * @return this returns the session that is used by this source
    */
   public Session getSession() {
      return session;
   }
     
   /**
    * This is used to acquire the <code>Style</code> for the format.
    * If no style has been set a default style is used, which does 
    * not modify the attributes and elements that are used to build
    * the resulting XML document.
    * 
    * @return this returns the style used for this format object
    */
   public Style getStyle() {
      if(style == null) {
         style = new DefaultStyle();
      }
      return style;
   }
   
   /**
    * This creates a <code>Scanner</code> object that can be used to
    * examine the fields within the XML class schema. The scanner
    * maintains information when a field from within the scanner is
    * visited, this allows the serialization and deserialization
    * process to determine if all required XML annotations are used.
    * 
    * @param type the schema class the scanner is created for
    * 
    * @return a scanner that can maintains information on the type
    * 
    * @throws Exception if the class contains an illegal schema 
    */ 
   private Scanner getScanner(Class type) throws Exception {
      return ScannerFactory.getInstance(type);
   }
   
   /**
    * This creates a <code>Schema</code> object that can be used to
    * examine the fields within the XML class schema. The schema
    * maintains information when a field from within the schema is
    * visited, this allows the serialization and deserialization
    * process to determine if all required XML annotations are used.
    * 
    * @param source the source object the schema is created for
    * 
    * @return a new schema that can track visits within the schema
    * 
    * @throws Exception if the class contains an illegal schema  
    */
   public Schema getSchema(Object source) throws Exception {
      return getSchema(source.getClass());           
   }

   /**
    * This creates a <code>Schema</code> object that can be used to
    * examine the fields within the XML class schema. The schema
    * maintains information when a field from within the schema is
    * visited, this allows the serialization and deserialization
    * process to determine if all required XML annotations are used.
    * 
    * @param type the schema class the schema is created for
    * 
    * @return a new schema that can track visits within the schema
    * 
    * @throws Exception if the class contains an illegal schema 
    */   
   public Schema getSchema(Class type) throws Exception {
      Scanner schema = getScanner(type);
      
      if(schema == null) {
         throw new PersistenceException("Invalid schema class %s", type);
      }
      return new Schema(schema, this);
   }
   
   /**
    * This is used to acquire the attribute mapped to the specified
    * key. In order for this to return a value it must have been
    * previously placed into the context as it is empty by default.
    * 
    * @param key this is the name of the attribute to retrieve
    *
    * @return this returns the value mapped to the specified key
    */     
   public Object getAttribute(Object key) {
      return session.get(key);
   }
   
   /**
    * This is used to resolve and load a class for the given element.
    * The class should be of the same type or a subclass of the class
    * specified. It can be resolved using the details within the
    * provided XML element, if the details used do not represent any
    * serializable values they should be removed so as not to disrupt
    * the deserialization process. For example the default strategy
    * removes all "class" attributes from the given elements.
    * 
    * @param type this is the type of the root element expected
    * @param node this is the element used to resolve an override
    * 
    * @return returns the type that should be used for the object
    * 
    * @throws Exception thrown if the class cannot be resolved  
    */
   public Type getOverride(Class type, InputNode node) throws Exception {
      NodeMap map = node.getAttributes();
      
      if(node.isRoot()) {
         return strategy.getRoot(type, map, session);
      }           
      return strategy.getElement(type, map, session);
   } 

   /**    
    * This is used to attach elements or attributes to the given 
    * element during the serialization process. This method allows
    * the strategy to augment the XML document so that it can be
    * deserialized using a similar strategy. For example the 
    * default strategy adds a "class" attribute to the element.
    * 
    * @param type this is the field type for the associated value 
    * @param value this is the instance variable being serialized
    * @param node this is the element used to represent the value
    * 
    * @return this returns true if serialization has complete
    * 
    * @throws Exception thrown if the details cannot be set
    */
   public boolean setOverride(Class type, Object value, OutputNode node) throws Exception {
      NodeMap map = node.getAttributes();
      
      if(node.isRoot()) {
         return strategy.setRoot(type, value, map, session);              
      }           
      return strategy.setElement(type, value, map, session);
   }

   /**
    * Replaces any template variables within the provided string. 
    * This is used in the deserialization process to replace 
    * variables with system properties, environment variables, or
    * used specified mappings. If a template variable does not have
    * a mapping from the <code>Filter</code> is is left unchanged.  
    * 
    * @param text this is processed by the template engine object
    * 
    * @return this returns the text will all variables replaced
    */
   public String getProperty(String text) {
      return engine.process(text);
   }
}
