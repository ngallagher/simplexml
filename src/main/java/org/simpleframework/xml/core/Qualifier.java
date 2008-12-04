/*
 * Qualifier.java July 2008
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

import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.stream.OutputNode;

/**
 * The <code>Qualifier</code> object is used to provide decorations
 * to an output node for namespaces. This will scan a provided
 * contact object for namespace annotations. If any are found they
 * can then be used to apply these namespaces to the provided node.
 * The <code>Contact</code> objects can represent fields or methods
 * that have been annotated with XML annotations.
 * 
 * @author Niall Gallagher
 */
class Qualifier implements Decorator {

   /**
    * This is the namespace decorator that is populated for use.
    */
   private NamespaceDecorator decorator;
   
   /**
    * Constructor for the <code>Qualifier</code> object. This is
    * used to create a decorator that will scan the provided
    * contact for <code>Namespace</code> annotations. These can
    * then be applied to the output node to provide qualification.
    * 
    * @param contact this is the contact to be scanned 
    */
   public Qualifier(Contact contact) {
      this.decorator = new NamespaceDecorator();
      this.scan(contact);
   }

   /**
    * This method is used to decorate the provided node. This node 
    * can be either an XML element or an attribute. Decorations that
    * can be applied to the node by invoking this method include
    * things like namespaces and namespace lists.
    * 
    * @param node this is the node that is to be decorated by this
    */
   public void decorate(OutputNode node) {
      decorator.decorate(node);
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
   public void decorate(OutputNode node, Decorator secondary) {
      decorator.decorate(node, secondary);
   }
   
   /**
    * This method is used to scan the <code>Contact</code> provided
    * to determine if there are any namespace annotations. If there
    * are any annotations then these are added to the internal
    * namespace decorator. This ensues that they can be applied to
    * the node when that node requests decoration.
    * 
    * @param contact this is the contact to be scanned for namespaces
    */
   private void scan(Contact contact) {
      namespace(contact);
      scope(contact);
   }
   
   /**
    * This is use to scan for <code>Namespace</code> annotations on
    * the contact. Once a namespace has been located then it is used
    * to populate the internal namespace decorator. This can then be
    * used to decorate any output node that requires it.
    * 
    * @param contact this is the contact to scan for namespaces
    */
   private void namespace(Contact contact) {
      Namespace primary = contact.getAnnotation(Namespace.class);
      
      if(primary != null) {
         decorator.set(primary);
         decorator.add(primary);
      }
   }
   
   /**
    * This is use to scan for <code>NamespaceList</code> annotations 
    * on the contact. Once a namespace list has been located then it is 
    * used to populate the internal namespace decorator. This can then 
    * be used to decorate any output node that requires it.
    * 
    * @param contact this is the contact to scan for namespace lists
    */
   private void scope(Contact contact) {
      NamespaceList scope = contact.getAnnotation(NamespaceList.class);
      
      if(scope != null) {
         for(Namespace name : scope.value()) {
            decorator.add(name);
         }
      }
   }
}
