/*
 * History.java July 2007
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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

package org.simpleframework.xml.load;

import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import java.util.HashMap;

/**
 * The <code>History</code> object is used to store labels that have 
 * been read from an XML class schema. This allows certain elements
 * to be declared within an XML document several times, such that
 * it can be repeatedly read. Repeat reads are typically only used
 * by inline lists an maps so that they can be scattered and mixed
 * within the parent element. Repeat reads allow for a more liberal
 * means of writing the XML source. 
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.load.Converter
 */
class History extends HashMap<String, Converter>{
   
   /**
    * This is the source deserialization context for the store.
    */
   private Source root;
   
   /**
    * Constructor for the <code>History</code> object. This creates an
    * object that is used for storing converters via its label name.
    * All stored converters can then be used to repeat reads on a
    * given object, which is typically an inline list or map.
    *  
    * @param root this is the deserialization context for the store
    */
   public History(Source root) {
      this.root = root;
   }
   
   /**
    * This is used to save the label and object for a given element.
    * Storing the original deserialized 
    * object ensures that should
    * there be an additional element with the same name as the label
    * that those XML elements could be read in to the object again.
    * 
    * @param label this is the label object used for the converter
    * @param value this is the value that was originally read
    */
   public void save(Label label, Object value) throws Exception {
      Converter convert = label.getConverter(root);
      String name = label.getName();
      
      if(convert instanceof Repeater) {
         put(name, new Adapter(convert, value));
      }
   }

   /**
    * The <code>Adapter</code> object is used to call the repeater
    * with the original deserialized object. Using this object the
    * converter interface can be used to perform repeat reads for
    * the object. This must be given a <code>Repeater</code> in 
    * order to invoke the repeat read method.
    * 
    * @author Niall Gallagher
    */
   private class Adapter implements Converter {
      
      /**
       * This is the repeater object used to perform a repeat read.
       */
      private final Repeater repeater;
      
      /**
       * This is the originally deserialized object value to use.
       */
      private final Object value;
      
      /**
       * Constructor for the <code>Adapter</code> object. This will
       * create an adapter between the converter an repeater such
       * that the reads will read from the XML to the original.
       * 
       * @param convert this is the repeater object to be used      
       * @param value this is the originally deserialized object
       */
      public Adapter(Converter convert, Object value) {
         this.repeater = (Repeater) convert;
         this.value = value;         
      }
      
      /**
       * This <code>read</code> method will perform a read using the
       * provided object with the repeater. Reading with this method
       * ensures that any additional XML elements within the source
       * will be added to the value.
       * 
       *  @param node this is the node that contains the extra data
       *  
       *  @return this will return the original deserialized object
       */
      public Object read(InputNode node) throws Exception {
         return repeater.read(node, value);
      }
      
      /**
       * This <code>write</code> method acts like any other write
       * in that it passes on the node and source object to write.
       * Typically this will not be used as the repeater object is
       * used for repeat reads of scattered XML elements.
       * 
       * @param node this is the node to write the data to
       * @param source this is the source object to be written
       */
      public void write(OutputNode node, Object source) throws Exception {
         repeater.write(node, source);;         
      }
   }
}
