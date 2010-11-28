/*
 * ModelAssembler.java November 2010
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

import org.simpleframework.xml.Order;

/**
 * The <code>ModelAssembler</code> is used to assemble the model
 * using registrations based on the specified order. The order of
 * elements and attributes is specified by an <code>Order</code>
 * annotation. If such an annotations exists then it is used to
 * perform the initial registrations and thus establish order.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.Order
 */
class ModelAssembler {
  
   /**
    * This is used to parse the XPath expressions in the order
    */
   private ExpressionBuilder builder;   

   /**
    * Constructor for the <code>ModelAssembler</code> object. If
    * no order has been specified for the schema class then this
    * will perform no registrations on the specified model.   
    * 
    * @param builder this is the builder for XPath expressions
    */
   public ModelAssembler(ExpressionBuilder builder) throws Exception {
      this.builder = builder;     
   }
   
   /**
    * This is used to assemble the model by perform registrations
    * based on the <code>Order</code> annotation. The initial
    * registrations performed by this establish the element and
    * attribute order for serialization of the schema class.
    * 
    * @param model the model to perform registrations on
    * @param order this is the order specified by the class   
    */
   public void assemble(Model model, Order order) throws Exception { 
      assembleElements(model, order);
      assembleAttributes(model, order);
   }
   
   /**
    * This is used to assemble the model by perform registrations
    * based on the <code>Order</code> annotation. The initial
    * registrations performed by this establish the element and
    * attribute order for serialization of the schema class.
    * 
    * @param model the model to perform registrations on
    * @param order this is the order specified by the class   
    */
   private void assembleElements(Model model, Order order) throws Exception {
      for(String path : order.elements()) {
         Expression expression = builder.build(path);
         
         if(expression != null) {
            buildElements(model, expression);
         }
      }
   }
   
   /**
    * This is used to assemble the model by perform registrations
    * based on the <code>Order</code> annotation. The initial
    * registrations performed by this establish the element and
    * attribute order for serialization of the schema class.
    * 
    * @param model the model to perform registrations on
    * @param order this is the order specified by the class   
    */
   private void assembleAttributes(Model model, Order order) throws Exception {
      for(String path : order.attributes()) {
         Expression expression = builder.build(path);
         
         if(expression != null) {
            buildAttributes(model, expression);
         }
      }
   }
   
   /**
    * This is used to perform registrations using an expression.
    * Each segment in the expression will create a new model and
    * the final segment of the expression is the attribute.
    * 
    * @param model the model to register the attribute with
    * @param expression this is the expression to be evaluated
    */
   private void buildAttributes(Model model, Expression expression) throws Exception {
      String name = expression.getFirst();         
      Model next = model.lookup(name);
      
      if(expression.isPath()) {
         expression = expression.getPath(1);
         
         if(next == null) {
            throw new PathException("Element '%s' does not exist", name);
         }
         buildAttributes(next, expression);
      } else {         
         model.registerAttribute(name);
      }
   }
   
   /**
    * This is used to perform registrations using an expression.
    * Each segment in the expression will create a new model and
    * the final segment of the expression is the element.
    * 
    * @param model the model to register the element with
    * @param expression this is the expression to be evaluated
    */
   private void buildElements(Model model, Expression expression) throws Exception {
      String name = expression.getFirst();         
      Model next = model.register(name); 
      
      if(expression.isPath()) {
         expression = expression.getPath(1);
         buildElements(next, expression);
      }
      model.registerElement(name);      
   }   
}
