package com.rbsfm.plugin.build.assemble;
import java.io.File;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import com.rbsfm.plugin.build.svn.Repository;
import com.rbsfm.plugin.build.svn.Scheme;
import com.rbsfm.plugin.build.svn.Subversion;
import com.rbsfm.plugin.build.ui.ButtonWindow;
class ProjectAssemblyWindow extends ButtonWindow{
   private IPreferenceStore store;
   private Text loginField;
   private Text passwordField;
   private Text projectField;
   private Text tagField;
   private Text installField;
   private Text environmentsField;
   private Text mailField;
   private File file;
   public ProjectAssemblyWindow(Composite parent,File file,IPreferenceStore store){
      super(parent);
      this.store=store;
      this.file = file;
      createGui();
   }
   private void createProjectDetails(){
      Group entryGroup=new Group(this,SWT.NONE);
      entryGroup.setText("Project Details");
      GridLayout entryLayout=new GridLayout(2,false);
      entryGroup.setLayout(entryLayout);
      entryGroup.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
      projectField=createLabelledText(entryGroup,"Project: ",40,"Enter the project name",null);
      tagField=createLabelledText(entryGroup,"Tag: ",40,"Enter the tag",null);
      installField=createLabelledText(entryGroup,"Install Name: ",40,"Enter install name",null);
      environmentsField=createLabelledText(entryGroup,"Environments: ",40,"Enter a comma separated list of environments",null);
      mailField=createLabelledText(entryGroup,"Mail: ",40,"Enter your mail address",null);
   }
   private void createSubversion(){
      Group subversionGroup=new Group(this,SWT.NONE);
      subversionGroup.setText("Subversion");
      GridLayout subversionLayout=new GridLayout(2,false);
      subversionGroup.setLayout(subversionLayout);
      subversionGroup.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
      loginField=createLabelledText(subversionGroup,"Login: ",40,"Enter your user name",System.getProperty("user.name"));
      passwordField=createLabelledText(subversionGroup,"Password: ",40,"Enter your password","password");  
   }
   private void createGui(){
      setLayout(new GridLayout(1,true));
      createProjectDetails();
      createSubversion();
      Composite buttons=new Composite(this,SWT.NONE);
      buttons.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
      FillLayout buttonLayout=new FillLayout();
      buttonLayout.marginHeight=2;
      buttonLayout.marginWidth=2;
      buttonLayout.spacing=5;
      buttons.setLayout(buttonLayout);
      createButton(buttons,"&Assemble","Assemble",new SelectionAdapter(){
         public void widgetSelected(SelectionEvent event){
            try{
               String login=loginField.getText();
               String password=passwordField.getText();
               String projectName=projectField.getText();
               String installName=installField.getText();
               String tagName=tagField.getText();
               String environments=environmentsField.getText();
               String mailAddress=mailField.getText();
               String[] environmentList=environments.split("\\s*,\\s*");
               Repository repository=Subversion.login(Scheme.SVN,login,password);
               ProjectAssembler assembler=new ProjectAssembler(repository,getShell());
               
               //store.putValue(login,password);
               assembler.assemble(file,projectName,installName,tagName,environmentList,mailAddress);
            }catch(Exception e){
               MessageDialog.openInformation(getShell(),"Error",e.getClass()+": "+e.getMessage());
               throw new RuntimeException(e);
            }
         }
      });
      createButton(buttons,"&Clear","Clear inputs",new SelectionAdapter(){
         public void widgetSelected(SelectionEvent e){
            clearFields();
            projectField.forceFocus();
         }
      });
   }
}