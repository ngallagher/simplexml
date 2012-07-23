package com.yieldbroker.common.swing.ui.graph;

import java.util.LinkedList;

import com.yieldbroker.common.chart.Series;

public class GraphModel<T> implements GraphUpdater<T>, Series {

	private final LinkedList<T> records;
	private final int capacity;

	public GraphModel(int capacity) {
		this.records = new LinkedList<T>();
		this.capacity = capacity;
	}
	
	@Override
	public int getLength() {
		return records.size();
	}

	@Override
	public T getValue(int index) {
		return records.get(index);
	}

	public void update(T record) {
		int size = records.size();

		if (size >= capacity) {
			records.removeLast();
		}
		records.addFirst(record);
	}
}