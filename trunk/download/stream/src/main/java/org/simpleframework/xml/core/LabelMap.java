/*
 * LabelMap.java July 2006
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

import java.util.LinkedHashMap;
import java.util.Iterator;

/**
 * The <code>LabelMap</code> object represents a map that contains 
 * string label mappings. This is used for convenience as a typedef
 * like construct to avoid having declare the generic type whenever
 * it is referenced. Also this allows <code>Label</code> values 
 * from the map to be iterated within for each loops.
 * 
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.core.Label
 */
class LabelMap extends LinkedHashMap<String, Label> implements Iterable<Label> { 
   
   /**
    * This is policy used to determine the type of mappings used.
    */        
   private final Policy policy;
        
   /**
    * Constructor for the <code>LabelMap</code> object is used to 
    * create an empty map. This is used for convenience as a typedef
    * like construct which avoids having to use the generic type.
    */ 
   public LabelMap() {
      this(null);
   }
   
   /**
    * Constructor for the <code>LabelMap</code> object is used to 
    * create an empty map. This is used for convenience as a typedef
    * like construct which avoids having to use the generic type.
    */ 
   public LabelMap(Policy policy) {
      this.policy = policy;
   }

   /**
    * This allows the <code>Label</code> objects within the label map
    * to be iterated within for each loops. This will provide all
    * remaining label objects within the map. The iteration order is
    * not maintained so label objects may be given in any sequence.
    *
    * @return this returns an iterator for existing label objects
    */ 
   public Iterator<Label> iterator() {
      return values().iterator();
   }

   /**
    * This performs a <code>remove</code> that will remove the label
    * from the map and return that label. This method allows the 
    * values within the map to be exclusively taken one at a time,
    * which enables the user to determine which labels remain.
    *
    * @param name this is the name of the element of attribute
    *
    * @return this is the label object representing the XML node
    */ 
   public Label take(String name) {
      return remove(name);    
   }

   /**
    * This method is used to clone the label map such that mappings
    * can be maintained in the original even if they are modified
    * in the clone. This is used to that the <code>Schema</code> can
    * remove mappings from the label map as they are visited. 
    *
    * @param context this is the context used to style the XML names
    *
    * @return this returns a cloned representation of this map
    */ 
   public LabelMap build(Context context) throws Exception {
      LabelMap clone = new LabelMap(policy);
      
      for(Label label : this) {
         if(label != null) {
            String name = label.getName(context);
            
            clone.put(name, label);
         }
      }         
      return clone;      
   }   

   /**
    * This method is used to determine whether strict mappings are
    * required. Strict mapping means that all labels in the class
    * schema must match the XML elements and attributes in the
    * source XML document. When strict mapping is disabled, then
    * XML elements and attributes that do not exist in the schema
    * class will be ignored without breaking the parser.
    * 
    * @param context this is used to determine if this is strict
    *
    * @return true if strict parsing is enabled, false otherwise
    */ 
   public boolean isStrict(Context context) {
      if(policy == null) {
         return context.isStrict();
      }
      return context.isStrict() && policy.isStrict();           
   }
}
