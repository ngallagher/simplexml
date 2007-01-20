/*
 * DefaultStrategy.java July 2006
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

import simple.xml.stream.NodeMap;
import simple.xml.stream.Node;
import java.util.Map;

/**
 * The <code>DefaultStrategy</code> object is used by the persister if
 * no strategy is specified. This will make use of a "class" attribute
 * by default to resolve the class to use for a given element during
 * the deserialization process. For the serialization process the 
 * "class" attribute will be added to the element specified. If there
 * is a need to use an attribute name other than "class" then the
 * name of the attribute to use can be specified.
 * 
 * @author Niall Gallagher
 */
final class DefaultStrategy implements Strategy {

   /**   
    * This is the attribute that is used to determine the real type.
    */
   private static final String LABEL = "class";
   
   /**   
    * This is the attribute that is used to determine the real type.
    */   
   private String label;
   
   /**
    * Constructor for the <code>DefaultStrategy</code> object. This 
    * is used to create a strategy that can resolve and load class
    * objects for deserialization using a "class" attribute. Also
    * for serialization this will add the appropriate "class" value.
    */
   public DefaultStrategy() {
      this(LABEL);           
   }        
   
   /**
    * Constructor for the <code>DefaultStrategy</code> object. This 
    * is used to create a strategy that can resolve and load class
    * objects for deserialization using the specified attribute. 
    * The attribute value can be any legal XML attribute name.
    * 
    * @param label this is the name of the attribute to use
    */
   public DefaultStrategy(String label) {
      this.label = label;           
   }
   
   /**
    * This is used to resolve and load a class for the given element.
    * Resolution of the class to used is done by inspecting the
    * XML element provided. If there is a "class" attribute on the
    * element then its value is used to resolve the class to use.
    * If no such attribute exists on the element this returns null.
    * 
    * @param field this is the type of the XML element expected
    * @param root this is the element used to resolve an override
    * @param map this is used to maintain contextual information
    * 
    * @return returns the class that should be used for the object
    * 
    * @throws Exception thrown if the class cannot be resolved
    */
   public Class readRoot(Class field, NodeMap node, Map map) throws Exception {
      return readElement(field, node, map);
   }  
   
   /**
    * This is used to resolve and load a class for the given element.
    * Resolution of the class to used is done by inspecting the
    * XML element provided. If there is a "class" attribute on the
    * element then its value is used to resolve the class to use.
    * If no such attribute exists on the element this returns null.
    * 
    * @param field this is the type of the XML element expected
    * @param node this is the element used to resolve an override
    * @param map this is used to maintain contextual information
    * 
    * @return returns the class that should be used for the object
    * 
    * @throws Exception thrown if the class cannot be resolved
    */
   public Class readElement(Class field, NodeMap node, Map map) throws Exception {
      Node entry = node.remove(label);
      
      if(entry != null) {
         return Class.forName(entry.getValue());              
      }      
      return null;             
   }
   
   /**
    * This is used to attach a attribute to the provided element
    * that is used to identify the class. The attribute name is
    * "class" and has the value of the fully qualified class 
    * name for the object provided. This will only be invoked
    * if the object class is different from the field class.
    *  
    * @param field this is the declared class for the field used
    * @param value this is the instance variable being serialized
    * @param root this is the element used to represent the value
    * @param map this is used to maintain contextual information
    * 
    * @throws Exception thrown if the details cannot be set
    */
   public void writeRoot(Class field, Object value, NodeMap node, Map map) throws Exception {
      writeElement(field, value, node, map);
   }   
   
   /**
    * This is used to attach a attribute to the provided element
    * that is used to identify the class. The attribute name is
    * "class" and has the value of the fully qualified class 
    * name for the object provided. This will only be invoked
    * if the object class is different from the field class.
    *
    * @param field this is the declared class for the field used
    * @param value this is the instance variable being serialized
    * @param node this is the element used to represent the value
    * @param map this is used to maintain contextual information
    * 
    * @throws Exception thrown if the details cannot be set
    */   
   public void writeElement(Class field, Object value, NodeMap node, Map map) throws Exception {
      Class type = value.getClass();
      
      if(type != field) {
         node.put(label, type.getName());
      }             
   }
}
