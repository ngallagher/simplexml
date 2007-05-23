/*
 * Composite.java July 2006
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
import simple.xml.stream.NodeMap;
import simple.xml.stream.OutputNode;
import simple.xml.stream.Position;

/**
 * The <code>Composite</code> object is used to perform serialization
 * of objects that contain XML annotation. Composite objects are objects
 * that are not primitive and contain references to serializable fields.
 * This <code>Converter</code> will visit each field within the object
 * and deserialize or serialize that field depending on the requested
 * action. If a required field is not present when deserializing from
 * an XML element this terminates the deserialization reports the error.
 * <pre>
 * 
 *    &lt;element name="test" class="some.package.Type"&gt;
 *       &lt;text&gt;string value&lt;/text&gt;
 *       &lt;integer&gt;1234&lt;/integer&gt;
 *    &lt;/element&gt;
 * 
 * </pre>
 * To deserialize the above XML source this will attempt to match the
 * attribute name with an <code>Attribute</code> annotation from the
 * XML schema class, which is specified as "some.package.Type". This
 * type must also contain <code>Element</code> annotations for the
 * "text" and "integer" elements.
 * <p>
 * Serialization requires that contacts marked as required must have
 * values that are not null. This ensures that the serialized object
 * can be deserialized at a later stage using the same class schema.
 * If a required value is null the serialization terminates an an
 * exception is thrown.
 * 
 * @author Niall Gallagher
 */
final class Composite implements Converter {
  
   /**
    * This factory creates instances of the deserialized object.
    */
   private ObjectFactory factory;

   /**
    * This is the source object for the instance of serialization.
    */
   private Source root;
        
   /**
    * Constructor for the <code>Composite</code> object. This creates 
    * a converter object capable of serializing and deserializing root
    * objects labeled with XML annotations. The XML schema class must 
    * be given to the instance in order to perform deserialization.
    *  
    * @param root the source object used to perform serialization
    * @param type this is the XML schema class to use
    */
   public Composite(Source root, Class type) {
      this.factory = new ObjectFactory(root, type);           
      this.root = root;
   }

   /**
    * This <code>read</code> method performs deserialization of the XML
    * schema class type by traversing the contacts and instantiating them
    * using details from the provided XML element. Because this will
    * convert a non-primitive value it delegates to other converters to
    * perform deserialization of lists and primitives.
    * <p>
    * If any of the required contacts are not present within the provided
    * XML element this will terminate deserialization and throw an
    * exception. The annotation missing is reported in the exception.
    * 
    * @param node the XML element contact values are deserialized from
    * 
    * @return this returns the fully deserialized object graph
    */
   public Object read(InputNode node) throws Exception {
      Type type = factory.getInstance(node);
      Object source = type.getInstance();
      
      if(!type.isReference()) {         
         read(node, source);
      }
      return source;
   }
   
   /**
    * This <code>read</code> method performs deserialization of the XML
    * schema class type by traversing the contacts and instantiating them
    * using details from the provided XML element. Because this will
    * convert a non-primitive value it delegates to other converters to
    * perform deserialization of lists and primitives.
    * <p>
    * If any of the required contacts are not present within the provided
    * XML element this will terminate deserialization and throw an
    * exception. The annotation missing is reported in the exception.
    * 
    * @param node the XML element contact values are deserialized from
    * @param source the object whose contacts are to be deserialized
    */
   private void read(InputNode node, Object source) throws Exception {
      Schema schema = root.getSchema(source);
      
      read(node, source, schema);
      schema.validate(source);
      schema.commit(source);
   }
   
   /**
    * This <code>read</code> method performs deserialization of the XML
    * schema class type by traversing the contacts and instantiating them
    * using details from the provided XML element. Because this will
    * convert a non-primitive value it delegates to other converters to
    * perform deserialization of lists and primitives.
    * <p>
    * If any of the required contacts are not present within the provided
    * XML element this will terminate deserialization and throw an
    * exception. The annotation missing is reported in the exception.
    * 
    * @param node the XML element contact values are deserialized from
    * @param source ths object whose contacts are to be deserialized
    * @param schema this object visits the objects contacts
    */
   private void read(InputNode node, Object source, Schema schema) throws Exception {
      readText(node, source, schema);
      readAttributes(node, source, schema);
      readElements(node, source, schema);
   }   

