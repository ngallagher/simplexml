/*
 * CycleStrategy.java April 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
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

package simple.xml.graph;

import simple.xml.stream.NodeMap;
import simple.xml.load.Strategy;
import simple.xml.load.Type;
import java.util.Map;

/**
 * The <code>CycleStrategy</code> represents a strategy that is used
 * to augment the deserialization and serialization process such that
 * cycles in an object graph can be supported. This adds additional 
 * attributes to the serializaed XML elements so that during the 
 * deserialization process an objects cycles can be created. Without
 * the use of a strategy such as this, cycles could cause an infinite
 * loop during the serialization process while traversing the graph.
 * <pre>
 * 
 *    &lt;root id="1"&gt;
 *       &lt;object id="2"&gt;
 *          &lt;object id="3" name="name"&gt;Example&lt;/item&gt;
 *          &lt;object reference="2"/&gt;
 *       &lt;/object&gt;
 *    &lt;/root&gt;
 * 
 * </pre>
 * In the above serialized XML there is a circular reference, where
 * the XML element with id "2" contains a reference to itself. In
 * most data binding frameworks this will cause an infinite loop, 
 * or in some cases will just fail to represent the references well.
 * With this strategy you can ensure that cycles in complex object
 * graphs will be maintained and can be serialized safely.
 * 
 * @author Niall Gallagher
 * 
 * @see simple.xml.load.Persister
 * @see simple.xml.load.Strategy
 */
public class CycleStrategy implements Strategy {
   
   /**
    * The default name of the attribute used for circular references.
    */
   private static final String REFER = "reference";
   
   /**
    * The default name of the attribute used to identify an object.
    */
   private static final String MARK = "id";
   
   /**
    * The default name of the attribute used to specify the length.
    */
   private static final String LENGTH = "length";
   
   /**
    * The default name of the attribute used to specify the class.
    */
   private static final String LABEL = "class";  
   
   /**
    * This is used to maintain session state for writing the graph.
    */
   private WriteState write;
   
   /**
    * This is used to maintain session state for reading the graph.
    */
   private ReadState read;
   
   /**
    * This is used to specify the length of array instances.
    */
   private String length;
   
   /**
    * This is the label used to mark the type of an object.
    */
   private String label;
   
   /**
    * This is the attribute used to mark the identity of an object.
    */
   private String mark;
   
   /**
    * Thsi is the attribute used to refer to an existing instance.
    */
   private String refer;
   
   /**
    * Constructor for the <code>CycleStrategy</code> object. This is
    * used to create a strategy with default values. By default the
    * values used are "id" and "reference". These values will be
    * added to XML elements during the serialization process. And 
    * will be used to deserialize the object cycles fully.
    */
   public CycleStrategy() {
      this(MARK, REFER);
   }
   
   /**
    * Constructor for the <code>CycleStrategy</code> object. This is
    * used to create a strategy with the sepcified attributes, which
    * will be added to serialized XML elements. These attributes 
    * are used to serialize the objects in such a way the cycles in
    * the object graph can be deserialized and used fully. 
    * 
    * @param mark this is used to mark the identity of an object
    * @param refer this is used to refer to an existing object
    */
   public CycleStrategy(String mark, String refer) {
      this(mark, refer, LABEL);      
   }
   
   /**
    * Constructor for the <code>CycleStrategy</code> object. This is
    * used to create a strategy with the sepcified attributes, which
    * will be added to serialized XML elements. These attributes 
    * are used to serialize the objects in such a way the cycles in
    * the object graph can be deserialized and used fully. 
    * 
    * @param mark this is used to mark the identity of an object
    * @param refer this is used to refer to an existing object
    * @param label this is used to specify the class for the field
    */   
   public CycleStrategy(String mark, String refer, String label){
      this(mark, refer, label, LENGTH);
   }
   
   /**
    * Constructor for the <code>CycleStrategy</code> object. This is
    * used to create a strategy with the sepcified attributes, which
    * will be added to serialized XML elements. These attributes 
    * are used to serialize the objects in such a way the cycles in
    * the object graph can be deserialized and used fully. 
    * 
    * @param mark this is used to mark the identity of an object
    * @param refer this is used to refer to an existing object
    * @param label this is used to specify the class for the field
    * @param length this is the length attribute used for arrays
    */   
   public CycleStrategy(String mark, String refer, String label, String length){  
      this.write = new WriteState(this);
      this.read = new ReadState(this);
      this.length = length;
      this.label = label;
      this.refer = refer;
      this.mark = mark;
   }
   
