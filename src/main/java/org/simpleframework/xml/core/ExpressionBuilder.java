/*
 * ExpressionBuilder.java November 2010
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

import java.util.HashMap;

/**
 * The <code>ExpressionBuilder</code> object is used build and cache
 * path expressions. The expressions provided by this must be valid
 * XPath expressions. However, only a subset of the full XPath 
 * syntax is supported. Because these expressions require parsing
 * they are cached internally. This improves performance as if a
 * path expression is declared several times it is parsed once.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.PathParser
 */
class ExpressionBuilder {
   
   /**
    * This is the cache of path expressions previously built.
    */
   private final Cache cache;
   
   /**
    * This is the type the expressions are being built for.
    */
   private final Class type;
   
   /**
    * Constructor for the <code>ExpressionBuilder</code>. This is
    * used to create a builder to cache frequently requested XPath
    * expressions. Such caching improves the overall performance.
    * 
    * @param type this is the type containing the expressions
    */
   public ExpressionBuilder(Class type) {
      this.cache = new Cache();
      this.type = type;
   }
   
   /**
    * This is used to create an <code>Expression</code> from the
    * provided path. If the path does not conform to the syntax
    * supported then an exception is thrown to indicate the error.
    * If the path was requested before, a cached instance is used.
    * 
    * @param path this is the XPath expression to be parsed
    * 
    * @return this returns the resulting expression object
    */
   public Expression build(String path) throws Exception {
      Expression expression = cache.get(path);
      
      if(expression == null) {
         return create(path);
      }
      return expression;
   }

   /**
    * This is used to create an <code>Expression</code> from the
    * provided path. If the path does not conform to the syntax
    * supported then an exception is thrown to indicate the error.
    * 
    * @param path this is the XPath expression to be parsed
    * 
    * @return this returns the resulting expression object
    */
   private Expression create(String path) throws Exception {
      Expression expression = new PathParser(type, path);
      
      if(cache != null) {
         cache.put(path, expression);
      }
      return expression;
   }

   /**
    * The <code>Cache</code> is used to cache the expressions with
    * the original path. This is effectively a typedef that hides
    * the ugly generics details from the class definition.
    * 
    * @author Niall Gallagher
    */
   private class Cache extends HashMap<String, Expression> {
      
      /**
       * Constructor for the <code>Cache</code> object. This will
       * create a cache for the path expressions that are built.
       * All path expressions are cached using the original path.
       */
      public Cache() {
         super();
      }
   }
}