   /**
    * This <code>read</code> method is used to read the attributes from
    * the provided XML element. This will iterate over all attributes
    * within the element and convert those attributes as primitives to
    * contact values within the source object.
    * <p>
    * Once all attributes within the XML element have been evaluated
    * the <code>Schema</code> is checked to ensure that there are no
    * required contacts annotated with the <code>Attribute</code> that
    * remain. If any required attribute remains an exception is thrown. 
    * 
    * @param node this is the XML element to be evaluated
    * @param source the source object which will be deserialized
    * @param schema this is used to visit the attribute contacts
    * 
    * @throws Exception thrown if any required attributes remain
    */
   private void readAttributes(InputNode node, Object source, Schema schema) throws Exception {
      NodeMap list = node.getAttributes();
      LabelMap map = schema.getAttributes();

      for(String name : list) {         
         readAttribute(node.getAttribute(name), source, map);
      }  
      validate(node, map, source);
   }

   /**
    * This <code>read</code> method is used to read the elements from
    * the provided XML element. This will iterate over all elements
    * within the element and convert those elements to primitives or
    * composite objects depending on the contact annotation.
    * <p>
    * Once all elements within the XML element have been evaluated
    * the <code>Schema</code> is checked to ensure that there are no
    * required contacts annotated with the <code>Element</code> that
    * remain. If any required element remains an exception is thrown. 
    * 
    * @param node this is the XML element to be evaluated
    * @param source the source object which will be deserialized
    * @param schema this is used to visit the element contacts
    * 
    * @throws Exception thrown if any required elements remain
    */
   private void readElements(InputNode node, Object source, Schema schema) throws Exception {
      LabelMap map = schema.getElements();
      
      while(true) {
         InputNode child = node.getNext(); 
         
         if(child == null) {
            break;
         }
         readElement(child, source, map);
      } 
      validate(node, map, source);
   }
   
   /**
    * This <code>read</code> method is used to read the text value
    * from the XML element node specified. This will check the class
    * schema to determine if a <code>Text</code> annotation was
    * specified. If one was specified then the text within the XML
    * element input node is used to populate the contact value.
    * 
    * @param node this is the XML element to acquire the text from
    * @param source the source object which will be deserialized
    * @param schema this is used to visit the element contacts
    * 
    * @throws Exception thrown if a required text value was null
    */
   private void readText(InputNode node, Object source, Schema schema) throws Exception {
      Label label = schema.getText();
      
      if(label != null) {
         read(node, source, label);
      }
   }
   
   /**
    * This <code>read</code> method is used to perform deserialization
    * of the provided node object using a delegate converter. This is
    * typically another <code>Composite</code> converter, or if the
    * node is an attribute a <code>Primitive</code> converter. When
    * the delegate converter has completed the deserialized value is
    * assigned to the contact.
    * 
    * @param node this is the node that contains the contact value
    * @param source the source object to assign the contact value to
    * @param map this is the map that contains the label objects
    * 
    * @throws Exception thrown if the the label object does not exist
    */
   private void readAttribute(InputNode node, Object source, LabelMap map) throws Exception {
      Position line = node.getPosition();
      String name = node.getName();
      Label label = map.take(name);
      
      if(label == null) {
         if(map.isStrict()) {              
            throw new AttributeException("Attribute '%s' does not exist at %s", name, line);
         }            
      } else {
         read(node, source, label);
      }         
   }

   /**
    * This <code>read</code> method is used to perform deserialization
    * of the provided node object using a delegate converter. This is
    * typically another <code>Composite</code> converter, or if the
    * node is an attribute a <code>Primitive</code> converter. When
    * the delegate converter has completed the deserialized value is
    * assigned to the contact.
    * 
    * @param node this is the node that contains the contact value
    * @param source the source object to assign the contact value to
    * @param map this is the map that contains the label objects
    * 
    * @throws Exception thrown if the the label object does not exist
    */
   private void readElement(InputNode node, Object source, LabelMap map) throws Exception {
      Position line = node.getPosition();
      String name = node.getName();
      Label label = map.take(name);
      
      if(label == null) {
         if(map.isStrict()) {              
            throw new ElementException("Element '%s' does not exist at %s", name, line);
         } else {
            node.skip();                 
         }
      } else {
         read(node, source, label);
      }         
   }
   
