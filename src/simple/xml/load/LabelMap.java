/*
 * LabelMap.java July 2006
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

package simple.xml.load;

import java.util.Iterator;
import java.util.HashMap;

/**
 * The <code>LabelMap</code> object represents a map that contains 
 * string label mappings. This is used for convinience as a typedef
 * like construct to avoid having declare the generic type whenever
 * it is referenced. Also this allows <code>Label</code> values 
 * from the map to be iterated within for each loops.
 * 
 * @author Niall Gallagher
 *
 * @see simple.xml.load.Label
 */
final class LabelMap extends HashMap<String, Label> implements Iterable<Label> { 
   
   /**
    * Constructor for the <code>LabelMap</code> object is used to 
    * create an empty map. This is used for convinience as a typedef
    * like construct which avoids having to use the generic type.
    */ 
   public LabelMap() {
      super();
   }
   
   /**
    * Copy constructor for the <code>LabelMap</code> object. This is
    * used to copy the mappings from the provided map. This is used
    * when the map needs to be manipulated and the original map needs
    * to remain unchanged. This allows the label maps to be cached. 
    *
    * @param map this is a map contain string to label mappings
    */ 
   public LabelMap(LabelMap map) {
      super(map);
   }

   /**
    * This allows the <code>Label</code> objects within the label map
    * to be iterated within for each loops. This will provide all
    * remaining label objects within the map. The iteration order is
    * not maintained so label objects may be given in any sequence.
    *
    * @return this returns an iterator for existing label objects
    */ 
   public Iterator<Label> iterator() {
      return values().iterator();
   }

   /**
    * This performs a <code>remove</code> that will remove the label
    * from the map in a case insensitive manner. This allows the XML
    * elements and attributes to be acquired regardless of how they
    * are represented in the XML schema class or XML document.
    *
    * @param name this is the name of the element of attribute
    *
    * @return this is the label object representing the XML node
    */ 
   public Label take(String name) {
      String key = name.toLowerCase();
      
      if(containsKey(key)) {
         return remove(key);              
      }
      return null;      
   }
}
