package com.rbsfm.plugin.build.publish;
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
import com.rbsfm.plugin.build.ivy.Module;
import com.rbsfm.plugin.build.svn.Repository;
import com.rbsfm.plugin.build.svn.Scheme;
import com.rbsfm.plugin.build.svn.Subversion;
import com.rbsfm.plugin.build.ui.ButtonWindow;
public class ModulePublicationWindow extends ButtonWindow{
   private IPreferenceStore store;
   private Module module;
   private Text moduleField;
   private Text revisionField;
   private Text branchField;
   private Text branchRevisionField;
   private Text mailField;
   private File file;
   public ModulePublicationWindow(Composite parent,Module module,File file,IPreferenceStore store){
      super(parent); 
      this.store=store;
      this.module=module;
      this.file=file;
      createGui();
   }
   private void createGui(){ 
      setLayout(new GridLayout(1,true));
      Group entryGroup=new Group(this,SWT.NONE);
      entryGroup.setText("Module Details");
      GridLayout entryLayout=new GridLayout(2,false);
      entryGroup.setLayout(entryLayout);
      entryGroup.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
      moduleField=createLabelledText(entryGroup,"Module: ",40,"Enter the module name",module.getModule());
      revisionField=createLabelledText(entryGroup,"Revision: ",40,"Enter the revision",module.getRevision());
      branchField=createLabelledText(entryGroup,"Branch: ",40,"Enter the branch",module.getBranch());
      branchRevisionField=createLabelledText(entryGroup,"Branch Revision: ",40,"Enter the branch revision",module.getBranchRevision());
      mailField=createLabelledText(entryGroup,"Mail: ",40,"Enter your mail address",null);
      Composite buttons=new Composite(this,SWT.NONE);
      buttons.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
      FillLayout buttonLayout=new FillLayout();
      buttonLayout.marginHeight=2;
      buttonLayout.marginWidth=2;
      buttonLayout.spacing=5;
      buttons.setLayout(buttonLayout);
      createButton(buttons,"&Publish","Publish",new SelectionAdapter(){
         public void widgetSelected(SelectionEvent event){
            try{
               String login = "gallane";
               String password="password";
               String moduleName = moduleField.getText();
               String revision=revisionField.getText();
               String branch=branchField.getText();
               String branchRevision=branchRevisionField.getText();
               String mailAddress=mailField.getText();
               Repository repository=Subversion.login(Scheme.SVN,login,password);
               ModulePublisher publisher=new ModulePublisher(repository,getShell());
               
               store.putValue(login,password);    
               publisher.publish(file,moduleName,revision,branch,branchRevision,mailAddress);
            }catch(Exception e){
               MessageDialog.openInformation(getShell(),"Error",e.getClass()+": "+e.getMessage());
               throw new RuntimeException(e);
            }
         }
      });
      createButton(buttons,"&Clear","Clear inputs",new SelectionAdapter(){
         public void widgetSelected(SelectionEvent e){
            clearFields();
            moduleField.forceFocus();
         }
      });
   }
}