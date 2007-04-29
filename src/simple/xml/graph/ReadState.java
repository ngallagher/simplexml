/*
 * ReadState.java April 2007
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
 * The <code>ReadState</code> object is used to store all graphs that
 * are currently been read with a given cycle strategy. The goal of
 * this object is to act as a temporary store for graphs such that 
 * when the persistence session has completed the read graph will be
 * garbage collected. This ensures that there are no lingering object
 * reference that could cause a memory leak. If a graph for the given
 * session key does not exist then a new one is created. 
 * 
 * @author Niall Gallagher
 * 
 * @see simple.xml.graph.ReadGraph
 */
final class ReadState extends WeakHashMap<Object, ReadGraph>{
   
   /** 
    * This is the contract that specifies the attributes to use.
    */
   private Contract contract;
           
   /**
    * Constructor for the <code>ReadState</code> object. This is used
    * to create graphs that are used for reading objects from the XML
    * document. The specified strategy is used to acquire the names
    * of the special attributes used during the serialization.
    * 
    * @param contract this is name scheme used by the cycle strategy 
    */
   public ReadState(Contract contract) {
      this.contract = contract;
   }

   /**
    * This will acquire the graph using the specified session map. If
    * a graph does not already exist mapped to the given session then
    * one will be created and stored with the key provided. Once the
    * specified key is garbage collected then so is the graph object.
    * 
    * @param map this is typically the persistence session map used 
    * 
    * @return returns a graph used for reading the XML document
    */
   public ReadGraph find(Object map) {
      ReadGraph read = get(map);
      
      if(read == null) {
         read = new ReadGraph(contract);
         put(map, read);
      }
      return read;
   }
}
