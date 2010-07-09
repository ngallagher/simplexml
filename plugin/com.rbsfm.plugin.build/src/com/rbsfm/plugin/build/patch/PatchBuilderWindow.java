package com.rbsfm.plugin.build.patch;
import java.io.File;
import java.net.URI;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.rbsfm.plugin.build.svn.Repository;
import com.rbsfm.plugin.build.svn.Scheme;
import com.rbsfm.plugin.build.svn.Subversion;
import com.rbsfm.plugin.build.ui.InputWindow;
import com.rbsfm.plugin.build.ui.MessageFormatter;
class PatchBuilderWindow extends InputWindow{
   private Text loginField;
   private Text passwordField;
   private Text projectField;
   private Text tagField;
   private Text installField;
   private Text environmentsField;
   private Text mailField;
   private Text idField;
   private Jardesc jardesc;
   private File file;
   public PatchBuilderWindow(Composite parent,Jardesc jardesc,File file){
      super(parent);
      this.jardesc = jardesc;
      this.file = file;
      createGui();
   }
   private void createProjectDetails(){
      Group entryGroup = new Group(this, SWT.NONE);
      entryGroup.setText("Project Details");
      GridLayout entryLayout = new GridLayout(2, false);
      entryGroup.setLayout(entryLayout);
      entryGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      projectField = createLabelledText(entryGroup, "Project: ", 40, "Enter the project name", null);
      tagField = createLabelledText(entryGroup, "Tag: ", 40, "Enter the tag", null);
      installField = createLabelledText(entryGroup, "Install Name: ", 40, "Enter install name", null);
      environmentsField = createLabelledText(entryGroup, "Environments: ", 40, "Enter a comma separated list of environments", null);
      mailField = createLabelledText(entryGroup, "Mail: ", 40, "Enter your mail address", null);
   }
   private void createSubversion(){
      Group subversionGroup = new Group(this, SWT.NONE);
      subversionGroup.setText("Subversion");
      GridLayout subversionLayout = new GridLayout(2, false);
      subversionGroup.setLayout(subversionLayout);
      subversionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      loginField = createLabelledText(subversionGroup, "Login: ", 40, "Enter your user name", System.getProperty("user.name"));
      passwordField = createLabelledText(subversionGroup, "Password: ", 40, "Enter your password", null, true);
   }
   private void createJIRA(){
      Group subversionGroup = new Group(this, SWT.NONE);
      subversionGroup.setText("JIRA");
      GridLayout subversionLayout = new GridLayout(2, false);
      subversionGroup.setLayout(subversionLayout);
      subversionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      idField = createLabelledText(subversionGroup, "ID: ", 40, "Enter a JIRA ID", null);
   }
   private void createGui(){
      setLayout(new GridLayout(1, true));
      createProjectDetails();
      createSubversion();
      createJIRA();
      Composite buttons = new Composite(this, SWT.NONE);
      buttons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
      FillLayout buttonLayout = new FillLayout();
      buttonLayout.marginHeight = 2;
      buttonLayout.marginWidth = 2;
      buttonLayout.spacing = 5;
      buttons.setLayout(buttonLayout);
      createButton(buttons, "&Assemble", "Assemble", new SelectionAdapter(){
         public void widgetSelected(SelectionEvent event){
            String login = loginField.getText();
            String password = passwordField.getText();
            Shell shell = getShell();
            try{
              // MessageDialog.openInformation(getShell(), "Error", jardesc.getIdentifiers().toString());
               /*IProject[] project = ResourcesPlugin.getWorkspace().getRoot().getProjects();
               StringBuilder builder = new StringBuilder();
               List<SourceIdentifier> identifiers = jardesc.getSourceFiles();
               Repository repo = Subversion.login(Scheme.HTTP, "gallane", "ca_dm_ecomtech_111");
               Workspace workspace = new Workspace(repo);
               for(SourceIdentifier source : identifiers) {
                  List<Resource> list = workspace.find(source);
                  for(Resource resource : list) {
                     builder.append(String.format("[%s] [%s] [%s]%n", resource.getFile(), resource.getVersion(), resource.getProjectDirectory()));
                  }
                  
               }*/
               PatchBuilder builder = new PatchBuilder(shell, "gallane", "ca_dm_ecomtech_111");
               builder.build(jardesc, "ceemea-2009WK52-22", "ceemea-2009WK52-22-patch-001", "rates", "\\\\lonms06277\\d$\\uat\\rbsfm\\cp\\ceemea-2009WK52-22", "ceemea-pricing-instmaint");
               /*
               List<SourceIdentifier> files = jardesc.getFiles();
               for(SourceIdentifier source : files) {
                  if(source.isFile()) {
                     String loc = ResourcesPlugin.getWorkspace().getRoot().getProject(source.getProject()).getFolder(source.getFolder()).getRawLocationURI().toString();
                     String path = loc + source.getFile();
                     URI uri = new URI(path);
                     File file = new File(uri);
                     if(file.exists()) {
                        builder.append(String.format("path=[%s] rev=[%s] project=[%s] file=[%s]%n",file.getAbsolutePath(),repo.info(file).version,source.getProject(),source.getFolder()));
                     } else {
                        builder.append("No such file ").append(loc).append("/").append(source.getFile());
                     }                     
                  }
               }*/
               //builder.append(Path.fromPortableString("/cpbuild/config/templates/pricing.xml").toFile().getAbsolutePath());
               //for(IProject p : project){
                //  builder.append(p.getFolder("src").getRawLocationURI().toString()+",");
              // }
               MessageDialog.openInformation(getShell(), "Error", builder.toString());
              // ProjectAssembler assembler = new ProjectAssembler(shell, login, password);
               String projectName = projectField.getText();
               String installName = installField.getText();
               String tagName = tagField.getText();
               String environments = environmentsField.getText();
               String mailAddress = mailField.getText();         
               String id = idField.getText();
             //  assembler.assemble(file, projectName, installName, tagName, environments, mailAddress, id);
            }catch(Exception cause){
               MessageDialog.openInformation(getShell(), "Error", MessageFormatter.format(cause));
               throw new RuntimeException(cause);
            }
         }
      });
      createButton(buttons, "&Clear", "Clear inputs", new SelectionAdapter(){
         public void widgetSelected(SelectionEvent e){
            clearFields();
            projectField.forceFocus();
         }
      });
   }
}