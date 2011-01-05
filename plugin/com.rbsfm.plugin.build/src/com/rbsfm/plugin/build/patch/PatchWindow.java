package com.rbsfm.plugin.build.patch;
import java.io.File;
import java.io.FileNotFoundException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.rbsfm.plugin.build.ui.InputWindow;
import com.rbsfm.plugin.build.ui.MessageFormatter;
import com.rbsfm.plugin.build.ui.MessageLogger;
class PatchWindow extends InputWindow{
   private Text loginField;
   private Text passwordField;
   private Text patchNameField;
   private Text configGroupField;
   private Text tagNameField;
   private Text hostDirectoryField;
   private Text includeRegexField;
   private Text excludeRegexField;
   private Text idField;
   private Patch patch;
   private File file;
   public PatchWindow(Composite parent,Patch patch,File file){
      super(parent);
      this.patch = patch;
      this.file = file;
      createGui();
   }
   private File resolveWorkspacePath() throws Exception {
     File root = file;
     while(root.exists()) {
       File parent = root.getParentFile();
       File metadata = new File(parent, ".metadata");
       if(metadata.exists() && metadata.isDirectory()) {
         return parent.getCanonicalFile();
       }
       root = parent;
     }
     throw new FileNotFoundException("Workspace direcory not found");
   }
   private void createProjectDetails(){
      Group entryGroup = new Group(this, SWT.NONE);
      entryGroup.setText("Project Details");
      GridLayout entryLayout = new GridLayout(2, false);
      entryGroup.setLayout(entryLayout);
      entryGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      patchNameField = createLabelledText(entryGroup, "Patch Name: ", 40, "Enter the name of the patch", null);
      configGroupField = createLabelledText(entryGroup, "Config Group: ", 40, "Enter either rates or credit", null);
      tagNameField = createLabelledText(entryGroup, "Tag Name: ", 40, "Enter the original tag name", null);
      hostDirectoryField = createLabelledText(entryGroup, "Host Directory: ", 200, "Enter the patch host directory", null);
      includeRegexField = createLabelledText(entryGroup, "Include Regex: ", 400, "Enter a regular expression to include services", null);
      excludeRegexField = createLabelledText(entryGroup, "Exclude Regex: ", 400, "Enter a regular expression to exclude services", null);
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
      createButton(buttons, "&Patch", "Patch", new SelectionAdapter(){
         public void widgetSelected(SelectionEvent event){
            String login = loginField.getText();
            String password = passwordField.getText();
            Shell shell = getShell();
            try{
               PatchCreator creator = new PatchCreator(shell, login, password);
               File root = resolveWorkspacePath();
               String patchName = patchNameField.getText();
               String tagName = tagNameField.getText();
               String configGroup = configGroupField.getText();
               String hostDir = hostDirectoryField.getText();
               String includeRegex = includeRegexField.getText();
               String excludeRegex = excludeRegexField.getText();                     
               String id = idField.getText();
               creator.create(patch, root, patchName, configGroup, tagName, hostDir, includeRegex, excludeRegex, id);              
            }catch(Exception cause){
               MessageLogger.openInformation(getShell(), "Error", MessageFormatter.format(cause));
               throw new RuntimeException(cause);
            }
         }
      });
      createButton(buttons, "&Clear", "Clear inputs", new SelectionAdapter(){
         public void widgetSelected(SelectionEvent e){
            clearFields();
            patchNameField.forceFocus();
         }
      });
   }
}