/*
 * NamespaceDecorator.java July 2008
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

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.stream.NamespaceMap;
import org.simpleframework.xml.stream.OutputNode;

/**
 * The <code>NamespaceDecorator</code> object is used to decorate
 * any output node with namespaces. All namespaces added to this are
 * applied to nodes that require decoration. This can add namespaces
 * to the node as well as setting the primary namespace reference
 * for the node. This results in qualification for the node.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframewor.xml.load.Qualifier
 */
class NamespaceDecorator implements Decorator {
   
   /**
    * This is used to contain the namespaces used for scoping.
    */
   private List<Namespace> scope;
   
   /**
    * This is used to set the primary namespace reference used.
    */
   private Namespace primary;
   
   /**
    * Constructor for the <code>NamespaceDecorator</code> object. A
    * namespace decorator can be used for applying namespaces to a
    * specified node. It can add namespaces to set the scope of the
    * namespace reference to the node and it can also be used to set
    * the primary namespace reference used for the node.
    */
   public NamespaceDecorator() {
      this.scope = new ArrayList<Namespace>();
   }
   
   /**
    * This is used to set the primary namespace for nodes that will
    * be decorated by the namespace decorator. If no namespace is set
    * using this method then this decorator will leave the namespace
    * reference unchanged and only add namespaces for scoping.
    * 
    * @param namespace this is the primary namespace to be set
    */
   public void set(Namespace namespace) {
      if(namespace != null) {
         add(namespace);
      }
      primary = namespace;
   }
   
   /**
    * This is used to add a namespace to the decorator so that it can
    * be added to decorated nodes. Namespaces that are added will be
    * set on the element so that child elements can reference the
    * namespace and will thus inherit the prefix from that elment.
    * 
    * @param namespace this is the namespace to be added for scoping
    */
   public void add(Namespace namespace) {
      scope.add(namespace);
   }
   
   /**
    * This method is used to decorate the provided node. This node 
    * can be either an XML element or an attribute. Decorations that
    * can be applied to the node by invoking this method include
    * things like comments and namespaces.
    * 
    * @param node this is the node that is to be decorated by this
    */
   public void decorate(OutputNode node) {
      decorate(node, null);
   }
   
   /**
    * This method is used to decorate the provided node. This node 
    * can be either an XML element or an attribute. Decorations that
    * can be applied to the node by invoking this method include
    * things like namespaces and namespace lists. This can also be 
    * given another <code>Decorator</code> which is applied before 
    * this decorator, any common data can then be overwritten.
    * 
    * @param node this is the node that is to be decorated by this
    * @param secondary this is a secondary decorator to be applied
    */
   public void decorate(OutputNode node, Decorator qualifier) {
      if(qualifier != null) {
         qualifier.decorate(node);
      }
      scope(node);
      namespace(node);
   }
   
   /**
    * This is use to apply for <code>NamespaceList</code> annotations 
    * on the node. If there is no namespace list then this will return 
    * and the node will be left unchanged. If however the namespace 
    * list is not empty the the namespaces are added.
    * 
    * @param node this is the node to apply the namespace list to
    */
   private void scope(OutputNode node) {
      NamespaceMap map = node.getNamespaces();
      
      for(Namespace next : scope) {
         String reference = next.reference();
         String prefix = next.prefix();
            
         map.put(reference, prefix);
      }
   }
   
   /**
    * This is use to apply the <code>Namespace</code> annotations on
    * the node. If there is no namespace then this will return and
    * the node will be left unchanged. If however the namespace is 
    * not null then the reference is applied to the specified node.
    * 
    * @param node this is the node to apply the namespace to
    */
   private void namespace(OutputNode node) {
      if(primary != null) {
         String reference = primary.reference();
         
         node.setReference(reference);
      }
   }
}
