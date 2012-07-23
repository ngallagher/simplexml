package com.yieldbroker.common.swing.ui.table;

import static java.awt.Color.WHITE;

import java.awt.Color;

public class Flash {

   private Gradient gradient;
   private int count;

   public Flash(Gradient gradient) {
      this.gradient = gradient;
   }
   
   public boolean isFlash() {
      return !color().equals(WHITE);
   }
   
   public void flash() {
      this.count = 0;
   }
   
   public Color color() {
      return gradient.get(count);
   }
   
   public Color fade() {
      return gradient.get(count++);
   }
}
