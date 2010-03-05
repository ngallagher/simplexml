package com.rbsfm.plugin.build.handler;  
  
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.handlers.HandlerUtil;
  
public class PublishHandler extends AbstractHandler {  
    private QualifiedName path = new QualifiedName("html", "path");  
  
    public Object execute(ExecutionEvent event) throws ExecutionException {  
       ApplicationWindow window = new ApplicationWindow(HandlerUtil.getActiveShell(event)) {
    		  protected Control createContents(Composite composite) {
    			  return new PublishModuleWindow(composite);
    		  }
       };
       window.open();

       MessageDialog.openInformation(HandlerUtil.getActiveShell(event),  
             "Information", "Please select a Java source file");  
        IStructuredSelection selection = (IStructuredSelection) HandlerUtil  
                .getActiveMenuSelection(event);  
        DirectoryDialog fileDialog = new DirectoryDialog(HandlerUtil  
                .getActiveShell(event));  
        String directory = "";  
        Object firstElement = selection.getFirstElement();  
        if (firstElement instanceof ICompilationUnit) {  
            ICompilationUnit cu = (ICompilationUnit) firstElement;  
            IResource res = cu.getResource();  
            boolean newDirectory = true;  
            directory = getPersistentProperty(res, path);  
  
            if (directory != null && directory.length() > 0) {  
                newDirectory = !(MessageDialog.openQuestion(HandlerUtil  
                        .getActiveShell(event), "Question",  
                        "Use the previous output directory?"));  
            }  
            if (newDirectory) {  
                directory = fileDialog.open();  
  
            }  
            if (directory != null && directory.length() > 0) {  
                analyze(cu);  
                setPersistentProperty(res, path, directory);  
                write(directory, cu);  
            }  
  
        } else {  
            MessageDialog.openInformation(HandlerUtil.getActiveShell(event),  
                    "Information", "Please select a Java source file");  
        }  
  
        // iterator.next();  
  
        // }  
        return null;  
    }  
  
    protected String getPersistentProperty(IResource res, QualifiedName qn) {  
        try {  
            return  res.getPersistentProperty(qn);  
        } catch (CoreException e) {  
            return "";  
        }  
    }  
  
    // TODO: Include this in the HTML output  
  
    private void analyze(ICompilationUnit cu) {  
        // Cool JDT allows you to analyze the code easily  
        // I don't see really a use case here but I just wanted to do this here  
        // as I consider this as cool and  
        // what to have a place where I can store the data  
        try {  
  
            IType type = null;  
            IType[] allTypes;  
            allTypes = cu.getAllTypes();  
            /** 
             * Search the public class 
             */  
            for (int t = 0; t < allTypes.length; t++) {  
                if (Flags.isPublic((allTypes[t].getFlags()))) {  
                    type = allTypes[t];  
                    break;  
                }  
            }  
  
            String classname = type.getFullyQualifiedName();  
            IMethod[] methods = type.getMethods();  
        } catch (JavaModelException e) {  
            e.printStackTrace();  
        }  
  
    }  
  
    protected void setPersistentProperty(IResource res, QualifiedName qn,  
            String value) {  
        try {  
            res.setPersistentProperty(qn, value);  
        } catch (CoreException e) {  
            e.printStackTrace();  
        }  
    }  
  
