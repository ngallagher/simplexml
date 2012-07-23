package com.yieldbroker.common.swing.ui.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel implements TableUpdater {

	private final List<Column> columns;
	private final List<String> names;
	private final Grid grid;

	public TableModel(List<Column> columns, List<String> names) {
		this.grid = new GridUpdater(this);
		this.columns = columns;
		this.names = names;
	}

	@Override
	public int getRowCount() {
		return grid.size();
	}

	@Override
	public int getColumnCount() {
		return names.size();
	}

	@Override
	public String getColumnName(int column) {
		return names.get(column);
	}

	@Override
	public Class getColumnClass(int column) {
		return columns.get(column).getType();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public Object getValueAt(int row, int column) {
		Row entry = grid.get(row);

		if (entry != null) {
			return entry.get(column).get();
		}
		return null;
	}

	@Override
	public void refresh() {
		grid.refresh();
	}

	@Override
	public void update(Object object) {
		List<Value> values = new ArrayList<Value>();
		Row row = new Row(values);
		
		for (Column column : columns) {
			Value value = column.getValue(object);
			values.add(value);
		}
		grid.update(row);
	}

	public Cell getCell(int row, int column) {
		return new Cell(grid);
	}
}
