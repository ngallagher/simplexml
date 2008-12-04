/*
 * Complete.java July 2006
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

package org.simpleframework.xml.core;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

/**
 * The <code>Complete</code> annotation is used to mark a method that
 * requires a callback from the persister once the serialization of
 * the object has completed. The complete method is typically used
 * in combination with the persist method, which is the method that
 * is annotated with the <code>Persist</code> annotation.
 * <p>
 * Typically the complete method will revert any changes made when 
 * the persist method was invoked. For example, should the persist
 * method acquire a lock to ensure the object is serialized in a
 * safe state then the commit method can be used to release the lock.
 * The complete method must be a no argument public method or a
 * method that takes a single <code>Map</code> object argument. The
 * complete method is invoked even if deserialization terminates.   
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Persist
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Complete {
}
