/*
 * ModelSection.java November 2010
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
import java.util.List;

import org.simpleframework.xml.stream.Style;

/**
 * The <code>ModelSection</code> represents a section that is backed
 * by a <code>Model</code> instance. This is used to expose the XML
 * structure of a schema class. In addition to wrapping the model
 * this will also apply a <code>Style</code> to the names of the
 * attributes and elements of the class schema. 
 * 
 * @author Niall Gallagher
 */
class ModelSection implements Section { 

   /**
    * Represents a mapping between styled names and attributes.
    */
   private LabelMap attributes;
   
   /**
    * Represents a mapping between styled names and elements.
    */
   private LabelMap elements;
   
   /**
    * Represents a mapping between styled names and models.
    */
   private ModelMap models;
   
   /**
    * This is the context that contains the style for names.
    */
   private Context context;
   
   /**
    * This is the style that is used to convert element names.
    */
   private Style style;
   
   /**
    * This is the model that contains the elements and attributes.
    */
   private Model model;
   
   /**
    * Constructor for the <code>ModelSection</code> object. This is
    * used to wrap a <code>Model</code> in such a way that it can
    * not be modified. This allows it to be used concurrently.
    * 
    * @param context this is the context used by this section
    * @param model this is the model this section will wrap
    */
   public ModelSection(Context context, Model model) {
      this.style = context.getStyle();
      this.context = context;
      this.model = model;
   }
   
   /**
    * This is used to return the name of the section. The name is 
    * must be a valid XML element name. It is used when a style
    * is applied to a path as the section name must be styled.
    * 
    * @return this returns the name of this section instance
    */
   public String getName() {
      return model.getName();
   }
   
   /**
    * This is used to acquire the path prefix for the section. The
    * path prefix is used when the section is transformed in to an
    * XML structure. This ensures that the XML element created to
    * represent the section contains the optional prefix.
    * 
    * @return this returns the prefix for this section
    */
   public String getPrefix() {
      return model.getPrefix();
   }
   
   /**
    * This will return the names of all elements contained within
    * the model. This includes the names of all XML elements that
    * have been registered as well as any other models that have
    * been added. Iteration is done in an ordered manner, according
    * to the registration of elements and models.
    * 
    * @return an ordered and styled list of elements and models
    */
   public Iterator<String> iterator() {
      List<String> list = new ArrayList<String>();
      
      for(String element : model) {
         String name = style.getElement(element);
         
         if(name != null) {
            list.add(name);
         }
      }
      return list.iterator();
   }
   
   /**
    * To differentiate between a section and an element this can be
    * used. When iterating over the elements within the section the
    * names of both elements and sections are provided. So in order
    * to determine how to interpret the structure this can be used.
    * 
    * @param name this is the name of the element to be determined
    * 
    * @return this returns true if the name represents a section
    */   
   public boolean isSection(String name) throws Exception {      
      return getModels().get(name) != null;
   }
   
   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields and methods marked with XML annotations. All of the
    * attribute annotations are considered and gathered by name in 
    * this map. Also, if there is an associated <code>Style</code> 
    * for serialization the attribute names are renamed with this.
    * 
    * @return returns the attributes associated with this section
    */
   public ModelMap getModels() throws Exception {
      if(models == null) {
         models = model.buildModels(context);
      }
      return models;
   }

   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields and methods marked with XML annotations. All of the
    * attribute annotations are considered and gathered by name in 
    * this map. Also, if there is an associated <code>Style</code> 
    * for serialization the attribute names are renamed with this.
    * 
    * @return returns the attributes associated with this section
    */
   public LabelMap getAttributes() throws Exception {
      if(attributes == null) {
         attributes = model.buildAttributes(context);
      }
      return attributes;
   }

   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields and methods marked with XML annotations. All of the
    * element annotations are considered and gathered by name in 
    * this map. Also, if there is an associated <code>Style</code> 
    * for serialization the element names are renamed with this.
    * 
    * @return returns the elements associated with this section
    */
   public LabelMap getElements() throws Exception {
      if(elements == null) {
         elements = model.buildElements(context);
      }
      return elements;
   }
   
   /**
    * Returns the named element as a <code>Label</code> object.
    * For convenience this method is provided so that when iterating
    * over the names of the elements in the section a specific one
    * of interest can be acquired. 
    * <p>
    * To ensure that elements of the same name are not referenced
    * more than once this will remove the element once acquired. 
    * This ensures that they are visited only once in serialization.
    * 
    * @param name the name of the element that is to be acquired
    * 
    * @return this returns the label associated with the name
    */
   public Label getElement(String name) throws Exception {
      return getElements().take(name);
   }

   /**
    * Returns the named section as a <code>Section</code> object.
    * For convenience this method is provided so that when iterating
    * over the names of the elements in the section a specific one
    * of interest can be acquired. 
    * <p>
    * To ensure that models of the same name are not referenced
    * more than once this will remove the model once acquired. 
    * This ensures that they are visited only once in serialization.
    * 
    * @param name the name of the element that is to be acquired
    * 
    * @return this returns the section associated with the name
    */
   public Section getSection(String name) throws Exception {
      ModelMap map = getModels();
      ModelList list = map.get(name);      
      
      if(list != null) {
         Model model = list.take();
         
         if(model != null){
            return new ModelSection(context, model);
         }
      }
      return null;
   }
}
