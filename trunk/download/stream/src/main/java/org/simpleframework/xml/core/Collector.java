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
 * The <code>Collector</code> object is used to store pointers for
 * a deserialized object. Each pointer contains the label and value
 * for a field or method. The <code>Composite</code> object uses
 * this to store deserialized values before committing them to the
 * objects methods and fields. 
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Composite
 */
class Collector implements Criteria {
   
   private final Registry registry;
   
   /**
    * This is the context object used by the serialization process.
    */
   private final Context context;
   
   /**
    * Constructor for the <code>Collector</code> object. This is 
    * used to store pointers to an objects fields and methods.
    * Each pointer is stored using the name of the label.
    */
   public Collector(Context context) {
      this.registry = new Registry();
      this.context = context;
   }
   
   public Variable get(String name) {
      return registry.get(name);
   }  

   public Variable remove(String name) {
      return registry.remove(name);
   }

   public Iterator<String> iterator() {
      return registry.iterator();
   }
   
   /**
    * This is used to create a <code>Pointer</code> and store it in
    * the collector. The pointer can be retrieved at a later stage
    * using the name of the label. This allows for repeat reads as
    * the pointer can be used to acquire the labels converter.
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
   
   
   private class Registry extends HashMap<String, Variable> {
      
      public Iterator<String> iterator() {
         return keySet().iterator();
      }
   }
}