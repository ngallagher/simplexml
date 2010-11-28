/*
 * PathParser.java November 2010
 *
 * Copyright (C) 2010, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The <code>PathParser</code> object is used to parse XPath paths.
 * This will parse a subset of the XPath expression syntax, such
 * that the path can be used to identify and navigate various XML
 * structures. Example paths that this can parse are as follows.
 * <pre>
 * 
 *    ./example/path
 *    ./example/path/
 *    example/path
 *    
 * </pre>
 * If the parsed path does not match an XPath expression similar to
 * the above then an exception is thrown. Once parsed the segments
 * of the path can be used to traverse data structures modelled on
 * an XML document or fragment.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.ExpressionBuilder
 */
class PathParser implements Expression {
   
   /**
    * This contains a list of the path segments that were parsed.
    */
   private LinkedList<String> list;
   
   /**
    * This is a cache of the canonical path representation.
    */
   private String path;
   
   /**
    * This is a copy of the source data that is to be parsed.
    */
   private char[] data;
   
   /**
    * This represents the number of characters in the source path.
    */
   private int count;
   
   /**
    * This is the start offset that skips any root references.
    */
   private int start;
 
   /**
    * This is the current seek position for the parser.
    */
   private int off;

   /**
    * Constructor for the <code>PathParser</code> object. This must
    * be given a valid XPath expression. Currently only a subset of
    * the XPath syntax is supported by this parser. Once finished
    * the parser will contain all the extracted path segments.
    * 
    * @param path this is the XPath expression to be parsed
    */
   public PathParser(String path) throws Exception {
      this.list = new LinkedList<String>();
      this.parse(path);
   }
   
   /**
    * This is used to determine if the expression is a path. An
    * expression represents a path if it contains more than one
    * segment. If only one segment exists it is an element name.
    * 
    * @return true if this contains more than one segment
    */
   public boolean isPath() {
      return list.size() > 1;
   }
   
   /**
    * This can be used to acquire the first path segment within
    * the expression. The first segment represents the parent XML
    * element of the path. All segments returned do not contain
    * any slashes and so represents the real element name.
    * 
    * @return this returns the parent element for the path
    */
   public String getFirst() {
      return list.getFirst();
   }
   
   /**
    * This can be used to acquire the last path segment within
    * the expression. The last segment represents the leaf XML
    * element of the path. All segments returned do not contain
    * any slashes and so represents the real element name.
    * 
    * @return this returns the leaf element for the path
    */ 
   public String getLast() {
      return list.getLast();
   }

   /**
    * This is used to iterate over the path segments that have 
    * been extracted from the source XPath expression. Iteration
    * over the segments is done in the order they were parsed
    * from the source path.
    * 
    * @return this returns an iterator for the path segments
    */
   public Iterator<String> iterator() {
      return list.iterator();
   }
   
   /**
    * This allows an expression to be extracted from the current
    * context. Extracting expressions in this manner makes it 
    * more convenient for navigating structures representing
    * the XML document. If an expression can not be extracted
    * with the given criteria an exception will be thrown.
    * 
    * @param from this is the number of segments to skip to
    * 
    * @return this returns an expression from this one
    */
   public Expression getPath(int from) {  
      return getPath(from, 0);
   }
   
   /**
    * This allows an expression to be extracted from the current
    * context. Extracting expressions in this manner makes it 
    * more convenient for navigating structures representing
    * the XML document. If an expression can not be extracted
    * with the given criteria an exception will be thrown.
    * 
    * @param from this is the number of segments to skip to
    * @param trim the number of segments to trim from the end
    * 
    * @return this returns an expression from this one
    */
   public Expression getPath(int from, int trim) {
      int last = list.size() - 1;
      
      if(last- trim >= from) {       
         return new PathSection(from, last -trim);
      }
      return new PathSection(from, from);
   }

   /**
    * This method is used to parse the provided XPath expression.
    * When parsing the expression this will trim any references
    * to the root context, also any trailing slashes are removed.
    * An exception is thrown if the path is invalid.
    * 
    * @param path this is the XPath expression to be parsed
    */
   private void parse(String path) throws Exception {
      if(path != null) {
         count = path.length();
         data = new char[count];
         path.getChars(0, count, data, 0);
      }
      path(path);
   }
   
   /**
    * This method is used to parse the provided XPath expression.
    * When parsing the expression this will trim any references
    * to the root context, also any trailing slashes are removed.
    * An exception is thrown if the path is invalid.
    * 
    * @param path this is the XPath expression to be parsed
    */ 
   private void path(String path) throws Exception {
      if (data[off] == '/') {
         throw new PathException("Path '%s' references document root", path);
      }
      if (data[off] == '.') {
         skip(path);
      }
      while (off < count) {
         segment(path);
      }
   }

   /**
    * This is used to skip any root prefix for the path. Skipping 
    * the root prefix ensures that it is not considered as a valid
    * path segment and so is not returned as part of the iterator
    * nor is it considered with building a string representation.
    * 
    * @param path this is the original path expression parsed
    */
   private void skip(String path) throws Exception {
      if (data.length > 1) {
         if (data[off + 1] != '/') {
            throw new PathException("Path '%s' has an illegal syntax", path);
         }
         off++;
      }      
      start = ++off;
   }