    private void write(String dir, ICompilationUnit cu) {  
        try {  
            cu.getCorrespondingResource().getName();  
            String test = cu.getCorrespondingResource().getName();  
            // Need  
            String[] name = test.split("\\.");  
            System.out.println(test);  
            System.out.println(name.length);  
            String htmlFile = dir + "\\" + name[0] + ".html";  
  
            System.out.println(htmlFile);  
            FileWriter output = new FileWriter(htmlFile);  
            BufferedWriter writer = new BufferedWriter(output);  
            writer.write("<html>");  
            writer.write("<head>");  
            writer.write("</head>");  
            writer.write("<body>");  
            writer.write("<pre>");  
            writer.write(cu.getSource());  
            writer.write("</pre>");  
            writer.write("</body>");  
            writer.write("</html>");  
            writer.flush();  
        } catch (JavaModelException e) {  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
    }  
    
    public static class PublishModuleWindow extends Composite {

        // input fields;  members so they can be referenced from event handlers
        Text moduleField;
        Text revisionField;
        Text branchField;
        Text branchRevisionField;
        Text mailField;

        ArrayList fields = new ArrayList();     // all fields

        // reset all registered fields
        protected void clearFields() {
            for (Iterator i = fields.iterator(); i.hasNext();) {
                ((Text)i.next()).setText("");
            }
        }

        /**
         * Constructor.
         */
        public PublishModuleWindow(Composite parent) {
            this(parent, SWT.NONE);  // must always supply parent
        }
        /**
         * Constructor.
         */
        public PublishModuleWindow(Composite parent, int style) {
            super(parent, style);   // must always supply parent and style
            createGui();
        }

        // GUI creation helpers

        protected Text createLabelledText(Composite parent, String label) {
            return createLabelledText(parent, label, 20, null);
        }
        protected Text createLabelledText(Composite parent, String label, int limit, String tip) {
            Label l = new Label(parent, SWT.LEFT);
            l.setText(label);
            Text text  = new Text(parent, SWT.SINGLE | SWT.BORDER);
            if (limit > 0) {
                text.setTextLimit(limit);
            }
            if (tip != null) {
                text.setToolTipText(tip);
            }
            text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            fields.add(text);
            return text;
        }

        protected Button createButton(Composite parent, String label, SelectionListener l) {
            return createButton(parent, label, l);
        }
        protected Button createButton(Composite parent, String label, String tip, SelectionListener l) {
            Button b = new Button(parent, SWT.NONE);
            b.setText(label);
            if (tip != null) {
                b.setToolTipText(tip);
            }
            if (l != null) {
                b.addSelectionListener(l);
            }
            return b;
        }

        // partial selection listener
        class MySelectionAdapter implements SelectionListener {
            public void widgetSelected(SelectionEvent e) {
                // default is to do nothing
            }
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        };

        protected void createGui() {
            setLayout(new GridLayout(1, true));

            // create the input area

            Group entryGroup = new Group(this, SWT.NONE);
            entryGroup.setText("Module Details");
            // use 2 columns, not same width
            GridLayout entryLayout = new GridLayout(2, false);
            entryGroup.setLayout(entryLayout);
            entryGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

            moduleField    = createLabelledText(entryGroup, "Module: ", 40, "Enter the module name");
            revisionField   = createLabelledText(entryGroup, "Revision: ", 40, "Enter the revision");
            branchField   = createLabelledText(entryGroup, "Branch: ", 20, "Enter the branch");
            branchRevisionField   = createLabelledText(entryGroup, "Branch Revision: ", 20, "Enter the branch revision");
            mailField   = createLabelledText(entryGroup, "Mail: ", 20, "Enter your mail address");
            
            
            // create the button area

            Composite buttons = new Composite(this, SWT.NONE);
            buttons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            // make all buttons the same size
            FillLayout buttonLayout = new FillLayout();
            buttonLayout.marginHeight = 2;
            buttonLayout.marginWidth = 2;
            buttonLayout.spacing = 5;
            buttons.setLayout(buttonLayout);

            // OK button prints input values
            Button okButton = createButton(buttons, "&Publish", "Publish",
                                           new MySelectionAdapter() {
                                               public void widgetSelected(SelectionEvent e) {
                                                   System.out.println("Module:         " + moduleField.getText());
                                                   System.out.println("Revision:      " + revisionField.getText());
                                                   System.out.println("Branch: " + branchField.getText());
                                                   System.out.println("Branch Revision:      " + branchRevisionField.getText());
                                                   System.out.println("Mail: " + mailField.getText());
                                               }
                                           });

            // Clear button resets input values
            Button clearButton = createButton(buttons, "&Clear", "Clear inputs",
                                              new MySelectionAdapter() {
                                                  public void widgetSelected(SelectionEvent e) {
                                                      clearFields();
                                                      moduleField.forceFocus();
                                                  }
                                              });
        }
    }
}  
