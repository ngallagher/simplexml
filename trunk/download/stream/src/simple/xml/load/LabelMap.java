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

import java.util.LinkedHashMap;
import java.util.Iterator;

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
final class LabelMap extends LinkedHashMap<String, Label> implements Iterable<Label> { 
   
   /**
    * This is the scanner object that represents the scanner used.
    */        
   private Scanner source;
        
   /**
    * Constructor for the <code>LabelMap</code> object is used to 
    * create an empty map. This is used for convinience as a typedef
    * like construct which avoids having to use the generic type.
    */ 
   public LabelMap(Scanner source) {
      this.source = source;
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
    * from the map and return that label. This method allows the 
    * values within the map to be exclusively taken one at a time,
    * which enables the user to determine which labels remain.
    *
    * @param name this is the name of the element of attribute
    *
    * @return this is the label object representing the XML node
    */ 
   public Label take(String name) {
      return remove(name);    
   }

   /**
    * This method is used to clone the label map such that mappings
    * can be maintained in the original even if they are modified
    * in the clone. This is used to that the <code>Schema</code> can
    * remove mappings from the label map as they are visited. 
    *
    * @return this returns a cloned representation of this map
    */ 
   public LabelMap clone() {
      LabelMap clone = new LabelMap(source);
      
      if(!isEmpty()) {
         clone.putAll(this);
      }         
      return clone;      
   }   

   /**
    * This method is used to determine whether strict mappings are
    * required. Strict mapping means that all labels in the class
    * schema must match the XML elements and attributes in the
    * source XML document. When strict mapping is disabled, then
    * XML elements and attributes that do not exist in the schema
    * class will be ignored without breaking the parser.
    *
    * @return true if strict parsing is enabled, false otherwise
    */ 
   public boolean isStrict() {
      return source.isStrict();           
   }
}
