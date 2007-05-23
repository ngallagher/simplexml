/*
 * Mode.java May 2007
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

package simple.xml.stream;

/**
 * The <code>Mode</code> enumeration is used to specify the output
 * mode for XML text. This is used by the <code>OutputNode</code> 
 * to describe if element text will be escaped or wrapped in a 
 * CDATA block. The mode is a three state object, the third of the
 * states indicates whether an explicit state has been set or not.
 * If a specific state has not been set then the node will inherit
 * its output mode from the last parent to have it set. 
 * 
 * @author Niall Gallagher
 * 
 * @see simple.xml.stream.OutputNode
 */
public enum Mode {
   
   /**
    * Indicates that data written will be within a CDATA block.
    */
   DATA,
   
   /**
    * Indicates that data written will be escaped if required.
    */
   ESCAPE,
   
   /**
    * Indicates that the mode will be inherited from its parent.
    */
   INHERIT;
}
