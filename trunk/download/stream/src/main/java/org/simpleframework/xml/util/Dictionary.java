/*
 * Dictionary.java July 2006
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

package org.simpleframework.xml.util;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The <code>Dictionary</code> object represents a mapped set of entry
 * objects that can be serialized and deserialized. This is used when
 * there is a need to load a list of objects that can be mapped using 
 * a name attribute. Using this object avoids the need to implement a
 * commonly required pattern of building a map of XML element objects.
 * <pre>
 *
 *    &lt;dictionary&gt;
 *       &lt;entry name="example"&gt;
 *          &lt;element&gt;example text&lt;/element&gt;
 *       &lt;/entry&gt;
 *       &lt;entry name="example"&gt;
 *          &lt;element&gt;example text&lt;/element&gt;
 *       &lt;/entry&gt;       
 *    &lt;/dictionary&gt;
 * 
 * </pre>
 * This can contain implementations of the <code>Entry</code> object 
 * which contains a required "name" attribute. Implementations of the
 * entry object can add further XML attributes an elements. This must
 * be annotated with the <code>ElementList</code> annotation in order
 * to be serialized and deserialized as an object field.
 * 
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.util.Entry
 */ 
public class Dictionary<E extends Entry> extends AbstractSet<E> {

   /**
    * Used to map the entries to their configured names.
    */         
   protected Table map;
        
   /**
    * Constructor for the <code>Dictionary</code> object. This 
    * is used to create a set that contains entry objects mapped 
    * to an XML attribute name value. Entry objects added to this
    * dictionary can be retrieved using its name value.
    */ 
   public Dictionary() {
      this.map = new Table();           
   }

   /**
    * This method is used to add the provided entry to this set. If
    * an entry of the same name already existed within the set then
    * it is replaced with the specified <code>Entry</code> object.
    * 
    * @param item this is the entry object that is to be inserted
    */ 
   public boolean add(E item) {
      return map.put(item.name, item) != null;           
   }

   /**
    * This returns the number of <code>Entry</code> objects within
    * the dictionary. This will use the internal map to acquire the
    * number of entry objects that have been inserted to the map.
    *
    * @return this returns the number of entry objects in the set
    */ 
   public int size() {
      return map.size();            
   }

   /**
    * Returns an iterator of <code>Entry</code> objects which can be
    * used to remove items from this set. This will use the internal
    * map object and return the iterator for the map values.
    * 
    * @return this returns an iterator for the entry objects
    */ 
   public Iterator<E> iterator() {
      return map.values().iterator();            
   }

   /**
    * This is used to acquire an <code>Entry</code> from the set by
    * its name. This uses the internal map to look for the entry, if
    * the entry exists it is returned, if not this returns null.
    * 
    * @param name this is the name of the entry object to retrieve
    *
    * @return this returns the entry mapped to the specified name
    */ 
   public E get(String name) {
      return map.get(name);           
   }

   /**
    * This is used to remove an <code>Entry</code> from the set by
    * its name. This uses the internal map to look for the entry, if
    * the entry exists it is returned and removed from the map.
    * 
    * @param name this is the name of the entry object to remove
    *
    * @return this returns the entry mapped to the specified name
    */ 
   public E remove(String name) {
      return map.remove(name);           
   }
 
   /**
    * The <code>Table</code> object is used to represent a map of
    * entries mapped to a string name. Each implementation of the
    * entry must contain a name attribute, which is used to insert
    * the entry into the map. This acts as a typedef.
    *
    * @see org.simpleframework.xml.util.Entry
    */
   private class Table extends HashMap<String, E> {
      
      /**
       * Constructor for the <code>Table</code> object. This will
       * create a map that is used to store the entry objects that
       * are serialized and deserialized to and from an XML source.
       */
      public Table() {
         super();
      }         
   }     
}
