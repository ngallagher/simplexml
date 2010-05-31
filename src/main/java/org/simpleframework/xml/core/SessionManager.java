/*
 * SessionManager May 2010
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml.core;

class SessionManager extends ThreadLocal<SessionReference> {
   
   public SessionManager() {
      super();
   }
   
   public Session open() throws Exception {
      return open(true);
   }

   public Session open(boolean strict) throws Exception {
      SessionReference session = get();
      
      if(session != null) {
         return session.get();
      }
      return create(strict);
   }
   
   private Session create(boolean strict) throws Exception {
      SessionReference session = new SessionReference(strict);
      
      if(session != null) {
         set(session);
      }
      return session.get();
   }
   
   public Session close() throws Exception {
      SessionReference session = get();
      
      if(session == null) {
         throw new PersistenceException("Session does not exist");
      } 
      int reference = session.clear();
      
      if(reference == 0) {
        remove();
      }
      return session.reference();
   }
}