   /**
    * This is returns the label used by this strategy instance. This
    * attribute name is used to add data to XML elements to enable
    * the deserialization process to know the exact instance to use
    * when creating a <code>Type</code> for a specific field.
    * 
    * @return the name of the attribute used to store the type
    */
   public String getLabel() {
      return label;
   }
   
   /**
    * This returns the attribute used to store references within the
    * serialized XML document. The reference attribute is added to
    * the serialized XML element so that cycles in the object graph 
    * can be recreated. This is an optional attribute.
    * 
    * @return this returns the name of the reference attribute
    */
   public String getReference() {
      return refer;
   }
   
   /**
    * This returns the attribute used to store the identities of all
    * objects serialized to the XML document. The identity attribute
    * stores a unique identifiers, which enables this strategy to
    * determine an objects identity within the serialized XML.
    * 
    * @return this returns the name of the identity attribute used
    */
   public String getIdentity() {
      return mark;
   }
   
   /**
    * This returns the attribute used to store the array length in
    * the serialized XML document. The array length is required so
    * that the deserialization process knows how to construct the
    * array before any of the array elements are deserialized.
    * 
    * @return this returns the name of the array length attribute
    */
   public String getLength() {
      return length;
   }  

   /**
    * This method is used to read an object from the specified node.
    * In order to get the root type the field and node map are 
    * specified. The field represents the annotated method or field
    * within the deserialized object. The node map is used to get
    * the attributes used to describe the objects identity, or in
    * the case of an existing object it contains an object reference.
    * 
    * @param field the method or field in the deserialized object
    * @param node this is the XML element attributes to read
    * @param map this is the session map used for deserialization
    * 
    * @return this returns an instance to insert into the object 
    */
   public Type getRoot(Class field, NodeMap node, Map map) throws Exception {
      return getElement(field, node, map);
   }  
   
   /**
    * This method is used to read an object from the specified node.
    * In order to get the root type the field and node map are 
    * specified. The field represents the annotated method or field
    * within the deserialized object. The node map is used to get
    * the attributes used to describe the objects identity, or in
    * the case of an existing object it contains an object reference.
    * 
    * @param field the method or field in the deserialized object
    * @param node this is the XML element attributes to read
    * @param map this is the session map used for deserialization
    * 
    * @return this returns an instance to insert into the object 
    */
   public Type getElement(Class field, NodeMap node, Map map) throws Exception {
      ReadGraph graph = read.find(map);
      
      if(graph != null) {
         return graph.getElement(field, node);
      }
      return null;
   }
   
   /**
    * This is used to set the reference in to the XML element that 
    * is to be written. This will either insert an object identity if
    * the object has not previously been written, or, if the object
    * has already been written in a previous element, this will write
    * the reference to that object. This allows all cycles within the
    * graph to be serialized so that they can be fully deserialized. 
    * 
    * @param field the type of the field or method in the object
    * @param value this is the actual object that is to be written
    * @param node tihs is the XML element attribute map to use
    * @param map this is the session map used for the serialization
    * 
    * @return returns true if the object has been fully serialized
    */
   public boolean setRoot(Class field, Object value, NodeMap node, Map map){
      return setElement(field, value, node, map);
   }  
   
   /**
    * This is used to set the reference in to the XML element that 
    * is to be written. This will either insert an object identity if
    * the object has not previously been written, or, if the object
    * has already been written in a previous element, this will write
    * the reference to that object. This allows all cycles within the
    * graph to be serialized so that they can be fully deserialized. 
    * 
    * @param field the type of the field or method in the object
    * @param value this is the actual object that is to be written
    * @param node tihs is the XML element attribute map to use
    * @param map this is the session map used for the serialization
    * 
    * @return returns true if the object has been fully serialized
    */
   public boolean setElement(Class field, Object value, NodeMap node, Map map){
      WriteGraph graph = write.find(map);
      
      if(graph != null) {
         return graph.setElement(field, value, node);
      }
      return false;
   }
}
