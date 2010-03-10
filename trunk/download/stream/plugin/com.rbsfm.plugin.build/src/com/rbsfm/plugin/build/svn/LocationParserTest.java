package com.rbsfm.plugin.build.svn;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
public class LocationParserTest{
   @Test
   public void parse() throws Exception{
      Location trunk = LocationParser.parse("http://domain/project/trunk/resource");
      assertEquals(trunk.path, "/resource");
      assertEquals(trunk.root, "http://domain/project/trunk");
      assertEquals(trunk.prefix, "");
      Location branches = LocationParser.parse("http://domain/project/branches/token/resource");
      assertEquals(branches.path, "/resource");
      assertEquals(branches.root, "http://domain/project/branches");
      assertEquals(branches.prefix, "token");
   }
   @Test
   public void build() throws Exception{
      Copy tag = LocationParser.copy("http://domain/project/trunk/resource", "test", Parent.TAGS);
      assertEquals(tag.to.path, "/resource");
      assertEquals(tag.to.root, "http://domain/project/tags");
      assertEquals(tag.to.prefix, "test");
      Copy branch = LocationParser.copy("http://domain/project/branches/token/resource", "branch", Parent.BRANCHES);
      assertEquals(branch.to.path, "/resource");
      assertEquals(branch.to.root, "http://domain/project/branches");
      assertEquals(branch.to.prefix, "branch");
   }
}
