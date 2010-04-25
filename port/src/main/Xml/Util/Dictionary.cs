#region License
//
// Dictionary.cs July 2006
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
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   /// <summary>
   /// The <c>Dictionary</c> object represents a mapped set of entry
   /// objects that can be serialized and deserialized. This is used when
   /// there is a need to load a list of objects that can be mapped using
   /// a name attribute. Using this object avoids the need to implement a
   /// commonly required pattern of building a map of XML element objects.
   /// </code>
   ///    &lt;dictionary&gt;
   ///       &lt;entry name="example"&gt;
   ///          &lt;element&gt;example text&lt;/element&gt;
   ///       &lt;/entry&gt;
   ///       &lt;entry name="example"&gt;
   ///          &lt;element&gt;example text&lt;/element&gt;
   ///       &lt;/entry&gt;
   ///    &lt;/dictionary&gt;
   /// </code>
   /// This can contain implementations of the <c>Entry</c> object
   /// which contains a required "name" attribute. Implementations of the
   /// entry object can add further XML attributes an elements. This must
   /// be annotated with the <c>ElementList</c> annotation in order
   /// to be serialized and deserialized as an object field.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Util.Entry
   /// </seealso>
   public class Dictionary<T> : List<T> where T : Entry {
      /// <summary>
      /// Used to map the entries to their configured names.
      /// </summary>
      protected Table map;
      /// <summary>
      /// Constructor for the <c>Dictionary</c> object. This
      /// is used to create a set that contains entry objects mapped
      /// to an XML attribute name value. Entry objects added to this
      /// dictionary can be retrieved using its name value.
      /// </summary>
      public Dictionary() {
         this.map = new Table();
      }
      /// <summary>
      /// This method is used to add the provided entry to this set. If
      /// an entry of the same name already existed within the set then
      /// it is replaced with the specified <c>Entry</c> object.
      /// </summary>
      /// <param name="item">
      /// this is the entry object that is to be inserted
      /// </param>
      public override void Add(T item) {
         map.Add(item.Name, item);
      }
      /// <summary>
      /// This returns the number of <c>Entry</c> objects within
      /// the dictionary. This will use the internal map to acquire the
      /// number of entry objects that have been inserted to the map.
      /// </summary>
      /// <returns>
      /// this returns the number of entry objects in the set
      /// </returns>
      public override int Count {
         get {
            return map.Count;
         }
      }
      /// <summary>
      /// Returns an iterator of <c>Entry</c> objects which can be
      /// used to remove items from this set. This will use the internal
      /// map object and return the iterator for the map values.
      /// </summary>
      /// <returns>
      /// this returns an iterator for the entry objects
      /// </returns>
      public override IEnumerator<T> GetEnumerator() {
         return map.Values.GetEnumerator();
      }
      /// <summary>
      /// This is used to acquire an <c>Entry</c> from the set by
      /// its name. This uses the internal map to look for the entry, if
      /// the entry exists it is returned, if not this returns null.
      /// </summary>
      /// <param name="name">
      /// this is the name of the entry object to retrieve
      /// </param>
      /// <returns>
      /// this returns the entry mapped to the specified name
      /// </returns>
      public T this[String name] {
         get {
            return map[name];
         }
      }
      /// <summary>
      /// This is used to remove an <c>Entry</c> from the set by
      /// its name. This uses the internal map to look for the entry, if
      /// the entry exists it is returned and removed from the map.
      /// </summary>
      /// <param name="name">
      /// this is the name of the entry object to remove
      /// </param>
      /// <returns>
      /// this returns the entry mapped to the specified name
      /// </returns>
      public T Remove(String name) {
         T value = map[name];

         if(value != null) {
            map.Remove(name);
         }
         return value;
      }
      /// <summary>
      /// The <c>Table</c> object is used to represent a map of
      /// entries mapped to a string name. Each implementation of the
      /// entry must contain a name attribute, which is used to insert
      /// the entry into the map. This acts as a typedef.
      /// </summary>
      /// <seealso>
      /// SimpleFramework.Xml.Util.Entry
      /// </seealso>
      private class Table : SortedDictionary<String, T> {
         /// <summary>
         /// Constructor for the <c>Table</c> object. This will
         /// create a map that is used to store the entry objects that
         /// are serialized and deserialized to and from an XML source.
         /// </summary>
         public Table() {
         }
      }
   }
}
