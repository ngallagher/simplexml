package com.yieldbroker.common.swing.ui.table;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class TablePanel extends JTable {
	
	private final TableModel tableModel;
	
	public TablePanel(TableModel tableModel) {
		this.tableModel = tableModel;
		this.setModel(tableModel);
	}	
	
	public TableUpdater getUpdater() {
		return tableModel;
	}
	
	@Override
	public TableModel getModel() {
		return tableModel;
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return tableModel.getCell(row, column);
	}
}
