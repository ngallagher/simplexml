/*
 * Collector.java December 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General 
 * Public License along with this library; if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
 * Boston, MA  02111-1307  USA
 */

package org.simpleframework.xml.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The <code>Collector</code> object is used to store variables for
 * a deserialized object. Each variable contains the label and value
 * for a field or method. The <code>Composite</code> object uses
 * this to store deserialized values before committing them to the
 * objects methods and fields. 
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Composite
 */
class Collector implements Criteria {
   
   /**
    * This is the registry containing all the variables collected.
    */
   private final Registry registry;
   
   /**
    * This is the context object used by the serialization process.
    */
   private final Context context;
   
   /**
    * Constructor for the <code>Collector</code> object. This is 
    * used to store variables for an objects fields and methods.
    * Each variable is stored using the name of the label.
    * 
    * @param context this is the context for the deserialization
    */
   public Collector(Context context) {
      this.registry = new Registry();
      this.context = context;
   }
   
   /**
    * This is used to get the <code>Variable</code> that represents
    * a deserialized object. The variable contains all the meta
    * data for the field or method and the value that is to be set
    * on the method or field.
    * 
    * @param name this is the name of the variable to be acquired
    * 
    * @return this returns the named variable if it exists
    */
   public Variable get(String name) {
      return registry.get(name);
   } 
   
   /**
    * This is used to remove the <code>Variable</code> from this
    * criteria object. When removed, the variable will no longer be
    * used to set the method or field when the <code>commit</code>
    * method is invoked.
    * 
    * @param name this is the name of the variable to be removed
    * 
    * @return this returns the named variable if it exists
    */
   public Variable remove(String name) {
      return registry.remove(name);
   }
   
   /**
    * This is used to acquire an iterator over the named variables.
    * Providing an <code>Iterator</code> allows the criteria to be
    * used in a for each loop. This is primarily for convenience.
    * 
    * @return this returns an iterator of all the variable names
    */
   public Iterator<String> iterator() {
      return registry.iterator();
   }
   
   /**
    * This is used to create a <code>Variable</code> and set it for
    * this criteria. The variable can be retrieved at a later stage
    * using the name of the label. This allows for repeat reads as
    * the variable can be used to acquire the labels converter.
    * 
    * @param label this is the label used to create the pointer
    * @param value this is the value of the object to be read
    */
   public void set(Label label, Object value) throws Exception {
      Variable pointer = new Variable(label, value);

      if(label != null) {
         String name = label.getName(context);     
         registry.put(name, pointer);
      }
   }
   
   /**
    * This is used to set the values for the methods and fields of
    * the specified object. Invoking this performs the population
    * of an object being deserialized. It ensures that each value 
    * is set after the XML element has been fully read.
    * 
    * @param source this is the object that is to be populated
    */
   public void commit(Object source) throws Exception {
      Collection<Variable> set = registry.values();
      
      for(Variable entry : set) { 
         Contact contact = entry.getContact();
         Object value = entry.getValue();

         contact.set(source, value);
      }
   }  
   
   /**
    * The <code>Registry</code> object is used to store variables 
    * for the collector. All variables are stored under its name so
    * that they can be later retrieved and used to populate the
    * object when deserialization of all variables has finished.
    * 
    * @author Niall Gallagher
    */
   private class Registry extends HashMap<String, Variable> {
      
      /**
       * This is used to iterate over the names of the variables
       * in the registry. This is primarily used for convenience
       * so that the variables can be acquired in a for each loop.
       * 
       * @return an iterator containing the names of the variables
       */
      public Iterator<String> iterator() {
         return keySet().iterator();
      }
   }
}