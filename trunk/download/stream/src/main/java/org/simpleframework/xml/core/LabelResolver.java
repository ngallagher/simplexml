/*
 * LabelResolver.java July 2011
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

/**
 * The <code>LabelResolver</code> object is used to resolve labels
 * based on a provided parameter. Resolution of labels is done using
 * a set of mappings based on the names of the labels. Because many
 * labels might have the same name, this will only map a label if it
 * has unique name. As well as resolving by name, this can resolve
 * using the path of the parameter.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.StructureBuilder
 */
class LabelResolver {

   /**
    * This is used to maintain the mappings for the attribute labels.
    */
   private final LabelMap attributes;
   
   /**
    * This is used to maintain the mappings for the element labels.
    */
   private final LabelMap elements;
   
   /**
    * This is used to maintain the mappings for the text labels.
    */
   private final LabelMap texts;
   
   /**
    * Constructor for the <code>LabelResolver</code> object. This 
    * is used to create an object that can resolve a label using a
    * parameter. Resolution is performed using either the name of
    * the parameter or the path of the parameter.
    */
   public LabelResolver() {
      this.attributes = new LabelMap();
      this.elements = new LabelMap();
      this.texts = new LabelMap();
   }
   
   /**
    * This <code>register</code> method is used to register a label
    * based on its name and path. Registration like this is done
    * to ensure that the label can be resolved based on a parameter
    * name or path. 
    * 
    * @param label this is the label that is to be registered
    */
   public void register(Label label) throws Exception {
      if(label.isAttribute()) {
         register(label, attributes);
      } else if(label.isText()){
         register(label, texts);
      } else {
         register(label, elements);
      }
   }
   
   /**
    * This <code>register</code> method is used to register a label
    * based on its name and path. Registration like this is done
    * to ensure that the label can be resolved based on a parameter
    * name or path. 
    * 
    * @param label this is the label that is to be registered
    * @param map this is the map that the label is registered with
    */
   private void register(Label label, LabelMap map) throws Exception {
      String name = label.getName();
      String path = label.getPath();
      
      if(map.containsKey(name)) {
         map.put(name, null);
      } else {
         map.put(name, label);
      }
      map.put(path, label);
   }
   
   /**
    * This <code>resolve</code> method is used to find a label based
    * on the name and path of the provided parameter. If it can not
    * be found then this will return null.
    * 
    * @param parameter this is the parameter used for resolution
    * 
    * @return the label that has been resolved, or null
    */
   public Label resolve(Parameter parameter) throws Exception {
      if(parameter.isAttribute()) {
         return resolve(parameter, attributes);
      } else if(parameter.isText()){
         return resolve(parameter, texts);
      }
      return resolve(parameter, elements);
 
   }
   
   /**
    * This <code>resolve</code> method is used to find a label based
    * on the name and path of the provided parameter. If it can not
    * be found then this will return null.
    * 
    * @param parameter this is the parameter used for resolution
    * @param map this is the map that is used for resolution
    * 
    * @return the label that has been resolved, or null
    */
   private Label resolve(Parameter parameter, LabelMap map) throws Exception {
      String name = parameter.getName();
      String path = parameter.getPath();
      Label label = map.get(path);
      
      if(label == null) {
         return map.get(name);
      }
      return label;
   }  
}