   /**
    * This method is used to extract a path segment from the source
    * expression. Before extracting the segment this validates the
    * input to ensure it represents a valid path. If the path is
    * not valid then this will thrown an exception.
    * 
    * @param path this is the original path expression parsed
    */
   private void segment(String path) throws Exception {
      int mark = off;

      if (data[off] == '/') {
         throw new PathException("Invalid path expression '%s'", path);
      }
      while (off < count) {
         if (data[off] == '/') {
            break;
         }
         off++;
      }
      append(mark, off++ - mark);
   }

   /**
    * This will add a path segment to the list of segments. A path
    * segment is added only if it has at least one character. All
    * segments can be iterated over when parsing has completed.
    * 
    * @param start this is the start offset for the path segment
    * @param count this is the number of characters in the segment
    */
   private void append(int start, int count) {
      String segment = new String(data, start, count);

      if (count > 0) {
         list.add(segment);
      }
   }
   
   /**
    * Provides a canonical XPath expression. This is used for both
    * debugging and reporting. The path returned represents the 
    * original path that has been parsed to form the expression.
    * 
    * @return this returns the string format for the XPath
    */
   public String toString() {
      int size = off - start;
      
      if(path == null) {
         return new String(data, start, size-1);
      }
      return path;
   } 
   
   /**
    * The <code>PathSection</code> represents a section of a path 
    * that is extracted. Providing a section allows the expression
    * to be broken up in to smaller parts without having to parse
    * the path again. This is used primarily for better performance.
    * 
    * @author Niall Gallagher
    */
   private class PathSection implements Expression {
      
      /**
       * This contains a cache of the path segments of the section.
       */
      private List<String> cache;
      
      /**
       * This contains a cache of the canonical path representation.
       */
      private String path;
      
      /**
       * This is the first section index for this path section.
       */
      private int begin;
      
      /**
       * This is the last section index for this path section.
       */
      private int end;
      
      /**
       * Constructor for the <code>PathSection</code> object. A path
       * section represents a section of an original path expression.
       * To create a section the first and last segment index needs
       * to be provided.
       * 
       * @param index this is the first path segment index 
       * @param end this is the last path segment index
       */
      public PathSection(int index, int end) {
         this.cache = new ArrayList<String>();
         this.begin = index;         
         this.end = end;
      }
      
      /**
       * This is used to determine if the expression is a path. An
       * expression represents a path if it contains more than one
       * segment. If only one segment exists it is an element name.
       * 
       * @return true if this contains more than one segment
       */
      public boolean isPath() {
         return end - begin >= 1;
      }
      
      /**
       * This can be used to acquire the first path segment within
       * the expression. The first segment represents the parent XML
       * element of the path. All segments returned do not contain
       * any slashes and so represents the real element name.
       * 
       * @return this returns the parent element for the path
       */
      public String getFirst() {
         return list.get(begin);
      }
      
      /**
       * This can be used to acquire the last path segment within
       * the expression. The last segment represents the leaf XML
       * element of the path. All segments returned do not contain
       * any slashes and so represents the real element name.
       * 
       * @return this returns the leaf element for the path
       */ 
      public String getLast() {
         return list.get(end);
      }
      
      /**
       * This allows an expression to be extracted from the current
       * context. Extracting expressions in this manner makes it 
       * more convenient for navigating structures representing
       * the XML document. If an expression can not be extracted
       * with the given criteria an exception will be thrown.
       * 
       * @param from this is the number of segments to skip to
       * 
       * @return this returns an expression from this one
       */
      public Expression getPath(int from) {     
         return getPath(from, 0);
      }
      
      /**
       * This allows an expression to be extracted from the current
       * context. Extracting expressions in this manner makes it 
       * more convenient for navigating structures representing
       * the XML document. If an expression can not be extracted
       * with the given criteria an exception will be thrown.
       * 
       * @param from this is the number of segments to skip to
       * @param trim the number of segments to trim from the end
       * 
       * @return this returns an expression from this one
       */
      public Expression getPath(int from, int trim) {
         return new PathSection(begin + from, end - trim);
      }
      
      /**
       * This is used to iterate over the path segments that have 
       * been extracted from the source XPath expression. Iteration
       * over the segments is done in the order they were parsed
       * from the source path.
       * 
       * @return this returns an iterator for the path segments
       */
      public Iterator<String> iterator() {
         if(cache.isEmpty()) {
            for(int i = begin; i <= end; i++) {
               String segment = list.get(i);
               
               if(segment != null) {
                  cache.add(segment);
               }
            }
         }
         return cache.iterator();         
      }      
      
      /**
       * Provides a canonical XPath expression. This is used for both
       * debugging and reporting. The path returned represents the 
       * original path that has been parsed to form the expression.
       * 
       * @return this returns the string format for the XPath
       */
      private String getPath() {
         int off = start;   
         int size = 0;
         int pos = 0;
         
         for(String name : list) {
            int count = name.length();
            
            if(pos < begin) {
               off += count + 1;
            } else if(pos <=  end){
               size += count + 1;
            }     
            pos++;
         }
         return new String(data, off, size - 1);
         
      }
      
      /**
       * Provides a canonical XPath expression. This is used for both
       * debugging and reporting. The path returned represents the 
       * original path that has been parsed to form the expression.
       * 
       * @return this returns the string format for the XPath
       */
      public String toString() {
         if(path == null) {
            path = getPath();
         }
         return path;
      }   
   }
}
