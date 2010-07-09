package com.rbsfm.plugin.build.patch;
import java.io.StringWriter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.simpleframework.xml.core.Persister;
import com.rbsfm.plugin.build.svn.Repository;
import com.rbsfm.plugin.build.svn.Scheme;
import com.rbsfm.plugin.build.svn.Subversion;
public class PatchBuilder {
   private final Repository repository;
   private final ProjectBuilder builder;
   private final Persister persister;
   private final Shell shell;
   public PatchBuilder(Shell shell,String login, String password) throws Exception {
      this.repository = Subversion.login(Scheme.HTTP, login, password);
      this.builder = new ProjectBuilder(repository);
      this.persister = new Persister();
      this.shell = shell;
   }
   public void build(Jardesc jardesc, String tagName, String patchTag, String group, String host, String pattern, String jira) throws Exception {
      Project project = builder.build(patchTag, jardesc);
      project.addProperty("config.group", group);
      project.addProperty("scm.tag", tagName);
      project.addProperty("patch.host.dir", host);
      project.addProperty("include.servers.regex", pattern);
      StringWriter writer = new StringWriter();
      persister.write(project, writer);
      MessageDialog.openInformation(shell, "Error", writer.toString());
      
   }
}
