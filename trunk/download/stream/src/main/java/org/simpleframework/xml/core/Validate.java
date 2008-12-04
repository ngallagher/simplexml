/*
 * Validate.java July 2006
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
 * The <code>Validate</code> annotation is used to mark a method in
 * a serializable object that requires a callback from the persister
 * once the deserialization completes. The validate method is invoked
 * by the <code>Persister</code> after all fields have been assigned
 * and before the commit method is invoked.
 * <p>
 * Typically the validate method is used to validate the fields that
 * have been assigned once deserialization has been completed. The
 * validate method must be a no argument public method or a method
 * that takes a <code>Map</code> as the only argument. When invoked 
 * the object can determine whether the fields are valid, if the  
 * field values do not conform to the objects requirements then the
 * method can throw an exception to terminate deserialization.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Commit
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
}
