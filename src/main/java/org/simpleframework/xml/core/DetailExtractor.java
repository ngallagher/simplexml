/*
 * DetailExtractor.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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

import org.simpleframework.xml.util.Cache;
import org.simpleframework.xml.util.ConcurrentCache;

/**
 * 
 * @author Niall Gallagher
 */
class DetailExtractor {
   
   private final Cache<ContactList> methods;
   private final Cache<ContactList> fields;
   private final Cache<Detail> details;
   private final Support support;
   
   public DetailExtractor(Support support) {
      this.methods = new ConcurrentCache<ContactList>();
      this.fields = new ConcurrentCache<ContactList>();
      this.details = new ConcurrentCache<Detail>();
      this.support = support;
   }
   
   public Detail getDetail(Class type) throws Exception {
      Detail detail = details.fetch(type);
      
      if(detail == null) {
         detail = new DetailScanner(type);
         details.cache(type,  detail);
      }
      return detail;
   }
   
   public ContactList getFields(Class type) throws Exception {
      ContactList list = fields.fetch(type);
      
      if(list == null) {
         Detail detail = getDetail(type);
         
         if(detail != null) {
            list = getFields(detail);
         }
      }
      return list;
   }
   
   private ContactList getFields(Detail detail) throws Exception {
      ContactList list = new FieldScanner(detail, support);
      
      if(detail != null) {
         fields.cache(detail, list);
      }
      return list;
   }
   
   public ContactList getMethods(Class type) throws Exception {
      ContactList list = methods.fetch(type);
      
      if(list == null) {
         Detail detail = getDetail(type);
         
         if(detail != null) {
            list = getMethods(detail);
         }
      }
      return list;
   }
   
   private ContactList getMethods(Detail detail) throws Exception {
      ContactList list = new MethodScanner(detail, support);
      
      if(detail != null) {
         methods.cache(detail, list);
      }
      return list;
   }
}
