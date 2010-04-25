#region License
//
// Session.cs February 2005
//
// Copyright (C) 2005, Niall Gallagher <niallg@users.sf.net>
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
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Session</c> object represents a session with name
   /// value pairs. The persister uses this to allow objects to add
   /// or remove name value pairs to an from an internal map. This is
   /// done so that the deserialized objects can set template values
   /// as well as share information. In particular this is useful for
   /// any <c>Strategy</c> implementation as it allows it so
   /// store persistence state during the persistence process.
   /// <p>
   /// Another important reason for the session map is that it is
   /// used to wrap the map that is handed to objects during callback
   /// methods. This opens the possibility for those objects to grab
   /// a reference to the map, which will cause problems for any of
   /// the strategy implementations that wanted to use the session
   /// reference for weakly storing persistence artifacts.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.Strategy
   /// </seealso>
   sealed class Session : Dictionary {
      /// <summary>
      /// This is the internal map that provides storage for pairs.
      /// </summary>
      private readonly Dictionary map;
      /// <summary>
      /// Constructor for the <c>Session</c> object. This is
      /// used to create a new session that makes use of a hash map
      /// to store key value pairs which are maintained throughout
      /// the duration of the persistence process this is used in.
      /// </summary>
      public Session(){
         this.map = new HashMap();
      }
      /// <summary>
      /// This returns the inner map used by the session object. The
      /// internal map is the <c>Map</c> instance that is used
      /// for persister callbacks, a reference to this map can be
      /// safely made by any object receiving a callback.
      /// </summary>
      /// <returns>
      /// this returns the internal session map used
      /// </returns>
      public Dictionary Map {
         get {
            return map;
         }
      }
      //public Dictionary GetMap() {
      //   return map;
      //}
      /// This obviously enough provides the number of pairs that
      /// have been inserted into the internal map. This acts as
      /// a proxy method for the internal map <c>size</c>.
      /// </summary>
      /// <returns>
      /// this returns the number of pairs are available
      /// </returns>
      public int Size() {
         return map.Size();
      }
      /// <summary>
      /// This method is used to determine whether the session has
      /// any pairs available. If the <c>size</c> is zero then
      /// the session is empty and this returns true. The is acts as
      /// a proxy the the <c>isEmpty</c> of the internal map.
      /// </summary>
      /// <returns>
      /// this is true if there are no available pairs
      /// </returns>
      public bool IsEmpty() {
         return map.IsEmpty();
      }
      /// <summary>
      /// This is used to determine whether a value representing the
      /// name of a pair has been inserted into the internal map. The
      /// object passed into this method is typically a string which
      /// references a template variable but can be any object.
      /// </summary>
      /// <param name="name">
      /// this is the name of a pair within the map
      /// </param>
      /// <returns>
      /// this returns true if the pair of that name exists
      /// </returns>
      public bool ContainsKey(Object name) {
         return map.ContainsKey(name);
      }
      /// <summary>
      /// This method is used to determine whether any pair that has
      /// been inserted into the internal map had the presented value.
      /// If one or more pairs within the collected mappings contains
      /// the value provided then this method will return true.
      /// </summary>
      /// <param name="value">
      /// this is the value that is to be searched for
      /// </param>
      /// <returns>
      /// this returns true if any value is equal to this
      /// </returns>
      public bool ContainsValue(Object value) {
         return map.ContainsValue(value);
      }
      /// <summary>
      /// The <c>get</c> method is used to acquire the value for
      /// a named pair. So if a mapping for the specified name exists
      /// within the internal map the mapped entry value is returned.
      /// </summary>
      /// <param name="name">
      /// this is a name used to search for the value
      /// </param>
      /// <returns>
      /// this returns the value mapped to the given name
      /// </returns>
      public Object Get(Object name) {
         return map.Get(name);
      }
      /// <summary>
      /// The <c>put</c> method is used to insert the name and
      /// value provided into the internal session map. The inserted
      /// value will be available to all objects receiving callbacks.
      /// </summary>
      /// <param name="name">
      /// this is the name the value is mapped under
      /// </param>
      /// <param name="value">
      /// this is the value to mapped with the name
      /// </param>
      /// <returns>
      /// this returns the previous value if there was any
      /// </returns>
      public Object Put(Object name, Object value) {
         return map.Put(name, value);
      }
      /// <summary>
      /// The <c>remove</c> method is used to remove the named
      /// mapping from the internal session map. This ensures that
      /// the mapping is no longer available for persister callbacks.
      /// </summary>
      /// <param name="name">
      /// this is a string used to search for the value
      /// </param>
      /// <returns>
      /// this returns the value mapped to the given name
      /// </returns>
      public Object Remove(Object name) {
         return map.Remove(name);
      }
      /// <summary>
      /// This method is used to insert a collection of mappings into
      /// the session map. This is used when another source of pairs
      /// is required to populate the collection currently maintained
      /// within this sessions internal map. Any pairs that currently
      /// exist with similar names will be overwritten by this.
      /// </summary>
      /// <param name="data">
      /// this is the collection of pairs to be added
      /// </param>
      public void PutAll(Dictionary data) {
         map.PutAll(data);
      }
      /// <summary>
      /// This is used to acquire the names for all the pairs that
      /// have currently been collected by this session. This is used
      /// to determine which mappings are available within the map.
      /// </summary>
      /// <returns>
      /// the set of names for all mappings in the session
      /// </returns>
      public Set KeySet() {
         return map.KeySet();
      }
      /// <summary>
      /// This method is used to acquire the value for all pairs that
      /// have currently been collected by this session. This is used
      /// to determine the values that are available in the session.
      /// </summary>
      /// <returns>
      /// the list of values for all mappings in the session
      /// </returns>
      public Collection Values() {
         return map.Values();
      }
      /// <summary>
      /// This method is used to acquire the name and value pairs that
      /// have currently been collected by this session. This is used
      /// to determine which mappings are available within the session.
      /// </summary>
      /// <returns>
      /// thie set of mappings that exist within the session
      /// </returns>
      public Set EntrySet() {
         return map.EntrySet();
      }
      /// <summary>
      /// The <c>clear</c> method is used to wipe out all the
      /// currently existing pairs from the collection. This is used
      /// when all mappings within the session should be erased.
      /// </summary>
      public void Clear() {
         map.Clear();
      }
   }
}
