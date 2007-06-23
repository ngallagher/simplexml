/*
 * NodeException.java July 2006
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

package org.simpleframework.xml.stream;

/**
 * The <code>NodeException</code> is thrown to indicate the state of
 * either the input node or output node being invalid. All messages
 * provided to this exception are formatted in a similar manner to 
 * the <code>PrintStream.printf</code> method.
 * 
 * @author Niall Gallagher
 */
public class NodeException extends Exception {

   /**
    * Constructor for the <code>NodeException</code> object. This is
    * given the message to be reported when the exception is thrown.
    * 
    * @param text a format string used to present the error message
    */
   public NodeException(String text) {
      super(text);
   }        
}
