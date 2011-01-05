package com.rbsfm.plugin.build.ui;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
public class MessageLogger {
  public static void openInformation(Shell shell, String title, String message) {
    if(shell != null) {
      MessageDialog.openInformation(shell, title, message);
    } else {
      System.err.printf("%s: %s%n", title, message);
    }    
  }
  public static void openError(Shell shell, String title, String message) {
    if(shell != null) {
      MessageDialog.openError(shell, title, message);
    } else {
      System.err.printf("%s: %s%n", title, message);
    } 
  }
}
