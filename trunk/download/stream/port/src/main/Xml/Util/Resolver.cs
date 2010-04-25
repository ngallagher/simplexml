#region License
//
// Resolver.cs February 2001
//
// Copyright (C) 2001, Niall Gallagher <niallg@users.sf.net>
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
   /// This is used to store <c>Match</c> objects, which can then be
   /// retrieved using a string by comparing that string to the pattern of
   /// the <c>Match</c> objects. Patterns consist of characters
   /// with either the '*' or '?' characters as wild characters. The '*'
   /// character is completely wild meaning that is will match nothing or
   /// a long sequence of characters. The '?' character matches a single
   /// character.
   /// <p>
   /// If the '?' character immediately follows the '*' character then the
   /// match is made as any sequence of characters up to the first match
   /// of the next character. For example "/*?/index.jsp" will match all
   /// files preceeded by only a single path. So "/pub/index.jsp" will
   /// match, however "/pub/bin/index.jsp" will not, as it has two paths.
   /// So, in effect the '*?' sequence will match anything or nothing up
   /// to the first occurence of the next character in the pattern.
   /// <p>
   /// A design goal of the <c>Resolver</c> was to make it capable
   /// of  high performance. In order to achieve a high performance the
   /// <c>Resolver</c> can cache the resolutions it makes so that if
   /// the same text is given to the <c>Resolver.resolve</c> method
   /// a cached result can be retrived quickly which will decrease the
   /// length of time and work required to perform the match.
   /// <p>
   /// The semantics of the resolver are such that the last pattern added
   /// with a wild string is the first one checked for a match. This means
   /// that if a sequence of insertions like <c>Add(x)</c> followed
   /// by <c>Add(y)</c> is made, then a <c>Resolve(z)</c> will
   /// result in a comparison to y first and then x, if z matches y then
   /// it is given as the result and if z does not match y and matches x
   /// then x is returned, remember if z matches both x and y then y will
   /// be the result due to the fact that is was the last pattern added.
   /// </summary>
   public class Resolver<T> : List<T> where T : Match {
      /// <summary>
      /// Caches the text resolutions made to reduce the work required.
      /// </summary>
      private readonly Cache cache;
      /// <summary>
      /// Stores the matches added to the resolver in resolution order.
      /// </summary>
      private readonly Stack stack;
      /// <summary>
      /// The default constructor will create a <c>Resolver</c>
      /// without a large cache size. This is intended for use when
      /// the requests for <c>resolve</c> tend to use strings
      /// that are reasonably similar. If the strings issued to this
      /// instance are dramatically different then the cache tends
      /// to be an overhead rather than a bonus.
      /// </summary>
      public Resolver(){
         this.stack = new Stack(this);
         this.cache = new Cache(this);
      }
      /// <summary>
      /// This will search the patterns in this <c>Resolver</c> to
      /// see if there is a pattern in it that matches the string given.
      /// This will search the patterns from the last entered pattern to
      /// the first entered. So that the last entered patterns are the
      /// most searched patterns and will resolve it first if it matches.
      /// </summary>
      /// <param name="text">
      /// this is the string that is to be matched by this
      /// </param>
      /// <returns>
      /// this will return the first match within the resolver
      /// </returns>
      public T Resolve(String text) {
         List<T> list = cache[text];

         if(list == null) {
            list = ResolveAll(text);
         }
         if(list.Count == 0) {
            return default(T);
         }
         return list[0];
      }
      /// <summary>
      /// This will search the patterns in this <c>Resolver</c> to
      /// see if there is a pattern in it that matches the string given.
      /// This will search the patterns from the last entered pattern to
      /// the first entered. So that the last entered patterns are the
      /// most searched patterns and will resolve it first if it matches.
      /// </summary>
      /// <param name="text">
      /// this is the string that is to be matched by this
      /// </param>
      /// <returns>
      /// this will return all of the matches within the resolver
      /// </returns>
      public List<T> ResolveAll(String text) {
         List<T> list = cache[text];

         if(list != null) {
            return list;
         }
         char[] array = text.ToCharArray();

         if(array == null) {
            return null;
         }
         return ResolveAll(text, array);
      }
      /// <summary>
      /// This will search the patterns in this <c>Resolver</c> to
      /// see if there is a pattern in it that matches the string given.
      /// This will search the patterns from the last entered pattern to
      /// the first entered. So that the last entered patterns are the
      /// most searched patterns and will resolve it first if it matches.
      /// </summary>
      /// <param name="text">
      /// this is the string that is to be matched by this
      /// </param>
      /// <param name="array">
      /// this is the character array of the text string
      /// </param>
      /// <returns>
      /// this will return all of the matches within the resolver
      /// </returns>
      public List<T> ResolveAll(String text, char[] array) {
         List<T> list = new List<T>();

         foreach(T match in stack) {
            String wild = match.Pattern;

            if(Match(array, wild.ToCharArray())){
               cache[text] = list;
               list.Add(match);
            }
         }
         return list;
      }
      /// <summary>
      /// This inserts the <c>Match</c> implementation into the set
      /// so that it can be used for resolutions. The last added match is
      /// the first resolved. Because this changes the state of the
      /// resolver this clears the cache as it may affect resolutions.
      /// </summary>
      /// <param name="match">
      /// this is the match that is to be inserted to this
      /// </param>
      /// <returns>
      /// returns true if the addition succeeded, always true
      /// </returns>
      public bool Add(T match) {
         stack.Push(match);
         return true;
      }
      /// <summary>
      /// This returns an <c>Iterator</c> that iterates over the
      /// matches in insertion order. So the first match added is the
      /// first retrieved from the <c>Iterator</c>. This order is
      /// used to ensure that resolver can be serialized properly.
      /// </summary>
      /// <returns>
      /// returns an iterator for the sequence of insertion
      /// </returns>
      public Iterator<M> Iterator() {
         return stack.Sequence();
      }
      /// <summary>
      /// This is used to remove the <c>Match</c> implementation
      /// from the resolver. This clears the cache as the removal of
      /// a match may affect the resoultions existing in the cache. The
      /// <c>equals</c> method of the match must be implemented.
      /// </summary>
      /// <param name="match">
      /// this is the match that is to be removed
      /// </param>
      /// <returns>
      /// true of the removal of the match was successful
      /// </returns>
      public bool Remove(M match) {
         cache.Clear();
         return stack.Remove(match);
      }
      /// <summary>
      /// Returns the number of matches that have been inserted into
      /// the <c>Resolver</c>. Although this is a set, it does
      /// not mean that matches cannot used the same pattern string.
      /// </summary>
      /// <returns>
      /// this returns the number of matches within the set
      /// </returns>
      public int Size() {
         return stack.Size();
      }
      /// <summary>
      /// This is used to clear all matches from the set. This ensures
      /// that the resolver contains no matches and that the resolution
      /// cache is cleared. This is used to that the set can be reused
      /// and have new pattern matches inserted into it for resolution.
      /// </summary>
      public void Clear() {
         cache.Clear();
         stack.Clear();
      }
      /// <summary>
      /// This acts as a driver to the <c>match</c> method so that
      /// the offsets can be used as zeros for the start of matching for
      /// the <c>Match(char[],int,char[],int)</c>. method. This is
      /// also used as the initializing driver for the recursive method.
      /// </summary>
      /// <param name="text">
      /// this is the buffer that is to be resolved
      /// </param>
      /// <param name="wild">
      /// this is the pattern that will be used
      /// </param>
      public bool Match(char[] text, char[] wild) {
         return Match(text, 0, wild, 0);
      }
      /// <summary>
      /// This will be used to check to see if a certain buffer matches
      /// the pattern if it does then it returns <c>true</c>. This
      /// is a recursive method that will attempt to match the buffers
      /// based on the wild characters '?' and '*'. If there is a match
      /// then this returns <c>true</c>.
      /// </summary>
      /// <param name="text">
      /// this is the buffer that is to be resolved
      /// </param>
      /// <param name="off">
      /// this is the read offset for the text buffer
      /// </param>
      /// <param name="wild">
      /// this is the pattern that will be used
      /// </param>
      /// <param name="pos">
      /// this is the read offset for the wild buffer
      /// </param>
      public bool Match(char[] text, int off, char[] wild, int pos) {
         while(pos < wild.length && off < text.length){ /* examine chars */
            if(wild[pos] == '*'){
               while(wild[pos] == '*'){ /* totally wild */
                  if(++pos >= wild.length) /* if finished */
                     return true;
               }
               if(wild[pos] == '?') { /* *? is special */
                  if(++pos >= wild.length)
                     return true;
               }
               for(; off < text.length; off++){ /* find next matching char */
                  if(text[off] == wild[pos] || wild[pos] == '?'){ /* match */
                     if(wild[pos - 1] != '?'){
                        if(Match(text, off, wild, pos))
                           return true;
                     } else {
                        break;
                     }
                  }
               }
               if(text.length == off)
                  return false;
            }
            if(text[off++] != wild[pos++]){
               if(wild[pos-1] != '?')
                  return false; /* if not equal */
            }
         }
         if(wild.length == pos){ /* if wild is finished */
             return text.length == off; /* is text finished */
         }
         while(wild[pos] == '*'){ /* ends in all stars */
            if(++pos >= wild.length) /* if finished */
               return true;
         }
         return false;
      }
      /// <summary>
      /// This is used to cache resolutions made so that the matches can
      /// be acquired the next time without performing the resolution.
      /// This is an LRU cache so regardless of the number of resolutions
      /// made this will not result in a memory leak for the resolver.
      /// </summary>
      private class Cache : SortedDictionary<String, List<T>> {
         /// <summary>
         /// The count used to acquire objects from the stack.
         /// </summary>
         private int capacity;
         /// <summary>
         /// Constructor for the <c>Cache</c> object. This is a
         /// constructor that creates the linked hash map such that
         /// it will purge the entries that are oldest within the map.
         /// </summary>
         public Cache(Resolver<T> resolver) {
            this.capacity = resolver.Capacity;
         }
      }
      /// <summary>
      /// This is used to store the <c>Match</c> implementations in
      /// resolution order. Resolving the match objects is performed so
      /// that the last inserted match object is the first used in the
      /// resolution process. This gives priority to the last inserted.
      /// </summary>
      private class Stack : List<T> {
         private Resolver<T> resolver;
         public Stack(Resolver<T> resolver) {
            this.resolver = resolver;
         }
         /// <summary>
         /// The <c>push</c> method is used to push the match to
         /// the top of the stack. This also ensures that the cache is
         /// cleared so the semantics of the resolver are not affected.
         /// </summary>
         /// <param name="match">
         /// this is the match to be inserted to the stack
         /// </param>
         public void Push(T match) {
            resolver.cache.Clear();
            Add(match);
         }
         /// <summary>
         /// The <c>purge</c> method is used to purge a match from
         /// the provided position. This also ensures that the cache is
         /// cleared so that the semantics of the resolver do not change.
         /// </summary>
         /// <param name="index">
         /// the index of the match that is to be removed
         /// </param>
         public void Purge(int index) {
            resolver.cache.Clear();
            RemoveAt(index);
         }
         /// <summary>
         /// This is returned from the <c>Resolver.iterator</c> so
         /// that matches can be iterated in insertion order. When a
         /// match is removed from this iterator then it clears the cache
         /// and removed the match from the <c>Stack</c> object.
         /// </summary>
         /// <returns>
         /// returns an iterator to iterate in insertion order
         /// </returns>
         public IEnumerator<T> Sequence() {
            return new Sequence(resolver);
         }
         /// <summary>
         /// The is used to order the <c>Match</c> objects in the
         /// insertion order. Iterating in insertion order allows the
         /// resolver object to be serialized and deserialized to and
         /// from an XML document without disruption resolution order.
         /// </summary>
         private class Sequence : IEnumerator<T> {
            /// <summary>
            /// The cursor used to acquire objects from the stack.
            /// </summary>
            private int cursor;
            /// <summary>
            /// Constructor for the <c>Sequence</c> object. This is
            /// used to position the cursor at the end of the list so the
            /// first inserted match is the first returned from this.
            /// </summary>
            public Sequence(Resolver<T> resolver) {
               this.cursor = resolver.Count;
            }
            /// <summary>
            /// This returns the <c>Match</c> object at the cursor
            /// position. If the cursor has reached the start of the
            /// list then this returns null instead of the first match.
            /// </summary>
            /// <returns>
            /// this returns the match from the cursor position
            /// </returns>
            public M Next() {
               if(HasNext()) {
                   return get(--cursor);
               }
               return null;
            }
            /// <summary>
            /// This is used to determine if the cursor has reached the
            /// start of the list. When the cursor reaches the start of
            /// the list then this method returns false.
            /// </summary>
            /// <returns>
            /// this returns true if there are more matches left
            /// </returns>
            public bool HasNext() {
               return cursor > 0;
            }
            /// <summary>
            /// Removes the match from the cursor position. This also
            /// ensures that the cache is cleared so that resolutions
            /// made before the removal do not affect the semantics.
            /// </summary>
            public void Remove() {
               Purge(cursor);
            }
         }
      }
   }
}
