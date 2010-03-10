package com.rbsfm.plugin.build.ui;
public class MessageFormatter{
   public static String format(Throwable cause){
      return String.format("%s: %s", cause.getClass(), cause.getMessage());
   }
}
