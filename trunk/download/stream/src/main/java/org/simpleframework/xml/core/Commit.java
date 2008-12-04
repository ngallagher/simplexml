/*
 * Commit.java July 2006
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
 * The <code>Commit</code> annotation is used to mark a method within
 * a serializable object that requires a callback from the persister
 * once the deserialization completes. The commit method is invoked
 * by the <code>Persister</code> after all fields have been assigned
 * and after the validation method has been invoked, if the object
 * has a method marked with the <code>Validate</code> annotation.
 * <p>
 * Typically the commit method is used to complete deserialization
 * by allowing the object to build further data structures from the
 * fields that have been created from the deserialization process.
 * The commit method must be a no argument method or a method that
 * takes a single <code>Map</code> object argument, and may throw an
 * exception, in which case the deserialization process terminates.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Validate
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Commit {
}
