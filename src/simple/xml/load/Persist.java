/*
 * Persist.java July 2006
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

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

/**
 * The <code>Persist</code> annotation is used to mark a method that
 * requires a callback from the persister before serialization of
 * an object begins. If a method is marked with this annotation then
 * it will be invoked so that it can prepare the object for the
 * serialization process.
 * <p>
 * The persist method can be used to perform any preparation needed
 * before serialization. For example, should the object be a list
 * or table of sorts the persist method can be used to grab a lock
 * for the internal data structure. Such a scheme will ensure that
 * the object is serialized in a known state. The persist method
 * must be a no argument public method or a method that takes a 
 * single <code>Map</code> argument, it may throw an exception to 
 * terminate the serialization process if required.
 * 
 * @author Niall Gallagher
 * 
 * @see simple.xml.load.Complete
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Persist {
}