   /**
    * This <code>read</code> method is used to perform deserialization
    * of the provided node object using a delegate converter. This is
    * typically another <code>Composite</code> converter, or if the
    * node is an attribute a <code>Primitive</code> converter. When
    * the delegate converter has completed the deserialized value is
    * assigned to the contact.
    * 
    * @param node this is the node that contains the contact value
    * @param source the source object to assign the contact value to
    * @param label this is the label used to create the converter
    * 
    * @throws Exception thrown if the contact could not be deserialized
    */
   private void read(InputNode node, Object source, Label label) throws Exception {      
      Converter reader = label.getConverter(root);
      Contact contact = label.getContact();      
      Object object = reader.read(node);
    
      if(object == null) { 
         Position line = node.getPosition();
         Class type = source.getClass();
         
         if(label.isRequired()) {              
            throw new ValueRequiredException("Empty value for %s in %s at %s", label, type, line);
         }
      } else {         
         contact.set(source, object);
      }         
   }
   
   /**
    * This method checks to see if there are any <code>Label</code>
    * objects remaining in the provided map that are required. This is
    * used when deserialization is performed to ensure the the XML
    * element deserialized contains sufficient details to satisfy the
    * XML schema class annotations. If there is a required label that
    * remains it is reported within the exception thrown.
    * 
    * @param map this is the map to check for remaining labels
    * @param source this is the object that has been deserialized 
    * 
    * @throws Exception thrown if an XML property was not declared
    */
   private void validate(InputNode node, LabelMap map, Object source) throws Exception {
      Position line = node.getPosition();
      Class type = source.getClass();
      
      for(Label label : map) {
         if(label.isRequired()) {
            throw new ValueRequiredException("Unable to satisfy %s for %s at %s", label, type, line);
         }
      }      
   }
   
   /**
    * This <code>write</code> method is used to perform serialization of
    * the given source object. Serialization is performed by appending
    * elements and attributes from the source object to the provided XML
    * element object. How the objects contacts are serialized is 
    * determined by the XML schema class that the source object is an
    * instance of. If a required contact is null an exception is thrown.
    * 
    * @param source this is the source object to be serialized
    * @param node the XML element the object is to be serialized to 
    * 
    * @throws Exception thrown if there is a serialization problem
    */
   public void write(OutputNode node, Object source) throws Exception {
      Schema schema = root.getSchema(source);
      
      try {
         schema.persist(source); 
         write(node, source, schema);
      } finally {
         schema.complete(source);
      }
   }
   
   /**
    * This <code>write</code> method is used to perform serialization of
    * the given source object. Serialization is performed by appending
    * elements and attributes from the source object to the provided XML
    * element object. How the objects contacts are serialized is 
    * determined by the XML schema class that the source object is an
    * instance of. If a required contact is null an exception is thrown.
    * 
    * @param source this is the source object to be serialized
    * @param node the XML element the object is to be serialized to
    * @param schema this is used to track the referenced contacts 
    * 
    * @throws Exception thrown if there is a serialization problem
    */
   private void write(OutputNode node, Object source, Schema schema) throws Exception {
      writeAttributes(node, source, schema);
      writeElements(node, source, schema);
      writeText(node, source, schema);
   }

   /**
    * This write method is used to write all the attribute contacts from
    * the provided source object to the XML element. This visits all
    * the contacts marked with the <code>Attribute</code> annotation in
    * the source object. All annotated contacts are written as attributes
    * to the XML element. This will throw an exception if a required
    * contact within the source object is null. 
    * 
    * @param source this is the source object to be serialized
    * @param node this is the XML element to write attributes to
    * @param schema this is used to track the referenced attributes
    * 
    * @throws Exception thrown if there is a serialization problem
    */
   private void writeAttributes(OutputNode node, Object source, Schema schema) throws Exception {
      LabelMap attributes = schema.getAttributes();

      for(Label label : attributes) {
         Contact contact = label.getContact();         
         Object value = contact.get(source);
         
         if(value == null && label.isRequired()) {
            throw new AttributeException("Value for %s is null", label);
         }
         writeAttribute(node, value, label);              
      }      
   }

