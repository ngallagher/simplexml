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

package simple.xml.load;

import simple.xml.stream.OutputNode;
import simple.xml.stream.InputNode;
import simple.xml.stream.NodeMap;
import simple.xml.filter.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * The <code>Source</code> object acts as a contextual object that is
 * used to store all information regarding an instance of serialization
 * or deserialization. This maintains the <code>Strategy</code> as
 * well as the <code>Filter</code> used to replace template variables.
 * When serialization and deserialization are performed the source is
 * required as tt acts as a factory for objects used in the process.
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
 * @see simple.xml.filter.Filter
 */ 
final class Source {

   /**
    * This is used to cache all schemas built to represent a class.
    * 
    * @see simple.xml.load.Scanner
    */
   private static ScannerCache cache;

   static {
      cache = new ScannerCache();           
   }
   
   /**
    * This is used to replace variables within the XML source.
    */
   private TemplateEngine engine;
  
   /**
    * This is the strategy used to resolve the element classes.
    */
   private Strategy strategy;
   
   /**
    * This is the filter used by this object for templating.
    */ 
   private Filter filter;

   /**
    * This is used to store the source context attribute values.
    */ 
   private Map table;

   /**
    * This counts the number of elements that have been read.
    */ 
   private int read;

   /**
    * This counts the number of elements that have been written.
    */
   private int write;
   
   /**
    * Constructor for the <code>Source</code> object. This is used to
    * maintain a context during the serialization process. It holds 
    * the <code>Strategy</code> and <code>Filter</code> used in the
    * serialization process. The same source instance is used for 
    * each XML element evaluated in a the serialization process. 
    * 
    * @param strategy this is used to resolve the classes used   
    * @param data this is used for replacing the template variables
    */       
   public Source(Strategy strategy, Filter data) {
      this.filter = new TemplateFilter(this, data);           
      this.engine = new TemplateEngine(filter);           
      this.table = new HashMap();
      this.strategy = strategy;
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
      return table.get(key);
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
    * @return returns the class that should be used for the object
    * 
    * @throws Exception thrown if the class cannot be resolved  
    */
   public Class getOverride(Class type, InputNode node) throws Exception {
      NodeMap map = node.getAttributes();
      
      if(read++ == 0) {
         return strategy.readRoot(type, map, table);
      }           
      return strategy.readElement(type, map, table);
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
    * @throws Exception thrown if the details cannot be set
    */
   public void setOverride(Class type, Object value, OutputNode node) throws Exception {
      NodeMap map = node.getAttributes();
      
      if(write++ == 0) {
         strategy.writeRoot(type, value, map, table);              
      }           
      strategy.writeElement(type, value, map, table);
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
    */
   public Schema getSchema(Object source) {
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
    */   
   public Schema getSchema(Class type) {
      Scanner schema = cache.get(type);
      
      if(schema == null) {
         schema = new Scanner(type);             
         cache.cache(type, schema);
      }
      return new Schema(schema, table);
   }

   /**
    * Replaces any template variables within the provided string. 
    * This is used in the deserialization process to replace 
    * variables with system properties, environment variables, or
    * used specified mappings. If a template variable does not have
    * a mapping from the <code>Filter</code> is is left unchanged.  
    * 
    * @param text this is text that will have template variables
    *        replaced with filter mappings 
    * 
    * @return this returns the text will all variables replaced
    */
   public String getProperty(String text) {
      return engine.process(text);
   }
}
