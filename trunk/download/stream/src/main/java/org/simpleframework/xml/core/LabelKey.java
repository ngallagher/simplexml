/*
 * LabelKey.java April 2012
 *
 * Copyright (C) 2012, Niall Gallagher <niallg@users.sf.net>
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

import java.lang.annotation.Annotation;

class LabelKey {

   private final Class label;
   private final Class owner;
   private final Class type;
   private final String name;
   
   public LabelKey(Contact contact, Annotation label) {
      this.owner = contact.getDeclaringClass();
      this.label = label.annotationType();
      this.name = contact.getName();
      this.type = contact.getType();    
   }
   
   public int hashCode() {
      return name.hashCode() ^ owner.hashCode();
   }
   
   public boolean equals(Object value) {
      if(value instanceof LabelKey) {
         return equals((LabelKey)value);
      }
      return false;
   }
   
   private boolean equals(LabelKey key) {
      if(key.label != label) {
         return false;
      }
      if(key.owner != owner) {
         return false;
      }
      if(key.type != type) {
         return false;
      }
      return key.name.equals(name);
   }
   
   public String toString() {
      return String.format("key '%s' for %s", name, owner);
   }
}
