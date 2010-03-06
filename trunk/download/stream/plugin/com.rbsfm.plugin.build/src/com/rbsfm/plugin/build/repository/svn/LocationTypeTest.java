package com.rbsfm.plugin.build.repository.svn;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.rbsfm.plugin.build.repository.Location;
public class LocationTypeTest{
   @Test
   public void parse() throws Exception{
      Location trunk=LocationType.parse("http://domain/project/trunk/resource");
      assertEquals(trunk.path,"/resource");
      assertEquals(trunk.root,"http://domain/project/trunk");
      assertEquals(trunk.prefix,"");
      Location branches=LocationType.parse("http://domain/project/branches/token/resource");
      assertEquals(branches.path,"/resource");
      assertEquals(branches.root,"http://domain/project/branches");
      assertEquals(branches.prefix,"token");
   }
   @Test
   public void build() throws Exception{
      Location tag=LocationType.build("http://domain/project/trunk/resource","test",LocationType.TAGS);
      assertEquals(tag.path,"/resource");
      assertEquals(tag.root,"http://domain/project/tags");
      assertEquals(tag.prefix,"test");
      Location branch=LocationType.build("http://domain/project/branches/token/resource","branch",LocationType.BRANCHES);
      assertEquals(branch.path,"/resource");
      assertEquals(branch.root,"http://domain/project/branches");
      assertEquals(branch.prefix,"branch");
   }
}
