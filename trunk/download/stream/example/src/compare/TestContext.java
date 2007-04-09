package compare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import simple.xml.Attribute;
import simple.xml.Root;
import simple.xml.load.Validate;

@Root(name="context")
public class TestContext {

   @Attribute(name="identifier")
   private String identifier;
   
   @Attribute(name="rootPath")
   private String root;
   
   @Validate
   public void validate(Map session) throws IOException {
      File file = new File(root);
      
      if(!file.exists()) {
         throw new FileNotFoundException("Root path '" +file+ "' does not exist");
      }
      session.put(identifier, file.getAbsolutePath());
   }
   
}
