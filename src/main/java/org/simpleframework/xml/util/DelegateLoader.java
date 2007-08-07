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

/**
 * This delegates to the class loaders available to the caller. The
 * sequence taken delegates from the system class loader to the thread 
 * context class loader. Delegation to the various class loaders that
 * are available to the caller ensures that the deserialization can
 * be performed even if the  
 * 
 * @author Niall Gallagher
 */
public class DelegateLoader implements Loader {
   
   private final Loader system;
   
   private final Loader caller;
   
   private final Loader thread;
   
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
         }catch(ClassNotFoundException cause) {
            continue;
         }
      }            
      throw new ClassNotFoundException(type);
   }
}



