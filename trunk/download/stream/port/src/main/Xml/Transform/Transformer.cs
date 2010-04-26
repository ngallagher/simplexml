#region License
//
// Transformer.cs May 2007
//
// Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
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
using SimpleFramework.Xml.Util;
using SimpleFramework.Xml;
using System;
#endregion
package SimpleFramework.Xml.transform;
/// <summary>
/// The <c>Transformer</c> object is used to convert strings to
/// and from object instances. This is used during the serialization
/// and deserialization process to transform types from the Java class
/// libraries, as well as other types which do not contain XML schema
/// annotations. Typically this will be used to transform primitive
/// types to and from strings, such as <c>int</c> values.
/// </code>
///    &#64;Element
///    private String[] value;
/// </code>
/// For example taking the above value the array of strings needs to
/// be converted in to a single string value that can be inserted in
/// to the element in such a way that in can be read later. In this
/// case the serialized value of the string array would be as follows.
/// </code>
///    &lt;value&gt;one, two, three&lt;/value&gt;
/// </code>
/// Here each non-null string is inserted in to a comma separated
/// list of values, which can later be deserialized. Just to note the
/// above array could be annotated with <c>ElementList</c> just
/// as easily, in which case each entry would have its own element.
/// The choice of which annotation to use is up to the developer. A
/// more obvious benefit to transformations like this can be seen for
/// values annotated with the <c>Attribute</c> annotation.
/// </summary>
/// @author Niall Gallagher
public class Transformer {
   /// <summary>
   /// This is used to cache all transforms matched to a given type.
   /// </summary>
   private readonly TransformCache cache;
   /// <summary>
   /// This is used to perform the matching of types to transforms.
   /// </summary>
   private readonly Matcher matcher;
   /// <summary>
   /// This is used to cache the types that to not have a transform.
   /// </summary>
   private readonly Cache error;
   /// <summary>
   /// Constructor for the <c>Transformer</c> object. This is
   /// used to create a transformer which will transform specified
   /// types using transforms loaded from the class path. Transforms
   /// are matched to types using the specified matcher object.
   /// </summary>
   /// <param name="matcher">
   /// this is used to match types to transforms
   /// </param>
   public Transformer(Matcher matcher) {
      this.matcher = new DefaultMatcher(matcher);
      this.cache = new TransformCache();
      this.error = new WeakCache();
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
      Transform transform = Lookup(type);
      if(transform == null) {
         throw new TransformException("Transform of %s not supported", type);
      }
      return transform.Read(value);
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
      Transform transform = Lookup(type);
      if(transform == null) {
         throw new TransformException("Transform of %s not supported", type);
      }
      return transform.Write(value);
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
      return Lookup(type) != null;
   }
   /// <summary>
   /// This method is used to acquire a <c>Transform</c> for
   /// the the specified type. If there is no transform for the type
   /// then this will return null. Once acquired once the transform
   /// is cached so that subsequent lookups will be performed faster.
   /// </summary>
   /// <param name="type">
   /// the type to determine whether its transformable
   /// </param>
   /// <returns>
   /// this will return a transform for the specified type
   /// </returns>
   public Transform Lookup(Class type) {
      Transform transform = cache.fetch(type);
      if(transform != null) {
         return transform;
      }
      if(error.contains(type)) {
         return null;
      }
      return Match(type);
   }
   /// <summary>
   /// This method is used to acquire a <c>Transform</c> for
   /// the the specified type. If there is no transform for the type
   /// then this will return null. Once acquired once the transform
   /// is cached so that subsequent lookups will be performed faster.
   /// </summary>
   /// <param name="type">
   /// the type to determine whether its transformable
   /// </param>
   /// <returns>
   /// this will return a transform for the specified type
   /// </returns>
   public Transform Match(Class type) {
      Transform transform = matcher.Match(type);
      if(transform != null) {
         cache.cache(type, transform);
      } else {
         error.cache(type, this);
      }
      return transform;
   }
}
}
