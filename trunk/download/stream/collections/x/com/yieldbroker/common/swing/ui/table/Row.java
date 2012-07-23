package com.yieldbroker.common.swing.ui.table;

import java.util.List;

public class Row {

   private final List<Value> values;

   public Row(List<Value> cells) {
      this.values = cells;
   }
   
   public Object getKey() {
	   return get(0).get();
   }
   
   public int size() {
	   return values.size();
   }

   public void set(int column, String text) {
      Value value = values.get(column);
      
      if(value == null) {
         throw new TableException("Column %s does not exist", column);
      }
      value.set(text);
   }

   public Value get(int column) {
      int size = values.size();
      
      if(column >= size) {
         throw new TableException("Column %s does not exist", column);
      }
      return values.get(column);
   }
}