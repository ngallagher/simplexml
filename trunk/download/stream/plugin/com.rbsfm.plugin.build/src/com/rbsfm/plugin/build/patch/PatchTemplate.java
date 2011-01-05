package com.rbsfm.plugin.build.patch;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
public class PatchTemplate {
  private final String patchName;
  private final String patchHostDir;
  private final String configGroup;
  private final String tagName;
  private final String includeRegex;
  private final String excludeRegex;
  public PatchTemplate(String patchName, String configGroup, String scmTag, String patchHostDir, String includeRegex, String excludeRegex) {
    this.patchHostDir = patchHostDir;
    this.configGroup = configGroup;
    this.tagName = scmTag;
    this.includeRegex = includeRegex;
    this.excludeRegex = excludeRegex;
    this.patchName = patchName;    
  }
  public String build(List<PatchFile> patchFiles) throws Exception {
    Map<String, PatchFile> sortedFiles = new TreeMap<String, PatchFile>();
    for(PatchFile patchFile : patchFiles) {
      sortedFiles.put(String.format("rbsfm/%s", patchFile.getPath()), patchFile);
    }
    StringWriter writer = new StringWriter();
    PrintWriter template = new PrintWriter(writer);
    template.println("<?xml version='1.0'?>");
    template.printf("<project name='%s-%s' default='get.patch.files' basedir='.'>%n", tagName, patchName);
    template.println("   <import file='${basedir}/build/build-scm.xml'/>");
    template.println("   <target name='set.patch.properties'>");
    template.printf("      <property name='config.group' value='%s'/>%n", configGroup);
    template.printf("      <property name='scm.tag' value='%s'/>%n", tagName);
    template.printf("      <property name='patch.host.dir' value='%s'/>%n", patchHostDir);
    if(includeRegex != null && !includeRegex.equals("")) {
      template.printf("      <property name='include.servers.regex' value='%s'/>%n", includeRegex);
    }
    if(excludeRegex != null && !excludeRegex.equals("")) {
      template.printf("      <property name='exclude.servers.regex' value='%s'/>%n", excludeRegex);
    }
    template.println("   </target>");
    template.println("   <target name='get.patch.files'>");
    for(String filePath : sortedFiles.keySet()) {
      PatchFile patchFile = sortedFiles.get(filePath);
      long version = patchFile.getVersion();
      template.printf("      <scmpatchcheckout revision='%s' tag='%s-%s' file='%s'/>%n", version, tagName, patchName, filePath);
    }
    template.println("   </target>");
    template.println("</project>");
    template.flush();
    return writer.toString();
  }
  
}
