package com.rbsfm.plugin.build.assemble;
import java.io.File;
import java.util.prefs.Preferences;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.rbsfm.plugin.build.ivy.Project;
import com.rbsfm.plugin.build.ui.InputWindow;
import com.rbsfm.plugin.build.ui.MessageFormatter;
import com.rbsfm.plugin.build.ui.MessageLogger;
class ProjectAssemblyWindow extends InputWindow{
   private Text loginField;
   private Text passwordField;
   private Text projectField;
   private Text tagField;
   private Text installField;
   private Text environmentsField;
   private Text mailField;
   private Text idField;
   private Project project;
   private File file;
   public ProjectAssemblyWindow(Composite parent,Project project,File file){
      super(parent);
      this.project = project;
      this.file = file;
      createGui();
   }
   private void createProjectDetails(){
      Group entryGroup = new Group(this, SWT.NONE);
      entryGroup.setText("Project Details");
      GridLayout entryLayout = new GridLayout(2, false);
      entryGroup.setLayout(entryLayout);
      entryGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      projectField = createLabelledText(entryGroup, "Project: ", 40, "Enter the project name", project.getProject());
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
               ProjectAssembler assembler = new ProjectAssembler(shell, login, password);
               String projectName = projectField.getText();
               String installName = installField.getText();
               String tagName = tagField.getText();
               String environments = environmentsField.getText();
               String mailAddress = mailField.getText();         
               String id = idField.getText();
               assembler.assemble(file, projectName, installName, tagName, environments, mailAddress, id);
            }catch(Exception cause){
               MessageLogger.openInformation(getShell(), "Error", MessageFormatter.format(cause));
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
      createButton(buttons, "&Server", "Set server location", new SelectionAdapter(){
        public void widgetSelected(SelectionEvent e) {
          Preferences preferences = Preferences.userNodeForPackage(ProjectAssemblyRequestBuilder.class);
          String server = preferences.get("server", "");
          InputDialog dialog = new InputDialog(getShell(), "Server location", "Set the location of the assembly server", server, null);
          dialog.open();
          String result = dialog.getValue();
          if(result != null && !result.equals("")) {
            preferences.put("server", result);
          }
          projectField.forceFocus();          
        }
      });
   }
}