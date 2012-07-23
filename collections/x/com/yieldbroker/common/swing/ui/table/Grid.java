package com.yieldbroker.common.swing.ui.table;

public interface Grid {
	int size();
	Row get(int row);
	Value get(int row, int column);
	void update(Row row);
	void remove(Row row);	
	void refresh();
}
