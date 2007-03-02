/*
 * Type.java January 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
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

/**
 * The <code>Type</code> object describes a type that is represented 
 * by an XML element. This enables a <code>Strategy</code> to define
 * not only the type an element represents, but also defines how that
 * type can be created. This allows objects that do not have default
 * no argument constructors to be created during deserialization.
 * <p>
 * Typically the <code>getInstance</code> method acts as a proxy to 
 * the classes new instance method, which takes no arguments. Simply
 * delegating to <code>Class.newInstance</code> will sometimes not
 * be sufficient, is such cases reflectively acquiring the classes
 * constructor may be required in order to pass arguments.
 * 
 * @author Niall Gallagher
 * 
 * @see simple.xml.load.Strategy
 */
public interface Type {
	
	/**
	 * This method is used to acquire an instance of the type that
	 * is defined by this object. If for some reason the type can
	 * not be instantiated an exception is thrown from this.
	 * 
	 * @return an instance of the type this object represents
	 */
	public Object getInstance() throws Exception;
	
	/**
	 * This is the type of the object instance that will be created
	 * by the <code>getInstance</code> method. This allows the 
	 * deserialization process to perform checks against the field.
	 * 
	 * @return the type of the object that will be instantiated
	 */
	public Class getType();
}
