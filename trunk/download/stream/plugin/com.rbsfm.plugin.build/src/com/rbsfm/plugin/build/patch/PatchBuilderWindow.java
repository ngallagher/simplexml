package com.rbsfm.plugin.build.patch;
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
import com.rbsfm.plugin.build.ui.InputWindow;
import com.rbsfm.plugin.build.ui.MessageFormatter;
class PatchBuilderWindow extends InputWindow{
   private Text loginField;
   private Text passwordField;
   private Text buildTagField;
   private Text patchTagField;
   private Text groupField;
   private Text hostPathField;
   private Text includeField;
   private Text idField;
   private Jardesc jardesc;
   public PatchBuilderWindow(Composite parent,Jardesc jardesc){
      super(parent);
      this.jardesc = jardesc;
      createGui();
   }
   private void createProjectDetails(){
      Group entryGroup = new Group(this, SWT.NONE);
      entryGroup.setText("Patch Details");
      GridLayout entryLayout = new GridLayout(2, false);
      entryGroup.setLayout(entryLayout);
      entryGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      buildTagField = createLabelledText(entryGroup, "Build Tag: ", 40, "Enter name of the build tag", null);
      patchTagField = createLabelledText(entryGroup, "Patch Tag: ", 40, "Enter the name of the patch tag", null);
      groupField = createLabelledText(entryGroup, "Group: ", 40, "Enter config group name", "rates");
      hostPathField = createLabelledText(entryGroup, "Host Path: ", 40, "Enter the deployment host path", null);
      includeField = createLabelledText(entryGroup, "Include: ", 40, "Enter regular expression of services to include", ".*");
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
               PatchBuilder builder = new PatchBuilder(shell, login, password);
               String buildTag = buildTagField.getText();
               String group = groupField.getText();
               String patchTag = patchTagField.getText();
               String hostPath = hostPathField.getText();
               String include = includeField.getText();         
               String id = idField.getText();
               builder.build(jardesc, buildTag, patchTag, group, hostPath, include, id);
            }catch(Exception cause){
               MessageDialog.openInformation(getShell(), "Error", MessageFormatter.format(cause));
               throw new RuntimeException(cause);
            }
         }
      });
      createButton(buttons, "&Clear", "Clear inputs", new SelectionAdapter(){
         public void widgetSelected(SelectionEvent e){
            clearFields();
            buildTagField.forceFocus();
         }
      });
   }
}