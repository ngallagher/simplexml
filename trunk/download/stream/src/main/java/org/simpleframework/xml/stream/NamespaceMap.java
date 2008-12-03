/*
 * NamespaceMap.java July 2008
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

package org.simpleframework.xml.stream;

import java.util.Iterator;

/**
 * The <code>NamespaceMap</code> object is used store the namespaces
 * for an element. Each namespace added to this map can be added
 * with a prefix. A prefix is added only if the associated reference
 * has not been added to a parent element. If a parent element has
 * the associated reference, then the parents prefix is the one that
 * will be returned when requested from this map. 
 * 
 * @author Niall Gallagher
 */
public interface NamespaceMap extends Iterable<String> {
   
   /**
    * This is the prefix that is associated with the source element.
    * If the source element does not contain a namespace reference
    * then this will return its parents namespace. This ensures 
    * that if a namespace has been declared its child elements will
    * inherit its prefix.
    * 
    * @return this returns the prefix that is currently in scope
    */
   public String getPrefix();

   /**
    * This acquires the prefix for the specified namespace reference.
    * If the namespace reference has been set on this node with a
    * given prefix then that prefix is returned, however if it has
    * not been set this will search the parent elements to find the
    * prefix that is in scope for the specified reference.
    * 
    * @param reference the reference to find a matching prefix for
    * 
    * @return this will return the prefix that is is scope
    */
   public String get(String reference);

   /**
    * This is used to remove the prefix that is matched to the 
    * given reference. If no prefix is matched to the reference then
    * this will silently return. This will only remove mappings
    * from the current map, and will ignore the parent nodes.
    * 
    * @param reference this is the reference that is to be removed 
    * 
    * @return this returns the prefix that was matched to this
    */
   public String remove(String reference);

   /**
    * This returns an iterator for the namespace of all the nodes 
    * in this <code>NamespaceMap</code>. This allows the namespaces 
    * to be iterated within a for each loop in order to extract the
    * prefix values associated with the map.
    *
    * @return this returns the namespaces contained in this map
    */ 
   public Iterator<String> iterator();
   
   /**
    * This is used to add the namespace reference to the namespace
    * map. If the namespace has been added to a parent node then
    * this will not add the reference. The prefix added to the map
    * will be the default namespace, which is an empty prefix.
    * 
    * @param reference this is the reference to be added 
    * 
    * @return this returns the prefix that has been replaced
    */
   public String put(String reference);
   
   /**
    * This is used to add the namespace reference to the namespace
    * map. If the namespace has been added to a parent node then
    * this will not add the reference. 
    * 
    * @param reference this is the reference to be added 
    * @param prefix this is the prefix to be added to the reference
    * 
    * @return this returns the prefix that has been replaced
    */
   public String put(String reference, String prefix);
}