   /**
    * This write method is used to write all the element contacts from
    * the provided source object to the XML element. This visits all
    * the contacts marked with the <code>Element</code> annotation in
    * the source object. All annotated contacts are written as children
    * to the XML element. This will throw an exception if a required
    * contact within the source object is null. 
    * 
    * @param source this is the source object to be serialized
    * @param node this is the XML element to write elements to
    * @param schema this is used to track the referenced elements
    * 
    * @throws Exception thrown if there is a serialization problem
    */
   private void writeElements(OutputNode node, Object source, Schema schema) throws Exception {
      LabelMap elements = schema.getElements();
      
      for(Label label : elements) {
         Contact contact = label.getContact();
         Object value = contact.get(source);
                 
         if(value == null && label.isRequired()) {
            throw new ElementException("Value for %s is null", label);
         }
         writeElement(node, value, label);
      }         
   }
   
   /**
    * This write method is used to write the text contact from the 
    * provided source object to the XML element. This takes the text
    * value from the source object and writes it to the single contact
    * marked with the <code>Text</code> annotation. If the value is
    * null and the contact value is required an exception is thrown.
    * 
    * @param source this is the source object to be serialized
    * @param node this is the XML element to write text value to
    * @param schema this is used to track the referenced elements
    * 
    * @throws Exception thrown if there is a serialization problem
    */
   private void writeText(OutputNode node, Object source, Schema schema) throws Exception {
      Label label = schema.getText();

      if(label != null) {
         Contact contact = label.getContact();
         Object value = contact.get(source);
                 
         if(value == null && label.isRequired()) {
            throw new TextException("Value for %s is null", label);
         }
         writeText(node, value, label); 
      }         
   }
   
   /**
    * This write method is used to set the value of the provided object
    * as an attribute to the XML element. This will acquire the string
    * value of the object using <code>toString</code> only if the
    * object provided is not an enumerated type. If the object is an
    * enumerated type then the <code>Enum.name</code> method is used.
    * 
    * @param value this is the value to be set as an attribute
    * @param node this is the XML element to write the attribute to
    * @param label the label that contains the contact details
    * 
    * @throws Exception thrown if there is a serialization problem
    */
   private void writeAttribute(OutputNode node, Object value, Label label) throws Exception {
      if(value != null) {
         String name = label.getName();
         String text = value.toString();
         
         if(value instanceof Enum) {
            Enum type = (Enum) value;
            text = type.name();
         }
         node.setAttribute(name, text);
      }
   }
   
   /**
    * This write method is used to append the provided object as an
    * element to the given XML element object. This will recursively
    * write the contacts from the provided object as elements. This is
    * done using the <code>Converter</code> acquired from the contact
    * label. If the type of the contact value is not of the same
    * type as the XML schema class a "class" attribute is appended.
    * 
    * @param value this is the value to be set as an element
    * @param node this is the XML element to write the element to
    * @param label the label that contains the contact details
    * 
    * @throws Exception thrown if there is a serialization problem
    */
   private void writeElement(OutputNode node, Object value, Label label) throws Exception {
      if(value != null) {
         String name = label.getName();
         OutputNode next = node.getChild(name);
         Class type = label.getType();

         if(label.isInline() || !isOverridden(next, value, type)) {
            Converter convert = label.getConverter(root);
            boolean data = label.isData();
            
            next.setData(data);
            convert.write(next, value);
         }
      }
   }
   
   /**
    * This write method is used to set the value of the provided object
    * as the text for the XML element. This will acquire the string
    * value of the object using <code>toString</code> only if the
    * object provided is not an enumerated type. If the object is an
    * enumerated type then the <code>Enum.name</code> method is used.
    * 
    * @param value this is the value toset as the XML element text
    * @param node this is the XML element to write the text value to
    * @param label the label that contains the contact details
    * 
    * @throws Exception thrown if there is a serialization problem
    */
   private void writeText(OutputNode node, Object value, Label label) throws Exception {
      if(value != null) {         
         String text = value.toString(); 
         boolean data = label.isData();
         
         if(value instanceof Enum) {
            Enum type = (Enum) value;
            text = type.name();
         }
         node.setData(data);
         node.setValue(text);        
      }
   }
   
   /**
    * This is used to determine whether the specified value has been
    * overrideen by the strategy. If the item has been overridden
    * then no more serialization is require for that value, this is
    * effectivly telling the serialization process to stop writing.
    * 
    * @param node the node that a potential override is written to
    * @param value this is the object instance to be serialized
    * @param type this is the type of the object to be serialized
    * 
    * @return returns true if the strategy overrides the object
    */
   private boolean isOverridden(OutputNode node, Object value, Class type) throws Exception{
      return factory.setOverride(type, value, node);
   }
}
