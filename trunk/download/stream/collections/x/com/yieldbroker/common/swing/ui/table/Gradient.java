package com.yieldbroker.common.swing.ui.table;

import static java.awt.Color.decode;

import java.awt.Color;

public enum Gradient {
   BLUE("blue", "#123456", "#163A5D", "#264C71", "#496E93", "#8BA7C2", "#FFFFFF"),
   RED("red", "#9F1D35", "#A3233B", "#AE374D", "#C25C6F", "#DC9CA8", "#FFFFFF"),
   GREEN("green", "#006400", "#046A04", "#147D14", "#389C38", "#7FC77F", "#FFFFFF"),
   NONE("none", "#FFFFFF");
   
   private final String[] sequence;
   private final String term;
   
   private Gradient(String term, String... sequence) {
      this.sequence = sequence;
      this.term = term;
   }
   
   public Color get(int count) {
      String code = "#FFFFFF";
      
      if(count < sequence.length) {
         code = sequence[count];
      }
      return decode(code);
   }
   
   public static Gradient resolve(String name) {
      if(name != null) {
         String term = name.toLowerCase();
         
         for(Gradient gradient : values()) {
            if(gradient.term.equals(term)) {
               return gradient;
            }
         }
      }
      return NONE;
   }
}
