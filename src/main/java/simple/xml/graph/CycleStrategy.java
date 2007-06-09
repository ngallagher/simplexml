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
    * This is used to provide the names of the attributes to use.
    */
   private Contract contract;
   
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
      this.contract = new Contract(mark, refer, label, length);
      this.write = new WriteState(contract);
      this.read = new ReadState(contract);
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
