/*
 * Entry.java July 2006
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

/**
 * The <code>Entry</code> object represents entries to the dictionary
 * object. Every entry must have a name attribute, which is used to
 * establish mappings within the <code>Dictionary</code> object. Each
 * entry entered into the dictionary can be retrieved using its name. 
 * <p>
 * The entry can be serialzed with the dictionary to an XML document.
 * Items stored within the dictionary need to extend this entry
 * object to ensure that they can be mapped and serialized with the
 * dictionary. Implementations should override the root annotation.
 *
 * @author Niall Gallagher
 */ 
public interface Entry {

   /**
    * Represents the name of the entry instance used for mappings.
    * This will be used to map the object to the internal map in
    * the <code>Dictionary</code>. This allows serialized objects
    * to be added to the dictionary transparently.
    * 
    * @return this returns the name of the enrty that is used 
    */         
   public String getName();
}
