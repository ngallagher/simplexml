#region License
//
// Composite.cs July 2006
//
// Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied. See the License for the specific language governing
// permissions and limitations under the License.
//
#endregion
#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Composite</c> object is used to perform serialization
   /// of objects that contain XML annotation. Composite objects are objects
   /// that are not primitive and contain references to serializable fields.
   /// This <c>Converter</c> will visit each field within the object
   /// and deserialize or serialize that field depending on the requested
   /// action. If a required field is not present when deserializing from
   /// an XML element this terminates the deserialization reports the error.
   /// </code>
   ///    &lt;element name="test" class="some.package.Type"&gt;
   ///       &lt;text&gt;string value&lt;/text&gt;
   ///       &lt;integer&gt;1234&lt;/integer&gt;
   ///    &lt;/element&gt;
   /// </code>
   /// To deserialize the above XML source this will attempt to match the
   /// attribute name with an <c>Attribute</c> annotation from the
   /// XML schema class, which is specified as "some.package.Type". This
   /// type must also contain <c>Element</c> annotations for the
   /// "text" and "integer" elements.
   /// <p>
   /// Serialization requires that contacts marked as required must have
   /// values that are not null. This ensures that the serialized object
   /// can be deserialized at a later stage using the same class schema.
   /// If a required value is null the serialization terminates and an
   /// exception is thrown.
   /// </summary>
   class Composite : Converter {
      /// <summary>
      /// This factory creates instances of the deserialized object.
      /// </summary>
      private readonly ObjectFactory factory;
      /// <summary>
      /// This is used to convert any primitive values that are needed.
      /// </summary>
      private readonly Primitive primitive;
      /// <summary>
      /// This is used to store objects so that they can be read again.
      /// </summary>
      private readonly Criteria criteria;
      /// <summary>
      /// This is the current revision of this composite converter.
      /// </summary>
      private readonly Revision revision;
      /// <summary>
      /// This is the source object for the instance of serialization.
      /// </summary>
      private readonly Context context;
      /// <summary>
      /// This is the type that this composite produces instances of.
      /// </summary>
      private readonly Type type;
      /// <summary>
      /// Constructor for the <c>Composite</c> object. This creates
      /// a converter object capable of serializing and deserializing root
      /// objects labeled with XML annotations. The XML schema class must
      /// be given to the instance in order to perform deserialization.
      /// </summary>
      /// <param name="context">
      /// the source object used to perform serialization
      /// </param>
      /// <param name="type">
      /// this is the XML schema type to use for this
      /// </param>
      public Composite(Context context, Type type) {
         this.factory = new ObjectFactory(context, type);
         this.primitive = new Primitive(context, type);
         this.criteria = new Collector(context);
         this.revision = new Revision();
         this.context = context;
         this.type = type;
      }
      /// <summary>
      /// This <c>read</c> method performs deserialization of the XML
      /// schema class type by traversing the contacts and instantiating them
      /// using details from the provided XML element. Because this will
      /// convert a non-primitive value it delegates to other converters to
      /// perform deserialization of lists and primitives.
      /// <p>
      /// If any of the required contacts are not present within the provided
      /// XML element this will terminate deserialization and throw an
      /// exception. The annotation missing is reported in the exception.
      /// </summary>
      /// <param name="node">
      /// the XML element contact values are deserialized from
      /// </param>
      /// <returns>
      /// this returns the fully deserialized object graph
      /// </returns>
      public Object Read(InputNode node) {
         Instance value = factory.GetInstance(node);
         Class type = value.Type;
         if(value.isReference()) {
            return value.GetInstance();
         }
         if(context.IsPrimitive(type)) {
            return ReadPrimitive(node, value);
         }
         return Read(node, value, type);
      }
      /// <summary>
      /// This <c>read</c> method performs deserialization of the XML
      /// schema class type by traversing the contacts and instantiating them
      /// using details from the provided XML element. Because this will
      /// convert a non-primitive value it delegates to other converters to
      /// perform deserialization of lists and primitives.
      /// <p>
      /// If any of the required contacts are not present within the provided
      /// XML element this will terminate deserialization and throw an
      /// exception. The annotation missing is reported in the exception.
      /// </summary>
      /// <param name="node">
      /// the XML element contact values are deserialized from
      /// </param>
      /// <param name="source">
      /// the object whose contacts are to be deserialized
      /// </param>
      /// <returns>
      /// this returns the fully deserialized object graph
      /// </returns>
      public Object Read(InputNode node, Object source) {
         Class type = source.getClass();
         Schema schema = context.GetSchema(type);
         Caller caller = schema.GetCaller();
         Read(node, source, schema);
         criteria.Commit(source);
         caller.Validate(source);
         caller.Commit(source);
         return ReadResolve(node, source, caller);
      }
      /// <summary>
      /// This <c>read</c> method performs deserialization of the XML
      /// schema class type by traversing the contacts and instantiating them
      /// using details from the provided XML element. Because this will
      /// convert a non-primitive value it delegates to other converters to
      /// perform deserialization of lists and primitives.
      /// <p>
      /// If any of the required contacts are not present within the provided
      /// XML element this will terminate deserialization and throw an
      /// exception. The annotation missing is reported in the exception.
      /// </summary>
      /// <param name="node">
      /// the XML element contact values are deserialized from
      /// </param>
      /// <param name="value">
      /// this is the instance for the object within the graph
      /// </param>
      /// <param name="real">
      /// this is the real type that is to be evaluated
      /// </param>
      /// <returns>
      /// this returns the fully deserialized object graph
      /// </returns>
      public Object Read(InputNode node, Instance value, Class real) {
         Schema schema = context.GetSchema(real);
         Caller caller = schema.GetCaller();
         Object source = Read(node, schema, value);
         caller.Validate(source);
         caller.Commit(source);
         value.setInstance(source);
         return ReadResolve(node, source, caller);
      }
      /// <summary>
      /// This <c>read</c> method performs deserialization of the XML
      /// schema class type by traversing the contacts and instantiating them
      /// using details from the provided XML element. Because this will
      /// convert a non-primitive value it delegates to other converters to
      /// perform deserialization of lists and primitives.
      /// <p>
      /// If any of the required contacts are not present within the provided
      /// XML element this will terminate deserialization and throw an
      /// exception. The annotation missing is reported in the exception.
      /// </summary>
      /// <param name="node">
      /// the XML element contact values are deserialized from
      /// </param>
      /// <param name="schema">
      /// this is the schema for the class to be deserialized
      /// </param>
      /// <param name="value">
      /// this is the value used for the deserialization
      /// </param>
      /// <returns>
      /// this returns the fully deserialized object graph
      /// </returns>
      public Object Read(InputNode node, Schema schema, Instance value) {
         Creator creator = schema.getCreator();
         if(creator.isDefault()) {
            return ReadDefault(node, schema, value);
         } else {
            Read(node, null, schema);
         }
         return ReadConstructor(node, schema, value);
      }
      /// <summary>
      /// This <c>readDefault</c> method performs deserialization of the
      /// XM schema class type by traversing the contacts and instantiating
      /// them using details from the provided XML element. Because this will
      /// convert a non-primitive value it delegates to other converters to
      /// perform deserialization of lists and primitives.
      /// <p>
      /// This takes the approach that the object is instantiated first and
      /// then the annotated fields and methods are deserialized from the XML
      /// elements and attributes. When all the details have be deserialized
      /// they are set on the internal contacts of the object.
      /// </summary>
      /// <param name="node">
      /// the XML element contact values are deserialized from
      /// </param>
      /// <param name="schema">
      /// this is the schema for the class to be deserialized
      /// </param>
      /// <param name="value">
      /// this is the value used for the deserialization
      /// </param>
      /// <returns>
      /// this returns the fully deserialized object graph
      /// </returns>
      public Object ReadDefault(InputNode node, Schema schema, Instance value) {
         Object source = value.GetInstance();
         if(value != null) {
            value.setInstance(source);
            Read(node, source, schema);
            criteria.Commit(source);
         }
         return source;
      }
      /// <summary>
      /// This <c>readConstructor</c> method performs deserialization of
      /// the XML schema class type by traversing the contacts and creating
      /// them using details from the provided XML element. Because this will
      /// convert a non-primitive value it delegates to other converters to
      /// perform deserialization of lists and primitives.
      /// <p>
      /// This takes the approach of reading the XML elements and attributes
      /// before instantiating the object. Instantiation is performed using a
      /// declared constructor. The parameters for the constructor are taken
      /// from the deserialized objects.
      /// </summary>
      /// <param name="node">
      /// the XML element contact values are deserialized from
      /// </param>
      /// <param name="schema">
      /// this is the schema for the class to be deserialized
      /// </param>
      /// <param name="value">
      /// this is the value used for the deserialization
      /// </param>
      /// <returns>
      /// this returns the fully deserialized object graph
      /// </returns>
      public Object ReadConstructor(InputNode node, Schema schema, Instance value) {
         Creator creator = schema.getCreator();
         Object source = creator.GetInstance(criteria);
         if(value != null) {
            value.setInstance(source);
            criteria.Commit(source);
         }
         return source;
      }
      /// <summary>
      /// This <c>readPrimitive</c> method will extract the text value
      /// from the node and replace any template variables before converting
      /// it to a primitive value. This uses a <c>Primitive</c> object
      /// to convert the node text to the resulting string. This will also
      /// respect all references on the node so cycle can be followed.
      /// </summary>
      /// <param name="node">
      /// this is the node to be converted to a primitive
      /// </param>
      /// <param name="value">
      /// this is the type for the object within the graph
      /// </param>
      /// <returns>
      /// this returns the primitive that has been deserialized
      /// </returns>
      public Object ReadPrimitive(InputNode node, Instance value) {
         Class type = value.Type;
         Object result = primitive.Read(node, type);
         if(type != null) {
            value.setInstance(result);
         }
         return result;
      }
      /// <summary>
      /// The <c>readResolve</c> method is used to determine if there
      /// is a resolution method which can be used to substitute the object
      /// deserialized. The resolve method is used when an object wishes
      /// to provide a substitute within the deserialized object graph.
      /// This acts as an equivalent to the Java Object Serialization
      /// <c>readResolve</c> method for the object deserialization.
      /// </summary>
      /// <param name="node">
      /// the XML element object provided as a replacement
      /// </param>
      /// <param name="source">
      /// the type of the object that is being deserialized
      /// </param>
      /// <param name="caller">
      /// this is used to invoke the callback methods
      /// </param>
      /// <returns>
      /// this returns a replacement for the deserialized object
      /// </returns>
      public Object ReadResolve(InputNode node, Object source, Caller caller) {
         if(source != null) {
            Position line = node.getPosition();
            Object value = caller.resolve(source);
            Class expect = type.Type;
            Class real = value.getClass();
            if(!expect.isAssignableFrom(real)) {
               throw new ElementException("Type %s does not match %s at %s", real, expect, line);
            }
            return value;
         }
         return source;
      }
      /// <summary>
      /// This <c>read</c> method performs deserialization of the XML
      /// schema class type by traversing the contacts and instantiating them
      /// using details from the provided XML element. Because this will
      /// convert a non-primitive value it delegates to other converters to
      /// perform deserialization of lists and primitives.
      /// <p>
      /// If any of the required contacts are not present within the provided
      /// XML element this will terminate deserialization and throw an
      /// exception. The annotation missing is reported in the exception.
      /// </summary>
      /// <param name="node">
      /// the XML element contact values are deserialized from
      /// </param>
      /// <param name="source">
      /// this type of the object that is to be deserialized
      /// </param>
      /// <param name="schema">
      /// this object visits the objects contacts
      /// </param>
      public void Read(InputNode node, Object source, Schema schema) {
         ReadVersion(node, source, schema);
         ReadText(node, source, schema);
         ReadAttributes(node, source, schema);
         ReadElements(node, source, schema);
      }
      /// <summary>
      /// This method is used to read the version from the provided input
      /// node. Once the version has been read it is used to determine how
      /// to deserialize the object. If the version is not the initial
      /// version then it is read in a manner that ignores excessive XML
      /// elements and attributes. Also none of the annotated fields or
      /// methods are required if the version is not the initial version.
      /// </summary>
      /// <param name="node">
      /// the XML element contact values are deserialized from
      /// </param>
      /// <param name="source">
      /// this object whose contacts are to be deserialized
      /// </param>
      /// <param name="schema">
      /// this object visits the objects contacts
      /// </param>
      public void ReadVersion(InputNode node, Object source, Schema schema) {
         Label label = schema.GetVersion();
         Class expect = type.Type;
         if(label != null) {
            String name = label.GetName();
            NodeMap<InputNode> map = node.getAttributes();
            InputNode value = map.Remove(name);
            if(value != null) {
               ReadVersion(value, source, label);
            } else {
               Version version = context.GetVersion(expect);
               Double start = revision.Default;
               Double expected = version.revision();
               criteria.Set(label, start);
               revision.Compare(expected, start);
            }
         }
      }
      /// <summary>
      /// This method is used to read the version from the provided input
      /// node. Once the version has been read it is used to determine how
      /// to deserialize the object. If the version is not the initial
      /// version then it is read in a manner that ignores excessive XML
      /// elements and attributes. Also none of the annotated fields or
      /// methods are required if the version is not the initial version.
      /// </summary>
      /// <param name="node">
      /// the XML element contact values are deserialized from
      /// </param>
      /// <param name="source">
      /// the type of the object that is being deserialized
      /// </param>
      /// <param name="label">
      /// this is the label used to read the version attribute
      /// </param>
      public void ReadVersion(InputNode node, Object source, Label label) {
         Object value = Read(node, source, label);
         Class expect = type.Type;
         if(value != null) {
            Version version = context.GetVersion(expect);
            Double actual = version.revision();
            if(!value.equals(revision)) {
               revision.Compare(actual, value);
            }
         }
      }
      /// <summary>
      /// This <c>readAttributes</c> method reads the attributes from
      /// the provided XML element. This will iterate over all attributes
      /// within the element and convert those attributes as primitives to
      /// contact values within the source object.
      /// <p>
      /// Once all attributes within the XML element have been evaluated
      /// the <c>Schema</c> is checked to ensure that there are no
      /// required contacts annotated with the <c>Attribute</c> that
      /// remain. If any required attribute remains an exception is thrown.
      /// </summary>
      /// <param name="node">
      /// this is the XML element to be evaluated
      /// </param>
      /// <param name="source">
      /// the type of the object that is being deserialized
      /// </param>
      /// <param name="schema">
      /// this is used to visit the attribute contacts
      /// </param>
      public void ReadAttributes(InputNode node, Object source, Schema schema) {
         NodeMap<InputNode> list = node.getAttributes();
         LabelMap map = schema.getAttributes();
         for(String name : list) {
            ReadAttribute(node.GetAttribute(name), source, map);
         }
         Validate(node, map, source);
      }
      /// <summary>
      /// This <c>readElements</c> method reads the elements from
      /// the provided XML element. This will iterate over all elements
      /// within the element and convert those elements to primitives or
      /// composite objects depending on the contact annotation.
      /// <p>
      /// Once all elements within the XML element have been evaluated
      /// the <c>Schema</c> is checked to ensure that there are no
      /// required contacts annotated with the <c>Element</c> that
      /// remain. If any required element remains an exception is thrown.
      /// </summary>
      /// <param name="node">
      /// this is the XML element to be evaluated
      /// </param>
      /// <param name="source">
      /// the type of the object that is being deserialized
      /// </param>
      /// <param name="schema">
      /// this is used to visit the element contacts
      /// </param>
      public void ReadElements(InputNode node, Object source, Schema schema) {
         LabelMap map = schema.getElements();
         while(true) {
            InputNode child = node.getNext();
            if(child == null) {
               break;
            }
            ReadElement(child, source, map);
         }
         Validate(node, map, source);
      }
      /// <summary>
      /// This <c>readText</c> method is used to read the text value
      /// from the XML element node specified. This will check the class
      /// schema to determine if a <c>Text</c> annotation was
      /// specified. If one was specified then the text within the XML
      /// element input node is used to populate the contact value.
      /// </summary>
      /// <param name="node">
      /// this is the XML element to acquire the text from
      /// </param>
      /// <param name="source">
      /// the type of the object that is being deserialized
      /// </param>
      /// <param name="schema">
      /// this is used to visit the element contacts
      /// </param>
      public void ReadText(InputNode node, Object source, Schema schema) {
         Label label = schema.getText();
         if(label != null) {
            Read(node, source, label);
         }
      }
      /// <summary>
      /// This <c>readAttribute</c> method is used for deserialization
      /// of the provided node object using a delegate converter. This is
      /// typically another <c>Composite</c> converter, or if the
      /// node is an attribute a <c>Primitive</c> converter. When
      /// the delegate converter has completed the deserialized value is
      /// assigned to the contact.
      /// </summary>
      /// <param name="node">
      /// this is the node that contains the contact value
      /// </param>
      /// <param name="source">
      /// the type of the object that is being deserialized
      /// </param>
      /// <param name="map">
      /// this is the map that contains the label objects
      /// </param>
      public void ReadAttribute(InputNode node, Object source, LabelMap map) {
         String name = node.GetName();
         Label label = map.take(name);
         if(label == null) {
            Position line = node.getPosition();
            Class type = source.getClass();
            if(map.IsStrict(context) && revision.IsEqual()) {
               throw new AttributeException("Attribute '%s' does not have a match in %s at %s", name, type, line);
            }
         } else {
            Read(node, source, label);
         }
      }
      /// <summary>
      /// This <c>readElement</c> method is used for deserialization
      /// of the provided node object using a delegate converter. This is
      /// typically another <c>Composite</c> converter, or if the
      /// node is an attribute a <c>Primitive</c> converter. When
      /// the delegate converter has completed the deserialized value is
      /// assigned to the contact.
      /// </summary>
      /// <param name="node">
      /// this is the node that contains the contact value
      /// </param>
      /// <param name="source">
      /// the type of the object that is being deserialized
      /// </param>
      /// <param name="map">
      /// this is the map that contains the label objects
      /// </param>
      public void ReadElement(InputNode node, Object source, LabelMap map) {
         String name = node.GetName();
         Label label = map.take(name);
         if(label == null) {
            label = criteria.Get(name);
         }
         if(label == null) {
            Position line = node.getPosition();
            Class type = source.getClass();
            if(map.IsStrict(context) && revision.IsEqual()) {
               throw new ElementException("Element '%s' does not have a match in %s at %s", name, type, line);
            } else {
               node.skip();
            }
         } else {
            Read(node, source, label);
         }
      }
      /// <summary>
      /// This <c>read</c> method is used to perform deserialization
      /// of the provided node object using a delegate converter. This is
      /// typically another <c>Composite</c> converter, or if the
      /// node is an attribute a <c>Primitive</c> converter. When
      /// the delegate converter has completed the deserialized value is
      /// assigned to the contact.
      /// </summary>
      /// <param name="node">
      /// this is the node that contains the contact value
      /// </param>
      /// <param name="source">
      /// the type of the object that is being deserialized
      /// </param>
      /// <param name="label">
      /// this is the label used to create the converter
      /// </param>
      public Object Read(InputNode node, Object source, Label label) {
         Object object = ReadObject(node, source, label);
         if(object == null) {
            Position line = node.getPosition();
            Class expect = type.Type;
            if(source != null) {
               expect = source.getClass();
            }
            if(label.isRequired() && revision.IsEqual()) {
               throw new ValueRequiredException("Empty value for %s in %s at %s", label, expect, line);
            }
         } else {
            if(object != label.getEmpty(context)) {
               criteria.Set(label, object);
            }
         }
         return object;
      }
      /// <summary>
      /// This <c>readObject</c> method is used to perform the
      /// deserialization of the XML in to any original value. If there
      /// is no original value then this will do a read and instantiate
      /// a new value to deserialize in to. Reading in to the original
      /// ensures that existing lists or maps can be read in to.
      /// </summary>
      /// <param name="node">
      /// this is the node that contains the contact value
      /// </param>
      /// <param name="source">
      /// the source object to assign the contact value to
      /// </param>
      /// <param name="label">
      /// this is the label used to create the converter
      /// </param>
      /// <returns>
      /// this returns the original value deserialized in to
      /// </returns>
      public Object ReadObject(InputNode node, Object source, Label label) {
         Converter reader = label.getConverter(context);
         String name = label.GetName(context);
         if(label.isCollection()) {
            Variable variable = criteria.Get(name);
            Contact contact = label.getContact();
            if(variable != null) {
               Object value = variable.getValue();
               return reader.Read(node, value);
            } else {
               if(source != null) {
                  Object value = contact.Get(source);
                  if(value != null) {
                     return reader.Read(node, value);
                  }
               }
            }
         }
         return reader.Read(node);
      }
      /// <summary>
      /// This method checks to see if there are any <c>Label</c>
      /// objects remaining in the provided map that are required. This is
      /// used when deserialization is performed to ensure the the XML
      /// element deserialized contains sufficient details to satisfy the
      /// XML schema class annotations. If there is a required label that
      /// remains it is reported within the exception thrown.
      /// </summary>
      /// <param name="map">
      /// this is the map to check for remaining labels
      /// </param>
      /// <param name="source">
      /// this is the object that has been deserialized
      /// </param>
      public void Validate(InputNode node, LabelMap map, Object source) {
         Position line = node.getPosition();
         Class expect = type.Type;
         if(source != null) {
            expect = source.getClass();
         }
         for(Label label : map) {
            if(label.isRequired() && revision.IsEqual()) {
               throw new ValueRequiredException("Unable to satisfy %s for %s at %s", label, expect, line);
            }
            Object value = label.getEmpty(context);
            if(value != null) {
               criteria.Set(label, value);
            }
         }
      }
      /// <summary>
      /// This <c>validate</c> method performs validation of the XML
      /// schema class type by traversing the contacts and validating them
      /// using details from the provided XML element. Because this will
      /// validate a non-primitive value it delegates to other converters
      /// to perform validation of lists, maps, and primitives.
      /// <p>
      /// If any of the required contacts are not present within the given
      /// XML element this will terminate validation and throw an exception
      /// The annotation missing is reported in the exception.
      /// </summary>
      /// <param name="node">
      /// the XML element contact values are validated from
      /// </param>
      /// <returns>
      /// true if the XML element matches the XML schema class given
      /// </returns>
      public bool Validate(InputNode node) {
         Instance value = factory.GetInstance(node);
         if(!value.isReference()) {
            Object result = value.setInstance(null);
            Class type = value.Type;
            return Validate(node, type);
         }
         return true;
      }
      /// <summary>
      /// This <c>validate</c> method performs validation of the XML
      /// schema class type by traversing the contacts and validating them
      /// using details from the provided XML element. Because this will
      /// validate a non-primitive value it delegates to other converters
      /// to perform validation of lists, maps, and primitives.
      /// <p>
      /// If any of the required contacts are not present within the given
      /// XML element this will terminate validation and throw an exception
      /// The annotation missing is reported in the exception.
      /// </summary>
      /// <param name="node">
      /// the XML element contact values are validated from
      /// </param>
      /// <param name="type">
      /// this is the type to validate against the input node
      /// </param>
      public bool Validate(InputNode node, Class type) {
         Schema schema = context.GetSchema(type);
         ValidateText(node, schema);
         ValidateAttributes(node, schema);
         ValidateElements(node, schema);
         return node.isElement();
      }
      /// <summary>
      /// This <c>validateAttributes</c> method validates the attributes
      /// from the provided XML element. This will iterate over all attributes
      /// within the element and validate those attributes as primitives to
      /// contact values within the source object.
      /// <p>
      /// Once all attributes within the XML element have been evaluated the
      /// <c>Schema</c> is checked to ensure that there are no required
      /// contacts annotated with the <c>Attribute</c> that remain. If
      /// any required attribute remains an exception is thrown.
      /// </summary>
      /// <param name="node">
      /// this is the XML element to be validated
      /// </param>
      /// <param name="schema">
      /// this is used to visit the attribute contacts
      /// </param>
      public void ValidateAttributes(InputNode node, Schema schema) {
         NodeMap<InputNode> list = node.getAttributes();
         LabelMap map = schema.getAttributes();
         for(String name : list) {
            ValidateAttribute(node.GetAttribute(name), map);
         }
         Validate(node, map);
      }
      /// <summary>
      /// This <c>validateElements</c> method validates the elements
      /// from the provided XML element. This will iterate over all elements
      /// within the element and validate those elements as primitives or
      /// composite objects depending on the contact annotation.
      /// <p>
      /// Once all elements within the XML element have been evaluated
      /// the <c>Schema</c> is checked to ensure that there are no
      /// required contacts annotated with the <c>Element</c> that
      /// remain. If any required element remains an exception is thrown.
      /// </summary>
      /// <param name="node">
      /// this is the XML element to be evaluated
      /// </param>
      /// <param name="schema">
      /// this is used to visit the element contacts
      /// </param>
      public void ValidateElements(InputNode node, Schema schema) {
         LabelMap map = schema.getElements();
         while(true) {
            InputNode child = node.getNext();
            if(child == null) {
               break;
            }
            ValidateElement(child, map);
         }
         Validate(node, map);
      }
      /// <summary>
      /// This <c>validateText</c> method validates the text value
      /// from the XML element node specified. This will check the class
      /// schema to determine if a <c>Text</c> annotation was used.
      /// If one was specified then the text within the XML element input
      /// node is checked to determine if it is a valid entry.
      /// </summary>
      /// <param name="node">
      /// this is the XML element to acquire the text from
      /// </param>
      /// <param name="schema">
      /// this is used to visit the element contacts
      /// </param>
      public void ValidateText(InputNode node, Schema schema) {
         Label label = schema.getText();
         if(label != null) {
            Validate(node, label);
         }
      }
      /// <summary>
      /// This <c>validateAttribute</c> method performs a validation
      /// of the provided node object using a delegate converter. This is
      /// typically another <c>Composite</c> converter, or if the
      /// node is an attribute a <c>Primitive</c> converter. If this
      /// fails validation then an exception is thrown to report the issue.
      /// </summary>
      /// <param name="node">
      /// this is the node that contains the contact value
      /// </param>
      /// <param name="map">
      /// this is the map that contains the label objects
      /// </param>
      public void ValidateAttribute(InputNode node, LabelMap map) {
         Position line = node.getPosition();
         String name = node.GetName();
         Label label = map.take(name);
         if(label == null) {
            if(map.IsStrict(context) && revision.IsEqual()) {
               throw new AttributeException("Attribute '%s' does not exist at %s", name, line);
            }
         } else {
            Validate(node, label);
         }
      }
      /// <summary>
      /// This <c>validateElement</c> method performs a validation
      /// of the provided node object using a delegate converter. This is
      /// typically another <c>Composite</c> converter, or if the
      /// node is an attribute a <c>Primitive</c> converter. If this
      /// fails validation then an exception is thrown to report the issue.
      /// </summary>
      /// <param name="node">
      /// this is the node that contains the contact value
      /// </param>
      /// <param name="map">
      /// this is the map that contains the label objects
      /// </param>
      public void ValidateElement(InputNode node, LabelMap map) {
         String name = node.GetName();
         Label label = map.take(name);
         if(label == null) {
            label = criteria.Get(name);
         }
         if(label == null) {
            Position line = node.getPosition();
            if(map.IsStrict(context) && revision.IsEqual()) {
               throw new ElementException("Element '%s' does not exist at %s", name, line);
            } else {
               node.skip();
            }
         } else {
            Validate(node, label);
         }
      }
      /// <summary>
      /// This <c>validate</c> method is used to perform validation
      /// of the provided node object using a delegate converter. This is
      /// typically another <c>Composite</c> converter, or if the
      /// node is an attribute a <c>Primitive</c> converter. If this
      /// fails validation then an exception is thrown to report the issue.
      /// </summary>
      /// <param name="node">
      /// this is the node that contains the contact value
      /// </param>
      /// <param name="label">
      /// this is the label used to create the converter
      /// </param>
      public void Validate(InputNode node, Label label) {
         Converter reader = label.getConverter(context);
         Position line = node.getPosition();
         Class expect = type.Type;
         bool valid = reader.Validate(node);
         if(valid == false) {
           throw new PersistenceException("Invalid value for %s in %s at %s", label, expect, line);
         }
         criteria.Set(label, null);
      }
      /// <summary>
      /// This method checks to see if there are any <c>Label</c>
      /// objects remaining in the provided map that are required. This is
      /// used when validation is performed to ensure the the XML element
      /// validated contains sufficient details to satisfy the XML schema
      /// class annotations. If there is a required label that remains it
      /// is reported within the exception thrown.
      /// </summary>
      /// <param name="node">
      /// this is the node that contains the composite data
      /// </param>
      /// <param name="map">
      /// this contains the converters to perform validation
      /// </param>
      public void Validate(InputNode node, LabelMap map) {
         Position line = node.getPosition();
         for(Label label : map) {
            if(label.isRequired() && revision.IsEqual()) {
               throw new ValueRequiredException("Unable to satisfy %s at %s", label, line);
            }
         }
      }
      /// <summary>
      /// This <c>write</c> method is used to perform serialization of
      /// the given source object. Serialization is performed by appending
      /// elements and attributes from the source object to the provided XML
      /// element object. How the objects contacts are serialized is
      /// determined by the XML schema class that the source object is an
      /// instance of. If a required contact is null an exception is thrown.
      /// </summary>
      /// <param name="source">
      /// this is the source object to be serialized
      /// </param>
      /// <param name="node">
      /// the XML element the object is to be serialized to
      /// </param>
      public void Write(OutputNode node, Object source) {
         Class type = source.getClass();
         Schema schema = context.GetSchema(type);
         Caller caller = schema.GetCaller();
         try {
            if(schema.IsPrimitive()) {
               primitive.Write(node, source);
            } else {
               caller.persist(source);
               Write(node, source, schema);
            }
         } finally {
            caller.complete(source);
         }
      }
      /// <summary>
      /// This <c>write</c> method is used to perform serialization of
      /// the given source object. Serialization is performed by appending
      /// elements and attributes from the source object to the provided XML
      /// element object. How the objects contacts are serialized is
      /// determined by the XML schema class that the source object is an
      /// instance of. If a required contact is null an exception is thrown.
      /// </summary>
      /// <param name="source">
      /// this is the source object to be serialized
      /// </param>
      /// <param name="node">
      /// the XML element the object is to be serialized to
      /// </param>
      /// <param name="schema">
      /// this is used to track the referenced contacts
      /// </param>
      public void Write(OutputNode node, Object source, Schema schema) {
         WriteVersion(node, source, schema);
         WriteAttributes(node, source, schema);
         WriteElements(node, source, schema);
         WriteText(node, source, schema);
      }
      /// <summary>
      /// This method is used to write the version attribute. A version is
      /// written only if it is not the initial version or if it required.
      /// The version is used to determine how to deserialize the XML. If
      /// the version is different from the expected version then it allows
      /// the object to be deserialized in a manner that does not require
      /// any attributes or elements, and unmatched nodes are ignored.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the version attribute from
      /// </param>
      /// <param name="source">
      /// this is the source object that is to be written
      /// </param>
      /// <param name="schema">
      /// this is the schema that contains the version
      /// </param>
      public void WriteVersion(OutputNode node, Object source, Schema schema) {
         Version version = schema.getRevision();
         Label label = schema.GetVersion();
         if(version != null) {
            Double start = revision.Default;
            Double value = version.revision();
            if(revision.Compare(value, start)) {
               if(label.isRequired()) {
                  WriteAttribute(node, value, label);
               }
            } else {
               WriteAttribute(node, value, label);
            }
         }
      }
      /// <summary>
      /// This write method is used to write all the attribute contacts from
      /// the provided source object to the XML element. This visits all
      /// the contacts marked with the <c>Attribute</c> annotation in
      /// the source object. All annotated contacts are written as attributes
      /// to the XML element. This will throw an exception if a required
      /// contact within the source object is null.
      /// </summary>
      /// <param name="source">
      /// this is the source object to be serialized
      /// </param>
      /// <param name="node">
      /// this is the XML element to write attributes to
      /// </param>
      /// <param name="schema">
      /// this is used to track the referenced attributes
      /// </param>
      public void WriteAttributes(OutputNode node, Object source, Schema schema) {
         LabelMap attributes = schema.getAttributes();
         for(Label label : attributes) {
            Contact contact = label.getContact();
            Object value = contact.Get(source);
            if(value == null) {
               value = label.getEmpty(context);
            }
            if(value == null && label.isRequired()) {
               throw new AttributeException("Value for %s is null", label);
            }
            WriteAttribute(node, value, label);
         }
      }
      /// <summary>
      /// This write method is used to write all the element contacts from
      /// the provided source object to the XML element. This visits all
      /// the contacts marked with the <c>Element</c> annotation in
      /// the source object. All annotated contacts are written as children
      /// to the XML element. This will throw an exception if a required
      /// contact within the source object is null.
      /// </summary>
      /// <param name="source">
      /// this is the source object to be serialized
      /// </param>
      /// <param name="node">
      /// this is the XML element to write elements to
      /// </param>
      /// <param name="schema">
      /// this is used to track the referenced elements
      /// </param>
      public void WriteElements(OutputNode node, Object source, Schema schema) {
         LabelMap elements = schema.getElements();
         for(Label label : elements) {
            Contact contact = label.getContact();
            Object value = contact.Get(source);
            if(value == null && label.isRequired()) {
               throw new ElementException("Value for %s is null", label);
            }
            Object replace = WriteReplace(value);
            if(replace != null) {
               WriteElement(node, replace, label);
            }
         }
      }
      /// <summary>
      /// The <c>replace</c> method is used to replace an object
      /// before it is serialized. This is used so that an object can give
      /// a substitute to be written to the XML document in the event that
      /// the actual object is not suitable or desired for serialization.
      /// This acts as an equivalent to the Java Object Serialization
      /// <c>writeReplace</c> method for the object serialization.
      /// </summary>
      /// <param name="source">
      /// this is the source object that is to be replaced
      /// </param>
      /// <returns>
      /// this returns the object to use as a replacement value
      /// </returns>
      public Object WriteReplace(Object source) {
         if(source != null) {
            Class type = source.getClass();
            Caller caller = context.GetCaller(type);
            return caller.replace(source);
         }
         return source;
      }
      /// <summary>
      /// This write method is used to write the text contact from the
      /// provided source object to the XML element. This takes the text
      /// value from the source object and writes it to the single contact
      /// marked with the <c>Text</c> annotation. If the value is
      /// null and the contact value is required an exception is thrown.
      /// </summary>
      /// <param name="source">
      /// this is the source object to be serialized
      /// </param>
      /// <param name="node">
      /// this is the XML element to write text value to
      /// </param>
      /// <param name="schema">
      /// this is used to track the referenced elements
      /// </param>
      public void WriteText(OutputNode node, Object source, Schema schema) {
         Label label = schema.getText();
         if(label != null) {
            Contact contact = label.getContact();
            Object value = contact.Get(source);
            Class type = source.getClass();
            if(value == null) {
               value = label.getEmpty(context);
            }
            if(value == null && label.isRequired()) {
               throw new TextException("Value for %s is null for %s", label, type);
            }
            WriteText(node, value, label);
         }
      }
      /// <summary>
      /// This write method is used to set the value of the provided object
      /// as an attribute to the XML element. This will acquire the string
      /// value of the object using <c>toString</c> only if the
      /// object provided is not an enumerated type. If the object is an
      /// enumerated type then the <c>Enum.name</c> method is used.
      /// </summary>
      /// <param name="value">
      /// this is the value to be set as an attribute
      /// </param>
      /// <param name="node">
      /// this is the XML element to write the attribute to
      /// </param>
      /// <param name="label">
      /// the label that contains the contact details
      /// </param>
      public void WriteAttribute(OutputNode node, Object value, Label label) {
         if(value != null) {
            Decorator decorator = label.GetDecorator();
            String name = label.GetName(context);
            String text = factory.getText(value);
            OutputNode done = node.setAttribute(name, text);
            decorator.decorate(done);
         }
      }
      /// <summary>
      /// This write method is used to append the provided object as an
      /// element to the given XML element object. This will recursively
      /// write the contacts from the provided object as elements. This is
      /// done using the <c>Converter</c> acquired from the contact
      /// label. If the type of the contact value is not of the same
      /// type as the XML schema class a "class" attribute is appended.
      /// <p>
      /// If the element being written is inline, then this will not
      /// check to see if there is a "class" attribute specifying the
      /// name of the class. This is because inline elements do not have
      /// an outer class and thus could never have an override.
      /// </summary>
      /// <param name="value">
      /// this is the value to be set as an element
      /// </param>
      /// <param name="node">
      /// this is the XML element to write the element to
      /// </param>
      /// <param name="label">
      /// the label that contains the contact details
      /// </param>
      public void WriteElement(OutputNode node, Object value, Label label) {
         if(value != null) {
            String name = label.GetName(context);
            OutputNode next = node.getChild(name);
            Type contact = label.getContact();
            Class type = contact.Type;
            if(!label.isInline()) {
               WriteNamespaces(next, type, label);
            }
            if(label.isInline() || !IsOverridden(next, value, contact)) {
               Converter convert = label.getConverter(context);
               bool data = label.isData();
               next.setData(data);
               WriteElement(next, value, convert);
            }
         }
      }
      /// <summary>
      /// This is used write the element specified using the specified
      /// converter. Writing the value using the specified converter will
      /// result in the node being populated with the elements, attributes,
      /// and text values to the provided node. If there is a problem
      /// writing the value using the converter an exception is thrown.
      /// </summary>
      /// <param name="node">
      /// this is the node that the value is to be written to
      /// </param>
      /// <param name="value">
      /// this is the value that is to be written
      /// </param>
      /// <param name="convert">
      /// this is the converter used to perform writing
      /// </param>
      public void WriteElement(OutputNode node, Object value, Converter convert) {
         convert.Write(node, value);
      }
      /// <summary>
      /// This is used to apply <c>Decorator</c> objects to the
      /// provided node before it is written. Application of decorations
      /// before the node is written allows namespaces and comments to be
      /// applied to the node. Decorations such as this do not affect the
      /// overall structure of the XML that is written.
      /// </summary>
      /// <param name="node">
      /// this is the node that decorations are applied to
      /// </param>
      /// <param name="type">
      /// this is the type to acquire the decoration for
      /// </param>
      /// <param name="label">
      /// this contains the primary decorator to be used
      /// </param>
      public void WriteNamespaces(OutputNode node, Class type, Label label) {
         Decorator primary = context.GetDecorator(type);
         Decorator decorator = label.GetDecorator();
         decorator.decorate(node, primary);
      }
      /// <summary>
      /// This write method is used to set the value of the provided object
      /// as the text for the XML element. This will acquire the string
      /// value of the object using <c>toString</c> only if the
      /// object provided is not an enumerated type. If the object is an
      /// enumerated type then the <c>Enum.name</c> method is used.
      /// </summary>
      /// <param name="value">
      /// this is the value to set as the XML element text
      /// </param>
      /// <param name="node">
      /// this is the XML element to write the text value to
      /// </param>
      /// <param name="label">
      /// the label that contains the contact details
      /// </param>
      public void WriteText(OutputNode node, Object value, Label label) {
         if(value != null) {
            String text = factory.getText(value);
            bool data = label.isData();
            node.setData(data);
            node.setValue(text);
         }
      }
      /// <summary>
      /// This is used to determine whether the specified value has been
      /// overridden by the strategy. If the item has been overridden
      /// then no more serialization is require for that value, this is
      /// effectively telling the serialization process to stop writing.
      /// </summary>
      /// <param name="node">
      /// the node that a potential override is written to
      /// </param>
      /// <param name="value">
      /// this is the object instance to be serialized
      /// </param>
      /// <param name="type">
      /// this is the type of the object to be serialized
      /// </param>
      /// <returns>
      /// returns true if the strategy overrides the object
      /// </returns>
      public bool IsOverridden(OutputNode node, Object value, Type type) {
         return factory.SetOverride(type, value, node);
      }
   }
}
