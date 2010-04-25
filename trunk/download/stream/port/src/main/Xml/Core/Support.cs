#region License
//
// Support.cs May 2006
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
using SimpleFramework.Xml.Filter;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Support</c> object is used to provide support to the
   /// serialization engine for processing and transforming strings. This
   /// contains a <c>Transformer</c> which will create objects from
   /// strings and will also reverse this process converting an object
   /// to a string. This is used in the conversion of primitive types.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.transform.Transformer
   /// </seealso>
   class Support : Filter {
      /// <summary>
      /// This will perform the scanning of types are provide scanners.
      /// </summary>
      private readonly ScannerFactory factory;
      /// <summary>
      /// This is the factory that is used to create the scanners.
      /// </summary>
      private readonly Instantiator creator;
      /// <summary>
      /// This is the transformer used to transform objects to text.
      /// </summary>
      private readonly Transformer transform;
      /// <summary>
      /// This is the matcher used to acquire the transform objects.
      /// </summary>
      private readonly Matcher matcher;
      /// <summary>
      /// This is the filter used to transform the template variables.
      /// </summary>
      private readonly Filter filter;
      /// <summary>
      /// Constructor for the <c>Support</c> object. This will
      /// create a support object with a default matcher and default
      /// platform filter. This ensures it contains enough information
      /// to process a template and transform basic primitive types.
      /// </summary>
      public Support() {
         this(new PlatformFilter());
      }
      /// <summary>
      /// Constructor for the <c>Support</c> object. This will
      /// create a support object with a default matcher and the filter
      /// provided. This ensures it contains enough information to
      /// process a template and transform basic primitive types.
      /// </summary>
      /// <param name="filter">
      /// this is the filter to use with this support
      /// </param>
      public Support(Filter filter) {
         this(filter, new EmptyMatcher());
      }
      /// <summary>
      /// Constructor for the <c>Support</c> object. This will
      /// create a support object with the matcher and filter provided.
      /// This allows the user to override the transformations that
      /// are used to convert types to strings and back again.
      /// </summary>
      /// <param name="filter">
      /// this is the filter to use with this support
      /// </param>
      /// <param name="matcher">
      /// this is the matcher used for transformations
      /// </param>
      public Support(Filter filter, Matcher matcher) {
         this.transform = new Transformer(matcher);
         this.factory = new ScannerFactory();
         this.creator = new Instantiator();
         this.matcher = matcher;
         this.filter = filter;
      }
      /// <summary>
      /// Replaces the text provided with some property. This method
      /// acts much like a the get method of the <c>Map</c>
      /// object, in that it uses the provided text as a key to some
      /// value. However it can also be used to evaluate expressions
      /// and output the result for inclusion in the generated XML.
      /// </summary>
      /// <param name="text">
      /// this is the text value that is to be replaced
      /// </param>
      /// <returns>
      /// returns a replacement for the provided text value
      /// </returns>
      public String Replace(String text) {
         return filter.Replace(text);
      }
      /// <summary>
      /// This will create an <c>Instance</c> that can be used
      /// to instantiate objects of the specified class. This leverages
      /// an internal constructor cache to ensure creation is quicker.
      /// </summary>
      /// <param name="value">
      /// this contains information on the object instance
      /// </param>
      /// <returns>
      /// this will return an object for instantiating objects
      /// </returns>
      public Instance GetInstance(Value value) {
         return creator.GetInstance(value);
      }
      /// <summary>
      /// This will create an <c>Instance</c> that can be used
      /// to instantiate objects of the specified class. This leverages
      /// an internal constructor cache to ensure creation is quicker.
      /// </summary>
      /// <param name="type">
      /// this is the type that is to be instantiated
      /// </param>
      /// <returns>
      /// this will return an object for instantiating objects
      /// </returns>
      public Instance GetInstance(Class type) {
         return creator.GetInstance(type);
      }
      /// <summary>
      /// This is used to match a <c>Transform</c> using the type
      /// specified. If no transform can be acquired then this returns
      /// a null value indicating that no transform could be found.
      /// </summary>
      /// <param name="type">
      /// this is the type to acquire the transform for
      /// </param>
      /// <returns>
      /// returns a transform for processing the type given
      /// </returns>
      public Transform GetTransform(Class type) {
         return matcher.Match(type);
      }
      /// <summary>
      /// This creates a <c>Scanner</c> object that can be used to
      /// examine the fields within the XML class schema. The scanner
      /// maintains information when a field from within the scanner is
      /// visited, this allows the serialization and deserialization
      /// process to determine if all required XML annotations are used.
      /// </summary>
      /// <param name="type">
      /// the schema class the scanner is created for
      /// </param>
      /// <returns>
      /// a scanner that can maintains information on the type
      /// </returns>
      public Scanner GetScanner(Class type) {
         return factory.GetInstance(type);
      }
      /// <summary>
      /// This method is used to convert the string value given to an
      /// appropriate representation. This is used when an object is
      /// being deserialized from the XML document and the value for
      /// the string representation is required.
      /// </summary>
      /// <param name="value">
      /// this is the string representation of the value
      /// </param>
      /// <param name="type">
      /// this is the type to convert the string value to
      /// </param>
      /// <returns>
      /// this returns an appropriate instanced to be used
      /// </returns>
      public Object Read(String value, Class type) {
         return transform.Read(value, type);
      }
      /// <summary>
      /// This method is used to convert the provided value into an XML
      /// usable format. This is used in the serialization process when
      /// there is a need to convert a field value in to a string so
      /// that that value can be written as a valid XML entity.
      /// </summary>
      /// <param name="value">
      /// this is the value to be converted to a string
      /// </param>
      /// <param name="type">
      /// this is the type to convert to a string value
      /// </param>
      /// <returns>
      /// this is the string representation of the given value
      /// </returns>
      public String Write(Object value, Class type) {
         return transform.Write(value, type);
      }
      /// <summary>
      /// This method is used to determine if the type specified can be
      /// transformed. This will use the <c>Matcher</c> to find a
      /// suitable transform, if one exists then this returns true, if
      /// not then this returns false. This is used during serialization
      /// to determine how to convert a field or method parameter.
      /// </summary>
      /// <param name="type">
      /// the type to determine whether its transformable
      /// </param>
      /// <returns>
      /// true if the type specified can be transformed by this
      /// </returns>
      public bool Valid(Class type) {
         return transform.Valid(type);
      }
      /// <summary>
      /// This is used to acquire the name of the specified type using
      /// the <c>Root</c> annotation for the class. This will
      /// use either the name explicitly provided by the annotation or
      /// it will use the name of the class that the annotation was
      /// placed on if there is no explicit name for the root.
      /// </summary>
      /// <param name="type">
      /// this is the type to acquire the root name for
      /// </param>
      /// <returns>
      /// this returns the name of the type from the root
      /// </returns>
      public String GetName(Class type) {
         Scanner schema = GetScanner(type);
         String name = schema.GetName();
         if(name != null) {
            return name;
         }
         return GetClassName(type);
      }
      /// <summary>
      /// This returns the name of the class specified. If there is a root
      /// annotation on the type, then this is ignored in favor of the
      /// actual class name. This is typically used when the type is a
      /// primitive or if there is no <c>Root</c> annotation present.
      /// </summary>
      /// <param name="type">
      /// this is the type to acquire the root name for
      /// </param>
      /// <returns>
      /// this returns the name of the type from the root
      /// </returns>
      public String GetClassName(Class type) {
         if(type.isArray()) {
            type = type.getComponentType();
         }
         String name = type.getSimpleName();
         if(type.IsPrimitive()) {
            return name;
         }
         return Reflector.GetName(name);
      }
      /// <summary>
      /// This is used to determine whether the scanned class represents
      /// a primitive type. A primitive type is a type that contains no
      /// XML annotations and so cannot be serialized with an XML form.
      /// Instead primitives a serialized using transformations.
      /// </summary>
      /// <param name="type">
      /// this is the type to determine if it is primitive
      /// </param>
      /// <returns>
      /// this returns true if no XML annotations were found
      /// </returns>
      public bool IsPrimitive(Class type) {
         if(type == String.class) {
            return true;
         }
         if(type.isEnum()) {
            return true;
         }
         if(type.IsPrimitive()) {
            return true;
         }
         return transform.Valid(type);
      }
      /// <summary>
      /// This is used to determine if the type specified is a floating
      /// point type. Types that are floating point are the double and
      /// float primitives as well as the java types for this primitives.
      /// </summary>
      /// <param name="type">
      /// this is the type to determine if it is a float
      /// </param>
      /// <returns>
      /// this returns true if the type is a floating point
      /// </returns>
      public bool IsFloat(Class type) {
         if(type == Double.class) {
            return true;
         }
         if(type == Float.class) {
            return true;
         }
         if(type == float.class) {
            return true;
         }
         if(type == double.class) {
            return true;
         }
         return false;
      }
   }
}
