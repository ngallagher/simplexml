/*
 * NodeMap.java July 2006
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

package simple.xml.stream;

import java.util.Iterator;

/**
 * The <code>NodeMap</code> object represents a map of nodes that
 * can be set as name value pairs. This typically represents the
 * attributes that belong to an element and is used as an neutral
 * way to access an element for either an input or output event.
 *
 * @author Niall Gallagher
 *
 * @see simple.xml.stream.Node
 */ 
public interface NodeMap extends Iterable<String> {

   /**
    * This is used to get the name of the element that owns the
    * nodes for the specified map. This can be used to determine
    * which element the node map belongs to.
    * 
    * @return this returns the name of the owning element
    */         
   public String getName();        

   /**
    * This is used to acquire the <code>Node</code> mapped to the
    * given name. This returns a name value pair that represents
    * either an attribute or element. If no node is mapped to the
    * specified name then this method will return null.
    *
    * @param name this is the name of the node to retrieve
    * 
    * @return this will return the node mapped to the given name
    */         
   public Node get(String name);        

   /**
    * This is used to remove the <code>Node</code> mapped to the
    * given name.  This returns a name value pair that represents
    * either an attribute or element. If no node is mapped to the
    * specified name then this method will return null.
    *
    * @param name this is the name of the node to remove
    * 
    * @return this will return the node mapped to the given name
    */ 
   public Node remove(String name);
   
   /**
    * This returns an iterator for the names of all the nodes in
    * this <code>NodeMap</code>. This allows the names to be 
    * iterated within a for each loop in order to extract nodes.
    *
    * @return this returns the names of the nodes in the map
    */ 
   public Iterator<String> iterator();

   /**
    * This is used to add a new <code>Node</code> to the map. The
    * type of node that is created an added is left up to the map
    * implementation. Once a node is created with the name value
    * pair it can be retrieved and used.
    *
    * @param name this is the name of the node to be created
    * @param value this is the value to be given to the node
    */ 
   public void put(String name, String value);
}
