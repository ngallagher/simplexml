/*
 * WriteState.java April 2007
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

package simple.xml.graph;

import java.util.WeakHashMap;

/**
 * The <code>WriteState</code> object is used to store all graphs that
 * are currently been written with a given cycle strategy. The goal of
 * this object is to act as a temporary store for graphs such that 
 * when the persistence session has completed the write graph will be
 * garbage collected. This ensures that there are no lingering object
 * reference that could cause a memory leak. If a graph for the given
 * session key does not exist then a new one is created. 
 * 
 * @author Niall Gallagher
 * 
 * @see simple.xml.graph.WriteGraph
 */
final class WriteState extends WeakHashMap<Object, WriteGraph> {
   
   /**
    * This is the label used to mark the type of an object.
    */
   private String label;
   
   /**
    * This is the attribute used to mark the identity of an object.
    */
   private String mark;
   
   /**
    * Thsi is the attribute used to refer to an existing instance.
    */
   private String refer;
   
   /**
    * Constructor for the <code>WriteState</code> object. This is
    * used to create graphs that are used for writing objects to the
    * the XML document. The specified strings represent attributes
    * that are inserted in to the XML during the serialization.
    * 
    * @param mark this is used to mark the identity of an object
    * @param refer this is used to refer to an existing instance
    * @param label this is used to represent the objects type
    */
   public WriteState(String mark, String refer, String label) {
      this.label = label;
      this.refer = refer;
      this.mark = mark;
   }

   /**
    * This will acquire the graph using the specified session map. If
    * a graph does not already exist mapped to the given session then
    * one will be created and stored with the key provided. Once the
    * specified key is garbage collected then so is the graph object.
    * 
    * @param map this is typically the persistence session map used 
    * 
    * @return returns a graph used for writing the XML document
    */
   public WriteGraph find(Object map) {
      WriteGraph read = get(map);
      
      if(read == null) {
         read = new WriteGraph(mark, refer, label);
         put(map, read);
      }
      return read;
   }
}
