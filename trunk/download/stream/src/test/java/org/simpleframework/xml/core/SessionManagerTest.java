package org.simpleframework.xml.core;

import junit.framework.TestCase;

public class SessionManagerTest extends TestCase {
   
   public void testManager() throws Exception{
      SessionManager manager = new SessionManager();
      Session session = manager.open(true);      
   }

}
