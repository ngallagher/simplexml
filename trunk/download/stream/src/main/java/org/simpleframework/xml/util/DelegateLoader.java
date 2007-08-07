/*
 * DelegateLoader.java July 2006
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

public class DelegateLoader implements Loader {
   
   private Loader system;
   
   private Loader caller;
   
   private Loader thread;
   
   public DelegateLoader() {
      this.system = new SystemLoader();
      this.caller = new CallerLoader();
      this.thread = new ThreadLoader();
   }
   
   public Class loadClass(String type) throws ClassNotFoundException {
      return loadClass(type, system, caller, thread);
   }
   
   private Class loadClass(String type, Loader... list) throws ClassNotFoundException {
      for(Loader loader : list) {
         try {
            return loader.loadClass(type);
         }catch(ClassNotFoundException e) {            
         }
      }
      throw new ClassNotFoundException();      
   }
}



