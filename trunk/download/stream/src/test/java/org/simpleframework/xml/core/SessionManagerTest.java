package org.simpleframework.xml.core;

import junit.framework.TestCase;

public class SessionManagerTest extends TestCase {
   
   public void testManager() throws Exception{
      SessionManager manager = new SessionManager();
      Session session = manager.open(true);
      
      assertEquals(session.isStrict(), true);
      assertEquals(manager.close(), session);
      assertTrue(manager.open(true) != session); 
      
      session = manager.open(true);
      
      assertTrue(session == manager.close());
      assertTrue(session == manager.close());
      
      try {
         session = manager.close();
      }catch(Exception e){
         e.printStackTrace();
      }
   }

}
