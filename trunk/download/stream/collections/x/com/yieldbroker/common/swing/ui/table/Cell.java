package com.yieldbroker.common.swing.ui.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class Cell extends JLabel implements TableCellRenderer {

   private final Grid grid;

   public Cell(Grid grid) {
      this.grid = grid;
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object item, boolean isSelected, boolean hasFocus, int row, int column) {
      int modelColumn = table.convertColumnIndexToModel(column);
      int modelRow = table.convertRowIndexToModel(row);
      Value value = grid.get(modelRow, modelColumn);
      Color color = value.color();
      String text = String.valueOf(item);

      setOpaque(true);
      setText(text);
      setBackground(color);
      return this;
   }
}
