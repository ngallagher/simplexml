package com.rbsfm.plugin.build.repository;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
public class LocationTypeTest {
   @Test
   public void parse() throws Exception {
      Location trunk = LocationType.parse("http://domain/project/trunk/resource");
      assertEquals(trunk.path, "/resource");
      assertEquals(trunk.root, "http://domain/project/trunk");
      
      Location branches = LocationType.parse("http://domain/project/branches/path/resource");
      assertEquals(branches.path, "/path/resource");
      assertEquals(branches.root, "http://domain/project/branches");
   }
}
