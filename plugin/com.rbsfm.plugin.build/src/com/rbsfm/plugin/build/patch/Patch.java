package com.rbsfm.plugin.build.patch;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import com.rbsfm.plugin.build.svn.Repository;
@Root
public class Patch {  
  @Path("selectedElements")
  @ElementList(inline=true, entry="javaElement")
  private List<JavaElement> javaElements;
  @Path("selectedElements")
  @ElementList(inline=true, entry="file")
  private List<FileElement> fileElements;
  public List<String> getElements(){
    List<String> elements = new ArrayList<String>();
    for(JavaElement element : javaElements){
      elements.add(element.getIdentifier());
    }
    for(FileElement element : fileElements){
      elements.add(element.getIdentifier());
    }
    return elements;
  }
  public List<PatchFile> collect(Repository repository, File root) throws Exception {
    PatchFileParser parser = new PatchFileParser(repository, root);
    List<PatchFile> patchFiles = new ArrayList<PatchFile>();
    for(JavaElement element : javaElements) {
      String identifier = element.getIdentifier();
      List<PatchFile> expandedList = parser.parseIdentifier(identifier);
      patchFiles.addAll(expandedList);
    }
    for(FileElement element : fileElements) {
      String identifier = element.getIdentifier();
      List<PatchFile> expandedList = parser.parseFile(identifier);
      patchFiles.addAll(expandedList);
    }
    return patchFiles;
  }
  @Root
  private static class FileElement {
    private @Attribute String path;
    public String getIdentifier() {
      return path;
    }
  }
  @Root
  private static class JavaElement {
    private @Attribute String handleIdentifier;    
    public String getIdentifier(){
      return handleIdentifier;
    }
  }
}
