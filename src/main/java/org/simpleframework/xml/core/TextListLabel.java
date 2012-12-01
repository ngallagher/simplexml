/*
 * TextListLabel.java December 2012
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

import org.simpleframework.xml.strategy.Type;

class TextListLabel extends TemplateLabel {
   
   private final Label label;
   
   public TextListLabel(Label label) {
      this.label = label;
   }


   public Decorator getDecorator() throws Exception {
      return null;
   }


   public Type getType(Class type) throws Exception {
      return null;
   }


   public Label getLabel(Class type) throws Exception {
      return null;
   }


   public String[] getNames() throws Exception {
      return label.getNames();
   }


   public String[] getPaths() throws Exception {
      return label.getPaths();
   }


   public String getEmpty(Context context) throws Exception {
      return null;
   }


   // This will be an individual element list so each of the registered 
   // labels will be of a specific type...only the last one registered
   // will be used
   //
   // It seems to me that the composite list union is the way
   // to go, this knows that it is a composite, it only needs to
   // know it has text values and read them or write them...
   public Converter getConverter(Context context) throws Exception {
      Type dependent = getDependent();
      Class real = dependent.getType();
      Type type = getContact();
      
      //if(real == String.class) {
        return new TextList(context, type, label);
     // }
      
      //if(!context.isPrimitive(type)) {
      //   throw new TextException("Cannot use %s to represent %s", type, label);
     // }
      
      
      // Perhaps we should wrap this converter, and then for each of the values that
      // have been read we will strip off the text too....?????
      
      //
      // IDEA: here what we do is we return the CompositeListUnion converter object when a text converter
      // IDEA: is requested. The CompositeListUnion label will handle the text before we read the element.
      // IDEA: it will also through some means be used to read the final text from a section, when the
      // IDEA: section is finished we need to grab the CompsiteListUnion label and read the final bit of
      // IDEA: text to complete the list.
      //
     //return label.getConverter(context);
   }


   public String getName() throws Exception {
      return label.getName();
   }


   public String getPath() throws Exception {
      return label.getPath();
   }


   public Expression getExpression() throws Exception {
      return label.getExpression();
   }


   public Type getDependent() throws Exception {
      return label.getDependent();
   }


   public String getEntry() throws Exception {
      return label.getEntry();
   }


   public Object getKey() throws Exception {
      return label.getKey();
   }


   public Annotation getAnnotation() {
      return label.getAnnotation();
   }


   public Contact getContact() {
      return label.getContact();
   }


   public Class getType() {
      return label.getType();
   }


   public String getOverride() {
      return label.getOverride();
   }


   public boolean isData() {
      return label.isData();
   }


   public boolean isRequired() {
      return label.isRequired();
   }


   public boolean isCollection() {
      return true;
   }


   public boolean isInline() {
      return label.isInline();
   }


   public boolean isTextList() {
      return true;
   }
}
