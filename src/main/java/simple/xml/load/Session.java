/*
 * Session.java February 2005
 *
 * Copyright (C) 2005, Niall Gallagher <niallg@users.sf.net>
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The <code>Session</code> object represents a session with name
 * value pairs. The persister uses this to allow objects to add
 * or remove name value pairs to an from an internal map. This is
 * done so that the deserialized objects can set template values
 * as well as share information. In particular this is useful for
 * any <code>Strategy</code> implementation as it allows it so
 * store persistence state during the persistence process.
 * <p>
 * Another important reason for the session map is that it is
 * used to wrap the map that is handed to objects during callback
 * methods. This opens the possibility for those objects to grab
 * a reference to the map, which will cause problems for any of
 * the strategy implementations that wanted to use the session
 * reference for weakly storing persistence artifacts.
 * 
 * @author Niall Gallagher
 * 
 * @see simple.xml.load.Strategy
 */ 
final class Session implements Map {

   /**
    * This is the internal map that provides storage for pairs.
    */         
   private Map map;        

   /**
    * Constructor for the <code>Session</code> object. This is 
    * used to create a new session that makes use of a hash map
    * to store key value pairs which are maintained throughout
    * the duration of the persistence process this is used in.
    */ 
   public Session(){
      this.map = new HashMap();                
   }

   /**
    * This returns the inner map used by the session object. The
    * internal map is the <code>Map</code> instance that is used
    * for persister callbacks, a reference to this map can be
    * safely made by any object receiving a callback.
    * 
    * @return this returns the internal session map used
    */
   public Map getMap() {
      return map;
   }     

   /**
    * This obviously enough provides the number of pairs that
    * have been inserted into the internal map. This acts as
    * a proxy method for the internal map <code>size</code>.
    *
    * @return this returns the number of pairs are available
    */ 
   public int size() {
      return map.size();           
   }

   /**
    * This method is used to determine whether the session has 
    * any pairs available. If the <code>size</code> is zero then
    * the session is empty and this returns true. The is acts as 
    * a proxy the the <code>isEmpty</code> of the internal map.
    * 
    * @return this is true if there are no available pairs
    */ 
   public boolean isEmpty() {
      return map.isEmpty();           
   }

   /**
    * This is used to determine whether a value representing the
    * name of a pair has been inserted into the internal map. The
    * object passed into this method is typically a string which
    * references a template variable but can be any object.
    *  
    * @param name this is the name of a pair within the map
    *
    * @return this returns true if the pair of that name exists
    */ 
   public boolean containsKey(Object name) {
      return map.containsKey(name);           
   }

   /**
    * This method is used to determine whether any pair that has
    * been inserted into the internal map had the presented value.
    * If one or more pairs within the collected mappings contains
    * the value provided then this method will return true.
    * 
    * @param value this is the value that is to be searched for
    *
    * @return this returns true if any value is equal to this
    */    
   public boolean containsValue(Object value) {
      return map.containsValue(value);           
   }

   /**
    * The <code>get</code> method is used to acquire the value for
    * a named pair. So if a mapping for the specified name exists
    * within the internal map the mapped entry value is returned.
    *
    * @param name this is a name used to search for the value
    *
    * @return this returns the value mapped to the given name     
    */ 
   public Object get(Object name) {
      return map.get(name);           
   }
   
   /**
    * The <code>put</code> method is used to insert the name and
    * value provided into the internal session map. The inserted
    * value will be available to all objects receiving callbacks.
    *
    * @param name this is the name the value is mapped under    
    * @param value this is the value to mapped with the name
    *
    * @return this returns the previous value if there was any
    */ 
   public Object put(Object name, Object value) {
      return map.put(name, value);           
   }

   /**
    * The <code>remove</code> method is used to remove the named
    * mapping from the internal session map. This ensures that
    * the mapping is no longer available for persister callbacks.
    *
    * @param name this is a string used to search for the value
    *
    * @return this returns the value mapped to the given name   
    */ 
   public Object remove(Object name) {
      return map.remove(name);           
   }

   /**
    * This method is used to insert a collection of mappings into 
    * the session map. This is used when another source of pairs
    * is required to populate the collection currently maintained
    * within this sessions internal map. Any pairs that currently
    * exist with similar names will be overwritten by this.
    * 
    * @param data this is the collection of pairs to be added
    */ 
   public void putAll(Map data) {
      map.putAll(data);           
   }

   /**
    * This is used to acquire the names for all the pairs that 
    * have currently been collected by this session. This is used
    * to determine which mappings are available within the map.
    *
    * @return the set of names for all mappings in the session    
    */ 
   public Set keySet() {
      return map.keySet();           
   }

   /**
    * This method is used to acquire the value for all pairs that
    * have currently been collected by this session. This is used
    * to determine the values that are available in the session.
    *
    * @return the list of values for all mappings in the session   
    */ 
   public Collection values() {
      return map.values();           
   }

   /**
    * This method is used to acquire the name and value pairs that
    * have currently been collected by this session. This is used
    * to determine which mappings are available within the session.
    *
    * @return thie set of mappings that exist within the session   
    */ 
   public Set entrySet() {
      return map.entrySet();           
   }

   /**
    * The <code>clear</code> method is used to wipe out all the
    * currently existing pairs from the collection. This is used
    * when all mappings within the session should be erased.
    */ 
   public void clear() {
      map.clear();           
   }   
}

