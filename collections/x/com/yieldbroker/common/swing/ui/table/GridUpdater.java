package com.yieldbroker.common.swing.ui.table;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GridUpdater implements Grid {

	private final TableModel tableModel;
	private final Map<Object, Row> grid;
	private final List<Row> rows;

	public GridUpdater(TableModel tableModel) {
		this.grid = new ConcurrentHashMap<Object, Row>();
		this.rows = new CopyOnWriteArrayList<Row>();
		this.tableModel = tableModel;
	}
	
	@Override
	public int size() {
		return rows.size();
	}

	@Override
	public Row get(int row) {
		return rows.get(row);
	}
	
	@Override
	public Value get(int row, int column) {
		return get(row).get(column);
	}

	@Override
	public void update(Row row) {
		Object key = row.getKey();

		if (grid.containsKey(key)) {
			update(key, row);
		} else {
			insert(key, row);
		}
	}
	
	@Override
	public void remove(Row row) {
		Object key = row.getKey();
		Row existing = grid.remove(key);
		
		if(existing != null) {
			rows.remove(existing);
		}
	}

	private void update(Object key, Row row) {
		Row existing = grid.get(key);
		
		for (int i = 0; i < row.size(); i++) {
			Value current = existing.get(i);
			Value update = row.get(i);
			String text = update.get();

			if(!update.isIgnore()) {
				if (!equals(current, update)) {
					current.flash();			
				}
				current.set(text);
			}
		}
	}
	
	private boolean equals(Value current, Value update) {
		String before = current.get();
		String after = update.get();
		
		if(before != null && after != null) {
			return before.equals(after);
		}
		return before == after;
	}
	
	private void insert(Object key, Row row) {
		int bottom = rows.size();
		
		grid.put(key, row);
		rows.add(row);
		tableModel.fireTableRowsInserted(bottom, bottom);
	}

	@Override
	public void refresh() {
		for (int i = 0; i < rows.size(); i++) {
			Row row = rows.get(i);

			for (int j = 0; j < row.size(); j++) {
				Value value = row.get(j);

				if (value.isFlash()) {
					value.refresh();
					tableModel.fireTableCellUpdated(i, j);
				}
			}
		}
	}
}
