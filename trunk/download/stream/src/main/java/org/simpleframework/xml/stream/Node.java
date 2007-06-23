/*
 * Node.java July 2006
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

package org.simpleframework.xml.stream;

/**
 * The <code>Node</code> is used to represent a name value pair and
 * acts as the base form of data used within the framework. Each of
 * the attributes and elements are represented as nodes.  
 * 
 * @author Niall Gallagher
 */
public interface Node {

   /**
    * Returns the name of the node that this represents. This is
    * an immutable property and should not change for any node.  
    *  
    * @return returns the name of the node that this represents
    */
   public String getName();

   /**
    * Returns the value for the node that this represents. This 
    * is a modifiable property for the node and can be changed.
    * 
    * @return the name of the value for this node instance
    * 
    * @throws Exception if there is a problem getting the value
    */
   public String getValue() throws Exception;  
   
   /**
    * This is used to acquire the <code>Node</code> that is the
    * parent of this node. This will return the node that is
    * the direct parent of this node and allows for siblings to
    * make use of nodes with their parents if required.  
    *   
    * @return this returns the parent node for this node
    */
   public Node getParent();
}
