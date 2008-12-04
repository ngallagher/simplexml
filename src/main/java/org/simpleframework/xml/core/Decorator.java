/*
 * Decorator.java July 2008
 *
 * Copyright (C) 2008, Niall Gallagher <niallg@users.sf.net>
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

import org.simpleframework.xml.stream.OutputNode;

/**
 * The <code>Decorator</code> interface is used to describe an object
 * that is used to add decorations to an output node. A decoration is
 * a object that adds information to the output node without any
 * change to the structure of the node. Decorations can include extra
 * information like comments and namespaces.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Label
 */
interface Decorator {
   
   /**
    * This method is used to decorate the provided node. This node 
    * can be either an XML element or an attribute. Decorations that
    * can be applied to the node by invoking this method include
    * things like comments and namespaces.
    * 
    * @param node this is the node that is to be decorated by this
    */
   public void decorate(OutputNode node);
   
   /**
    * This method is used to decorate the provided node. This node 
    * can be either an XML element or an attribute. Decorations that
    * can be applied to the node by invoking this method include
    * things like comments and namespaces. This can also be given
    * another <code>Decorator</code> which is applied before this
    * decorator, any common data can then be overwritten.
    * 
    * @param node this is the node that is to be decorated by this
    * @param secondary this is a secondary decorator to be applied
    */
   public void decorate(OutputNode node, Decorator secondary);
}